package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

import java.io.Serializable;

public class Cart_Demo implements Serializable {

    private String name;
    private double price;
    private int quantity;
    private int imageResId;

    public Cart_Demo(String name, double price, int quantity, int imageResId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getImageResId() {
        return imageResId;
    }
}
