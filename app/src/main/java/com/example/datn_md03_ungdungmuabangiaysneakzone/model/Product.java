package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("_id")  //trường -id nhận từ id của api
    private String id;
    private String Masanpham;
    private String TenSP;
    private String Thuonghieu;
    private List<KichThuoc> KichThuoc;
    private double GiaBan;
    private String MoTa;
    private List<String> HinhAnh;
    private boolean TrangThaiYeuThich;

    private String productId;
    private String name;
    private double price;
    private List<String> images;
    private int quantitySold;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isTrangThaiYeuThich() {
        return TrangThaiYeuThich;
    }

    public void setTrangThaiYeuThich(boolean trangThaiYeuThich) {
        TrangThaiYeuThich = trangThaiYeuThich;
    }

    public Product() {
    }


    public Product(double giaBan, List<String> hinhAnh, String id, List<com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc> kichThuoc, String masanpham, String moTa, String tenSP, String thuonghieu, boolean trangThaiYeuThich) {
        GiaBan = giaBan;
        HinhAnh = hinhAnh;
        this.id = id;
        KichThuoc = kichThuoc;
        Masanpham = masanpham;
        MoTa = moTa;
        TenSP = tenSP;
        Thuonghieu = thuonghieu;
        TrangThaiYeuThich = trangThaiYeuThich;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<KichThuoc> getKichThuoc() {
        return KichThuoc;
    }

    public void setKichThuoc(List<KichThuoc> kichThuoc) {
        KichThuoc = kichThuoc;
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

    public String getMasanpham() {
        return Masanpham;
    }

    public void setMasanpham(String masanpham) {
        Masanpham = masanpham;
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

}
