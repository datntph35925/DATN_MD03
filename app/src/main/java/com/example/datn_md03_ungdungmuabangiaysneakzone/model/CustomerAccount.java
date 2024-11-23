package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import android.net.Uri;

public class CustomerAccount {
    private String Matk;
    private String Tentaikhoan;
    private String Hoten;
    private String Matkhau;
    private String Anhtk;
    private int seq;
    private String verificationCode;
    private String EmailMoi;
    private String MatkhauMoi; // Thêm trường MatkhauMoi


    // Constructor
//    public CustomerAccount(String matk, String tentaikhoan, String hoten, String matkhau, String anhtk, int seq) {
//        this.Matk = matk;
//        this.Tentaikhoan = tentaikhoan;
//        this.Hoten = hoten;
//        this.Matkhau = matkhau;
//        this.Anhtk = anhtk;
//        this.seq = seq;
//    }
//    public CustomerAccount(String hoten, String email, String password) {
//        this.Hoten = hoten;
//        this.Tentaikhoan = email;
//        this.Matkhau = password;
//    }
//    public CustomerAccount( String email, String password) {
//        this.Tentaikhoan = email;
//        this.Matkhau = password;
//    }
//    public CustomerAccount() {
//    }

    private CustomerAccount(Builder builder) {
        this.Matk = builder.matk;
        this.Tentaikhoan = builder.tentaikhoan;
        this.Hoten = builder.hoten;
        this.Matkhau = builder.matkhau;
        this.Anhtk = builder.anhtk;
        this.seq = builder.seq;
        this.verificationCode = builder.verificationCode;
        this.EmailMoi = builder.emailMoi;
        this.MatkhauMoi = builder.matkhauMoi; // Thêm MatkhauMoi từ Builder
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

    public String getMatkhauMoi() {
        return MatkhauMoi;
    }

    // Setter cho MatkhauMoi
    public void setMatkhauMoi(String matkhauMoi) {
        this.MatkhauMoi = matkhauMoi;
    }

    public static class Builder {
        private String matk;
        private String tentaikhoan;
        private String hoten;
        private String matkhau;
        private String anhtk;
        private int seq;
        private String verificationCode;
        private String emailMoi;
        private String matkhauMoi; // Thêm trường này vào Builder

        public Builder setMatk(String matk) {
            this.matk = matk;
            return this;
        }

        public Builder setTentaikhoan(String tentaikhoan) {
            this.tentaikhoan = tentaikhoan;
            return this;
        }

        public Builder setHoten(String hoten) {
            this.hoten = hoten;
            return this;
        }

        public Builder setMatkhau(String matkhau) {
            this.matkhau = matkhau;
            return this;
        }

        public Builder setAnhtk(String anhtk) {
            this.anhtk = anhtk;
            return this;
        }

        public Builder setSeq(int seq) {
            this.seq = seq;
            return this;
        }

        public Builder setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
            return this;
        }

        public Builder setEmailMoi(String emailMoi) {
            this.emailMoi = emailMoi;
            return this;
        }

        public Builder setMatkhauMoi(String matkhauMoi) {
            this.matkhauMoi = matkhauMoi;
            return this;
        }

        // Phương thức build để tạo đối tượng CustomerAccount
        public CustomerAccount build() {
            return new CustomerAccount(this);
        }
    }
}

