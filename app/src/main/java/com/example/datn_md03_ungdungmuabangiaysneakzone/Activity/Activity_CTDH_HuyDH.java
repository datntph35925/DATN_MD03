package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.ThanhToanAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.ArrayList;

public class Activity_CTDH_HuyDH extends AppCompatActivity {
    RecyclerView rcvCTSP_Huy;
    TextView tvName, tvAdress, tvPhone, tvPayMent, tvTT, tvTCP;
    String name, phone, address, payMent, tt;
    ArrayList<ProductItemCart> sanPhamList;
    ThanhToanAdapter thanhToanAdapter;
    double tcp;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ctdh_huy_dh);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rcvCTSP_Huy = findViewById(R.id.rcvCTDH_HuyDH);

        tvTT = findViewById(R.id.tvTrangThai_CTDH_HuyDH);
        tvTCP = findViewById(R.id.tvTCP_CTDH_HuyDH);

        Intent intent = getIntent();
        name = intent.getStringExtra("order_ten");
        address = intent.getStringExtra("order_diachi");
        phone = intent.getStringExtra("order_sdt");
        payMent = intent.getStringExtra("order_pttt");
        tt = intent.getStringExtra("order_ttdh");
        tcp = intent.getDoubleExtra("order_tongTien", 0.0);
        imgBack = findViewById(R.id.imageView4);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_CTDH_HuyDH.this, Activity_DonHang.class);
                intent.putExtra("select_tab", 3);
                startActivity(intent);
                finish();
            }
        });
        
        tvTT.setText(tt);
        tvTCP.setText(String.format("%.2f", tcp));

        sanPhamList = new ArrayList<>();
        sanPhamList = (ArrayList<ProductItemCart>) intent.getSerializableExtra("order_sp");
        thanhToanAdapter = new ThanhToanAdapter(this, sanPhamList, true);
        rcvCTSP_Huy.setLayoutManager(new LinearLayoutManager(this));
        rcvCTSP_Huy.setAdapter(thanhToanAdapter);
    }
}