package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.YeuThichAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.YeuThich_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Activity_YeuThich extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_yeu_thich);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        setBottomNavigationView();

        RecyclerView recyclerView = findViewById(R.id.rcvYeuThich_YT);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<YeuThich_Demo> shoeList = new ArrayList<>();
        shoeList.add(new YeuThich_Demo("Nike Jordan", 58.7, R.drawable.nice_shoe));
        shoeList.add(new YeuThich_Demo("Nike Air Max", 37.8, R.drawable.nice_shoe));
        shoeList.add(new YeuThich_Demo("Nike Club Max", 47.7, R.drawable.nice_shoe));


        YeuThichAdapter_Demo adapter = new YeuThichAdapter_Demo(shoeList);
        recyclerView.setAdapter(adapter);
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