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

public class manhinhnhanguima extends AppCompatActivity {

    private TextView tvEmail;
    private Button btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhnhanguima);

        tvEmail = findViewById(R.id.tvEmaill);
        btnNext = findViewById(R.id.btnNext);

        ImageButton btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnhanguima.this,Activity_FixInfor.class);
                startActivity(intent);
            }
        });
        // Lấy tên tài khoản (email) từ SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("Tentaikhoan", "");

        // Kiểm tra log để xem giá trị của Tentaikhoan
        Log.d("SharedPreferences", "Tentaikhoan: " + email);

        // Hiển thị tên tài khoản (email) trong TextView nếu có
        if (!email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            Toast.makeText(this, "Không tìm thấy Tentaikhoan trong SharedPreferences", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xử lý sự kiện click của nút "Tiếp theo"
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(manhinhnhanguima.this, "Không có kết nối Internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendVerificationCode(email);
            }
        });
    }

    // Phương thức kiểm tra kết nối Internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Phương thức gửi mã xác thực qua API
    private void sendVerificationCode(String email) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng CustomerAccount để gửi yêu cầu
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setTentaikhoan(email);
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();

        // Gửi yêu cầu đến API
        Call<ApiResponse> call = apiService.sendVerificationCode(customerAccount);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(manhinhnhanguima.this, "Mã xác thực đã được gửi: " + message, Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình xác thực mã
                    Log.d("VerificationSuccess", "Chuyển sang màn hình xác thực mã");
                    Intent intent = new Intent(manhinhnhanguima.this, xacthucmaemailhientai.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    String errorMessage = "Lỗi: " + response.code() + " - " + response.message();
                    Toast.makeText(manhinhnhanguima.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("VerificationRequest", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(manhinhnhanguima.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VerificationRequest", "Error: " + t.getMessage(), t);
            }
        });
    }
}
