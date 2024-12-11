package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Xacthucmathaydoimatkhau extends AppCompatActivity {

    private EditText edtCode1, edtCode2, edtCode3, edtCode4, edtCode5, edtCode6;
    private Button btnConfirm;
    private TextView tvResendCode, tvEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacthucmathaydoimatkhau);

        // Ánh xạ giao diện
        edtCode1 = findViewById(R.id.edtVerificationCode1);
        edtCode2 = findViewById(R.id.edtVerificationCode2);
        edtCode3 = findViewById(R.id.edtVerificationCode3);
        edtCode4 = findViewById(R.id.edtVerificationCode4);
        edtCode5 = findViewById(R.id.edtVerificationCode5);
        edtCode6 = findViewById(R.id.edtVerificationCode6);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvResendCode = findViewById(R.id.tvResendCode);
        tvEmail = findViewById(R.id.tvEmail);
        ImageButton btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Xacthucmathaydoimatkhau.this, Manhinhguimathaydoimatkhau.class);
                startActivity(intent);
            }
        });
        // Lấy email từ Intent
        email = getIntent().getStringExtra("email");
        if (!TextUtils.isEmpty(email)) {
            tvEmail.setText(email);
        } else {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Xử lý sự kiện khi nhấn nút "Xác nhận"
        btnConfirm.setOnClickListener(v -> {
            String verificationCode = getVerificationCode();
            if (verificationCode.length() == 6) {
                verifyCode(verificationCode);
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ 6 số của mã xác thực", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấn "Gửi lại mã"
        tvResendCode.setOnClickListener(v -> resendCode());
    }

    // Lấy mã xác thực từ các ô nhập liệu
    private String getVerificationCode() {
        return edtCode1.getText().toString().trim() +
                edtCode2.getText().toString().trim() +
                edtCode3.getText().toString().trim() +
                edtCode4.getText().toString().trim() +
                edtCode5.getText().toString().trim() +
                edtCode6.getText().toString().trim();
    }

    // Gửi mã xác thực tới API
    private void verifyCode(String code) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng CustomerAccount để gửi mã xác thực
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .setVerificationCode(code)
                .build();

        apiService.confirmCodeAndChangePassword(customerAccount).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Xacthucmathaydoimatkhau.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                    // Chuyển đến màn hình đổi mật khẩu thành công
                    startActivity(new Intent(Xacthucmathaydoimatkhau.this, Activity_Profile.class));
                    finish();
                } else {
                    Toast.makeText(Xacthucmathaydoimatkhau.this, "Xác thực thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Xacthucmathaydoimatkhau.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Gửi lại mã xác thực
    private void resendCode() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng CustomerAccount để gửi lại mã
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();

        apiService.sendVerificationCode(customerAccount).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Xacthucmathaydoimatkhau.this, "Mã xác thực mới đã được gửi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Xacthucmathaydoimatkhau.this, "Không thể gửi lại mã: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Xacthucmathaydoimatkhau.this, "Lỗi mạng: Không thể gửi lại mã", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
