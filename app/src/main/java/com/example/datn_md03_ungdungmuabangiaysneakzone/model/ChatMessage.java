package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class ChatMessage {
    private String _id; // ID của tin nhắn
    private String senderId; // ID người gửi
    private String receiverId; // ID người nhận
    private String message; // Nội dung tin nhắn
    private String timestamp; // Thời gian gửi tin nhắn

    // Constructor mặc định
    public ChatMessage() {}

    // Constructor đầy đủ
    public ChatMessage(String _id, String senderId, String receiverId, String message, String timestamp) {
        this._id = _id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter và Setter
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
