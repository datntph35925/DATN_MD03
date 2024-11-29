var express = require('express');
var router = express.Router();
const Orders = require('../models/Orders');
const CustomerAccounts = require('../models/CustomerAccounts');
const Products = require('../models/Products');
const Carts = require('../models/Carts');


router.get("/get-list-order", async (req, res) => {
    try {
        //lấy ds sản phẩm
        const data = await Orders.find().sort({ createdAt: -1 });
        if (data) {
            res.json({
                status: 200,
                messenger: "Lấy danh sách thành công",
                data: data,
            });
        } else {
            res.json({
                status: 400,
                messenger: "lấy danh sách thất bại",
                data: [],
            });
        }
    } catch (error) {
        console.log(error);
    }
});

router.get("/get-list-order/:Tentaikhoan", async (req, res) => {
    try {
        // Lấy Tentaikhoan từ URL params
        const { Tentaikhoan } = req.params;

        // Tìm danh sách đơn hàng của tài khoản người dùng
        const data = await Orders.find({ Tentaikhoan }).sort({ createdAt: -1 });

        if (data && data.length > 0) {
            res.json({
                status: 200,
                messenger: "Lấy danh sách đơn hàng thành công",
                data: data,
            });
        } else {
            res.json({
                status: 400,
                messenger: "Không có đơn hàng nào cho tài khoản này",
                data: [],
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            status: 500,
            messenger: "Lỗi khi lấy danh sách đơn hàng",
        });
    }
});





router.post('/add-order-from-cart/:userId', async (req, res) => {
    try {
        const { userId } = req.params; // Lấy userId từ URL params

        // Lấy dữ liệu từ body của request (địa chỉ, phương thức thanh toán, v.v.)
        const {
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            PhuongThucThanhToan,
            selectedProducts // Các sản phẩm đã chọn từ giỏ hàng (danh sách ID sản phẩm)
        } = req.body;

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản người dùng không tồn tại!' });
        }

        // Kiểm tra giỏ hàng của người dùng
        const cart = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan });
        if (!cart || cart.SanPham.length === 0) {
            return res.status(400).json({ message: 'Giỏ hàng trống hoặc không tồn tại!' });
        }

        // Lọc ra các sản phẩm được chọn từ giỏ hàng
        const cartProducts = cart.SanPham.filter(item => selectedProducts.includes(item.MaSanPham.toString()));

        // Kiểm tra nếu không có sản phẩm nào được chọn từ giỏ hàng
        if (cartProducts.length === 0) {
            return res.status(400).json({ message: 'Không có sản phẩm nào được chọn cho đơn hàng!' });
        }

        // Kiểm tra giỏ hàng có đủ số lượng sản phẩm không
        const productIds = cartProducts.map(item => item.MaSanPham); // Lấy tất cả IDs của sản phẩm
        const productsInDb = await Products.find({ _id: { $in: productIds } });

        // Kiểm tra xem tất cả sản phẩm trong giỏ hàng có tồn tại không
        const invalidProducts = cartProducts.filter(item =>
            !productsInDb.some(product => product._id.toString() === item.MaSanPham.toString())
        );
        if (invalidProducts.length > 0) {
            return res.status(400).json({
                message: `Các sản phẩm sau không tồn tại trong cơ sở dữ liệu: ${invalidProducts.map(item => item.TenSP).join(", ")}`
            });
        }

        // Kiểm tra số lượng sản phẩm trong giỏ hàng có hợp lệ không
        const invalidQuantityProducts = cartProducts.filter(item => !item.SoLuongGioHang || item.SoLuongGioHang <= 0);
        if (invalidQuantityProducts.length > 0) {
            return res.status(400).json({
                message: `Sản phẩm sau không có số lượng hợp lệ: ${invalidQuantityProducts.map(item => item.TenSP).join(", ")}`
            });
        }

        // Cập nhật lại các sản phẩm trong giỏ hàng với trường SoLuongGioHang và Size (nếu cần)
        const updatedProducts = cartProducts.map(item => {
            if (!item.SoLuongGioHang) {
                item.SoLuongGioHang = 1; // Gán số lượng mặc định nếu không có
            }

            // Lấy thông tin sản phẩm từ cơ sở dữ liệu (bao gồm cả Size)
            const productDetails = productsInDb.find(product => product._id.toString() === item.MaSanPham.toString());

            if (productDetails) {
                // Lấy thông tin hình ảnh và size từ sản phẩm trong cơ sở dữ liệu
                item.HinhAnh = productDetails.HinhAnh;

                // Nếu sản phẩm có nhiều size, thì bạn cần kiểm tra và gán size từ giỏ hàng vào sản phẩm
                const productSize = item.Size || productDetails.Size; // Gán size từ giỏ hàng hoặc cơ sở dữ liệu
                item.Size = productSize;
            }

            return item;
        });

        // Tính tổng số lượng và tổng tiền cho các sản phẩm đã chọn
        const TongSoLuong = updatedProducts.reduce((total, item) => total + item.SoLuongGioHang, 0);
        const TongTien = updatedProducts.reduce((total, item) => total + item.TongTien, 0);

        // Tạo đơn hàng mới
        const newOrder = new Orders({
            Tentaikhoan: userAccount.Tentaikhoan,
            SanPham: updatedProducts,  // Các sản phẩm đã được cập nhật
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            TrangThai: 'Chờ xử lý',
            TongSoLuong,
            TongTien,
            PhuongThucThanhToan
        });

        // Lưu đơn hàng vào cơ sở dữ liệu
        const savedOrder = await newOrder.save();

        // Trả về kết quả
        res.status(201).json({
            message: 'Đơn hàng đã được tạo thành công từ giỏ hàng!',
            order: savedOrder
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Lỗi khi tạo đơn hàng từ giỏ hàng!' });
    }
});

router.post('/add-order-directly/:userId', async (req, res) => {
    try {
        const { userId } = req.params; // Lấy userId từ URL params

        // Lấy dữ liệu từ body của request (danh sách sản phẩm, địa chỉ giao hàng, phương thức thanh toán, v.v.)
        const {
            SanPham, // Mảng sản phẩm trực tiếp từ body
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            PhuongThucThanhToan
        } = req.body;

        console.log('Dữ liệu đầu vào từ người dùng:');
        console.log({
            SanPham,
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            PhuongThucThanhToan
        });

        if (!SanPham || SanPham.length === 0) {
            return res.status(401).json({ message: 'Danh sách sản phẩm không được để trống!' });
        }

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });

        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản người dùng không tồn tại!' });
        }

        // Lấy thông tin sản phẩm từ cơ sở dữ liệu (kiểm tra mỗi sản phẩm có tồn tại không)
        const productIds = SanPham.map(item => item.MaSanPham); // Lấy tất cả IDs của sản phẩm
        const productsInDb = await Products.find({ _id: { $in: productIds } });

        // Kiểm tra xem tất cả sản phẩm trong đơn hàng có tồn tại không
        const invalidProducts = SanPham.filter(item =>
            !productsInDb.some(product => product._id.toString() === item.MaSanPham.toString())
        );

        if (invalidProducts.length > 0) {
            return res.status(400).json({
                message: `Các sản phẩm sau không tồn tại trong cơ sở dữ liệu: ${invalidProducts.map(item => item.TenSP).join(", ")}`
            });
        }

        // Kiểm tra thông tin sản phẩm (giá, tồn kho, v.v.)
        const invalidProductDetails = SanPham.filter(item => {
            const productInDb = productsInDb.find(product => product._id.toString() === item.MaSanPham.toString());

            if (!productInDb) {
                // Nếu không tìm thấy sản phẩm trong cơ sở dữ liệu
                return true; // Đánh dấu sản phẩm này là không hợp lệ
            }

            // Kiểm tra thông tin sản phẩm và số lượng tồn kho theo kích cỡ
            const sizeInfo = productInDb.KichThuoc.find(size => size.size === item.Size); // Tìm thông tin kích cỡ sản phẩm
            if (!sizeInfo) {
                return true; // Kích cỡ không hợp lệ
            }

            // Kiểm tra xem sản phẩm có đủ tồn kho hay không
            if (sizeInfo.soLuongTon < item.SoLuongGioHang) {
                return true; // Không đủ tồn kho
            }

            // Kiểm tra giá sản phẩm có hợp lệ không
            if (productInDb.GiaBan !== item.Gia) {
                return true; // Giá sản phẩm không hợp lệ
            }

            // Cập nhật hình ảnh cho sản phẩm nếu chưa có
            if (!item.HinhAnh) {
                item.HinhAnh = productInDb.HinhAnh || []; // Đảm bảo HinhAnh là mảng (có thể là mảng rỗng)
            }

            return false;
        });

        if (invalidProductDetails.length > 0) {
            return res.status(402).json({
                message: `Các sản phẩm sau có vấn đề về tồn kho hoặc giá: ${invalidProductDetails.map(item => item.TenSP).join(", ")}`
            });
        }

        // Tính tổng số lượng và tổng tiền
        const TongSoLuong = SanPham.reduce((total, item) => total + item.SoLuongGioHang, 0);
        const TongTien = SanPham.reduce((total, item) => total + item.TongTien, 0);

        // Tạo đơn hàng mới
        const newOrder = new Orders({
            Tentaikhoan: userAccount.Tentaikhoan,
            SanPham: SanPham.map(item => ({
                MaSanPham: item.MaSanPham,
                TenSP: item.TenSP,
                SoLuongGioHang: item.SoLuongGioHang,
                Size: item.Size,
                Gia: item.Gia,
                TongTien: item.TongTien,
                HinhAnh: item.HinhAnh || [] // Đảm bảo HinhAnh là một mảng
            })),
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            TrangThai: 'Chờ xử lý',
            TongSoLuong,
            TongTien,
            PhuongThucThanhToan
        });

        // Lưu đơn hàng vào cơ sở dữ liệu
        const savedOrder = await newOrder.save();

        // Trả về kết quả
        res.status(201).json({
            message: 'Đơn hàng đã được tạo thành công từ sản phẩm trực tiếp!',
            order: savedOrder
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Lỗi khi tạo đơn hàng từ sản phẩm trực tiếp!' });
    }
});

router.put('/update-order-status/:id', async (req, res) => {
    const { id } = req.params; // Lấy id từ URL
    const { TrangThai } = req.body; // Lấy trạng thái mới từ request body
  
    try {
      // Kiểm tra trạng thái hợp lệ
      const validStatuses = ['Chờ xử lý', 'Đang giao', 'Đã giao', 'Hủy'];
      if (!validStatuses.includes(TrangThai)) {
        return res.status(400).json({
          status: 400,
          message: 'Trạng thái không hợp lệ.',
        });
      }
  
      // Tìm và cập nhật trạng thái đơn hàng
      const updatedOrder = await Orders.findByIdAndUpdate(
        id,
        { TrangThai }, // Cập nhật trạng thái mới
        { new: true } // Trả về tài liệu sau khi cập nhật
      );
  
      // Kiểm tra nếu không tìm thấy đơn hàng
      if (!updatedOrder) {
        return res.status(404).json({
          status: 404,
          message: 'Không tìm thấy đơn hàng với ID đã cung cấp.',
        });
      }
  
      // Trả về kết quả thành công
      res.status(200).json({
        status: 200,
        message: 'Cập nhật trạng thái đơn hàng thành công.',
        data: updatedOrder, // Đơn hàng sau khi cập nhật
      });
    } catch (error) {
      console.error('Lỗi khi cập nhật trạng thái đơn hàng:', error);
      res.status(500).json({
        status: 500,
        message: 'Đã xảy ra lỗi khi cập nhật trạng thái đơn hàng.',
      });
    }
  });

module.exports = router;
