package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
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
    ImageView imageViewSlider; // ImageView cho slideshow
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
        setBottomNavigationView();

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
    }

    private void loadDuLieu(ArrayList<Product> list){
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
                    if(response.body().getStatus() == 200) {
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
        // Runnable để thay đổi hình ảnh mỗi 3 giây
        Runnable slideshowRunnable = new Runnable() {
            @Override
            public void run() {
                // Đổi ảnh trong ImageView
                imageViewSlider.setImageResource(imageList[currentImageIndex]);

                // Cập nhật chỉ mục ảnh
                currentImageIndex++;
                if (currentImageIndex >= imageList.length) {
                    currentImageIndex = 0; // Nếu đến ảnh cuối cùng thì quay lại ảnh đầu tiên
                }

                // Lặp lại sau 3 giây
                handler.postDelayed(this, 3000); // 3000ms = 3 giây
            }
        };

        // Bắt đầu slideshow
        handler.post(slideshowRunnable);
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
                if(item.getItemId() == R.id.yeuthich){
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
