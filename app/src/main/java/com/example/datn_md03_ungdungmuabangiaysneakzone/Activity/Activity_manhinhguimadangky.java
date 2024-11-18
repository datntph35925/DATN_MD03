package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.TemporaryVerificationCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_manhinhguimadangky extends AppCompatActivity {

    private TextView tvEmailVerification; // TextView hiển thị email
    private Button btnNext; // Nút Tiếp Theo
    private ApiService apiService; // Dùng để gọi API gửi mã xác thực

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhguimadangky);

        // Ánh xạ các view từ layout
        tvEmailVerification = findViewById(R.id.tvEmailxacthuctaikhoan);
        btnNext = findViewById(R.id.btnNextmoi);

        // Khởi tạo ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Nhận email từ Intent được truyền từ màn hình trước
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        // Hiển thị email lên TextView
        if (email != null && !email.isEmpty()) {
            tvEmailVerification.setText(email);
        } else {
            tvEmailVerification.setText("Không có email được truyền vào!");
        }

        // Xử lý khi bấm nút Tiếp Theo
        btnNext.setOnClickListener(v -> {
            if (email == null || email.isEmpty()) {
                Toast.makeText(Activity_manhinhguimadangky.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi mã xác thực qua API
            sendVerificationCode(email);
        });
    }

    private void sendVerificationCode(String email) {
        // Tạo đối tượng TemporaryVerificationCode với chỉ email
        TemporaryVerificationCode tempCode = new TemporaryVerificationCode(email);

        // Gọi API gửi mã xác thực
        Call<ApiResponse> call = apiService.sendVerificationCodee(tempCode);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Activity_manhinhguimadangky.this, "Mã xác thực đã được gửi!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình tiếp theo để nhập mã xác thực
                    Intent intent = new Intent(Activity_manhinhguimadangky.this, Activity_Manhinhxacthucdangky.class);
                    intent.putExtra("email", email); // Truyền email sang màn hình tiếp theo
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Activity_manhinhguimadangky.this, "Không thể gửi mã xác thực!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Activity_manhinhguimadangky.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
