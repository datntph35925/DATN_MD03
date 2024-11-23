package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class DeleteCartRequest {
    String productId;
    int size;

    public DeleteCartRequest(String productId, int size) {
        this.productId = productId;
        this.size = size;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
