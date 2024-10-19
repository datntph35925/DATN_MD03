package com.example.datn_md03_ungdungmuabangiaysneakzone.Domain;

public class SanPham {
    String tenSanPham;
    Double gia;
    String PicUrl;

    public SanPham() {
    }

    public SanPham(String tenSanPham, Double gia, String picUrl) {
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        PicUrl = picUrl;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
