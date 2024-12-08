const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const VoucherSchema = new Schema({
    MaVoucher: { type: String, unique: true, required: true }, // Mã voucher
    GiaTri: { type: Number, required: true }, // Giá trị giảm giá (tính theo % hoặc giá trị cố định)
    LoaiVoucher: {
        type: String,
        enum: ['Giảm giá theo %', 'Giảm giá cố định'], // Loại voucher (có thể giảm theo % hoặc giá trị cố định)
        required: true
    },
    TrangThai: {
        type: String,
        enum: ['Có thể sử dụng', 'Không thể sử dụng'],
        default: 'Có thể sử dụng', // Trạng thái voucher
        required: true
    },
    NgayBatDau: { type: Date, required: true }, // Ngày bắt đầu áp dụng
    NgayKetThuc: { type: Date, required: true }, // Ngày kết thúc áp dụng
    UsedBy: [{ type: String, ref: 'CustomerAccounts' }], // Lưu danh sách khách hàng đã sử dụng voucher
}, {
    timestamps: true
});

// Kiểm tra xem khách hàng đã sử dụng voucher chưa
VoucherSchema.methods.canUseVoucher = function (customerId) {
    if (this.TrangThai === 'Không thể sử dụng') {
        return false;
    }
    if (this.UsedBy.includes(customerId)) {
        return false; // Khách hàng đã sử dụng voucher
    }
    return true; // Khách hàng chưa sử dụng voucher
};

module.exports = mongoose.model('Voucher', VoucherSchema);