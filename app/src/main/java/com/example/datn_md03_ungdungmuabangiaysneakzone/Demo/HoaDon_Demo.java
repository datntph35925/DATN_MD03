package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

public class HoaDon_Demo {
    private String tenNguoiMua;
    private String diaChi;
    private String sdt;
    private int soLuongSP;
    private double tongTien;
    private String date;

    // Constructor
    public HoaDon_Demo(String tenNguoiMua, String diaChi, String sdt, int soLuongSP, double tongTien, String date) {
        this.tenNguoiMua = tenNguoiMua;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.soLuongSP = soLuongSP;
        this.tongTien = tongTien;
        this.date = date;
    }

    // Getter and Setter methods
    public String getTenNguoiMua() {
        return tenNguoiMua;
    }

    public void setTenNguoiMua(String tenNguoiMua) {
        this.tenNguoiMua = tenNguoiMua;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getSoLuongSP() {
        return soLuongSP;
    }

    public void setSoLuongSP(int soLuongSP) {
        this.soLuongSP = soLuongSP;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
