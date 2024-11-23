package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;

public class ApiResponse {
    private String message;
    private String token; // Nếu API đăng nhập trả về token
    private String id;
    private CustomerAccount data;

    public CustomerAccount getData() {
        return data;
    }

    public void setData(CustomerAccount data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

