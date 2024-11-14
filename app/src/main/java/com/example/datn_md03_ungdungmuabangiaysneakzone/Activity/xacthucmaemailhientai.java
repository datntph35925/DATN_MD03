package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

        // Initialize views
        edtVerificationCode1 = findViewById(R.id.edtVerificationCode1);
        edtVerificationCode2 = findViewById(R.id.edtVerificationCode2);
        edtVerificationCode3 = findViewById(R.id.edtVerificationCode3);
        edtVerificationCode4 = findViewById(R.id.edtVerificationCode4);
        edtVerificationCode5 = findViewById(R.id.edtVerificationCode5);
        edtVerificationCode6 = findViewById(R.id.edtVerificationCode6);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvResendCode = findViewById(R.id.tvResendCode);
        tvEmail = findViewById(R.id.tvEmail);

        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", "");

        // Display email in TextView if available
        if (!email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("Không tìm thấy email");
        }

        // Handle "Xác nhận" button click
        btnConfirm.setOnClickListener(view -> verifyEmailCode());

        // Handle "Gửi lại bây giờ" button click
        tvResendCode.setOnClickListener(view -> resendVerificationCode());
    }

    private void verifyEmailCode() {
        // Concatenate verification code from EditTexts
        String code = edtVerificationCode1.getText().toString().trim() +
                edtVerificationCode2.getText().toString().trim() +
                edtVerificationCode3.getText().toString().trim() +
                edtVerificationCode4.getText().toString().trim() +
                edtVerificationCode5.getText().toString().trim() +
                edtVerificationCode6.getText().toString().trim();

        // Check if the verification code has 6 characters
        if (code.length() != 6) {
            Toast.makeText(this, "Mã xác thực phải có 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("VerificationCode", "Mã xác thực: " + code);

        // Send verification code to API
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setTentaikhoan(email);
//        customerAccount.setVerificationCode(code);

        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .setVerificationCode(code)
                .build();

        // Call API to verify the code
        Call<ApiResponse> call = apiService.verifyCode(customerAccount);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(xacthucmaemailhientai.this, "Xác thực thành công: " + message, Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình mới khi xác thực thành công
                    Intent intent = new Intent(xacthucmaemailhientai.this, manguimaemailmoi.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = "Lỗi: " + response.code() + " - " + response.message();
                    Toast.makeText(xacthucmaemailhientai.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("VerificationRequest", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(xacthucmaemailhientai.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VerificationRequest", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void resendVerificationCode() {
        // Send request to resend verification code
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setTentaikhoan(email);

        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();

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
                    Log.d("ResendCodeRequest", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(xacthucmaemailhientai.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ResendCodeRequest", "Error: " + t.getMessage(), t);
            }
        });
    }
}
