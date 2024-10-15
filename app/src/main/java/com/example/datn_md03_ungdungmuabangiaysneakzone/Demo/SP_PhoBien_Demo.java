package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

public class SP_PhoBien_Demo {
    private String name;
    private double price;
    private int imageResId;

    public SP_PhoBien_Demo(int imageResId, String name, double price) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
