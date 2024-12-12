package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("_id")
    private String id;             // Mã đơn hàng
    private String MaDonHang;
    private String Tentaikhoan;   // Tên tài khoản người dùng
    private List<ProductItemCart> SanPham; // Danh sách sản phẩm trong đơn hàng
    private String TenNguoiNhan;   // Tên người nhận
    private String DiaChiGiaoHang; // Địa chỉ giao hàng
    private String SoDienThoai;    // Số điện thoại người nhận
    private String TrangThai;      // Trạng thái đơn hàng
    private int TongSoLuong;       // Tổng số lượng sản phẩm trong đơn hàng
    private double TongTien;       // Tổng tiền đơn hàng
    private String PhuongThucThanhToan; // Phương thức thanh toán
    private Date NgayDatHang;    // Ngày đặt hàng
    private String NgayGiaoHang;   // Ngày giao hàng (nếu có)
    private String Voucher;

    public String getVoucher() {
        return Voucher;
    }

    public void setVoucher(String voucher) {
        Voucher = voucher;
    }

    public String getMaDonHang() {
        return MaDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        MaDonHang = maDonHang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter methods
    private List<String> selectedProducts;

    public List<String> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<String> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public Order() {
    }

    public Order(String diaChiGiaoHang, String id, String maDonHang, Date ngayDatHang, String ngayGiaoHang, String phuongThucThanhToan, List<ProductItemCart> sanPham, List<String> selectedProducts, String soDienThoai, String tenNguoiNhan, String tentaikhoan, int tongSoLuong, double tongTien, String trangThai, String voucher) {
        DiaChiGiaoHang = diaChiGiaoHang;
        this.id = id;
        MaDonHang = maDonHang;
        NgayDatHang = ngayDatHang;
        NgayGiaoHang = ngayGiaoHang;
        PhuongThucThanhToan = phuongThucThanhToan;
        SanPham = sanPham;
        this.selectedProducts = selectedProducts;
        SoDienThoai = soDienThoai;
        TenNguoiNhan = tenNguoiNhan;
        Tentaikhoan = tentaikhoan;
        TongSoLuong = tongSoLuong;
        TongTien = tongTien;
        TrangThai = trangThai;
        Voucher = voucher;
    }

    public Date getNgayDatHang() {
        return NgayDatHang;
    }

    public void setNgayDatHang(Date ngayDatHang) {
        NgayDatHang = ngayDatHang;
    }

    public String getDiaChiGiaoHang() {
        return DiaChiGiaoHang;
    }

    public void setDiaChiGiaoHang(String diaChiGiaoHang) {
        DiaChiGiaoHang = diaChiGiaoHang;
    }


    public String getNgayGiaoHang() {
        return NgayGiaoHang;
    }

    public void setNgayGiaoHang(String ngayGiaoHang) {
        NgayGiaoHang = ngayGiaoHang;
    }

    public String getPhuongThucThanhToan() {
        return PhuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        PhuongThucThanhToan = phuongThucThanhToan;
    }

    public List<ProductItemCart> getSanPham() {
        return SanPham;
    }

    public void setSanPham(List<ProductItemCart> sanPham) {
        SanPham = sanPham;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getTenNguoiNhan() {
        return TenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        TenNguoiNhan = tenNguoiNhan;
    }

    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        Tentaikhoan = tentaikhoan;
    }

    public int getTongSoLuong() {
        return TongSoLuong;
    }

    public void setTongSoLuong(int tongSoLuong) {
        TongSoLuong = tongSoLuong;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double tongTien) {
        TongTien = tongTien;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }
}

