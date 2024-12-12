package com.example.datn_md03_ungdungmuabangiaysneakzone.api;

import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Cart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.DeleteCartRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Favorite;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.FavoriteAdd;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Location;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.LocationRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.PaymentAuthentication;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.RemoveItemsRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ResetPasswordRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Review;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ReviewResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.TemporaryVerificationCode;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ChatMessage;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Thongbao;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.UpdateStatusRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ------------------- Authentication --------------------
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
    Call<ApiResponse> sendVerificationCode(@Body CustomerAccount customerAccount);

    @POST("/auth/xacthucma")
    Call<ApiResponse> verifyCode(@Body CustomerAccount customerAccount);

    @POST("/auth/ma-xac-thuc-email-moi")
    Call<ApiResponse> sendVerificationCodeToNewEmail(@Body CustomerAccount customerAccount);

    @POST("/auth/xacthucma-email-moi")
    Call<ApiResponse> verifyCodeAndUpdateEmail(@Body HashMap<String, String> requestData);

    @GET("/auth/chitiettaikhoan")
    Call<CustomerAccount> getUserDetails(@Query("Tentaikhoan") String tentaikhoan);

    @POST("/auth/doi-matkhau-tam-thoi")
    Call<ApiResponse> saveTemporaryPassword(@Body CustomerAccount customerAccount);

    @POST("/auth/xacthucma-doi-matkhau")
    Call<ApiResponse> confirmCodeAndChangePassword(@Body CustomerAccount customerAccount);

    @PUT("/auth/doi-hoten")
    Call<ApiResponse> changeNameWithBody(@Body Map<String, String> params);

    @Multipart
    @PUT("/auth/doi-anh")
    Call<ApiResponse> uploadAvatar(
            @Part("Tentaikhoan") RequestBody tentaikhoan,
            @Part MultipartBody.Part image
    );

    // ------------------- Product APIs ---------------------

    @GET("/api/get-list-product")
    Call<Response<ArrayList<Product>>> getListProducts();

    @GET("/api/get-product-by-id/{id}")
    Call<Response<Product>> getProductById(@Path("id") String id);

    // ------------------- Chat APIs ------------------------
    @POST("/chatRouter/khach-hang-gui-tin-nhan")
    Call<Void> sendMessage(@Body Map<String, String> messageData);

    @GET("/chatRouter/lich-su-tin-nhan")
    Call<List<ChatMessage>> getMessages(@Query("TentaiKhoan") String tentaiKhoan);

    @DELETE("/chatRouter/khach-hang-xoa-tin-nhan/{id}")
    Call<Void> deleteMessage(@Path("id") String messageId, @Query("TentaiKhoan") String tentaiKhoan);

    // ------------------- Cart APIs ------------------------
    @POST("/cart/add-cart/{userId}")
    Call<Response<ArrayList<Cart>>> addListCart(@Path("userId") String userId, @Body Cart cart);

    @GET("/cart/get-list-cart-by-id/{id}")
    Call<Response<Cart>> getListCartById(@Path("id") String userId);

    @HTTP(method = "DELETE", path = "/cart/remove-item/{userId}", hasBody = true)
    Call<Response<DeleteCartRequest>> removeCartItem(
            @Path("userId") String userId,
            @Body DeleteCartRequest request
    );

    // Lấy danh sách địa chỉ của một người dùng
    @GET("/locations/get-list-location-by-id/{id}")
    Call<Response<ArrayList<Location>>> getListLocationById(@Path("id") String userId);

    // Thêm địa chỉ
    @POST("/locations/add-location/{userId}")
    Call<Location> addLocation(@Path("userId") String userId, @Body Location location);

    // Cập nhật địa chỉ
    @PUT("/locations/update-location/{userId}")
    Call<Response<Location>> updateLocation(@Path("userId") String userId, @Body Location location);

    // Xóa địa chỉ
    @HTTP(method = "DELETE", path = "/locations/remove-location/{userId}", hasBody = true)
    Call<Response<LocationRequest>> removeLocation(@Path("userId") String userId, @Body LocationRequest location);

    // Lấy đơn hàng
    @GET("/order/get-list-order/{Tentaikhoan}")
    Call<Response<ArrayList<Order>>> getOrderById(@Path("Tentaikhoan") String orderId);

    @POST("/order/add-order-directly/{userId}")
    Call<Order> createOrder( @Path("userId") String userId, @Body Order order);

    @POST("/order/add-order-from-cart/{userId}")
    Call<Order> createOrderFromCart( @Path("userId") String userId, @Body Order order);

    @GET("/order/get-order-by-id/{id}")
    Call<Response<ArrayList<Order>>> getOrderDetailById(@Path("id") String id);

    @PUT("/order/update-order-status/{id}")
    Call<Response<Order>> updateOrderStatus(@Path("id") String orderId, @Body UpdateStatusRequest status);

    @GET("/api/search-products")
    Call<Response<ArrayList<Product>>> searchProducts(@Query("keyword") String keyword);

    @HTTP(method = "DELETE", path = "/cart/remove-items/{userId}", hasBody = true)
    Call<Response<RemoveItemsRequest>> removeItems(
            @Path("userId") String userId,
            @Body RemoveItemsRequest request
    );

    @GET("/favorite/get-favorites/{Tentaikhoan}")
    Call<Response<ArrayList<Favorite>>> getFavorites(@Path("Tentaikhoan") String tentaikhoan);

    @GET("/favorite/get-favorite-status/{Tentaikhoan}/{SanPhamId}")
    Call<Response> getFavoriteStatus(@Path("Tentaikhoan") String tentaikhoan, @Path("SanPhamId") String sanPhamId);

    // Cập nhật trạng thái yêu thích cho sản phẩm (thêm/xóa)
    @PUT("/favorite/update-favorite-status/{Tentaikhoan}/{SanPhamId}")
    Call<Response> updateFavoriteStatus(@Path("Tentaikhoan") String tentaikhoan, @Path("SanPhamId") String sanPhamId, @Body FavoriteAdd request);

    //xác thực thanh toán
    @POST("/paymentAuthentication/add-paymentauthentication")
    Call<PaymentAuthentication> addPaymentAuthentication(@Body PaymentAuthentication request);

    // ------------------- Thong bao APIs ------------------------
    @GET("routes/notifications/{tentaikhoan}")
    Call<List<Thongbao>> getNotifications(@Path("tentaikhoan") String tentaikhoan);
    @DELETE("routes/notifications/{id}")
    Call<Void> deleteNotification(@Path("id") String notificationId);
    @PUT("routes/notifications/{id}/read")  // Địa chỉ API của bạn
    Call<Void> markNotificationAsRead(@Path("id") String notificationId);
    @GET("routes/notifications/{tentaikhoan}/tongtb")
    Call<ApiResponse> getUnreadNotificationCount(@Path("tentaikhoan") String tentaikhoan);

    //review
    @POST("/reviews/add-reviews/{Tentaikhoan}")
    Call<Review> addReview(
            @Path("Tentaikhoan") String tentaikhoan,
            @Body Review review
    );

    @GET("/reviews/get-reviews-product/{MaSanPham}")
    Call<Response<ArrayList<Review>>> getReviewsByProduct(@Path("MaSanPham") String maSanPham);

    @GET("/reviews/get-reviews-account/{Tentaikhoan}")
    Call<List<Review>> getReviewsByAccount(@Path("Tentaikhoan") String tentaikhoan);

    @DELETE("/reviews/reviews/{id}")
    Call<Void> deleteReview(@Path("id") String reviewId);

    @GET("/reviews/get-reviews-for-product-and-user/{MaSanPham}/{Tentaikhoan}")
    Call<ReviewResponse<List<Review>>> getReviewsForProductAndUser(@Path("MaSanPham") String MaSanPham, @Path("Tentaikhoan") String Tentaikhoan);

    //Voucher
    @GET("/voucher/get-list-vouchers")
    Call<Response<ArrayList<Voucher>>> getListVouchers();

}

