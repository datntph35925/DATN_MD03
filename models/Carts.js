const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Định nghĩa Schema cho ProductItem trong giỏ hàng
const ProductItemSchema = new Schema({
  MaSanPham: { type: Schema.Types.ObjectId, ref: 'Product', required: true }, // Tham chiếu đến Product
  TenSP: { type: String, required: true }, // Tên sản phẩm để hiển thị
  SoLuongGioHang: { type: Number, required: true }, // Số lượng sản phẩm trong giỏ hàng
  Size: { type: Number, required: true }, // Kích cỡ của sản phẩm
  Gia: { type: Number, required: true },
  TongTien: { type: Number, required: true }, // Tổng tiền = Giá x Số lượng
  HinhAnh: [String], 
  soLuongTon: { type: Number, required: true } 
});

// Định nghĩa Schema cho GioHang
const CartSchema = new Schema({
  Tentaikhoan: { type: String, ref: 'CustomerAccounts', required: true, unique: true }, // Tham chiếu đến tài khoản người dùng
  SanPham: [ProductItemSchema], // Mảng chứa các sản phẩm trong giỏ hàng
  TongSoLuong: { type: Number, default: 0 }, // Tổng số lượng sản phẩm trong giỏ hàng
  TongGiaTri: { type: Number, default: 0 }, // Tổng giá trị của giỏ hàng
}, {
  timestamps: true // Tự động tạo thời gian tạo và cập nhật
});

module.exports = mongoose.model('cart', CartSchema);
