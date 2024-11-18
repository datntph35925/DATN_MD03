package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.TemporaryVerificationCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Manhinhxacthucdangky extends AppCompatActivity {

    private TextView tvEmail, tvResendCode;
    private EditText edtCode1, edtCode2, edtCode3, edtCode4, edtCode5, edtCode6;
    private Button btnConfirm;
    private ImageButton btnBack;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhxacthucdangky);

        // Ánh xạ các view
        tvEmail = findViewById(R.id.tvEmail);
        tvResendCode = findViewById(R.id.tvResendCode);
        edtCode1 = findViewById(R.id.edtVerificationCode1);
        edtCode2 = findViewById(R.id.edtVerificationCode2);
        edtCode3 = findViewById(R.id.edtVerificationCode3);
        edtCode4 = findViewById(R.id.edtVerificationCode4);
        edtCode5 = findViewById(R.id.edtVerificationCode5);
        edtCode6 = findViewById(R.id.edtVerificationCode6);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);

        // Khởi tạo ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Lấy email từ Intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        // Hiển thị email
        if (email != null && !email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("Không có email!");
        }

        // Xử lý tự động chuyển giữa các ô mã xác thực
        setupEditTextFocusHandling();

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Xử lý nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            String code = getVerificationCode();
            if (code.length() == 6) {
                verifyAccount(email, code);
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ 6 số mã xác thực!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý gửi lại mã
        tvResendCode.setOnClickListener(v -> resendVerificationCode(email));
    }

    // Lấy mã xác thực từ các ô
    private String getVerificationCode() {
        return edtCode1.getText().toString().trim() +
                edtCode2.getText().toString().trim() +
                edtCode3.getText().toString().trim() +
                edtCode4.getText().toString().trim() +
                edtCode5.getText().toString().trim() +
                edtCode6.getText().toString().trim();
    }

    // Tự động chuyển ô khi nhập hoặc xóa ký tự
    private void setupEditTextFocusHandling() {
        EditText[] editTexts = {edtCode1, edtCode2, edtCode3, edtCode4, edtCode5, edtCode6};

        for (int i = 0; i < editTexts.length; i++) {
            int currentIndex = i;

            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && currentIndex < editTexts.length - 1) {
                        // Chuyển sang ô tiếp theo
                        editTexts[currentIndex + 1].requestFocus();
                    } else if (s.length() == 0 && currentIndex > 0) {
                        // Quay lại ô trước nếu xóa
                        editTexts[currentIndex - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    // Gửi mã xác thực đến server để kiểm tra
    private void verifyAccount(String email, String code) {
        TemporaryVerificationCode tempCode = new TemporaryVerificationCode();
        tempCode.setTentaikhoan(email);
        tempCode.setVerificationCode(code);

        Call<ApiResponse> call = apiService.verifyCodee(tempCode);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Activity_Manhinhxacthucdangky.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(Activity_Manhinhxacthucdangky.this, DangNhap.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Activity_Manhinhxacthucdangky.this, "Không thể xác thực!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Activity_Manhinhxacthucdangky.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Gửi lại mã xác thực
    private void resendVerificationCode(String email) {
        TemporaryVerificationCode tempCode = new TemporaryVerificationCode();
        tempCode.setTentaikhoan(email);

        Call<ApiResponse> call = apiService.sendVerificationCodee(tempCode);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Activity_Manhinhxacthucdangky.this, "Mã xác thực đã được gửi lại!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity_Manhinhxacthucdangky.this, "Không thể gửi lại mã xác thực!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Activity_Manhinhxacthucdangky.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
