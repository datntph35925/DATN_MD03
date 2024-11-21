const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Schema cho Admin
const AdminSchema = new Schema({
    username: { type: String, required: true, unique: true }, // Tài khoản email
    password: { type: String, required: true } // Mật khẩu
});

// Tạo và xuất model
module.exports = mongoose.model('Admin', AdminSchema);
