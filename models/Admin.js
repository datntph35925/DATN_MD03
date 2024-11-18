const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const AdminSchema = new Schema({
  username: { type: String, required: true }, // Tên tài khoản của admin (root)
  password: { type: String, required: true }  // Mật khẩu (không mã hóa)
});

module.exports = mongoose.model('Admin', AdminSchema);
