package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

public class Activity_FixInfor extends AppCompatActivity {

    Button btnEmaill,btnChangePass,btnName , btn_Imgae;
    ImageView imgBack_fixInfor ;
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
        btn_Imgae = findViewById(R.id.btncapnhat_Imgae);
        btnName = findViewById(R.id.btnName);
        btnEmaill = findViewById(R.id.btnEmaill);
        btnChangePass = findViewById(R.id.btnChangePass);
        imgBack_fixInfor = findViewById(R.id.imgBack_fixinfor);
        imgBack_fixInfor.setOnClickListener(view -> {
            startActivity(new Intent(Activity_FixInfor.this, Activity_Profile.class));
        });
//        btnBack.setOnClickListener(v -> {
//            // Chuyển sang Activity khác khi nhấn nút quay lại
//            Intent intent = new Intent(Activity_FixInfor.this, Activity_Profile.class);
//            startActivity(intent);
//            finish(); // Kết thúc Activity hiện tại nếu muốn
//        });

        btn_Imgae.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, activity_update_avatar.class ));
        });
        btnName.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, CapnhathotenActivity.class));
        });
        btnEmaill.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, manhinhnhanguima.class));
        });
        btnChangePass.setOnClickListener(v -> {

            startActivity(new Intent(Activity_FixInfor.this, Activity_DoiMK.class));
        });
    }
}