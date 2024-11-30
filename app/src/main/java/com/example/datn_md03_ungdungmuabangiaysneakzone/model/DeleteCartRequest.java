package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.util.ArrayList;
import java.util.List;

public class DeleteCartRequest {
    String productId;
    int size;

    private List<String> maSPList;
    private List<String> sizeList;


    public DeleteCartRequest(List<String> sizeList, List<String> maSPList) {
        this.sizeList = sizeList;
        this.maSPList = maSPList;
    }

    public List<String> getMaSPList() {
        return maSPList;
    }

    public void setMaSPList(List<String> maSPList) {
        this.maSPList = maSPList;
    }

    public List<String> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<String> sizeList) {
        this.sizeList = sizeList;
    }

    public DeleteCartRequest(String productId, int size) {
        this.productId = productId;
        this.size = size;
    }

    public DeleteCartRequest(String email, ArrayList<String> maSPList, ArrayList<String> size) {
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
