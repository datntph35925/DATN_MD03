const express = require('express');
const router = express.Router();
const Review = require('../models/Review'); // Model Review
const CustomerAccount = require('../models/CustomerAccounts'); // Model CustomerAccount
const Product = require('../models/Products'); // Model Products

// Tạo mới một đánh giá
router.post('/add-reviews/:Tentaikhoan', async (req, res) => {
    try {
        const { Tentaikhoan } = req.params; // Lấy Tentaikhoan từ URL params
        const { MaSanPham, DanhGia, BinhLuan } = req.body; // Lấy thông tin đánh giá từ body

        // Tìm tài khoản dựa trên `Tentaikhoan`
        const customer = await CustomerAccount.findOne({ Tentaikhoan });
        if (!customer) {
            return res.status(404).json({ success: false, message: 'Tài khoản không tồn tại!' });
        }

        // Tìm sản phẩm dựa trên `MaSanPham`
        const product = await Product.findById(MaSanPham);
        if (!product) {
            return res.status(404).json({ success: false, message: 'Sản phẩm không tồn tại!' });
        }

        // Kiểm tra xem đã tồn tại đánh giá cho sản phẩm này hay chưa
        const existingReview = await Review.findOne({ Tentaikhoan, MaSanPham });
        if (existingReview) {
            return res.status(400).json({ success: false, message: 'Bạn đã đánh giá sản phẩm này trước đó!' });
        }

        // Tạo một đánh giá mới
        const newReview = new Review({
            Tentaikhoan, 
            MaSanPham, // Lưu ObjectId của sản phẩm
            DanhGia,
            BinhLuan,
            AnhDaiDien: customer.Anhtk, // Lấy ảnh từ CustomerAccount
            Hoten: customer.Hoten // Lấy họ tên từ CustomerAccount
        });

        // Lưu đánh giá vào cơ sở dữ liệu
        const savedReview = await newReview.save();
        res.status(201).json({ success: true, review: savedReview });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Lỗi server.', error });
    }
});

// Lấy tất cả đánh giá của một sản phẩm
router.get('/get-reviews-product/:MaSanPham', async (req, res) => {
    try {
        const { MaSanPham } = req.params;

        // Kiểm tra sản phẩm có tồn tại không
        const product = await Product.findById(MaSanPham);
        if (!product) {
            return res.status(404).json({ success: false, message: 'Sản phẩm không tồn tại!' });
        }

        // Tìm tất cả đánh giá của sản phẩm và populate thông tin khách hàng
        const data = await Review.find({ MaSanPham }).populate('Tentaikhoan', 'Hoten AnhDaiDien');
        res.status(200).json({ success: true, data });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Lỗi server.', error });
    }
});

// Lấy tất cả đánh giá của một tài khoản
router.get('/get-reviews-account/:Tentaikhoan', async (req, res) => {
    try {
        const { Tentaikhoan } = req.params;

        // Tìm tài khoản dựa trên `Tentaikhoan`
        const customer = await CustomerAccount.findOne({ Tentaikhoan });
        if (!customer) {
            return res.status(404).json({ success: false, message: 'Tài khoản không tồn tại!' });
        }

        // Tìm tất cả đánh giá của tài khoản
        const data = await Review.find({ Tentaikhoan }).populate('MaSanPham', 'TenSP GiaBan');
        res.status(200).json({ success: true, data });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Lỗi server.', error });
    }
});

// Xóa một đánh giá
router.delete('/delete-reviews/:id', async (req, res) => {
    try {
        const { id } = req.params;

        // Tìm và xóa đánh giá theo id
        const deletedReview = await Review.findByIdAndDelete(id);

        if (!deletedReview) {
            return res.status(404).json({ success: false, message: 'Đánh giá không tồn tại.' });
        }

        res.status(200).json({ success: true, message: 'Xóa đánh giá thành công.', review: deletedReview });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Lỗi server.', error });
    }
});


// Lấy danh sách đánh giá của người dùng cho sản phẩm cụ thể
router.get('/get-reviews-for-product-and-user/:MaSanPham/:Tentaikhoan', async (req, res) => {
    try {
        const { MaSanPham, Tentaikhoan } = req.params;

        // Tìm đánh giá của người dùng cho sản phẩm này
        const reviews = await Review.find({ MaSanPham, Tentaikhoan });

        if (reviews.length > 0) {
            // Nếu có đánh giá, trả về thông tin đánh giá
            return res.status(200).json({ success: true, data: reviews });
        } else {
            // Nếu không có đánh giá, trả về mảng rỗng
            return res.status(200).json({ success: true, data: [] });
        }
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Lỗi server', error });
    }
});


module.exports = router;
