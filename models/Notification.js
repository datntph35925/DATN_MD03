const mongoose = require('mongoose');

// Định nghĩa Schema cho thông báo
const NotificationSchema = new mongoose.Schema({
    tentaikhoan: { type: String, required: true }, // Tên tài khoản của người nhận
    title: { type: String, required: true },      // Tiêu đề thông báo
    message: { type: String, required: true },    // Nội dung thông báo
    timestamp: { type: Date, default: Date.now }, // Thời gian gửi thông báo
    read: { type: Boolean, default: false },      // Trạng thái đã đọc
});

// Tạo Model từ Schema
const Notification = mongoose.model('Notification', NotificationSchema);

module.exports = Notification;
