package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class Response<T>{
    private int status;
    private String messerger;
    private T data; // T kiểu gen

    private boolean success;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private String token;
    private String refreshToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Response() {
    }

    public Response(int status, String messerger, T data) {
        this.status = status;
        this.messerger = messerger;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMesserger() {
        return messerger;
    }

    public void setMesserger(String messerger) {
        this.messerger = messerger;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
