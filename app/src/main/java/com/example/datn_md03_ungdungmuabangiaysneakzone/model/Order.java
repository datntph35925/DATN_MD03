package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String Tentaikhoan;   // Tên tài khoản người dùng
    private List<ProductItemCart> SanPham; // Danh sách sản phẩm trong đơn hàng
    private String TenNguoiNhan;   // Tên người nhận
    private String DiaChiGiaoHang; // Địa chỉ giao hàng
    private String SoDienThoai;    // Số điện thoại người nhận
    private String TrangThai;      // Trạng thái đơn hàng
    private int TongSoLuong;       // Tổng số lượng sản phẩm trong đơn hàng
    private double TongTien;       // Tổng tiền đơn hàng
    private String PhuongThucThanhToan; // Phương thức thanh toán
    private String NgayDatHang;    // Ngày đặt hàng
    private String NgayGiaoHang;   // Ngày giao hàng (nếu có)

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

    public Order(String diaChiGiaoHang, String ngayDatHang, String ngayGiaoHang, String phuongThucThanhToan, List<ProductItemCart> sanPham, String soDienThoai, String tenNguoiNhan, String tentaikhoan, int tongSoLuong, double tongTien, String trangThai) {
        DiaChiGiaoHang = diaChiGiaoHang;
        NgayDatHang = ngayDatHang;
        NgayGiaoHang = ngayGiaoHang;
        PhuongThucThanhToan = phuongThucThanhToan;
        SanPham = sanPham;
        SoDienThoai = soDienThoai;
        TenNguoiNhan = tenNguoiNhan;
        Tentaikhoan = tentaikhoan;
        TongSoLuong = tongSoLuong;
        TongTien = tongTien;
        TrangThai = trangThai;
    }

    public String getDiaChiGiaoHang() {
        return DiaChiGiaoHang;
    }

    public void setDiaChiGiaoHang(String diaChiGiaoHang) {
        DiaChiGiaoHang = diaChiGiaoHang;
    }

    public String getNgayDatHang() {
        return NgayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        NgayDatHang = ngayDatHang;
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

