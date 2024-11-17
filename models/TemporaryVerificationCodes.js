const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Định nghĩa mô hình TemporaryVerificationCodes
const TemporaryVerificationCodesSchema = new Schema({
    Tentaikhoan: { type: String, required: true }, // Tên tài khoản (email)
    Hoten: { type: String, required: true },       // Họ tên
    Matkhau: { type: String, required: true },     // Mật khẩu (dạng thuần túy hoặc mã hóa nếu cần)
    Anhtk: { type: String, default: '' },          // Ảnh đại diện (nếu có)
    verificationCode: { type: String },            // Mã xác thực
    createdAt: { type: Date, default: Date.now, expires: 300 } // Tự động xóa sau 5 phút (300 giây)
});

// Xuất mô hình
module.exports = mongoose.model('TemporaryVerificationCodes', TemporaryVerificationCodesSchema);
