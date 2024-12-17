package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class DangKy extends AppCompatActivity {
    private EditText editTextTen, editTextEmail, editTextPassword, editTextRePassword;
    private Button btnDangKy;
    private ApiService apiService;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        // Ánh xạ các view
        editTextTen = findViewById(R.id.editTextTen);
        editTextEmail = findViewById(R.id.editTextEmail2);
        editTextPassword = findViewById(R.id.editTextPassword2);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        btnDangKy = findViewById(R.id.btnDangKy);
        backButton = findViewById(R.id.backButton);

        // Khởi tạo ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Kiểm tra và điền lại thông tin nếu có
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String rePassword = intent.getStringExtra("rePassword");

        if (name != null) editTextTen.setText(name);
        if (email != null) editTextEmail.setText(email);
        if (password != null) editTextPassword.setText(password);
        if (rePassword != null) editTextRePassword.setText(rePassword);

        // Xử lý khi bấm nút Đăng Ký
        btnDangKy.setOnClickListener(v -> {
            String ten = editTextTen.getText().toString().trim();
            String emailInput = editTextEmail.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();
            String rePasswordInput = editTextRePassword.getText().toString().trim();

            // Kiểm tra thông tin đầu vào
            boolean isValid = true;

            // Kiểm tra tên
            if (TextUtils.isEmpty(ten)) {
                editTextTen.setError("Vui lòng nhập họ tên!");
                isValid = false;
            } else if (ten.length() < 3 || ten.matches(".*[0-9@#$%^&*!].*")) {
                editTextTen.setError("Tên không hợp lệ! Tên phải có ít nhất 3 ký tự và không chứa ký tự đặc biệt.");
                isValid = false;
            }

            // Kiểm tra email
            if (TextUtils.isEmpty(emailInput)) {
                editTextEmail.setError("Vui lòng nhập email!");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                editTextEmail.setError("Vui lòng nhập email hợp lệ!");
                isValid = false;
            } else if (emailInput.length() < 5 || emailInput.length() > 50) {
                editTextEmail.setError("Email phải từ 5 đến 50 ký tự!");
                isValid = false;
            }

            // Kiểm tra mật khẩu
            if (TextUtils.isEmpty(passwordInput)) {
                editTextPassword.setError("Vui lòng nhập mật khẩu!");
                isValid = false;
            } else if (passwordInput.length() < 6) {
                editTextPassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
                isValid = false;
            }

            // Kiểm tra mật khẩu xác nhận
            if (TextUtils.isEmpty(rePasswordInput)) {
                editTextRePassword.setError("Vui lòng xác nhận mật khẩu!");
                isValid = false;
            } else if (!passwordInput.equals(rePasswordInput)) {
                editTextRePassword.setError("Mật khẩu không khớp!");
                isValid = false;
            }

            if (!isValid) return;

            // Tạo đối tượng TemporaryVerificationCode
            TemporaryVerificationCode tempCode = new TemporaryVerificationCode();
            tempCode.setTentaikhoan(emailInput);
            tempCode.setHoten(ten);
            tempCode.setMatkhau(passwordInput);

            // Gửi yêu cầu đăng ký
            Call<ApiResponse> call = apiService.register(tempCode);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(DangKy.this, "Đăng ký tạm thời thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình gửi mã xác thực
                        Intent intent = new Intent(DangKy.this, Activity_manhinhguimadangky.class);
                        intent.putExtra("name", ten);
                        intent.putExtra("email", emailInput);
                        intent.putExtra("password", passwordInput);
                        intent.putExtra("rePassword", rePasswordInput);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DangKy.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(DangKy.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Xử lý nút quay lại
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(DangKy.this, DangNhap.class));
        });
    }

    @Override
    public void onBackPressed() {
        // Không làm gì để ngăn quay lại màn hình trước
    }
}
