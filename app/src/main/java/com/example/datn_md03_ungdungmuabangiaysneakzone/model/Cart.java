package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import java.util.ArrayList;

public class Cart {
    private String Tentaikhoan; // Tên tài khoản của người dùng
    private ArrayList<ProductItemCart> SanPham; // Danh sách sản phẩm trong giỏ hàng
    private int TongSoLuong; // Tổng số lượng sản phẩm trong giỏ hàng
    private double TongGiaTri; // Tổng giá trị của giỏ hàng

    private String productId; // ID của sản phẩm
    private int size;
    private int quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


// Constructor


    public Cart() {
    }

    public Cart(String tentaikhoan, ArrayList<ProductItemCart> sanPham) {
        this.Tentaikhoan = tentaikhoan;
        this.SanPham = sanPham;
        calculateTotals(); // Tính toán tổng số lượng và tổng giá trị
    }

    // Getters và Setters
    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        Tentaikhoan = tentaikhoan;
    }

    public ArrayList<ProductItemCart> getSanPham() {
        return SanPham;
    }

    public void setSanPham(ArrayList<ProductItemCart> sanPham) {
        SanPham = sanPham;
        calculateTotals(); // Cập nhật tổng số lượng và tổng giá trị khi thay đổi danh sách sản phẩm
    }

    public int getTongSoLuong() {
        return TongSoLuong;
    }

    public double getTongGiaTri() {
        return TongGiaTri;
    }

    // Phương thức để tính toán tổng số lượng và tổng giá trị
    private void calculateTotals() {
        TongSoLuong = 0;
        TongGiaTri = 0;
        for (ProductItemCart item : SanPham) {
            TongSoLuong += item.getSoLuongGioHang();
            TongGiaTri += item.getTongTien();
        }
    }
}
