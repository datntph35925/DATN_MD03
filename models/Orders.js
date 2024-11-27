const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Định nghĩa Schema cho ProductItem trong đơn hàng (giống như trong giỏ hàng)
const ProductItemSchema = new Schema({
  MaSanPham: { type: Schema.Types.ObjectId, ref: 'Product', required: true }, // Tham chiếu đến Product
  TenSP: { type: String, required: true }, // Tên sản phẩm
  SoLuong: { type: Number, required: true }, // Số lượng sản phẩm trong đơn hàng
  Size: { type: Number, required: true }, // Kích cỡ của sản phẩm
  Gia: { type: Number, required: true }, // Giá của sản phẩm
  TongTien: { type: Number, required: true }, // Tổng tiền = Giá x Số lượng
  HinhAnh: [String], 
});

// Định nghĩa Schema cho đơn hàng (Order)
const OrderSchema = new Schema({
  Tentaikhoan: { type: String, ref: 'CustomerAccounts', required: true }, // Tham chiếu đến tài khoản người dùng
  SanPham: [ProductItemSchema], // Mảng chứa các sản phẩm trong đơn hàng
  TenNguoiNhan: { type: String, required: true }, // Địa chỉ giao hàng (giữ nguyên kiểu String)
  DiaChiGiaoHang: { type: String, required: true }, // Địa chỉ giao hàng (giữ nguyên kiểu String)
  SoDienThoai: { type: String, required: true }, // Số điện thoại người nhận
  TrangThai: {
    type: String,
    enum: ['Chờ xử lý', 'Đang giao', 'Đã giao', 'Hủy'],
    default: 'Chờ xử lý', // Trạng thái đơn hàng (Chờ xử lý, Đang giao, Đã giao, Hủy)
  },
  TongSoLuong: { type: Number, required: true }, // Tổng số lượng sản phẩm trong đơn hàng
  TongTien: { type: Number, required: true }, // Tổng tiền của đơn hàng
  PhuongThucThanhToan: {
    type: String,
    enum: ['Thanh toán khi nhận hàng (COD)', 'Chuyển khoản', 'Thẻ tín dụng'],
    default: 'Thanh toán khi nhận hàng (COD)', // Phương thức thanh toán (Tiền mặt, Chuyển khoản, Thẻ tín dụng)
  },
  NgayDatHang: { type: Date, default: Date.now }, // Ngày đặt hàng
  NgayGiaoHang: { type: Date }, // Ngày giao hàng (nếu đã giao)
}, {
  timestamps: true
});

module.exports = mongoose.model('order', OrderSchema);
