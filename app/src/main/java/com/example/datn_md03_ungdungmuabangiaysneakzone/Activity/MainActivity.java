package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SanPhamAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.CartAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.Cart_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment.TrangChuFragment;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView spRecyclerView;
    private SanPhamAdapter sanPhamAdapter;
    private List<SanPham> sanPhams;
    //asdsd
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        setBottomNavigationView();

        spRecyclerView = findViewById(R.id.SanPhamPhoBienView);
        spRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sanPhams = new ArrayList<>();
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));
        sanPhams.add(new SanPham("Nike Air Max", 64.95, R.drawable.nice_shoe));

        sanPhamAdapter = new SanPhamAdapter((ArrayList<SanPham>) sanPhams);
        spRecyclerView.setAdapter(sanPhamAdapter);
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
