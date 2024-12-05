package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Thongbao {
    @SerializedName("_id") // Ánh xạ _id từ MongoDB thành id trong Android
    private String id;

    @SerializedName("tentaikhoan")
    private String tentaikhoan;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("timestamp")
    private Date timestamp;

    @SerializedName("read") // Ánh xạ trường "read" từ server
    private boolean read; // Trạng thái thông báo đã đọc hay chưa

    // Constructor
    public Thongbao(String id, String tentaikhoan, String title, String message, Date timestamp, boolean read) {
        this.id = id;
        this.tentaikhoan = tentaikhoan;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.read = read;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTentaikhoan() {
        return tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        this.tentaikhoan = tentaikhoan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read; // Trả về trạng thái đã đọc hay chưa
    }

    public void setRead(boolean read) {
        this.read = read; // Đặt trạng thái đã đọc
    }
}
