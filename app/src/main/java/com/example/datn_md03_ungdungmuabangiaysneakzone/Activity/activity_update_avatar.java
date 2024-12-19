package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;

import java.io.File;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_update_avatar extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private CircleImageView imgAvatar;
    private Button btnSelectImage, btnUploadImage;
    private Uri selectedImageUri;
    private SharedPreferences sharedPreferences;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_avatar);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        // Kiểm tra và yêu cầu quyền
        checkAndRequestPermissions();

        // Xử lý Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần giao diện
        imgBack = findViewById(R.id.imgBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);

        // Tải ảnh đại diện hiện tại từ cơ sở dữ liệu
        loadExistingAvatar();

        // Chọn ảnh từ thư viện
        btnSelectImage.setOnClickListener(v -> openImageChooser());

        // Upload ảnh mới
        btnUploadImage.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadAvatar(selectedImageUri);
            } else {
                Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });
        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(activity_update_avatar.this, Activity_FixInfor.class);
            startActivity(intent);
        });
    }

    // Phương thức để tải ảnh đại diện hiện tại
    private void loadExistingAvatar() {
        String tentaikhoanValue = sharedPreferences.getString("Tentaikhoan", "");

        if (tentaikhoanValue.isEmpty()) {
            Toast.makeText(this, "Tên tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API để lấy thông tin người dùng
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserDetails(tentaikhoanValue).enqueue(new Callback<CustomerAccount>() {
            @Override
            public void onResponse(Call<CustomerAccount> call, Response<CustomerAccount> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerAccount account = response.body();
                    String avatarUrl = account.getAnhtk();

                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        // Dùng Glide để tải ảnh vào CircleImageView
                        String fullUrl = "http://160.191.50.148:3000/" + avatarUrl; // Thêm đường dẫn đầy đủ
                        Glide.with(activity_update_avatar.this)
                                .load(fullUrl)
                                .placeholder(R.drawable.anh1) // Đặt ảnh mặc định khi tải
                                .error(R.drawable.btn_4) // Đặt ảnh mặc định khi có lỗi
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageResource(R.drawable.btn_4); // Đặt ảnh mặc định nếu URL trống
                    }
                } else {
                    Toast.makeText(activity_update_avatar.this, "Lỗi khi tải ảnh từ server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerAccount> call, Throwable t) {
                Toast.makeText(activity_update_avatar.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Mở trình chọn ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Sử dụng Glide để tải ảnh vào CircleImageView
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imgAvatar);
        }
    }

    private void uploadAvatar(Uri imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        if (filePath == null || filePath.isEmpty()) {
            Toast.makeText(this, "Không thể lấy đường dẫn file!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);

        // Lấy tentaikhoan từ SharedPreferences
        String tentaikhoanValue = sharedPreferences.getString("Tentaikhoan", "");
        if (tentaikhoanValue.isEmpty()) {
            Toast.makeText(this, "Tên tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo RequestBody từ file ảnh
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("AnhtkMoi", file.getName(), requestFile);

        // Tạo RequestBody cho tên tài khoản
        RequestBody tentaikhoan = RequestBody.create(MediaType.parse("text/plain"), tentaikhoanValue);

        // Gọi API qua Retrofit
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.uploadAvatar(tentaikhoan, body).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity_update_avatar.this, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_update_avatar.this, Manhinhthanhconganimation.class);
                    startActivity(intent);
//                    finish(); // Kết thúc Activity hiện tại nếu muốn
                } else {
                    Toast.makeText(activity_update_avatar.this, "Lỗi khi cập nhật ảnh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(activity_update_avatar.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    // Kiểm tra và yêu cầu quyền
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6.0+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập bộ nhớ đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lấy đường dẫn file thực từ URI
    private String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        }
        return path;
    }
}