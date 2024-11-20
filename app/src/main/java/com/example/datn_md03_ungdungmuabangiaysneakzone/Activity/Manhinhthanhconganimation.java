package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.airbnb.lottie.LottieAnimationView;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

public class Manhinhthanhconganimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhthanhconganimation);

        // Thiết lập cho Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo LottieAnimationView và chạy animation
        LottieAnimationView successAnimationView = findViewById(R.id.successAnimationView);
        successAnimationView.playAnimation();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Manhinhthanhconganimation.this, Activity_FixInfor.class);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại
        }, 3000); // 3000ms = 3 giây
    }

}
