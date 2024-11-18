package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class TemporaryVerificationCode {
    private String Tentaikhoan; // Email
    private String Hoten; // Tên người dùng (có thể null nếu không cần)
    private String Matkhau; // Mật khẩu (có thể null nếu không cần)
    private String verificationCode; // Mã xác thực
    private String createdAt; // Thời gian tạo mã (có thể null nếu không cần)

    // Constructor đầy đủ
    public TemporaryVerificationCode(String Tentaikhoan, String Hoten, String Matkhau, String verificationCode, String createdAt) {
        this.Tentaikhoan = Tentaikhoan;
        this.Hoten = Hoten;
        this.Matkhau = Matkhau;
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
    }

    public TemporaryVerificationCode() {
        // Không làm gì cả
    }
    // Constructor chỉ dùng khi gửi mã xác thực
    public TemporaryVerificationCode(String Tentaikhoan) {
        this.Tentaikhoan = Tentaikhoan;
    }

    // Constructor chỉ dùng khi xác thực mã
    public TemporaryVerificationCode(String Tentaikhoan, String verificationCode) {
        this.Tentaikhoan = Tentaikhoan;
        this.verificationCode = verificationCode;
    }

    // Getters and Setters
    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String Tentaikhoan) {
        this.Tentaikhoan = Tentaikhoan;
    }

    public String getHoten() {
        return Hoten;
    }

    public void setHoten(String Hoten) {
        this.Hoten = Hoten;
    }

    public String getMatkhau() {
        return Matkhau;
    }

    public void setMatkhau(String Matkhau) {
        this.Matkhau = Matkhau;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

