package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {

    @SerializedName("_id")  // Ánh xạ từ MongoDB (_id) sang biến id
    private String id;           // ID của tin nhắn (dùng để xóa)

    @SerializedName("senderId")
    private String senderId;     // ID của người gửi

    @SerializedName("receiverId")
    private String receiverId;   // ID của người nhận

    @SerializedName("message")
    private String message;      // Nội dung văn bản

    @SerializedName("imageUrl")
    private String imageUrl;     // URL của ảnh (nếu có)

    @SerializedName("videoUrl")
    private String videoUrl;     // URL của video (nếu có)

    @SerializedName("timestamp")
    private String timestamp;    // Thời gian gửi tin nhắn

    // Constructor
    public ChatMessage(String id, String senderId, String receiverId, String message,
                       String imageUrl, String videoUrl, String timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.timestamp = timestamp; // Khởi tạo thời gian gửi tin nhắn
    }

    // Getter và Setter cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Xác định xem tin nhắn này có phải của admin không
    public boolean isAdminMessage() {
        return senderId != null && senderId.equalsIgnoreCase("admin");
    }
}
