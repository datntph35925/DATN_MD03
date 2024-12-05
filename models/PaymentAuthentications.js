const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const PaymentAuthenticationSchema = new Schema({
    MaDonHang: { 
        type: String, 
        unique: true, 
        required: true 
    },
    Tentaikhoan: { 
        type: String, 
        required: true 
    },
    SoTien: {
        type: Number,
        required: true,
    },
    TrangThai: {
        type: String,
        enum: ['Chưa thanh toán', 'Đã thanh toán', 'Thất bại'],
        default: 'Chưa thanh toán',
        required: true,
    },
    NgayTao: {
        type: Date,
        default: Date.now,
    },
}, {
    timestamps: true
});

module.exports = mongoose.model('PaymentAuthentication', PaymentAuthenticationSchema);
