package com.example.datn_md03_ungdungmuabangiaysneakzone.Domain;

public class DiaChi {
    public String Ten;
    public String DiaChi;
    public int soDT;

    public DiaChi() {
    }

    public DiaChi(String ten, String diaChi, int soDT) {
        Ten = ten;
        DiaChi = diaChi;
        this.soDT = soDT;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public int getSoDT() {
        return soDT;
    }

    public void setSoDT(int soDT) {
        this.soDT = soDT;
    }
}
