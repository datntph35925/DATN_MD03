package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class ChatMessage {
    private String _id; // ID của tin nhắn từ MongoDB
    private String senderId; // ID người gửi
    private String receiverId; // ID người nhận
    private String message; // Nội dung tin nhắn
    private String timestamp; // Thời gian gửi tin nhắn

    // Getter và Setter cho các trường
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

    // Phương thức xác định tin nhắn từ admin
    public boolean isAdminMessage() {
        return "admin".equals(senderId); // Xác định nếu senderId là "admin"
    }
}
