package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

public class DanhGia_Demo {
    private String username;
    private int rating;
    private String comment;

    public DanhGia_Demo(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
