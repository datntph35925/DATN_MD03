// models/Location.js
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const LocationSchema = new Schema({
    Tentaikhoan: { type: String, required: true }, // Mã tài khoản người dùng
    tenNguoiNhan: { type: String, required: true }, // Tên người nhận
    diaChi: { type: String, required: true }, // Địa chỉ giao hàng
    sdt: { type: String, required: true }, // Số điện thoại người nhận
});

module.exports = mongoose.model('Location', LocationSchema);

