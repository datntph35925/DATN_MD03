var express = require('express');
var router = express.Router();
const Orders = require('../models/Orders');
const CustomerAccounts = require('../models/CustomerAccounts');
const Products = require('../models/Products');
const Carts = require('../models/Carts');
const Vouchers = require('../models/Vouchers');


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

        // Lấy dữ liệu từ body của request
        const {
            MaDonHang,
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            PhuongThucThanhToan,
            selectedProducts, // Danh sách ID sản phẩm được chọn từ giỏ hàng
            Voucher // Mã voucher (nếu có)
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

        // Kiểm tra sản phẩm trong giỏ hàng với cơ sở dữ liệu
        const productIds = cartProducts.map(item => item.MaSanPham); // Lấy IDs của sản phẩm
        const productsInDb = await Products.find({ _id: { $in: productIds } });

        const invalidProducts = cartProducts.filter(item =>
            !productsInDb.some(product => product._id.toString() === item.MaSanPham.toString())
        );
        if (invalidProducts.length > 0) {
            return res.status(400).json({
                message: `Các sản phẩm sau không tồn tại trong cơ sở dữ liệu: ${invalidProducts.map(item => item.TenSP).join(", ")}`,
            });
        }

        // Cập nhật lại các sản phẩm trong giỏ hàng
        const updatedProducts = cartProducts.map(item => {
            const productDetails = productsInDb.find(product => product._id.toString() === item.MaSanPham.toString());

            if (productDetails) {
                item.HinhAnh = productDetails.HinhAnh || [];
                item.Size = item.Size || productDetails.Size; // Gán size từ giỏ hàng hoặc cơ sở dữ liệu
                item.TongTien = item.Gia * item.SoLuongGioHang; // Tính lại tổng tiền sản phẩm
            }

            return item;
        });

        // Tính tổng số lượng và tổng tiền trước khi áp dụng voucher
        const TongSoLuong = updatedProducts.reduce((total, item) => total + item.SoLuongGioHang, 0);
        let TongTien = updatedProducts.reduce((total, item) => total + item.TongTien, 0);

        // Kiểm tra và áp dụng voucher
        let voucherApplied = null;
        let discountAmount = 0; // Số tiền giảm giá
        if (Voucher) {
            const voucher = await Vouchers.findOne({ MaVoucher: Voucher });

            if (!voucher) {
                return res.status(400).json({ message: 'Voucher không hợp lệ hoặc không tồn tại.' });
            }

            // Kiểm tra thời gian hiệu lực của voucher
            const currentDate = new Date();
            if (currentDate < voucher.NgayBatDau || currentDate > voucher.NgayKetThuc) {
                return res.status(400).json({ message: 'Voucher đã hết hạn.' });
            }

            // Kiểm tra xem voucher đã được sử dụng chưa
            if (voucher.UsedBy.includes(userAccount.Tentaikhoan)) {
                return res.status(400).json({ message: 'Voucher này đã được sử dụng cho tài khoản này.' });
            }

            // Áp dụng giảm giá
            if (voucher.LoaiVoucher === 'Giảm giá theo %') {
                discountAmount = TongTien * (voucher.GiaTri / 100);
            } else if (voucher.LoaiVoucher === 'Giảm giá cố định') {
                discountAmount = voucher.GiaTri;
            }

            // Đảm bảo số tiền giảm giá không vượt quá tổng tiền
            if (discountAmount > TongTien) {
                discountAmount = TongTien;
            }

            // Cập nhật voucher đã sử dụng
            voucherApplied = voucher.MaVoucher;
            voucher.UsedBy.push(userAccount.Tentaikhoan);
            await voucher.save();

            // Trừ tiền giảm giá vào tổng tiền
            TongTien -= discountAmount;
        }

        // Tạo đơn hàng mới
        const newOrder = new Orders({
            MaDonHang,
            Tentaikhoan: userAccount.Tentaikhoan,
            SanPham: updatedProducts, // Các sản phẩm đã được cập nhật
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            TrangThai: 'Chờ xử lý',
            TongSoLuong,
            TongTien,
            PhuongThucThanhToan,
            Voucher: voucherApplied // Thêm voucher vào đơn hàng nếu có
        });

        // Lưu đơn hàng vào cơ sở dữ liệu
        const savedOrder = await newOrder.save();

        // Trả về kết quả
        res.status(201).json({
            message: 'Đơn hàng đã được tạo thành công từ giỏ hàng!',
            order: savedOrder,
            discount: discountAmount // Số tiền giảm giá
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
            MaDonHang,
            SanPham, // Mảng sản phẩm trực tiếp từ body
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            PhuongThucThanhToan,
            Voucher // Mã voucher (nếu có)
        } = req.body;

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
                return true; // Đánh dấu sản phẩm này là không hợp lệ
            }

            const sizeInfo = productInDb.KichThuoc.find(size => size.size === item.Size); // Kiểm tra kích thước
            if (!sizeInfo) {
                return true; // Kích thước không hợp lệ
            }

            if (sizeInfo.soLuongTon < item.SoLuongGioHang) {
                return true; // Không đủ tồn kho
            }

            if (productInDb.GiaBan !== item.Gia) {
                return true; // Giá không hợp lệ
            }

            if (!item.HinhAnh) {
                item.HinhAnh = productInDb.HinhAnh || []; // Đảm bảo có HinhAnh
            }

            return false;
        });

        if (invalidProductDetails.length > 0) {
            return res.status(402).json({
                message: `Các sản phẩm sau có vấn đề về tồn kho hoặc giá: ${invalidProductDetails.map(item => item.TenSP).join(", ")}`
            });
        }

        // Tính tổng số lượng và tổng tiền trước khi áp dụng voucher
        const TongSoLuong = SanPham.reduce((total, item) => total + item.SoLuongGioHang, 0);
        let TongTien = SanPham.reduce((total, item) => total + (item.Gia * item.SoLuongGioHang), 0);

        // Kiểm tra voucher nếu có
        let voucherApplied = null;
        let discountAmount = 0; // Số tiền giảm giá
        if (Voucher) {
            // Tìm voucher trong hệ thống
            const voucher = await Vouchers.findOne({ MaVoucher: Voucher });

            if (!voucher) {
                return res.status(400).json({ message: 'Voucher không hợp lệ hoặc không tồn tại.' });
            }

            // Kiểm tra xem voucher có còn hiệu lực không
            const currentDate = new Date();
            if (currentDate < voucher.NgayBatDau || currentDate > voucher.NgayKetThuc) {
                return res.status(400).json({ message: 'Voucher đã hết hạn.' });
            }

            // Kiểm tra xem voucher đã được sử dụng chưa
            if (voucher.UsedBy.includes(userAccount.Tentaikhoan)) {
                return res.status(400).json({ message: 'Voucher này đã được sử dụng cho tài khoản này.' });
            }

            // Áp dụng giảm giá theo loại voucher
            if (voucher.LoaiVoucher === 'Giảm giá theo %') {
                discountAmount = TongTien * (voucher.GiaTri / 100);
            } else if (voucher.LoaiVoucher === 'Giảm giá cố định') {
                discountAmount = voucher.GiaTri;
            }

            // Đảm bảo số tiền giảm giá không lớn hơn tổng tiền
            if (discountAmount > TongTien) {
                discountAmount = TongTien;
            }

            // Cập nhật voucher đã sử dụng
            voucherApplied = voucher.MaVoucher;
            voucher.UsedBy.push(userAccount.Tentaikhoan);
            await voucher.save();

            // Trừ tiền giảm giá vào tổng tiền
            TongTien -= discountAmount;
        }

        // Tạo đơn hàng mới
        const newOrder = new Orders({
            MaDonHang,
            Tentaikhoan: userAccount.Tentaikhoan,
            SanPham: SanPham.map(item => ({
                MaSanPham: item.MaSanPham,
                TenSP: item.TenSP,
                SoLuongGioHang: item.SoLuongGioHang,
                Size: item.Size,
                Gia: item.Gia,
                TongTien: item.Gia * item.SoLuongGioHang,
                HinhAnh: item.HinhAnh || [] // Đảm bảo HinhAnh là một mảng
            })),
            TenNguoiNhan,
            DiaChiGiaoHang,
            SoDienThoai,
            TrangThai: 'Chờ xử lý',
            TongSoLuong,
            TongTien,
            PhuongThucThanhToan,
            Voucher: voucherApplied  // Thêm voucher vào đơn hàng nếu có
        });

        // Lưu đơn hàng vào cơ sở dữ liệu
        const savedOrder = await newOrder.save();

        // Trả về kết quả
        res.status(201).json({
            message: 'Đơn hàng đã được tạo thành công từ sản phẩm trực tiếp!',
            order: savedOrder,
            discount: discountAmount // Thông tin giảm giá áp dụng
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

router.get('/get-order-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params; // Lấy id từ params
        const order = await Orders.findById(id); // Tìm sản phẩm theo id

        if (order) {
            res.status(200).json({
                status: 200,
                message: 'Lấy thông tin sản phẩm thành công',
                data: order
            });
        } else {
            res.status(404).json({
                status: 404,
                message: 'Không tìm thấy sản phẩm',
                data: null
            });
        }
    } catch (error) {
        console.error('Lỗi:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server',
            error: error.message
        });
    }
});

router.get('/total-orders', async (req, res) => {
    try {
        // Count only orders with the status "Chờ xử lý"
        const unprocessedOrdersCount = await Orders.countDocuments({ TrangThai: 'Chờ xử lý' });
        res.status(200).json({
            success: true,
            unprocessedOrders: unprocessedOrdersCount
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Lỗi khi lấy tổng số lượng đơn hàng chưa xử lý',
            error: error.message
        });
    }
});

module.exports = router;
