package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Activity_Profile extends AppCompatActivity {

    TextView tvChinhSuaThongTin;
    CardView cvChangePass, cvAdddiachi, cvOderForShop, cvChatBox;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        CardView cvAddDiaChi = findViewById(R.id.cvAdddiachi);
        cvAddDiaChi.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Profile.this, ShowListLocationActivity.class);
            startActivity(intent);
        });
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        setBottomNavigationView();

        tvChinhSuaThongTin = findViewById(R.id.tvFixInfor_profile);
        tvChinhSuaThongTin.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, Activity_FixInfor.class));
        });

        cvChangePass = findViewById(R.id.cvChangePass);
        cvChangePass.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, Activity_DoiMK.class));
        });

        cvOderForShop = findViewById(R.id.cvOderForShop);
        cvOderForShop.setOnClickListener(view -> {
            startActivity(new Intent(Activity_Profile.this, Activity_DonHang.class));
        });

    }

    public void setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.trangchu){
                    startActivity(new Intent(Activity_Profile.this, MainActivity.class));
                } else if (item.getItemId() == R.id.giohang) {
                    startActivity(new Intent(Activity_Profile.this, Activity_Cart.class));
                } else if (item.getItemId() == R.id.yeuthich) {
                    startActivity(new Intent(Activity_Profile.this, Activity_YeuThich.class));
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.hoso);
    }
}