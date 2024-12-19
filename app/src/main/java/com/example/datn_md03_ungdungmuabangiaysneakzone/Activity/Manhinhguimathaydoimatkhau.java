package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Manhinhguimathaydoimatkhau extends AppCompatActivity {

    private TextView tvEmail;
    private Button btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhguimathaydoimatkhau);

        // Ánh xạ giao diện
        tvEmail = findViewById(R.id.tvEmaill);
        btnNext = findViewById(R.id.btnNext);
        ImageButton btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manhinhguimathaydoimatkhau.this, Activity_DoiMK.class);
                startActivity(intent);
            }
        });
        // Lấy email từ SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Tentaikhoan", "");

        // Hiển thị email trong giao diện nếu tồn tại
        if (!email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            Toast.makeText(this, "Không tìm thấy email trong SharedPreferences.", Toast.LENGTH_SHORT).show();
            Log.e("SharedPreferences", "Email không tồn tại.");
            return;
        }

        // Xử lý sự kiện khi nhấn nút "Tiếp theo"
        btnNext.setOnClickListener(v -> {
            if (!isNetworkAvailable()) {
                Toast.makeText(this, "Không có kết nối Internet.", Toast.LENGTH_SHORT).show();
            } else {
                sendVerificationCode(email);
            }
        });
    }

    // Phương thức kiểm tra kết nối Internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    // Phương thức gửi mã xác thực qua API
    private void sendVerificationCode(String email) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng CustomerAccount bằng Builder
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();

        // Gửi yêu cầu API
        apiService.sendVerificationCode(customerAccount).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(Manhinhguimathaydoimatkhau.this, "Mã xác thực đã được gửi!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình xác thực mã
                    Intent intent = new Intent(Manhinhguimathaydoimatkhau.this, Xacthucmathaydoimatkhau.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Log.e("API Response", "Lỗi: " + response.message());
                    Toast.makeText(Manhinhguimathaydoimatkhau.this, "Không thể vào màn xác thực vì mã vẫn tồn tại, vui lòng thử lại sau !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Call", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(Manhinhguimathaydoimatkhau.this, "Lỗi mạng: Không thể kết nối đến server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
