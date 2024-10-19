package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment.TrangChuFragment;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TrangChuFragment trangChuFragment = new TrangChuFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, trangChuFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.trangchu) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, trangChuFragment).commit();
                    return true;
                }
//                } else if (itemId == R.id.yeuthich) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, yeuThichFragment).commit();
//                    return true;
//                } else if (itemId == R.id.giohang) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, gioHangFragment).commit();
//                    return true;
//                } else if (itemId == R.id.hoso) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, hoSoFragment).commit();
//                    return true;
//                }

                return false;

            }
        });

    }

}
