package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class xacthucemailmoi extends AppCompatActivity {

    private EditText edtVerificationCode1, edtVerificationCode2, edtVerificationCode3,
            edtVerificationCode4, edtVerificationCode5, edtVerificationCode6;
    private Button btnChangeEmail;
    private String tentaikhoan, emailMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacthucemailmoi);

        // Khởi tạo các view
        edtVerificationCode1 = findViewById(R.id.edtVerificationCode1);
        edtVerificationCode2 = findViewById(R.id.edtVerificationCode2);
        edtVerificationCode3 = findViewById(R.id.edtVerificationCode3);
        edtVerificationCode4 = findViewById(R.id.edtVerificationCode4);
        edtVerificationCode5 = findViewById(R.id.edtVerificationCode5);
        edtVerificationCode6 = findViewById(R.id.edtVerificationCode6);
        btnChangeEmail = findViewById(R.id.btnChangeEmail);

        // Lấy dữ liệu từ Intent
        tentaikhoan = getIntent().getStringExtra("Tentaikhoan");
        emailMoi = getIntent().getStringExtra("EmailMoi");

        // Kiểm tra xem Tentaikhoan và EmailMoi có dữ liệu không
        if (tentaikhoan == null || emailMoi == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin tài khoản hoặc email mới", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Xử lý sự kiện khi nhấn "Thay đổi"
        btnChangeEmail.setOnClickListener(view -> verifyEmailCode());
    }

    private void verifyEmailCode() {
        // Ghép các mã xác thực từ các EditText
        String code = edtVerificationCode1.getText().toString().trim() +
                edtVerificationCode2.getText().toString().trim() +
                edtVerificationCode3.getText().toString().trim() +
                edtVerificationCode4.getText().toString().trim() +
                edtVerificationCode5.getText().toString().trim() +
                edtVerificationCode6.getText().toString().trim();

        if (code.length() != 6) {
            Toast.makeText(this, "Mã xác thực phải có 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi mã xác thực và cập nhật email mới
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("Tentaikhoan", tentaikhoan);
        requestData.put("EmailMoi", emailMoi);
        requestData.put("verificationCode", code);

        Call<ApiResponse> call = apiService.verifyCodeAndUpdateEmail(requestData);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(xacthucemailmoi.this, "Email đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                    // Quay về màn hình chính hoặc thực hiện hành động tiếp theo
                    Intent intent = new Intent(xacthucemailmoi.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Xử lý lỗi từ phản hồi không thành công
                    String errorMessage = "Lỗi: " + (response.body() != null ? response.body().getMessage() : response.message());
                    Toast.makeText(xacthucemailmoi.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(xacthucemailmoi.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
