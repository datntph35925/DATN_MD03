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

// router.post('/add-order/:userId', async (req, res) => {
//     try {
//         const { userId } = req.params; // Lấy userId từ URL params

//         // Lấy dữ liệu từ body của request
//         const {
//             SanPham, // Mảng sản phẩm từ người dùng gửi (nếu có)
//             TenNguoiNhan,
//             DiaChiGiaoHang,
//             SoDienThoai,
//             PhuongThucThanhToan
//         } = req.body;

//         // Kiểm tra xem tài khoản người dùng có tồn tại không
//         const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });

//         if (!userAccount) {
//             return res.status(404).json({ message: 'Tài khoản người dùng không tồn tại!' });
//         }

//         let cartProducts = [];

//         // Kiểm tra giỏ hàng của người dùng
//         const cart = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan });

//         if (cart) {
//             // Nếu giỏ hàng có sản phẩm, lấy thông tin sản phẩm từ giỏ hàng
//             cartProducts = cart.SanPham;
//         } else if (SanPham && SanPham.length > 0) {
//             // Nếu giỏ hàng không có, lấy thông tin sản phẩm từ request body (SanPham)
//             cartProducts = SanPham;
//         } else {
//             return res.status(400).json({ message: 'Không có sản phẩm trong giỏ hàng hoặc đơn hàng!' });
//         }

//         // Lấy thông tin sản phẩm từ cơ sở dữ liệu (kiểm tra mỗi sản phẩm có tồn tại không)
//         const productIds = cartProducts.map(item => item.MaSanPham); // Lấy tất cả IDs của sản phẩm
//         const productsInDb = await Products.find({ _id: { $in: productIds } });

//         // Kiểm tra xem tất cả sản phẩm trong đơn hàng có tồn tại không
//         const invalidProducts = cartProducts.filter(item => 
//             !productsInDb.some(product => product._id.toString() === item.MaSanPham.toString())
//         );

//         if (invalidProducts.length > 0) {
//             return res.status(400).json({
//                 message: `Các sản phẩm sau không tồn tại trong cơ sở dữ liệu: ${invalidProducts.map(item => item.TenSP).join(", ")}`
//             });
//         }

//         // Tính tổng số lượng và tổng tiền
//         const TongSoLuong = cartProducts.reduce((total, item) => total + item.SoLuongGioHang, 0);
//         const TongTien = cartProducts.reduce((total, item) => total + item.TongTien, 0);

//         // Tạo đơn hàng mới
//         const newOrder = new Orders({
//             Tentaikhoan: userAccount.Tentaikhoan, // Lưu tên tài khoản người dùng
//             SanPham: cartProducts,
//             TenNguoiNhan,
//             DiaChiGiaoHang,
//             SoDienThoai,
//             TrangThai: 'Chờ xử lý', // Trạng thái đơn hàng ban đầu
//             TongSoLuong,
//             TongTien,
//             PhuongThucThanhToan
//         });

//         // Lưu đơn hàng vào cơ sở dữ liệu
//         const savedOrder = await newOrder.save();

//         // Trả về kết quả
//         res.status(201).json({
//             message: 'Đơn hàng đã được tạo thành công!',
//             order: savedOrder
//         });

//     } catch (error) {
//         console.error(error);
//         res.status(500).json({ message: 'Lỗi khi tạo đơn hàng!' });
//     }
// });



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
                console.log(`Không tìm thấy sản phẩm với mã ${item.MaSanPham}`);
                return true; // Đánh dấu sản phẩm này là không hợp lệ
            }

            console.log(`Kiểm tra sản phẩm: ${productInDb.TenSP}`);
            console.log(`Số lượng người dùng nhập vào: ${item.SoLuong}`);
            console.log(`Giá người dùng nhập vào: ${item.Gia}`);
            console.log(`Số lượng tồn kho: ${productInDb.KichThuoc.find(size => size.size === item.Size)?.soLuongTon}`);
            console.log(`Giá trong CSDL: ${productInDb.GiaBan}`);

            // Kiểm tra thông tin sản phẩm và số lượng tồn kho theo kích cỡ
            const sizeInfo = productInDb.KichThuoc.find(size => size.size === item.Size); // Tìm thông tin kích cỡ sản phẩm
            if (!sizeInfo) {
                return true; // Kích cỡ không hợp lệ
            }

            // Kiểm tra xem sản phẩm có đủ tồn kho hay không
            if (sizeInfo.soLuongTon < item.SoLuong) {
                return true; // Không đủ tồn kho
            }

            // Kiểm tra giá sản phẩm có hợp lệ không
            if (productInDb.GiaBan !== item.Gia) {
                return true; // Giá sản phẩm không hợp lệ
            }

            return false;
        });

        if (invalidProductDetails.length > 0) {
            return res.status(402).json({
                message: `Các sản phẩm sau có vấn đề về tồn kho hoặc giá: ${invalidProductDetails.map(item => item.TenSP).join(", ")}`
            });
        }

        // Tính tổng số lượng và tổng tiền
        const TongSoLuong = SanPham.reduce((total, item) => total + item.SoLuong, 0);
        const TongTien = SanPham.reduce((total, item) => total + item.TongTien, 0);

        // Tạo đơn hàng mới
        const newOrder = new Orders({
            Tentaikhoan: userAccount.Tentaikhoan,
            SanPham: SanPham,
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





module.exports = router;
