package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Activity_QuenMatKhau extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        // Khởi tạo các view
        edtEmail = findViewById(R.id.ed_email);
        btnSend = findViewById(R.id.btn_send);
        progressBar = findViewById(R.id.progressBar);

        // Ẩn ProgressBar ban đầu
        progressBar.setVisibility(View.GONE);

        // Xử lý sự kiện khi nhấn nút "Gửi"
        btnSend.setOnClickListener(view -> sendForgotPasswordRequest());
    }

    private void sendForgotPasswordRequest() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Tạo instance của ApiService và gọi API
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        CustomerAccount customerAccount = new CustomerAccount();
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();
//        customerAccount.setTentaikhoan(email); // Gán email vào Tentaikhoan

        // Gọi API với Retrofit
        Call<ApiResponse> call = apiService.forgotPassword(customerAccount);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                // Ẩn ProgressBar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Xử lý phản hồi thành công
                    Toast.makeText(Activity_QuenMatKhau.this, "Yêu cầu quên mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý phản hồi không thành công
                    String errorMessage = "Gửi yêu cầu thất bại: " + response.code() + " - " + response.message();
                    Toast.makeText(Activity_QuenMatKhau.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("ForgotPasswordRequest", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Ẩn ProgressBar và xử lý lỗi
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Activity_QuenMatKhau.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ForgotPasswordRequest", "Error: " + t.getMessage(), t);
            }
        });
    }

}
