package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/auth/register")
    Call<ApiResponse> register(@Body CustomerAccount customerAccount);

    @POST("/auth/login")
    Call<ApiResponse> login(@Body CustomerAccount customerAccount);
}
