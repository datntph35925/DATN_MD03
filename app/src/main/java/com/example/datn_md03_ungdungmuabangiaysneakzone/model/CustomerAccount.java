package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class CustomerAccount {
    private String Matk;
    private String Tentaikhoan;
    private String Hoten;
    private String Matkhau;
    private String Anhtk;
    private int seq;
    private String verificationCode;
    private String EmailMoi;

    // Constructor private để chỉ Builder có thể tạo đối tượng
    private CustomerAccount(Builder builder) {
        this.Matk = builder.matk;
        this.Tentaikhoan = builder.tentaikhoan;
        this.Hoten = builder.hoten;
        this.Matkhau = builder.matkhau;
        this.Anhtk = builder.anhtk;
        this.seq = builder.seq;
        this.verificationCode = builder.verificationCode;
        this.EmailMoi = builder.emailMoi;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public String getEmailMoi() {
        return EmailMoi;
    }

    // Builder class
    public static class Builder {
        private String matk;
        private String tentaikhoan;
        private String hoten;
        private String matkhau;
        private String anhtk;
        private int seq;
        private String verificationCode;
        private String emailMoi;

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

        // Phương thức build để tạo đối tượng CustomerAccount
        public CustomerAccount build() {
            return new CustomerAccount(this);
        }
    }
}
