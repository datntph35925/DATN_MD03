package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

public class ApiResponse {
    private String message;
    private String token; // Nếu API đăng nhập trả về token

    // Getters và Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

