package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class ReviewResponse<T> {
    private boolean success;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ReviewResponse(T data, boolean success) {
        this.data = data;
        this.success = success;
    }
}
