package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

public class Activity_Profile extends AppCompatActivity {

    TextView tvChinhSuaThongTin;
    CardView cvChangePass, cvAdddiachi, cvOderForShop, cvChatBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
}