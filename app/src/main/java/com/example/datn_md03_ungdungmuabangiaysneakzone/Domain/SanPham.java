package com.example.datn_md03_ungdungmuabangiaysneakzone.Domain;

public class SanPham {
    String tenSanPham;
    Double gia;
    int PicUrl;

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public int getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(int picUrl) {
        PicUrl = picUrl;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public SanPham(String tenSanPham, Double gia, int PicUrl) {
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.PicUrl = PicUrl;
    }
}
