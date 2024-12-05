package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView imgQRCode;
    private Button btnConfirmPayment;
    private TextView tvOrderDetails;
    private Order order;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Khởi tạo các thành phần giao diện
        imgQRCode = findViewById(R.id.imgQRCode);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        tvOrderDetails = findViewById(R.id.tvOrderDetails);

        // Nhận dữ liệu từ Activity trước (ActivityCTSP_To_ThanhToan)
        Intent intent = getIntent();
        String qrUrl = intent.getStringExtra("qrUrl");
        order = (Order) intent.getSerializableExtra("order");

        // Hiển thị mã QR
        Glide.with(this).load(qrUrl).into(imgQRCode);

        // Hiển thị thông tin chi tiết đơn hàng
        if (order != null) {
            tvOrderDetails.setText("Tên người nhận: " + order.getTenNguoiNhan() +
                    "\nĐịa chỉ giao hàng: " + order.getDiaChiGiaoHang() +
                    "\nSố điện thoại: " + order.getSoDienThoai() +
                    "\nPhương thức thanh toán: " + order.getPhuongThucThanhToan());
        }

        // Xử lý sự kiện nhấn "Xác nhận thanh toán"
        btnConfirmPayment.setOnClickListener(view -> confirmPayment());
    }

    private void confirmPayment() {
        // Kiểm tra phương thức thanh toán
        if (order != null) {
            // Lấy email từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            String email = sharedPreferences.getString("Tentaikhoan", ""); // Lấy email từ SharedPreferences

            // Gọi API tạo đơn hàng
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class); // Khởi tạo apiService
            Call<Order> call = apiService.createOrder(email, order); // Truyền email vào API call

            // Thực hiện gọi API
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QRCodeActivity.this, "Order thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(QRCodeActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(QRCodeActivity.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(QRCodeActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(QRCodeActivity.this, "Đơn hàng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}
