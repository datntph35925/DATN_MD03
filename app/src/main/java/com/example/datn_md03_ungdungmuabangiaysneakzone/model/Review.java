package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Review {
    private String Tentaikhoan;
    private String MaSanPham;
    private float DanhGia;
    private String BinhLuan;
    private String AnhDaiDien;
    private String Hoten;

    @SerializedName("timestamp")
    private Date timestamp;    // Thời gian cập nhật đánh giá

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        this.Tentaikhoan = tentaikhoan;
    }

    public String getMaSanPham() {
        return MaSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.MaSanPham = maSanPham;
    }

    public float getDanhGia() {
        return DanhGia;
    }

    public void setDanhGia(float danhGia) {
        this.DanhGia = danhGia;
    }

    public String getBinhLuan() {
        return BinhLuan;
    }

    public void setBinhLuan(String binhLuan) {
        this.BinhLuan = binhLuan;
    }

    public String getAnhDaiDien() {
        return AnhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.AnhDaiDien = anhDaiDien;
    }

    public String getHoten() {
        return Hoten;
    }

    public void setHoten(String hoten) {
        this.Hoten = hoten;
    }
}
