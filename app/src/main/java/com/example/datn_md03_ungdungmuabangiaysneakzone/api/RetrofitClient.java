package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
   // private static final String BASE_URL = "http://157.66.219.203:3000/"; // Localhost trên máy thật Android
   private static final String BASE_URL = "http://10.0.2.2:3000/";

    // Tạo Retrofit client
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Tạo instance của ApiService
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
