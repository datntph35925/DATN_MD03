var express = require('express');
var router = express.Router();
const Carts = require('../models/Carts');
const Products = require('../models/Products');
const CustomerAccounts = require('../models/CustomerAccounts');

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

// Route để thêm sản phẩm vào giỏ hàng
router.post('/add-cart/:userId', async (req, res) => {
    try {
        const { userId } = req.params;
        const { productId, quantity, size } = req.body;

        if (quantity <= 0) {
            return res.status(405).json({ message: "Số lượng phải lớn hơn 0" });
        }

        // Kiểm tra xem tài khoản người dùng có tồn tại không
        const userAccount = await CustomerAccounts.findOne({ Tentaikhoan: userId });
        if (!userAccount) {
            return res.status(404).json({ message: 'Tài khoản không tồn tại' });
        }

        // Tìm sản phẩm trong cơ sở dữ liệu
        const product = await Products.findById(productId);
        if (!product) {
            return res.status(404).json({ message: 'Sản phẩm không tồn tại' });
        }

        // Kiểm tra kích thước sản phẩm và số lượng tồn kho
        // Kiểm tra kích thước sản phẩm và số lượng tồn kho
        const sizeItem = product.KichThuoc.find(item => item.size === Number(size));
        if (!sizeItem) {
            return res.status(400).json({
                message: `Kích cỡ ${size} không có sẵn cho sản phẩm này`
            });
        }

        if (sizeItem.soLuongTon < quantity) {
            return res.status(401).json({
                message: `Số lượng của size ${size} không đủ. Chỉ còn ${sizeItem.soLuongTon} sản phẩm trong kho`
            });
        }

        // Tìm giỏ hàng của người dùng hoặc tạo mới nếu chưa tồn tại
        let cart = await Carts.findOne({ Tentaikhoan: userAccount.Tentaikhoan }); // Thay _id bằng Tentaikhoan
        if (!cart) {
            cart = new Carts({ Tentaikhoan: userAccount.Tentaikhoan, SanPham: [] }); // Dùng Tentaikhoan khi tạo mới giỏ hàng
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
            // Thêm sản phẩm mới vào giỏ hàng
            const newProductItem = {
                MaSanPham: productId,
                TenSP: product.TenSP,
                SoLuongGioHang: quantity,
                Size: size,
                Gia: product.GiaBan,
                TongTien: product.GiaBan * quantity
            };

            cart.SanPham.push(newProductItem);
        }

        // Cập nhật tổng số lượng và tổng giá trị của giỏ hàng
        cart.TongSoLuong = cart.SanPham.reduce((total, item) => total + item.SoLuongGioHang, 0);
        cart.TongGiaTri = cart.SanPham.reduce((total, item) => total + item.TongTien, 0);

        // Lưu giỏ hàng sau khi thay đổi
        await cart.save();

        // Trả về giỏ hàng đã cập nhật
        res.status(200).json({
            message: "Thêm sản phẩm vào giỏ hàng thành công",
            cart
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng' });
    }
});

router.delete('/delete-cart/:userId/product/:productId', async (req, res) => {
    try {
        const userId = req.params.userId;
        const productId = req.params.productId;

        // Tìm giỏ hàng của người dùng
        const cart = await Carts.findOne({ Tentaikhoan: userId });
        if (!cart) {
            return res.status(404).json({ message: "Giỏ hàng không tồn tại" });
        }

        // Xóa sản phẩm cụ thể khỏi mảng SanPham
        const updatedProducts = cart.SanPham.filter(item => item.MaSanPham.toString() !== productId);

        if (cart.SanPham.length === updatedProducts.length) {
            return res.status(404).json({ message: "Sản phẩm không tồn tại trong giỏ hàng" });
        }

        // Cập nhật giỏ hàng
        cart.SanPham = updatedProducts;
        cart.TongSoLuong = updatedProducts.reduce((total, item) => total + item.SoLuongGioHang, 0);
        cart.TongGiaTri = updatedProducts.reduce((total, item) => total + item.TongTien, 0);

        await cart.save();

        res.status(200).json({ message: "Đã xóa sản phẩm khỏi giỏ hàng", cart });
    } catch (error) {
        res.status(500).json({ message: "Lỗi khi xóa sản phẩm khỏi giỏ hàng", error });
    }
});


module.exports = router;
