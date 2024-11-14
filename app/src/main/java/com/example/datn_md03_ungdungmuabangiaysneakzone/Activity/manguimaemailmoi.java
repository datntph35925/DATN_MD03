package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class manguimaemailmoi extends AppCompatActivity {

    private TextView tvCurrentAccount;
    private EditText tvEmailmoi;
    private Button btnNextmoi;
    private String tentaikhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manguimaemailmoi);

        // Khởi tạo các view
        tvCurrentAccount = findViewById(R.id.tvCurrentAccount);
        tvEmailmoi = findViewById(R.id.tvEmailmoi);
        btnNextmoi = findViewById(R.id.btnNextmoi);

        // Lấy tentaikhoan từ Intent hoặc SharedPreferences
        tentaikhoan = getIntent().getStringExtra("Tentaikhoan");
        if (tentaikhoan == null || tentaikhoan.isEmpty()) {
            tentaikhoan = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("Tentaikhoan", "");
        }

        // Hiển thị tentaikhoan nếu có dữ liệu
        if (tentaikhoan != null && !tentaikhoan.isEmpty()) {
            tvCurrentAccount.setText("Tài khoản hiện tại: " + tentaikhoan);
        } else {
            tvCurrentAccount.setText("Tài khoản hiện tại: Không xác định");
        }

        // Xử lý sự kiện khi nhấn "Tiếp theo"
        btnNextmoi.setOnClickListener(view -> {
            String emailMoi = tvEmailmoi.getText().toString().trim();
            if (emailMoi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email mới", Toast.LENGTH_SHORT).show();
                return;
            }
            sendVerificationCodeToNewEmail(tentaikhoan, emailMoi);
        });
    }

    private void sendVerificationCodeToNewEmail(String tentaikhoan, String emailMoi) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng CustomerAccount và thiết lập dữ liệu
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setTentaikhoan(tentaikhoan);
//        customerAccount.setEmailMoi(emailMoi); // Giả sử CustomerAccount có phương thức setEmailMoi
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(tentaikhoan)
                .setEmailMoi(emailMoi)
                .build();
        Log.d("Debug", "Sending data: " + customerAccount.getTentaikhoan() + ", " + customerAccount.getEmailMoi());

        // Gửi yêu cầu API
        Call<ApiResponse> call = apiService.sendVerificationCodeToNewEmail(customerAccount);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Log.d("API Response", "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", "Message: " + response.body().getMessage());
                    Toast.makeText(manguimaemailmoi.this, "Mã xác thực đã được gửi.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(manguimaemailmoi.this, xacthucemailmoi.class);
                    intent.putExtra("Tentaikhoan", tentaikhoan);
                    intent.putExtra("EmailMoi", emailMoi);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        // Lấy nội dung errorBody và in ra log
                        String errorBody = response.errorBody().string();
                        Log.e(  "API Error", "Error response: " + errorBody);
                        Toast.makeText(manguimaemailmoi.this, "Lỗi: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(manguimaemailmoi.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                    }
                }
            }



            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(manguimaemailmoi.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Failure", "Error: " + t.getMessage(), t);
            }
        });
    }
}
