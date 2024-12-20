package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView imgTogglePassword;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            navigateToMainActivity();
        } else {
            setContentView(R.layout.activity_dang_nhap);
            initializeViews();
            setupListeners();
            setupUI(findViewById(R.id.main)); // ID của layout cha
        }
    }

    private void initializeViews() {
        editTextEmail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        textviewQMK = findViewById(R.id.textviewQMK);
        imgTogglePassword = findViewById(R.id.imgTogglePasswordd); // Biểu tượng mắt
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void setupListeners() {
        // Chuyển tới màn hình đăng ký
        btnDangKy.setOnClickListener(view -> startActivity(new Intent(DangNhap.this, DangKy.class)));

        // Chuyển tới màn hình quên mật khẩu
        textviewQMK.setOnClickListener(view -> startActivity(new Intent(DangNhap.this, Activity_QuenMatKhau.class)));

        // Xử lý đăng nhập
        btnDangNhap.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!validateInputs(email, password)) return;

            CustomerAccount account = new CustomerAccount.Builder()
                    .setTentaikhoan(email)
                    .setMatkhau(password)
                    .build();

            performLogin(account);
        });

        // Xử lý ẩn/hiện mật khẩu
        imgTogglePassword.setOnClickListener(view -> togglePasswordVisibility());
    }

    private void setupUI(View view) {
        // Thiết lập ẩn bàn phím khi nhấn ra ngoài
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
        }

        // Nếu là ViewGroup, lặp qua các con của nó
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        if (email.isEmpty()) {
            editTextEmail.setError("Vui lòng nhập email!");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email không hợp lệ!");
            isValid = false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Vui lòng nhập mật khẩu!");
            isValid = false;
        } else if (password.length() < 6) {
            editTextPassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            isValid = false;
        }

        return isValid;
    }

    private void performLogin(CustomerAccount account) {
        apiService.login(account).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(account.getTentaikhoan(), account.getMatkhau());
                } else {
                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(DangNhap.this, "Lỗi kết nối. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(String email, String password) {
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Tentaikhoan", email);
        editor.putString("matkhau", password);
        editor.putBoolean("is_logged_in", true);
        editor.apply();

        navigateToMainActivity();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(DangNhap.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Đặt mật khẩu bị ẩn
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgTogglePassword.setImageResource(R.drawable.baseline_visibility_off_24); // Đổi icon thành "mắt đóng"
        } else {
            // Đặt mật khẩu hiển thị
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgTogglePassword.setImageResource(R.drawable.baseline_visibility_24); // Đổi icon thành "mắt mở"
        }
        isPasswordVisible = !isPasswordVisible;
        editTextPassword.setSelection(editTextPassword.getText().length()); // Đặt con trỏ ở cuối
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus(); // Xóa tiêu điểm khỏi EditText
        }
    }

    @Override
    public void onBackPressed() {
        // Ngăn không cho quay lại màn hình trước
    }
}
