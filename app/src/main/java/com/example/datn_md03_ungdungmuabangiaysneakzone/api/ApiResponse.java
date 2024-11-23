package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ChatMessage;

import java.util.List;

public class ApiResponse<T> {
    private boolean success; // Trường success được thêm từ phần Stashed changes
    private String message; // Thông báo từ server
    private String token; // Token nếu API đăng nhập trả về
    private String id; // ID được trả về từ server
    private CustomerAccount data; // Dữ liệu chi tiết của tài khoản khách hàng
    private List<ChatMessage> messages; // Danh sách tin nhắn từ server

    // Getters và Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomerAccount getData() {
        return data;
    }

    public void setData(CustomerAccount data) {
        this.data = data;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
