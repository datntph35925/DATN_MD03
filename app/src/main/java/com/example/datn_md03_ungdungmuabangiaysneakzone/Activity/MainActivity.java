package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
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
