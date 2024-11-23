package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.io.Serializable;
import java.util.List;

public class ProductItemCart implements Serializable {
    public String MaSanPham; // ID của sản phẩm
    public String TenSP; // Tên sản phẩm
    public int SoLuongGioHang; // Số lượng sản phẩm được chọn mua
    public int Size; // Kích cỡ của sản phẩm
    public double Gia; // Giá của sản phẩm
    public double TongTien; // Tổng tiền = Giá x Số lượng
    public List<String> HinhAnh;

    private boolean isChecked;

   private int SoLuongTon;

    public int getSoLuongTon() {
        return SoLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        SoLuongTon = soLuongTon;
    }

    // Trạng thái checkbox

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<String> getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(List<String> hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public ProductItemCart() {
    }

    // Constructor
    public ProductItemCart(String maSanPham, String tenSP, int soLuongGioHang, int size, double gia) {
        this.MaSanPham = maSanPham;
        this.TenSP = tenSP;
        this.SoLuongGioHang = soLuongGioHang;
        this.Size = size;
        this.Gia = gia;
        this.TongTien = gia * soLuongGioHang; // Tính tổng tiền
    }

    // Getters và Setters
    public String getMaSanPham() {
        return MaSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        MaSanPham = maSanPham;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public int getSoLuongGioHang() {
        return SoLuongGioHang;
    }

    public void setSoLuongGioHang(int soLuongGioHang) {
        SoLuongGioHang = soLuongGioHang;
        this.TongTien = this.Gia * soLuongGioHang; // Cập nhật tổng tiền khi thay đổi số lượng
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public double getGia() {
        return Gia;
    }

    public void setGia(double gia) {
        Gia = gia;
        this.TongTien = gia * this.SoLuongGioHang; // Cập nhật tổng tiền khi thay đổi giá
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double tongTien) {
        TongTien = tongTien;
    }
}