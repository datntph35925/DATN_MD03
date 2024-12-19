package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Activity_QuenMatKhau extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSend;
    private Button imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        // Khởi tạo các view
        edtEmail = findViewById(R.id.ed_email);
        btnSend = findViewById(R.id.btn_send);
        imgBack = findViewById(R.id.img_back);

        // Ẩn bàn phím khi nhấn ra ngoài
        setupUI(findViewById(R.id.relative_layout_id));

        // Xử lý sự kiện khi nhấn nút "Gửi"
        btnSend.setOnClickListener(view -> sendForgotPasswordRequest());

        // Xử lý sự kiện khi nhấn nút "Quay lại"
        imgBack.setOnClickListener(view -> {
            startActivity(new Intent(Activity_QuenMatKhau.this, DangNhap.class));
        });
    }

    private void sendForgotPasswordRequest() {
        String email = edtEmail.getText().toString().trim();

        // Kiểm tra đầu vào
        if (!validateEmail(email)) {
            return;
        }

        // Tạo instance của ApiService và gọi API
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(email)
                .build();

        Call<ApiResponse> call = apiService.forgotPassword(customerAccount);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Activity_QuenMatKhau.this, "Yêu cầu quên mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity_QuenMatKhau.this, DangNhap.class);
                    startActivity(intent);
                   finish(); // Kết thúc Activity hiện tại nếu muốn
                } else {
                    String errorMessage = "Gửi yêu cầu thất bại: " + response.code() + " - " + response.message();
                    Toast.makeText(Activity_QuenMatKhau.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("ForgotPasswordRequest", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Activity_QuenMatKhau.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ForgotPasswordRequest", "Error: " + t.getMessage(), t);
            }
        });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ! Vui lòng kiểm tra lại");
            edtEmail.requestFocus();
            return false;
        }

        if (email.contains(" ")) {
            edtEmail.setError("Email không được chứa khoảng trắng!");
            edtEmail.requestFocus();
            return false;
        }

        if (email.length() < 5) {
            edtEmail.setError("Email quá ngắn, vui lòng kiểm tra lại!");
            edtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void setupUI(View view) {
        // Thiết lập ẩn bàn phím khi nhấn ra ngoài
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                clearEditTextFocus();
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clearEditTextFocus() {
        edtEmail.clearFocus();
    }

    @Override
    public void onBackPressed() {
        // Không làm gì để ngăn không cho quay lại màn hình trước
    }
}
