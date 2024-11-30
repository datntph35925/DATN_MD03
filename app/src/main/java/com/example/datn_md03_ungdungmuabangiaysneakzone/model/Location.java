package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("_id")
    private String _id;
    private String Tentaikhoan;
    private String tenNguoiNhan;
    private String diaChi;
    private String sdt;

    public Location() {
    }

    public Location(String _id, String diaChi, String sdt, String tenNguoiNhan, String tentaikhoan) {
        this._id = _id;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.tenNguoiNhan = tenNguoiNhan;
        Tentaikhoan = tentaikhoan;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        Tentaikhoan = tentaikhoan;
    }
}
