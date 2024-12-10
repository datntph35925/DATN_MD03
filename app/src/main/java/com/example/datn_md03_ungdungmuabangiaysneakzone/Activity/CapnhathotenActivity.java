package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CapnhathotenActivity extends AppCompatActivity {

    private static final String TAG = "CapnhathotenActivity";

    private TextView tvCurrentName;
    private EditText edtNewName;
    private Button btnUpdateName;
    private SharedPreferences sharedPreferences;
    private String tentaikhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhathoten);

        // Ánh xạ các thành phần giao diện
        tvCurrentName = findViewById(R.id.tvCurrentName);
        edtNewName = findViewById(R.id.edtNewName);
        btnUpdateName = findViewById(R.id.btnChangeName);
        ImageView imgback = findViewById(R.id.buttonBack_hoten);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CapnhathotenActivity.this, Activity_FixInfor.class);
                startActivity(intent);
            }
        });

        // Lấy thông tin tài khoản từ SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        tentaikhoan = sharedPreferences.getString("Tentaikhoan", "");

        if (TextUtils.isEmpty(tentaikhoan)) {
            Toast.makeText(this, "Không tìm thấy tài khoản. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Tentaikhoan không tồn tại trong SharedPreferences.");
            finish();
            return;
        }

        Log.d(TAG, "Tentaikhoan: " + tentaikhoan);

        // Lấy họ tên hiện tại và hiển thị
        getCurrentName();

        // Xử lý sự kiện khi nhấn "Cập nhật họ tên"
        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = edtNewName.getText().toString().trim();

                if (TextUtils.isEmpty(newName)) {
                    edtNewName.setError("Vui lòng nhập họ tên mới!");
                    return;
                }

                updateName(newName);
            }
        });
    }

    // Phương thức lấy họ tên hiện tại
    private void getCurrentName() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getUserDetails(tentaikhoan).enqueue(new Callback<CustomerAccount>() {
            @Override
            public void onResponse(Call<CustomerAccount> call, Response<CustomerAccount> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerAccount account = response.body();

                    // Debug log để kiểm tra dữ liệu trả về từ API
                    Log.d(TAG, "Dữ liệu tài khoản từ API: " + account.toString());

                    // Hiển thị họ tên
                    tvCurrentName.setText(account.getHoten() != null ? "Họ tên hiện tại: " + account.getHoten() : "Họ tên hiện tại: Chưa cập nhật");

                    // Nếu cần thêm các xử lý khác từ API (như ảnh đại diện), có thể bổ sung tại đây
                    Log.d(TAG, "Thông tin họ tên đã được hiển thị thành công.");
                } else {
                    // Xử lý lỗi khi API trả về nhưng không có dữ liệu
                    Toast.makeText(CapnhathotenActivity.this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API trả về lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CustomerAccount> call, Throwable t) {
                // Xử lý lỗi khi kết nối API thất bại
                Toast.makeText(CapnhathotenActivity.this, "Lỗi kết nối mạng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Lỗi khi gọi API: ", t);
            }
        });
    }


    // Phương thức cập nhật họ tên mới
    private void updateName(String newName) {
        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(this, "Họ tên mới không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng chứa dữ liệu cần gửi trong body
        Map<String, String> params = new HashMap<>();
        params.put("Tentaikhoan", tentaikhoan);
        params.put("HotenMoi", newName);

        // Gửi dữ liệu qua body của PUT request
        apiService.changeNameWithBody(params).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CapnhathotenActivity.this, "Họ tên đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng sau khi cập nhật
                    Intent intent = new Intent(CapnhathotenActivity.this, Activity_Profile.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CapnhathotenActivity.this, "Lỗi: Không thể cập nhật họ tên.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API trả về lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CapnhathotenActivity.this, "Lỗi mạng: Không thể kết nối tới server.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Lỗi khi gọi API: ", t);
            }
        });
    }


}
