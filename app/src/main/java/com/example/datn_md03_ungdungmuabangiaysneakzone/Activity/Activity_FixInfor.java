package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

public class Activity_FixInfor extends AppCompatActivity {

    Button btnback;
    ImageView imgBack_fixInfor ;
    TextView textanh, textten, textemail, textmatkhau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fix_infor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // btnBack = findViewById(R.id.btnBack);
        textanh = findViewById(R.id.tvUpdateImageTitle);
        textten = findViewById(R.id.tvUpdateNameTitle);
        textemail = findViewById(R.id.tvUpdateEmailTitle);
        textmatkhau = findViewById(R.id.tvChangePassTitle);
        btnback = findViewById(R.id.btnBackToHome);
        btnback.setOnClickListener(view -> {
            startActivity(new Intent(Activity_FixInfor.this, Activity_Profile.class));
        });
//        btnBack.setOnClickListener(v -> {
//            // Chuyển sang Activity khác khi nhấn nút quay lại
//            Intent intent = new Intent(Activity_FixInfor.this, Activity_Profile.class);
//            startActivity(intent);
//            finish(); // Kết thúc Activity hiện tại nếu muốn
//        });

        textanh.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, activity_update_avatar.class ));
        });
        textten.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, CapnhathotenActivity.class));
        });
        textemail.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, manhinhnhanguima.class));
        });
        textmatkhau.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, Activity_DoiMK.class));
        });
    }
}