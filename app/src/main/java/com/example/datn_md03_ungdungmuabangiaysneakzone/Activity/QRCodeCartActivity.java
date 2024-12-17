package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.PaymentAuthentication;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.RemoveItemsRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeCartActivity extends AppCompatActivity {

    private ImageView imgQRCode;
    private Button btnConfirmPayment;
    private TextView tvOrderDetails;
    private Order order;
    private ApiService apiService;
    private ArrayList<String> maSPList;
    private ArrayList<Integer> sizeList;// Mảng chứa các maSP
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrcode_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        imgQRCode = findViewById(R.id.imgQRCode);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        tvOrderDetails = findViewById(R.id.tvOrderDetails);

        maSPList = new ArrayList<>();
        sizeList = new ArrayList<>();

        sizeList = getIntent().getIntegerArrayListExtra("sizeList");
        maSPList = getIntent().getStringArrayListExtra("maSP");

        apiService = RetrofitClient.getClient().create(ApiService.class);
        // Nhận dữ liệu từ Activity trước (ActivityCTSP_To_ThanhToan)
        Intent intent = getIntent();
        String qrUrl = intent.getStringExtra("qrUrl");
        order = (Order) intent.getSerializableExtra("order");

        Glide.with(this).load(qrUrl).into(imgQRCode);

        if (order != null) {
            tvOrderDetails.setText("Tên người nhận: " + order.getTenNguoiNhan() +
                    "\nĐịa chỉ giao hàng: " + order.getDiaChiGiaoHang() +
                    "\nSố điện thoại: " + order.getSoDienThoai() +
                    "\nPhương thức thanh toán: " + order.getPhuongThucThanhToan());
        }

        // Xử lý sự kiện nhấn "Xác nhận thanh toán"
        btnConfirmPayment.setOnClickListener(view -> {
            confirmPayment();
            xacThucThanhToan();
        });
    }

    private void removeCartItem(){
        List<RemoveItemsRequest.products> productItems = new ArrayList<>();
        for (int i = 0; i < maSPList.size(); i++) {
            List<String> productIdList = Collections.singletonList(maSPList.get(i)); // Mã sản phẩm
            List<Integer> sizeList = Collections.singletonList(this.sizeList.get(i)); // Kích thước

            // Thêm vào danh sách yêu cầu
            productItems.add(new RemoveItemsRequest.products(productIdList, sizeList));
        }
        Log.d("API_RESPONSE", "Success: " + productItems.toString());
        RemoveItemsRequest request = new RemoveItemsRequest(productItems);
        apiService.removeItems(email, request).enqueue(new Callback<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>>() {
            @Override
            public void onResponse(Call<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>> call, Response<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>> response) {
                if (response.isSuccessful()) {
                   // Toast.makeText(QRCodeCartActivity.this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    Log.d("API_RESPONSE", "Success: " + response.code());
                } else {
                    Toast.makeText(QRCodeCartActivity.this, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                    Log.e("API_RESPONSE", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>> call, Throwable t) {
                Toast.makeText(QRCodeCartActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void confirmPayment() {
        // Kiểm tra phương thức thanh toán
        if (order != null) {
            // Lấy email từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            email = sharedPreferences.getString("Tentaikhoan", ""); // Lấy email từ SharedPreferences

            // Gọi API tạo đơn hàng
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class); // Khởi tạo apiService
            Call<Order> call = apiService.createOrderFromCart(email, order); // Truyền email vào API call
            // Thực hiện gọi API
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        removeCartItem();
                        Toast.makeText(QRCodeCartActivity.this, "Order thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(QRCodeCartActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(QRCodeCartActivity.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(QRCodeCartActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(QRCodeCartActivity.this, "Đơn hàng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xacThucThanhToan(){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        PaymentAuthentication paymentAuthentication = new PaymentAuthentication();
        paymentAuthentication.setTentaikhoan(email);
        paymentAuthentication.setMaDonHang(order.getMaDonHang());
        paymentAuthentication.setSoTien(order.getTongTien());

        Call<PaymentAuthentication> call = apiService.addPaymentAuthentication(paymentAuthentication);
        call.enqueue(new Callback<PaymentAuthentication>() {
            @Override
            public void onResponse(Call<PaymentAuthentication> call, Response<PaymentAuthentication> response) {
                if(response.isSuccessful()){
                    Toast.makeText(QRCodeCartActivity.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QRCodeCartActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentAuthentication> call, Throwable t) {
                Toast.makeText(QRCodeCartActivity.this, "Looix", Toast.LENGTH_SHORT).show();
            }
        });
    }
}