package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhap extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private AppCompatButton btnDangKy, btnDangNhap;
    private TextView textviewQMK;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập khi mở ứng dụng
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false); // Kiểm tra trạng thái đăng nhập

        if (isLoggedIn) {
            // Nếu đã đăng nhập, chuyển sang trang chủ
            Intent intent = new Intent(DangNhap.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đảm bảo không quay lại màn hình đăng nhập
        } else {
            // Nếu chưa đăng nhập, hiển thị màn hình đăng nhập
            setContentView(R.layout.activity_dang_nhap);

            // Khởi tạo các phần tử giao diện
            editTextEmail = findViewById(R.id.editTextMail);
            editTextPassword = findViewById(R.id.editTextPassword);
            btnDangKy = findViewById(R.id.btnDangKy);
            btnDangNhap = findViewById(R.id.btnDangNhap);
            textviewQMK = findViewById(R.id.textviewQMK);

            // Khởi tạo ApiService
            apiService = RetrofitClient.getClient().create(ApiService.class);

            // Xử lý sự kiện khi bấm vào nút Đăng Ký
            btnDangKy.setOnClickListener(view -> {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            });

            // Xử lý sự kiện khi bấm vào TextView Quên Mật Khẩu
            textviewQMK.setOnClickListener(view -> {
                Intent intent = new Intent(DangNhap.this, Activity_QuenMatKhau.class);
                startActivity(intent);
            });

            // Xử lý sự kiện khi bấm vào nút Đăng Nhập
            btnDangNhap.setOnClickListener(view -> {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kiểm tra thông tin đăng nhập
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(DangNhap.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty() || password.length() < 6) {
                    Toast.makeText(DangNhap.this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuẩn bị dữ liệu đăng nhập
                CustomerAccount account = new CustomerAccount.Builder()
                        .setTentaikhoan(email)
                        .setMatkhau(password)
                        .build();

                // Gọi API để đăng nhập
                apiService.login(account).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Đăng nhập thành công
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            // Lưu thông tin tài khoản và trạng thái đăng nhập vào SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Tentaikhoan", email); // Lưu email vào SharedPreferences
                            editor.putBoolean("is_logged_in", true); // Lưu trạng thái đăng nhập
                            editor.apply();

                            // Chuyển sang MainActivity (Trang chủ)
                            Intent intent = new Intent(DangNhap.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Đảm bảo không quay lại màn hình đăng nhập
                        } else {
                            // Đăng nhập thất bại
                            Toast.makeText(DangNhap.this, "Đăng nhập thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        // Lỗi kết nối
                        Toast.makeText(DangNhap.this, "Lỗi kết nối. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
