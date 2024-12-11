package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class xacthucmaemailhientai extends AppCompatActivity {

    private EditText edtVerificationCode1, edtVerificationCode2, edtVerificationCode3,
            edtVerificationCode4, edtVerificationCode5, edtVerificationCode6;
    private Button btnConfirm;
    private TextView tvResendCode, tvEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacthucmaemailhientai);

        // Ánh xạ các view
        edtVerificationCode1 = findViewById(R.id.edtVerificationCode1);
        edtVerificationCode2 = findViewById(R.id.edtVerificationCode2);
        edtVerificationCode3 = findViewById(R.id.edtVerificationCode3);
        edtVerificationCode4 = findViewById(R.id.edtVerificationCode4);
        edtVerificationCode5 = findViewById(R.id.edtVerificationCode5);
        edtVerificationCode6 = findViewById(R.id.edtVerificationCode6);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvResendCode = findViewById(R.id.tvResendCode);
        tvEmail = findViewById(R.id.tvEmail);

        ImageButton btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(xacthucmaemailhientai.this, manhinhnhanguima.class);
                startActivity(intent);
            }
        });
        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", "");

        // Hiển thị email
        if (!email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("Không tìm thấy email");
        }

        // Thiết lập tự động chuyển focus giữa các ô nhập mã
        setupEditTextFocusHandling();

        // Xử lý khi nhấn nút "Xác nhận"
        btnConfirm.setOnClickListener(view -> verifyEmailCode());

        // Xử lý khi nhấn nút "Gửi lại bây giờ"
        tvResendCode.setOnClickListener(view -> resendVerificationCode());
    }

    private void setupEditTextFocusHandling() {
        EditText[] editTexts = {
                edtVerificationCode1, edtVerificationCode2, edtVerificationCode3,
                edtVerificationCode4, edtVerificationCode5, edtVerificationCode6
        };

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

    private void verifyEmailCode() {
        // Ghép các ký tự từ các EditText thành mã xác thực
        String code = edtVerificationCode1.getText().toString().trim() +
                edtVerificationCode2.getText().toString().trim() +
                edtVerificationCode3.getText().toString().trim() +
                edtVerificationCode4.getText().toString().trim() +
                edtVerificationCode5.getText().toString().trim() +
                edtVerificationCode6.getText().toString().trim();

        // Kiểm tra độ dài mã xác thực
        if (code.length() != 6) {
            Toast.makeText(this, "Mã xác thực phải có 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("VerificationCode", "Mã xác thực: " + code);

        // Tạo đối tượng CustomerAccount và gửi yêu cầu API
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .setVerificationCode(code)
                .build();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse> call = apiService.verifyCode(customerAccount);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(xacthucmaemailhientai.this, "Xác thực thành công: " + message, Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình khác
                    Intent intent = new Intent(xacthucmaemailhientai.this, manguimaemailmoi.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = "Lỗi: " + response.code() + " - " + response.message();
                    Toast.makeText(xacthucmaemailhientai.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(xacthucmaemailhientai.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendVerificationCode() {
        // Gửi yêu cầu gửi lại mã xác thực
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse> call = apiService.sendVerificationCode(customerAccount);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(xacthucmaemailhientai.this, "Mã xác thực đã được gửi lại: " + message, Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = "Lỗi: " + response.code() + " - " + response.message();
                    Toast.makeText(xacthucmaemailhientai.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(xacthucmaemailhientai.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
