package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

public class NotificationItem_Demo {

    private String title;
    private String price;
    private String time;
    private int imageResId;

    public NotificationItem_Demo(String title, String price, String time, int imageResId) {
        this.title = title;
        this.price = price;
        this.time = time;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public int getImageResId() {
        return imageResId;
    }
}
