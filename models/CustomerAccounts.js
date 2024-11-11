// models/CustomerAccounts.js
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const CustomerAccountsSchema = new Schema({
    Matk: { type: String, required: true }, // Mã tài khoản
    Tentaikhoan: { type: String, required: true }, // Tên tài khoản (email)
    Hoten: { type: String, required: true }, // Họ tên
    Matkhau: { type: String, required: true }, // Mật khẩu (mã hóa)
    Anhtk: { type: String, default: '' }, // Ảnh đại diện (nếu có)
    seq: { type: Number, default: 0 }, // Giá trị bộ đếm để tạo mã tài khoản tự động
    verificationCode: { type: String }, // Mã xác thực
    createdAt: { type: Date } // Thời gian mã được tạo
});

module.exports = mongoose.model('CustomerAccounts', CustomerAccountsSchema);
