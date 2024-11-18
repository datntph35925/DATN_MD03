const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Product = new Schema({
    MaSanPham: { type: String, required: true },
    TenSP: { type: String, required: true },
    ThuongHieu: { type: String, required: true },
    KichThuoc: [
        {
            size: { type: Number, required: true }, // Kích cỡ của sản phẩm
            soLuongTon: { type: Number, required: true } // Số lượng tồn tương ứng với kích cỡ
        }
    ],
    GiaBan: { type: Number, required: true },
    MoTa: String,
    HinhAnh: [String],
    TrangThaiYeuThich: { type: Boolean, default: false }
}, {
    timestamps: true
});

module.exports = mongoose.model('product', Product);