package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.util.List;

public class ProductYeuThich {
    private String MaSanPham;
    private String TenSP;
    private String Thuonghieu;
    private List<KichThuoc> KichThuoc;
    private double GiaBan;
    private String MoTa;
    private List<String> HinhAnh;
    private Boolean TrangThaiYeuThich;

    public ProductYeuThich() {
    }

    public ProductYeuThich(double giaBan, List<String> hinhAnh, List<com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc> kichThuoc, String masanpham, String moTa, String tenSP, String thuonghieu, Boolean trangThaiYeuThich) {
        GiaBan = giaBan;
        HinhAnh = hinhAnh;
        KichThuoc = kichThuoc;
        MaSanPham = masanpham;
        MoTa = moTa;
        TenSP = tenSP;
        Thuonghieu = thuonghieu;
        TrangThaiYeuThich = trangThaiYeuThich;
    }

    public double getGiaBan() {
        return GiaBan;
    }

    public void setGiaBan(double giaBan) {
        GiaBan = giaBan;
    }

    public List<String> getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(List<String> hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public List<com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc> getKichThuoc() {
        return KichThuoc;
    }

    public void setKichThuoc(List<com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc> kichThuoc) {
        KichThuoc = kichThuoc;
    }

    public String getMasanpham() {
        return MaSanPham;
    }

    public void setMasanpham(String masanpham) {
        MaSanPham = masanpham;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getThuonghieu() {
        return Thuonghieu;
    }

    public void setThuonghieu(String thuonghieu) {
        Thuonghieu = thuonghieu;
    }

    public Boolean getTrangThaiYeuThich() {
        return TrangThaiYeuThich;
    }

    public void setTrangThaiYeuThich(Boolean trangThaiYeuThich) {
        TrangThaiYeuThich = trangThaiYeuThich;
    }
}
