package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.FavoriteAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Favorite;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_YeuThich extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView rcvYT;
    FavoriteAdapter favoriteAdapter;
    ApiService apiService;
    String email;
    ArrayList<Favorite> favoriteArrayList;

    private Handler handler = new Handler();  // Khai báo handler
    private Runnable fetchFavoriteRunnable;   // Khai báo runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_yeu_thich);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        setBottomNavigationView();

        rcvYT = findViewById(R.id.rcvYeuThich_YT);
        rcvYT.setLayoutManager(new GridLayoutManager(this, 2));
        favoriteArrayList = new ArrayList<>();

        Log.d("CartAdapter", "Current Quantity: " + favoriteArrayList);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        fetchFavoriteRunnable = new Runnable() {
            @Override
            public void run() {
                getListFavorite(); // Gọi API để tải tin nhắn
                handler.postDelayed(this, 1000); // Lặp lại sau mỗi giây
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(fetchFavoriteRunnable); // Bắt đầu tải tin nhắn định kỳ
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(fetchFavoriteRunnable); // Dừng tải tin nhắn khi Activity dừng
    }

//    favoriteArrayList = response.body().getData();
//    favoriteAdapter = new FavoriteAdapter(Activity_YeuThich.this, favoriteArrayList);
//                    rcvYT.setAdapter(favoriteAdapter);

    private void getListFavorite() {
        Call<Response<ArrayList<Favorite>>> call = apiService.getFavorites(email);
        call.enqueue(new Callback<Response<ArrayList<Favorite>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Favorite>>> call, retrofit2.Response<Response<ArrayList<Favorite>>> response) {
                if(response.isSuccessful() && response.body() != null){
                  favoriteArrayList = response.body().getData();
                  favoriteAdapter = new FavoriteAdapter(Activity_YeuThich.this, favoriteArrayList);
                  rcvYT.setAdapter(favoriteAdapter);
                } else {
                    Toast.makeText(Activity_YeuThich.this, "Lỗi khi lấy danh sách sản phẩm yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Favorite>>> call, Throwable t) {
                Toast.makeText(Activity_YeuThich.this, "looi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.trangchu){
                    startActivity(new Intent(Activity_YeuThich.this, MainActivity.class));
                } else if (item.getItemId() == R.id.giohang) {
                    startActivity(new Intent(Activity_YeuThich.this, Activity_Cart.class));
                } else if (item.getItemId() == R.id.hoso) {
                    startActivity(new Intent(Activity_YeuThich.this, Activity_Profile.class));
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.yeuthich);
    }
}