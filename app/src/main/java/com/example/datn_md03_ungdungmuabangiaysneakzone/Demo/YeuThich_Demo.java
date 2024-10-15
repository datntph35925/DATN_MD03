package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

public class YeuThich_Demo {
    private String name;
    private double price;
    private int imageResId;

    public YeuThich_Demo(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
