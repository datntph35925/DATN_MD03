package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class UpdateStatusRequest {
    private String TrangThai;

    public UpdateStatusRequest(String TrangThai) {
        this.TrangThai = TrangThai;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }
}

