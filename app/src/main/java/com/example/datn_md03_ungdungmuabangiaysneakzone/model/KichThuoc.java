package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class KichThuoc {
    private int size;
    private int soLuongTon; // Số lượng tồn

    public KichThuoc() {
    }

    public KichThuoc(int size, int soLuongTon) {
        this.size = size;
        this.soLuongTon = soLuongTon;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }


}
