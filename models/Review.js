const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const ReviewSchema = new Schema({
Tentaikhoan: { type: String, required: true },
  MaSanPham: { type: Schema.Types.ObjectId, ref: 'product', required: true }, // Tham chiếu đến Product
  DanhGia: { type: Number, required: true, min: 1, max: 5 }, // Điểm đánh giá (1 đến 5)
  BinhLuan: { type: String }, // Nội dung bình luận (tùy chọn)
  AnhDaiDien: { type: String, default: '' }, // Ảnh đại diện (nếu có)
  Hoten: { type: String, required: true },
  timestamp: { type: Date, default: Date.now }, // Họ tên
});


module.exports = mongoose.model('Review', ReviewSchema);