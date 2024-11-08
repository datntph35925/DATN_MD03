package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhap extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private AppCompatButton btnDangKy, btnDangNhap;
    private TextView textviewQMK;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi bấm vào TextView Quên Mật Khẩu
        textviewQMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap.this, Activity_QuenMatKhau.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi bấm vào nút Đăng Nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập email hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuẩn bị dữ liệu đăng nhập
                CustomerAccount account = new CustomerAccount(email, password);

                // Gọi API để đăng nhập
                apiService.login(account).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Xử lý đăng nhập thành công
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DangNhap.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Xử lý đăng nhập thất bại
                            Toast.makeText(DangNhap.this, "Đăng nhập thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        // Xử lý lỗi kết nối
                        Toast.makeText(DangNhap.this, "Lỗi kết nối. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
