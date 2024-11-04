const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Product = new Schema({
    Masanpham: { type: String, required: true },
    TenSP: { type: String, required: true },
    Thuonghieu: { type: String, required: true },
    Size: { type: [Number], required: true },
    MauSac: { type: String, required: true },
    SoLuongTon: { type: Number, required: true },
    GiaBan: { type: Number, required: true },
    MoTa: String,
    HinhAnh: [String],
    TrangThaiYeuThich: { type: Boolean, default: false }
}, {
    timestamps: true
});

module.exports = mongoose.model('product', Product);

