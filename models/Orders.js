const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Định nghĩa Schema cho ProductItem trong đơn hàng (giống như trong giỏ hàng)
const ProductItemSchema = new Schema({
  MaSanPham: { type: Schema.Types.ObjectId, ref: 'Product', required: true },
  TenSP: { type: String, required: true },
  SoLuongGioHang: { type: Number, required: true },
  Size: { type: Number, required: true },
  Gia: { type: Number, required: true },
  TongTien: { type: Number, required: true },
  HinhAnh: [String],
});

// Định nghĩa Schema cho đơn hàng (Order)
const OrderSchema = new Schema({
  MaDonHang: { type: String, unique: true, required: true }, // Mã đơn hàng
  Tentaikhoan: { type: String, ref: 'CustomerAccounts', required: true }, // Tài khoản người dùng
  SanPham: [ProductItemSchema], // Mảng sản phẩm trong đơn hàng
  TenNguoiNhan: { type: String, required: true },
  DiaChiGiaoHang: { type: String, required: true },
  SoDienThoai: { type: String, required: true },
  TrangThai: {
    type: String,
    enum: ['Chờ xử lý', 'Đang giao', 'Đã giao', 'Hủy'],
    default: 'Chờ xử lý',
  },
  TongSoLuong: { type: Number, required: true },
  TongTien: { type: Number, required: true },
  PhuongThucThanhToan: {
    type: String,
    enum: ['Thanh toán khi nhận hàng (COD)', 'Thanh toán qua ngân hàng', 'Thẻ tín dụng'],
    default: 'Thanh toán khi nhận hàng (COD)',
  },
  NgayDatHang: { type: Date, default: Date.now },
  NgayGiaoHang: { type: Date },
  Voucher: { type: String, default: null }, // Mã voucher
  VoucherUsedBy: [{ type: String, ref: 'CustomerAccounts' }], // Lưu các tài khoản đã sử dụng voucher này
}, {
  timestamps: true
});

module.exports = mongoose.model('Order', OrderSchema);
