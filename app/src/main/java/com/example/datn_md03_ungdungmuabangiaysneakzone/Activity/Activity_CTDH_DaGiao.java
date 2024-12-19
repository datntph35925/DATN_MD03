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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Activity_CTDH_DaGiao extends AppCompatActivity {
    RecyclerView rcvCTSP_DaGiao;
    TextView tvName, tvAdress, tvPhone, tvPayMent, tvTT, tvTCP;
    String name, phone, address, payMent, tt;
    ArrayList<ProductItemCart> sanPhamList;
    ThanhToanAdapter thanhToanAdapter;
    double tcp;

    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Bật chế độ Edge-to-Edge UI
        setContentView(R.layout.activity_ctdh_da_giao);

        // Áp dụng padding để tránh tràn UI vào vùng hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liên kết các thành phần giao diện
        rcvCTSP_DaGiao = findViewById(R.id.rcvCTDH_DaGiao);
        tvName = findViewById(R.id.tvName_CTDH_DaGiao);
        tvAdress = findViewById(R.id.tvAdress_CTDH_DaGiao);
        tvPhone = findViewById(R.id.tvPhone_CTDH_DaGiao);
        tvPayMent = findViewById(R.id.tvPayMent_CTDH_DaGiao);
        tvTT = findViewById(R.id.tvTrangThai_CTDH_DaGiao);
        tvTCP = findViewById(R.id.tvTCP_CTDH_DaGiao);
        imgBack = findViewById(R.id.imageView4);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("order_ten");
        address = intent.getStringExtra("order_diachi");
        phone = intent.getStringExtra("order_sdt");
        payMent = intent.getStringExtra("order_pttt");
        tt = intent.getStringExtra("order_ttdh");
        tcp = intent.getDoubleExtra("order_tongTien", 0.0);

        // Xử lý sự kiện nút quay lại
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_CTDH_DaGiao.this, Activity_DonHang.class);
                intent.putExtra("select_tab", 2); // Chuyển sang tab đơn hàng đã giao
                startActivity(intent);
                finish();
            }
        });

        // Hiển thị thông tin đơn hàng
        tvName.setText(name);
        tvAdress.setText(address);
        tvPhone.setText(phone);
        tvPayMent.setText(payMent);
        tvTT.setText(tt);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(tcp);
        tvTCP.setText(formattedPrice + " VNĐ");

        // Lấy danh sách sản phẩm từ Intent và hiển thị trong RecyclerView
        sanPhamList = new ArrayList<>();
        sanPhamList = (ArrayList<ProductItemCart>) intent.getSerializableExtra("order_sp");
        thanhToanAdapter = new ThanhToanAdapter(this, sanPhamList, false);
        rcvCTSP_DaGiao.setLayoutManager(new LinearLayoutManager(this)); // Cài đặt layout dọc
        rcvCTSP_DaGiao.setAdapter(thanhToanAdapter); // Liên kết adapter với RecyclerView
    }
}