package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SanPhamAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    RecyclerView spRecyclerView;
    ApiService apiService;
    SanPhamAdapter productAdapter;
    ArrayList<Product> productArrayList;
    BottomNavigationView bottomNavigationView;
    TextView tvXemThem;
    ImageView imageViewSlider, imageView; // ImageView cho slideshow
    ImageButton btnPrev, btnNext; // Nút điều khiển ảnh
    int[] imageList = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3}; // Danh sách ảnh slideshow
    int currentImageIndex = 0; // Chỉ mục ảnh hiện tại
    Handler handler = new Handler(); // Handler để quản lý thời gian trễ cho slideshow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        imageViewSlider = findViewById(R.id.imageViewSlider); // ImageView cho slideshow
        btnPrev = findViewById(R.id.btnPrev); // Nút "Trước đó"
        btnNext = findViewById(R.id.btnNext); // Nút "Tiếp theo"
        setBottomNavigationView();
        imageView = findViewById(R.id.imageView);
        tvXemThem = findViewById(R.id.tvXemThem);
        tvXemThem.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Activity_SP_PhoBien.class));
        });

        spRecyclerView = findViewById(R.id.SanPhamPhoBienView);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        productArrayList = new ArrayList<>();
        // Gọi API để lấy danh sách sản phẩm
        getListProducts();

        // Bắt đầu slideshow
        startSlideshow();

        // Nút "Trước đó"
        btnPrev.setOnClickListener(v -> {
            // Giảm chỉ mục và cập nhật ảnh
            currentImageIndex--;
            if (currentImageIndex < 0) {
                currentImageIndex = imageList.length - 1; // Quay lại ảnh cuối cùng
            }
            showImage();
        });

        // Nút "Tiếp theo"
        btnNext.setOnClickListener(v -> {
            // Tăng chỉ mục và cập nhật ảnh
            currentImageIndex++;
            if (currentImageIndex >= imageList.length) {
                currentImageIndex = 0; // Quay lại ảnh đầu tiên
            }
            showImage();
        });
//        imageView.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
//            startActivity(intent);
//            finish(); // Đảm bảo không quay lại màn hình đăng nhập
//        });
    }

    private void loadDuLieu(ArrayList<Product> list) {
        productAdapter = new SanPhamAdapter(this, list);
        spRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        spRecyclerView.setAdapter(productAdapter);
    }

    private void getListProducts() {
        Call<Response<ArrayList<Product>>> call = apiService.getListProducts();
        call.enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        productArrayList = response.body().getData();
                        loadDuLieu(productArrayList);
                    } else {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSlideshow() {
        Runnable slideshowRunnable = new Runnable() {
            @Override
            public void run() {
                // Tạo hiệu ứng chuyển đổi mượt mà
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imageViewSlider, "alpha", 1f, 0f);
                fadeOut.setDuration(500); // Thời gian fade-out

                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Đổi ảnh sau khi fade-out
                        imageViewSlider.setImageResource(imageList[currentImageIndex]);

                        // Tạo hiệu ứng fade-in khi ảnh mới được hiển thị
                        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageViewSlider, "alpha", 0f, 1f);
                        fadeIn.setDuration(200); // Thời gian fade-in
                        fadeIn.start();
                    }
                });

                fadeOut.start();

                // Cập nhật chỉ mục ảnh
                currentImageIndex++;
                if (currentImageIndex >= imageList.length) {
                    currentImageIndex = 0; // Quay lại ảnh đầu tiên nếu đến ảnh cuối cùng
                }

                // Lặp lại sau 3 giây
                handler.postDelayed(this, 3000); // 3000ms = 3 giây
            }
        };

        // Bắt đầu slideshow ngay khi Activity bắt đầu
        handler.post(slideshowRunnable);
    }


    // Hàm để hiển thị ảnh mà không dùng animation
    private void showImage() {
        imageViewSlider.setImageResource(imageList[currentImageIndex]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy bỏ việc cập nhật khi activity bị destroy để tránh rò rỉ bộ nhớ
        handler.removeCallbacksAndMessages(null);
    }

    public void setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.yeuthich) {
                    startActivity(new Intent(MainActivity.this, Activity_YeuThich.class));
                } else if (item.getItemId() == R.id.giohang) {
                    startActivity(new Intent(MainActivity.this, Activity_Cart.class));
                } else if (item.getItemId() == R.id.hoso) {
                    startActivity(new Intent(MainActivity.this, Activity_Profile.class));
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.trangchu);
    }
}
