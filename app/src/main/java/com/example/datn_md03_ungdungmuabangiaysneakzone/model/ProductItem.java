package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.io.Serializable;
import java.util.List;

public class ProductItem implements Serializable {

    private String MaSanPham;  // Mã sản phẩm (sử dụng String để lưu ObjectId)
    private String TenSP;      // Tên sản phẩm
    private int SoLuong;       // Số lượng sản phẩm
    private int Size;          // Kích cỡ sản phẩm
    private double Gia;        // Giá sản phẩm
    private double TongTien;   // Tổng tiền (Giá * Số lượng)
    public List<String> HinhAnh;

    public List<String> getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(List<String> hinhAnh) {
        HinhAnh = hinhAnh;
    }

    // Getter and Setter methods


    public ProductItem() {
    }

    public ProductItem(double gia, String maSanPham, int size, int soLuong, String tenSP, double tongTien) {
        Gia = gia;
        MaSanPham = maSanPham;
        Size = size;
        SoLuong = soLuong;
        TenSP = tenSP;
        TongTien = tongTien;
    }

    public double getGia() {
        return Gia;
    }

    public void setGia(double gia) {
        Gia = gia;
    }

    public String getMaSanPham() {
        return MaSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        MaSanPham = maSanPham;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double tongTien) {
        TongTien = tongTien;
    }
}

