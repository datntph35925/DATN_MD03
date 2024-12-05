const express = require('express');
const Notification = require('../models/Notification'); 

const router = express.Router();

// 1. API để gửi thông báo
router.post('/gui_thong_bao', async (req, res) => {
    // Kiểm tra nếu req.body không tồn tại
    if (!req.body || Object.keys(req.body).length === 0) {
        return res.status(400).json({ error: 'Body của request bị thiếu hoặc không hợp lệ!' });
    }

    const { tentaikhoan, title, message } = req.body;

    try {
        const notification = new Notification({ tentaikhoan, title, message });
        await notification.save();

        res.status(201).json({
            message: 'Thông báo đã được gửi thành công!',
            notification
        });
    } catch (error) {
        res.status(500).json({
            error: 'Lỗi khi gửi thông báo',
            details: error.message
        });
    }
});

// 2. API để lấy danh sách thông báo theo tài khoản
router.get('/:tentaikhoan', async (req, res) => {
    const { tentaikhoan } = req.params; // Lấy tên tài khoản từ params

    try {
        const notifications = await Notification.find({ tentaikhoan }).sort({ timestamp: -1 }); // Sắp xếp theo thời gian mới nhất
        res.status(200).json(notifications);
    } catch (error) {
        res.status(500).json({
            error: 'Lỗi khi lấy danh sách thông báo',
            details: error.message
        });
    }
});

// 3. API để đánh dấu thông báo đã đọc
router.put('/:id/read', async (req, res) => {
    const { id } = req.params; // Lấy ID của thông báo

    try {
        const notification = await Notification.findByIdAndUpdate(
            id,
            { read: true },
            { new: true } // Trả về thông báo đã được cập nhật
        );

        if (!notification) {
            return res.status(404).json({ error: 'Thông báo không tồn tại' });
        }

        res.status(200).json({
            message: 'Thông báo đã được đánh dấu là đã đọc',
            notification
        });
    } catch (error) {
        res.status(500).json({
            error: 'Lỗi khi đánh dấu thông báo đã đọc',
            details: error.message
        });
    }
});
// 4. API để xóa thông báo theo ID
router.delete('/:id', async (req, res) => {
    const { id } = req.params; // Lấy ID của thông báo từ params

    try {
        const notification = await Notification.findByIdAndDelete(id);

        if (!notification) {
            return res.status(404).json({ error: 'Thông báo không tồn tại' });
        }

        res.status(200).json({
            message: 'Thông báo đã được xóa thành công',
            notification
        });
    } catch (error) {
        res.status(500).json({
            error: 'Lỗi khi xóa thông báo',
            details: error.message
        });
    }
});


module.exports = router;
