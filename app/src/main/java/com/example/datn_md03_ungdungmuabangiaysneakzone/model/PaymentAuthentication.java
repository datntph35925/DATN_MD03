package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class PaymentAuthentication {
    private String MaDonHang;
    private String Tentaikhoan;
    private double SoTien;

    public PaymentAuthentication() {
    }

    public String getMaDonHang() {
        return MaDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        MaDonHang = maDonHang;
    }



    public double getSoTien() {
        return SoTien;
    }

    public void setSoTien(double soTien) {
        SoTien = soTien;
    }

    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        Tentaikhoan = tentaikhoan;
    }

}
