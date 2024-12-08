package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.util.Date;

public class Voucher {
    private String _id;
    private String MaVoucher;
    private double GiaTri;
    private String LoaiVoucher;
    private String TrangThai;
    private Date NgayBatDau;
    private Date NgayKetThuc;

    public Voucher() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getGiaTri() {
        return GiaTri;
    }

    public void setGiaTri(double giaTri) {
        GiaTri = giaTri;
    }

    public String getLoaiVoucher() {
        return LoaiVoucher;
    }

    public void setLoaiVoucher(String loaiVoucher) {
        LoaiVoucher = loaiVoucher;
    }

    public String getMaVoucher() {
        return MaVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        MaVoucher = maVoucher;
    }

    public Date getNgayBatDau() {
        return NgayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        NgayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        NgayKetThuc = ngayKetThuc;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }
}
