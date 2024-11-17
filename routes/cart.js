var express = require('express');
var router = express.Router();
const Carts = require('../models/Carts');
const Products = require('../models/Products');

// Route để thêm sản phẩm vào giỏ hàng
router.post('/add-cart/:userId', async (req, res) => {
    try {
        const { userId } = req.params;
        const { productId, quantity, size } = req.body;

        if (quantity <= 0) {
            return res.status(405).json({ message: "Số lượng phải lơn hơn 0" });
        }

        // Tìm sản phẩm trong cơ sở dữ liệu
        const product = await Products.findById(productId);
        if (!product) {
            return res.status(404).json({ message: 'Sản phẩm không tồn tại' });
        }

        const sizeItem = product.KichThuoc.find(item => item.size === Number(size));
        if (!sizeItem || sizeItem.soLuongTon < quantity) {
            return res.status(401).json({
                message: `Số lượng của size ${size} không đủ. Chỉ còn ${sizeItem ? sizeItem.soLuongTon : 0} sản phẩm trong kho`
            });
        }

        const sizeExists = product.KichThuoc.some(item => item.size === Number(size));
        if (!sizeExists) {
            return res.status(400).json({ message: `Kích cỡ ${size} không có sẵn cho sản phẩm này` });
        }

        // Tìm giỏ hàng của người dùng hoặc tạo mới nếu chưa tồn tại
        let cart = await Carts.findOne({ Matk: userId });
        if (!cart) {
            cart = new Carts({ Matk: userId, SanPham: [] });
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

        // Lưu giỏ hàng đã cập nhật
        await cart.save();
        res.status(200).json({ message: 'Đã thêm sản phẩm vào giỏ hàng', cart });
    } catch (error) {
        res.status(500).json({ message: 'Đã xảy ra lỗi', error: error.message });
    }
});

router.delete('/delete-cart/:userId/product/:productId', async (req, res) => {
    try {
        const userId = req.params.userId; 
        const productId = req.params.productId; 

        // Tìm giỏ hàng của người dùng
        const cart = await Carts.findOne({ Matk: userId });
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
