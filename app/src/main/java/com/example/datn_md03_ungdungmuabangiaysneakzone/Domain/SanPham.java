package com.example.datn_md03_ungdungmuabangiaysneakzone.Domain;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SanPham {
    private String tenSanPham;
    private String anhUrl;
    private double gia;

    public SanPham() {
    }

    public SanPham(String tenSanPham, String anhUrl, double gia) {
        this.tenSanPham = tenSanPham;
        this.anhUrl = anhUrl;
        this.gia = gia;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getAnhUrl() {
        return anhUrl;
    }

    public void setAnhUrl(String anhUrl) {
        this.anhUrl = anhUrl;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
