package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Activity_DoiMK extends AppCompatActivity {

    // Các thành phần giao diện
    private ImageView imgBack_doiMK;
    private EditText editOldPass_doiMK, editNewPass_doiMK, editConfPass_doiMK;
    private Button btnChangePass_doiMK;

    // API Service
    private ApiService apiService;

    // Biến lưu Tentaikhoan
    private String tentaikhoan;

    private String currentPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mk);

        // Ánh xạ các thành phần giao diện
        imgBack_doiMK = findViewById(R.id.imgBack_doiMK);
        editOldPass_doiMK = findViewById(R.id.editOldPass_doiMK);
        editNewPass_doiMK = findViewById(R.id.editNewPass_doiMK);
        editConfPass_doiMK = findViewById(R.id.editConfPass_doiMK);
        btnChangePass_doiMK = findViewById(R.id.btnChangePass_doiMK);

        imgBack_doiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_DoiMK.this,Activity_FixInfor.class);
                startActivity(intent);
            }
        });
        // Khởi tạo API Service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Lấy Tentaikhoan từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        tentaikhoan = sharedPreferences.getString("Tentaikhoan", null); // Lấy email từ SharedPreferences
        currentPassword = sharedPreferences.getString("matkhau", null); // Lấy mật khẩu hiện tại

        // Kiểm tra nếu không có email
        if (tentaikhoan == null) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

//        // Xử lý sự kiện quay lại
//        imgBack_doiMK.setOnClickListener(v -> finish());

        // Xử lý sự kiện khi nhấn nút đổi mật khẩu
        btnChangePass_doiMK.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        // Lấy dữ liệu từ các trường nhập
        String oldPassword = editOldPass_doiMK.getText().toString().trim();
        String newPassword = editNewPass_doiMK.getText().toString().trim();
        String confirmPassword = editConfPass_doiMK.getText().toString().trim();

        // Kiểm tra tính hợp lệ của dữ liệu nhập
        boolean isValid = true;

        // Kiểm tra mật khẩu cũ
        if (TextUtils.isEmpty(oldPassword)) {
            editOldPass_doiMK.setError("Vui lòng nhập mật khẩu cũ!");
            isValid = false;
        } else if (!oldPassword.equals(currentPassword)) {
            editOldPass_doiMK.setError("Mật khẩu cũ không đúng!");
            isValid = false;
        }

        // Kiểm tra mật khẩu mới
        if (TextUtils.isEmpty(newPassword)) {
            editNewPass_doiMK.setError("Vui lòng nhập mật khẩu mới!");
            isValid = false;
        } else if (newPassword.length() < 6) {
            editNewPass_doiMK.setError("Mật khẩu mới phải có ít nhất 6 ký tự!");
            isValid = false;
        }

        // Kiểm tra mật khẩu xác nhận
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfPass_doiMK.setError("Vui lòng nhập lại mật khẩu mới!");
            isValid = false;
        } else if (!newPassword.equals(confirmPassword)) {
            editConfPass_doiMK.setError("Mật khẩu xác nhận không khớp!");
            isValid = false;
        }

        // Nếu thông tin không hợp lệ thì dừng xử lý
        if (!isValid) return;

        // Tạo đối tượng CustomerAccount bằng Builder
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(tentaikhoan) // Email từ SharedPreferences
                .setMatkhauMoi(newPassword)  // Mật khẩu tạm thời
                .build();

        // Gọi API để lưu mật khẩu tạm thời
        apiService.saveTemporaryPassword(customerAccount).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    // Hiển thị thông báo thành công
                    Toast.makeText(Activity_DoiMK.this, "Mật khẩu mới đã được lưu tạm thời!", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình khác
                    Intent intent = new Intent(Activity_DoiMK.this,Manhinhguimathaydoimatkhau.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Hiển thị lỗi từ server
                    Toast.makeText(Activity_DoiMK.this, "Lưu mật khẩu thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Hiển thị lỗi kết nối
                Toast.makeText(Activity_DoiMK.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
