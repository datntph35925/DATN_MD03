package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private ImageView imgBack_doiMK, imgToggleOldPass, imgToggleNewPass, imgToggleConfPass;
    private EditText editOldPass_doiMK, editNewPass_doiMK, editConfPass_doiMK;
    private Button btnChangePass_doiMK;
    private ApiService apiService;
    private String tentaikhoan, currentPassword;
    private ConstraintLayout mainLayout;

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
        mainLayout = findViewById(R.id.main);
        imgToggleOldPass = findViewById(R.id.imgToggleOldPass);
        imgToggleNewPass = findViewById(R.id.imgToggleNewPass);
        imgToggleConfPass = findViewById(R.id.imgToggleConfPass);

        // Thiết lập sự kiện ẩn bàn phím khi nhấn bên ngoài EditText
        setupHideKeyboardOnTouchOutside();

        // Sự kiện quay lại màn hình trước
        imgBack_doiMK.setOnClickListener(v -> onBackPressed());

        // Khởi tạo API Service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Lấy thông tin tài khoản từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        tentaikhoan = sharedPreferences.getString("Tentaikhoan", null);
        currentPassword = sharedPreferences.getString("matkhau", null);

        if (tentaikhoan == null || currentPassword == null) {
            showToast("Không tìm thấy thông tin tài khoản. Vui lòng đăng nhập lại.");
            finish();
            return;
        }

        // Thiết lập chức năng ẩn/hiện mật khẩu
        setupPasswordVisibilityToggle(imgToggleOldPass, editOldPass_doiMK);
        setupPasswordVisibilityToggle(imgToggleNewPass, editNewPass_doiMK);
        setupPasswordVisibilityToggle(imgToggleConfPass, editConfPass_doiMK);

        // Xử lý sự kiện khi nhấn nút "Đổi mật khẩu"
        btnChangePass_doiMK.setOnClickListener(v -> handleChangePassword());
    }

    private void setupHideKeyboardOnTouchOutside() {
        mainLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            return true;
        });
    }

    private void setupPasswordVisibilityToggle(ImageView toggleButton, EditText editText) {
        toggleButton.setOnClickListener(v -> {
            if (editText.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Hiển thị mật khẩu
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleButton.setImageResource(R.drawable.baseline_visibility_off_24); // Biểu tượng mắt đóng
            } else {
                // Ẩn mật khẩu
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleButton.setImageResource(R.drawable.baseline_visibility_24); // Biểu tượng mắt mở
            }
            editText.setSelection(editText.getText().length());
        });
    }

    private void handleChangePassword() {
        String oldPassword = editOldPass_doiMK.getText().toString().trim();
        String newPassword = editNewPass_doiMK.getText().toString().trim();
        String confirmPassword = editConfPass_doiMK.getText().toString().trim();

        if (!validateInputs(oldPassword, newPassword, confirmPassword)) return;

        CustomerAccount customerAccount = new CustomerAccount.Builder()
                .setTentaikhoan(tentaikhoan)
                .setMatkhauMoi(newPassword)
                .build();

        apiService.saveTemporaryPassword(customerAccount).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    showToast("Mật khẩu mới đã được lưu thành công!");
                    navigateToNextScreen();
                } else {
                    showToast("Đổi mật khẩu thất bại. Vui lòng thử lại!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showToast("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private boolean validateInputs(String oldPassword, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            editOldPass_doiMK.setError("Vui lòng nhập mật khẩu cũ!");
            return false;
        }
        if (!oldPassword.equals(currentPassword)) {
            editOldPass_doiMK.setError("Mật khẩu cũ không đúng!");
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            editNewPass_doiMK.setError("Vui lòng nhập mật khẩu mới!");
            return false;
        }
        if (newPassword.length() < 6) {
            editNewPass_doiMK.setError("Mật khẩu mới phải có ít nhất 6 ký tự!");
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfPass_doiMK.setError("Vui lòng nhập lại mật khẩu mới!");
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            editConfPass_doiMK.setError("Mật khẩu xác nhận không khớp!");
            return false;
        }
        return true;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showToast(String message) {
        Toast.makeText(Activity_DoiMK.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToNextScreen() {
        Intent intent = new Intent(Activity_DoiMK.this, Manhinhguimathaydoimatkhau.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Activity_DoiMK.this, Activity_FixInfor.class);
        startActivity(intent);
        finish();
    }
}
