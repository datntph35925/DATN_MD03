const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const AdminSchema = new Schema({
    username: { 
        type: String, 
        required: true, 
        unique: true // Đảm bảo username (email) là duy nhất 
    }, 
    password: { 
        type: String, 
        required: true // Lưu mật khẩu của admin
    },
    verificationCode: { 
        type: String, 
        default: null // Lưu mã OTP
    },
    codeExpiry: { 
        type: Date, 
        default: null // Lưu thời gian hết hạn của mã OTP
    }
});

// Xuất model Admin
module.exports = mongoose.model('Admin', AdminSchema);
