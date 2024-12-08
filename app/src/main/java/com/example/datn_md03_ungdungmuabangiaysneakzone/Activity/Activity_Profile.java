package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Profile extends AppCompatActivity {

    // Khai báo các view
    private ImageView imgAvatarProfile;
    private TextView tvNameProduct, tvEmail, tvChinhSuaThongTin;
    private CardView cvAdddiachi, cvOderForShop, cvChatBox , cvOut, cvVoucher;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ các view
        initView();

        // Lấy thông tin người dùng từ SharedPreferences
        loadUserData();

        // Thiết lập các sự kiện click
        setClickListeners();

        // Thiết lập BottomNavigationView
        setBottomNavigationView();
    }

    private void initView() {
        imgAvatarProfile = findViewById(R.id.imgAvatar_profile);
        tvNameProduct = findViewById(R.id.tvName_product);
        tvEmail = findViewById(R.id.tvEmail);
        tvChinhSuaThongTin = findViewById(R.id.tvFixInfor_profile);
        cvAdddiachi = findViewById(R.id.cvAdddiachi);
        cvOderForShop = findViewById(R.id.cvOderForShop);
        cvChatBox = findViewById(R.id.cvChatBox);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        cvOut = findViewById(R.id.cvOut);
        cvVoucher = findViewById(R.id.cvVoucher);
    }

    private void loadUserData() {
        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String tentaikhoan = sharedPreferences.getString("Tentaikhoan", null);

        // Kiểm tra nếu không có tên tài khoản trong SharedPreferences
        if (tentaikhoan == null || tentaikhoan.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy tài khoản trong SharedPreferences!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Tentaikhoan null hoặc rỗng trong SharedPreferences");
            return;
        }

        // Debug log để kiểm tra tên tài khoản
        Log.d(TAG, "Tên tài khoản lấy từ SharedPreferences: " + tentaikhoan);

        // Hiển thị tên tài khoản tạm thời (nếu cần)
        tvEmail.setText(tentaikhoan);

        // Gọi API để lấy thông tin chi tiết người dùng
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserDetails(tentaikhoan).enqueue(new Callback<CustomerAccount>() {
            @Override
            public void onResponse(Call<CustomerAccount> call, Response<CustomerAccount> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerAccount account = response.body();

                    // Debug log để kiểm tra dữ liệu trả về từ API
                    Log.d(TAG, "Dữ liệu tài khoản từ API: " + account);

                    // Hiển thị họ tên
                    tvNameProduct.setText(account.getHoten() != null ? account.getHoten() : "Chưa cập nhật");

                    // Hiển thị ảnh đại diện
                    String avatarUrl = account.getAnhtk();
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        // Kiểm tra và xây dựng URL đầy đủ cho ảnh
                        String imageUrl = "http://10.0.2.2:3000/" + avatarUrl;
                        Glide.with(Activity_Profile.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.anh1) // Ảnh mặc định khi đang tải
                                .error(R.drawable.anh1)       // Ảnh lỗi khi tải thất bại
                                .into(imgAvatarProfile);
                    } else {
                        imgAvatarProfile.setImageResource(R.drawable.anh1);
                    }

                    Log.d(TAG, "Thông tin tài khoản đã được hiển thị thành công.");
                } else {
                    // Xử lý lỗi khi API trả về nhưng không có dữ liệu
                    Toast.makeText(Activity_Profile.this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API trả về lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CustomerAccount> call, Throwable t) {
                // Xử lý lỗi khi kết nối API thất bại
                Toast.makeText(Activity_Profile.this, "Lỗi kết nối mạng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Lỗi khi gọi API: ", t);
            }
        });
    }

    private void setClickListeners() {
        tvChinhSuaThongTin.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, Activity_FixInfor.class));
        });

        cvAdddiachi.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, ActivityLocation.class));
        });

        cvOderForShop.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, Activity_DonHang.class));
        });

        cvChatBox.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this,activity_chat.class));
        });

        cvVoucher.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, Activity_Voucher.class));
        });

        cvOut.setOnClickListener(view -> {
            // Xóa thông tin đăng nhập khỏi SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("Tentaikhoan");  // Xóa tên tài khoản
            editor.putBoolean("is_logged_in", false);  // Đặt trạng thái đăng nhập thành false
            editor.apply();

            // Quay lại màn hình đăng nhập
            Intent intent = new Intent(Activity_Profile.this, DangNhap.class);
            startActivity(intent);
            finish();  // Đảm bảo không thể quay lại màn hình Profile sau khi đăng xuất
        });

        // Uncomment nếu cần sử dụng chức năng nhắn tin
        /*
        cvChatBox.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, ChatBoxActivity.class));
        });
        */
    }

    private void setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.trangchu) {
                    startActivity(new Intent(Activity_Profile.this, MainActivity.class));
                } else if (item.getItemId() == R.id.giohang) {
                    startActivity(new Intent(Activity_Profile.this, Activity_Cart.class));
                } else if (item.getItemId() == R.id.yeuthich) {
                    startActivity(new Intent(Activity_Profile.this, Activity_YeuThich.class));
                }
                return true;
            }
        });

        // Đặt tab "Hồ sơ" được chọn mặc định
        bottomNavigationView.setSelectedItemId(R.id.hoso);
    }
}
