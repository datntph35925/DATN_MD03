var express = require('express');
var router = express.Router();
const Carts = require('../models/Carts');
const Products = require('../models/Products');
const CustomerAccounts = require('../models/CustomerAccounts');

router.get('/get-list-cart-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params; // Lấy id từ params
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: id });
        // Tìm sản phẩm theo id

        const data = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan });
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
        console.error('Lỗi:', error);
        res.status(500).json({
            status: 500,
            message: 'Lỗi server',
            error: error.message
        });
    }
});


router.get("/get-list-cart", async (req, res) => {
    try {
        //lấy ds sản phẩm
        const data = await Carts.find().sort({ createdAt: -1 });
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

router.post('/add-cart/:userId', async (req, res) => {
    try {
        const { userId } = req.params; // Lấy userId từ URL
        const { productId, quantity, size } = req.body; // Lấy thông tin từ body

        // Kiểm tra số lượng phải lớn hơn 0
        if (quantity <= 0) {
            return res.status(400).json({ message: "Số lượng phải lớn hơn 0" });
        }

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tìm giỏ hàng của tài khoản (mỗi tài khoản chỉ có 1 giỏ hàng)
        let cart = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan });

        if (!cart) {
            // Nếu giỏ hàng chưa tồn tại, tạo mới
            cart = new Carts({
                Tentaikhoan: userAccount.Tentaikhoan, // Tạo giỏ hàng mới cho tài khoản này
                SanPham: []
            });
        }

        // Tìm sản phẩm trong cơ sở dữ liệu
        const product = await Products.findById(productId);
        if (!product) {
            return res.status(404).json({ message: 'Sản phẩm không tồn tại' });
        }

        // Kiểm tra kích thước sản phẩm
        const sizeItem = product.KichThuoc.find(item => item.size === Number(size));
        if (!sizeItem) {
            return res.status(400).json({ message: `Kích cỡ ${size} không có sẵn cho sản phẩm này` });
        }

        if (sizeItem.soLuongTon < quantity) {
            return res.status(400).json({
                message: `Không đủ sản phẩm tồn kho. Chỉ còn ${sizeItem.soLuongTon} sản phẩm cho size ${size}`
            });
        }

        // Đảm bảo `soLuongTon` được lấy đúng
        if (!sizeItem.soLuongTon && sizeItem.soLuongTon !== 0) {
            return res.status(400).json({ message: 'Số lượng tồn kho không xác định cho kích thước này' });
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        const productIndex = cart.SanPham.findIndex(
            item => item.MaSanPham.toString() === productId && item.Size === size
        );

        if (productIndex !== -1) {
            // Nếu sản phẩm đã tồn tại, cập nhật số lượng và tổng tiền
            cart.SanPham[productIndex].SoLuongGioHang += quantity;
            cart.SanPham[productIndex].TongTien = cart.SanPham[productIndex].Gia * cart.SanPham[productIndex].SoLuongGioHang;
        } else {
            if (!product.HinhAnh || product.HinhAnh.length === 0) {
                return res.status(400).json({ message: "Sản phẩm không có hình ảnh" });
            }
            // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới
            const newProductItem = {
                MaSanPham: productId,
                TenSP: product.TenSP,
                SoLuongGioHang: quantity,
                Size: size,
                Gia: product.GiaBan,
                TongTien: product.GiaBan * quantity,
                HinhAnh: product.HinhAnh[0],
                SoLuongTon: sizeItem.soLuongTon,
            };
            cart.SanPham.push(newProductItem);
        }

        // Cập nhật tổng số lượng và tổng giá trị của giỏ hàng
        cart.TongSoLuong = cart.SanPham.reduce((total, item) => total + item.SoLuongGioHang, 0);
        cart.TongGiaTri = cart.SanPham.reduce((total, item) => total + item.TongTien, 0);

        // Lưu giỏ hàng đã được cập nhật
        await cart.save();

        // Trả về phản hồi thành công
        res.status(200).json({ message: "Đã thêm sản phẩm vào giỏ hàng thành công", cart });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng', error: error.message });
    }
});


// Route để xóa một sản phẩm khỏi giỏ hàng
router.delete('/remove-item/:userId', async (req, res) => {
    try {
        const { userId } = req.params; // Lấy userId từ URL
        const { productId, size } = req.body; // Lấy productId và size từ body

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tìm giỏ hàng của người dùng
        const cart = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan });
        if (!cart) {
            return res.status(404).json({ message: 'Giỏ hàng không tồn tại' });
        }

        // Tìm sản phẩm cần xóa trong giỏ hàng
        const productIndex = cart.SanPham.findIndex(
            item => item.MaSanPham.toString() === productId && item.Size === size
        );

        if (productIndex === -1) {
            return res.status(404).json({ message: 'Sản phẩm không tồn tại trong giỏ hàng' });
        }

        // Xóa sản phẩm khỏi giỏ hàng
        cart.SanPham.splice(productIndex, 1);

        // Cập nhật tổng số lượng và tổng giá trị của giỏ hàng
        cart.TongSoLuong = cart.SanPham.reduce((total, item) => total + item.SoLuongGioHang, 0);
        cart.TongGiaTri = cart.SanPham.reduce((total, item) => total + item.TongTien, 0);

        // Lưu lại giỏ hàng sau khi xóa sản phẩm
        await cart.save();

        return res.status(200).json({
            message: 'Sản phẩm đã được xóa khỏi giỏ hàng thành công',
            cart
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng' });
    }
});

router.delete('/remove-items/:userId', async (req, res) => {
    try {
        const { userId } = req.params; // Lấy userId từ URL
        const { products } = req.body; // Lấy danh sách sản phẩm cần xóa từ body

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tìm giỏ hàng của người dùng
        const cart = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan });
        if (!cart) {
            return res.status(404).json({ message: 'Giỏ hàng không tồn tại' });
        }

        // Xóa từng sản phẩm từ danh sách gửi lên
        products.forEach(({ productId, size }) => {
            if (typeof size !== 'number' || size < 30 || size > 50) {
                // Kiểm tra size giày hợp lệ (tùy thuộc vào phạm vi size thực tế)
                throw new Error(`Size giày không hợp lệ: ${size}. Size phải từ 30 đến 50.`);
            }

            const productIndex = cart.SanPham.findIndex(
                item => item.MaSanPham.toString() === productId && item.Size === size
            );

            if (productIndex !== -1) {
                cart.SanPham.splice(productIndex, 1); // Xóa sản phẩm
            }
        });

        // Cập nhật tổng số lượng và tổng giá trị của giỏ hàng
        cart.TongSoLuong = cart.SanPham.reduce((total, item) => total + item.SoLuongGioHang, 0);
        cart.TongGiaTri = cart.SanPham.reduce((total, item) => total + item.TongTien, 0);

        // Lưu lại giỏ hàng sau khi xóa sản phẩm
        await cart.save();

        return res.status(200).json({
            message: 'Các sản phẩm đã được xóa khỏi giỏ hàng thành công',
            cart
        });
    } catch (error) {
        console.error(error);
        res.status(400).json({ message: error.message || 'Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng' });
    }
});

module.exports = router;
