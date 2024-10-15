package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.Adapter_ViewPage2_DonHang;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Activity_DonHang extends AppCompatActivity {

    ImageView imgBack_donHang;
    private TabLayout tablayout;
    private TabItem tab1;
    private TabItem tab2;
    private TabItem tab3;
    private TabItem tab4;
    private TabItem tab5;
    private ViewPager2 viewpagerTablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_don_hang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgBack_donHang = findViewById(R.id.imgBack_donHang);
        imgBack_donHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_DonHang.this, Activity_Profile.class));
            }
        });

        tablayout = (TabLayout)findViewById(R.id.tablayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        tab3 = (TabItem) findViewById(R.id.tab3);
        tab4 = (TabItem) findViewById(R.id.tab4);
        viewpagerTablayout = (ViewPager2) findViewById(R.id.viewpager_tablayout);

        Adapter_ViewPage2_DonHang adapterViewPage2DonHang = new Adapter_ViewPage2_DonHang(this);
        viewpagerTablayout.setAdapter(adapterViewPage2DonHang);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tablayout, viewpagerTablayout, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
              switch (position){
                  case 0:
                      tab.setText("Chờ xác nhận");
                      break;
                  case 1:
                      tab.setText("Đang giao");
                      break;
                  case 2:
                      tab.setText("Đã hoàn thành");
                      break;
                  case 3:
                      tab.setText("Đã hủy");
                      break;
              }
            }
        });
        tabLayoutMediator.attach();
    }
}