package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ResetPasswordRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.TemporaryVerificationCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/auth/register")
    Call<ApiResponse> register(@Body TemporaryVerificationCode tempCode);


    @POST("/auth/guimataotk")
    Call<ApiResponse> sendVerificationCodee(@Body TemporaryVerificationCode tempCode);

    @POST("/auth/xacthucmataotk")
    Call<ApiResponse> verifyCodee(@Body TemporaryVerificationCode tempCode);

    @POST("/auth/login")
    Call<ApiResponse> login(@Body CustomerAccount customerAccount);

    @POST("/auth/send-reset-password-email")
    Call<ApiResponse> forgotPassword(@Body CustomerAccount customerAccount);

    @POST("/auth/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);

    @POST("/auth/ma-xac-thuc")
    Call<ApiResponse>   sendVerificationCode(@Body CustomerAccount customerAccount);

    @POST("/auth/xacthucma")
    Call<ApiResponse> verifyCode(@Body CustomerAccount customerAccount);

    @POST("/auth/ma-xac-thuc-email-moi")
    Call<ApiResponse> sendVerificationCodeToNewEmail(@Body CustomerAccount customerAccount);

    @POST("/auth/xacthucma-email-moi")
    Call<ApiResponse> verifyCodeAndUpdateEmail(@Body HashMap<String, String> requestData);

    @GET("/api/get-list-product")
    Call<Response<ArrayList<Product>>> getListProducts();

    @GET("/api/get-product-by-id/{id}")
    Call<Response<Product>> getProductById(@Path("id") String id);

    @GET("/auth/chitiettaikhoan") // Đường dẫn API phù hợp
    Call<CustomerAccount> getUserDetails(@Query("Tentaikhoan") String tentaikhoan);

    @POST("/auth/doi-matkhau-tam-thoi")
    Call<ApiResponse> saveTemporaryPassword(@Body CustomerAccount CustomerAccount);

    @POST("/auth/xacthucma-doi-matkhau")
    Call<ApiResponse> confirmCodeAndChangePassword(@Body CustomerAccount CustomerAccount);


    @PUT("/auth/doi-hoten")
    Call<ApiResponse> changeNameWithBody(@Body Map<String, String> params);

    @Multipart
    @PUT("/auth/doi-anh") // Endpoint của API trên server
    Call<ApiResponse> uploadAvatar(
            @Part("Tentaikhoan") RequestBody tentaikhoan, // Truyền tên tài khoản
            @Part MultipartBody.Part image // Truyền file ảnh
    );
}
