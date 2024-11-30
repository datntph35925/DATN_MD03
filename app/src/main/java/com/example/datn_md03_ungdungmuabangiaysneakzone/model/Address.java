package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("_id")
    private String id;
    private String Tentaikhoan;
    private String TenNguoiNhan, DiaChi, SDT;

    public Address() {
    }

    public Address(String diaChi, String id, String SDT, String tenNguoiNhan, String tentaikhoan) {
        DiaChi = diaChi;
        this.id = id;
        this.SDT = SDT;
        TenNguoiNhan = tenNguoiNhan;
        Tentaikhoan = tentaikhoan;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
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
}
