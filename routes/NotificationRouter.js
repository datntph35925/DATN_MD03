const express = require('express');
const Notification = require('../models/Notification'); 
const CustomerAccounts = require('../models/CustomerAccounts');

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
// 5. API để tính tổng số thông báo chưa đọc của một tài khoản
router.get('/:tentaikhoan/tongtb', async (req, res) => {
    const { tentaikhoan } = req.params; // Lấy tên tài khoản từ params

    try {
        // Tìm tất cả các thông báo chưa đọc của tài khoản này
        const unreadNotificationsCount = await Notification.countDocuments({
            tentaikhoan,
            read: false // Chỉ đếm những thông báo chưa được đánh dấu là đã đọc
        });

        res.status(200).json({
            message: 'Lấy tổng số thông báo chưa đọc thành công',
            unreadCount: unreadNotificationsCount
        });
    } catch (error) {
        res.status(500).json({
            error: 'Lỗi khi tính tổng số thông báo chưa đọc',
            details: error.message
        });
    }
});
// 6. API để lấy tất cả thông báo (không cần tài khoản cụ thể)
router.get('/', async (req, res) => {
    try {
        // Lấy toàn bộ thông báo và sắp xếp theo thời gian mới nhất
        const notifications = await Notification.find({}).sort({ timestamp: -1 });
        res.status(200).json(notifications);
    } catch (error) {
        res.status(500).json({
            error: 'Lỗi khi lấy danh sách thông báo',
            details: error.message
        });
    }
});
// 7. API để gửi thông báo đến tất cả tài khoản
router.post('/gui_thong_bao_tat_ca', async (req, res) => {
    const { title, message, timestamp = new Date() } = req.body;

    try {
        // Lấy tất cả tài khoản và chỉ lấy trường Tentaikhoan (email)
        const accounts = await CustomerAccounts.find({}, { Tentaikhoan: 1 });

        // Kiểm tra xem có tài khoản nào trong hệ thống không
        if (accounts.length === 0) {
            return res.status(401).json({ error: 'Không có tài khoản nào trong hệ thống' });
        }

        // Tạo danh sách thông báo
        const notifications = accounts.map(account => {
            // Kiểm tra tài khoản có Tentaikhoan là string và không rỗng
            const tentaikhoan = String(account.Tentaikhoan).trim();  // Ép kiểu thành chuỗi và loại bỏ khoảng trắng

            if (!tentaikhoan || tentaikhoan === '') {
                console.warn(`Tài khoản không có Tentaikhoan hợp lệ: ${JSON.stringify(account)}`);
                return null; // Bỏ qua tài khoản không có Tentaikhoan hợp lệ
            }

            // Tạo đối tượng thông báo cho tài khoản hợp lệ
            return {
                Tentaikhoan: tentaikhoan,  // Lưu lại email
                title,  // Tiêu đề thông báo
                message,  // Nội dung thông báo
                timestamp,  // Thời gian thông báo
                read: false,  // Đánh dấu là chưa đọc
            };
        }).filter(notification => notification !== null); // Loại bỏ những thông báo không hợp lệ (null)

        // Kiểm tra nếu không có thông báo hợp lệ để gửi
        if (notifications.length === 0) {
            return res.status(400).json({ error: 'Không có thông báo hợp lệ để gửi' });
        }

        // Lưu tất cả các thông báo vào cơ sở dữ liệu
        await Notification.insertMany(notifications);

        // Trả về kết quả thông báo đã được gửi thành công
        res.status(200).json({
            message: 'Gửi thông báo thành công đến tất cả tài khoản',
            notificationsCount: notifications.length,
        });
    } catch (error) {
        // Bắt lỗi và trả về thông báo lỗi
        console.error("Lỗi khi gửi thông báo:", error);
        res.status(500).json({
            error: 'Lỗi khi gửi thông báo',
            details: error.message,
        });
    }
});


module.exports = router;
