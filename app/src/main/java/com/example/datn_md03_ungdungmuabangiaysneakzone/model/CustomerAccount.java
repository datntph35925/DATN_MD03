package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class CustomerAccount {
    private String Matk;
    private String Tentaikhoan;
    private String Hoten;
    private String Matkhau;
    private String Anhtk;
    private int seq;

    // Constructor
    public CustomerAccount(String matk, String tentaikhoan, String hoten, String matkhau, String anhtk, int seq) {
        this.Matk = matk;
        this.Tentaikhoan = tentaikhoan;
        this.Hoten = hoten;
        this.Matkhau = matkhau;
        this.Anhtk = anhtk;
        this.seq = seq;
    }
    public CustomerAccount(String hoten, String email, String password) {
        this.Hoten = hoten;
        this.Tentaikhoan = email;
        this.Matkhau = password;
    }
    public CustomerAccount( String email, String password) {
        this.Tentaikhoan = email;
        this.Matkhau = password;
    }
    public CustomerAccount() {
    }


    // Getters
    public String getMatk() {
        return Matk;
    }

    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public String getHoten() {
        return Hoten;
    }

    public String getMatkhau() {
        return Matkhau;
    }

    public String getAnhtk() {
        return Anhtk;
    }

    public int getSeq() {
        return seq;
    }

    // Setters
    public void setMatk(String matk) {
        this.Matk = matk;
    }

    public void setTentaikhoan(String tentaikhoan) {
        this.Tentaikhoan = tentaikhoan;
    }

    public void setHoten(String hoten) {
        this.Hoten = hoten;
    }

    public void setMatkhau(String matkhau) {
        this.Matkhau = matkhau;
    }

    public void setAnhtk(String anhtk) {
        this.Anhtk = anhtk;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}

