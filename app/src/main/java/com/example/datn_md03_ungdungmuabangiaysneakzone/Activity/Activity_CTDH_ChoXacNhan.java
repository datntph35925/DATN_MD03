package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.ThanhToanAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.UpdateStatusRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_CTDH_ChoXacNhan extends AppCompatActivity {

    RecyclerView rcvCTSP_ChoXacNhan;
    TextView tvName, tvAdress, tvPhone, tvPayMent, tvTT, tvTCP;
    String name, phone, address, payMent, tt;
    ArrayList<ProductItemCart> sanPhamList;
    ThanhToanAdapter thanhToanAdapter;
    double tcp;
    ImageView imgBack;
    Order order;

    Button btnHuyMuaHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ctdh_cho_xac_nhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rcvCTSP_ChoXacNhan = findViewById(R.id.rcvCTDH_ChoXacNhan);

        imgBack = findViewById(R.id.imageView4);
        tvName = findViewById(R.id.tvName_CTDH_ChoXacNhan);
        tvAdress = findViewById(R.id.tvAdress_CTDH_ChoXacNhan);
        tvPhone = findViewById(R.id.tvPhone_CTDH_ChoXacNhan);
        tvPayMent = findViewById(R.id.tvPayMent_CTDH_ChoXacNhan);
        tvTT = findViewById(R.id.tvTrangThai_CTDH_ChoXacNhan);
        tvTCP = findViewById(R.id.tvTCP_CTDH_ChoXacNhan);
        btnHuyMuaHang = findViewById(R.id.btnHuyDonHang);

        order = new Order();

        btnHuyMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderId = getIntent().getStringExtra("order_id");
                Log.d("CartAdapter", "Current Quantity: " + orderId);

                UpdateStatusRequest request = new UpdateStatusRequest("Hủy");

                RetrofitClient.getApiService().updateOrderStatus(orderId, request).enqueue(new Callback<Response<Order>>() {
                    @Override
                    public void onResponse(Call<Response<Order>> call, retrofit2.Response<Response<Order>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(Activity_CTDH_ChoXacNhan.this, "Order canceled successfully", Toast.LENGTH_SHORT).show();

                            // Chuyển về MainActivity và mở Fragment_DaHuy
                            Intent intent = new Intent(Activity_CTDH_ChoXacNhan.this, Activity_DonHang.class);
                            intent.putExtra("select_tab", 3);
                            startActivity(intent);
                            finish(); // Đóng Activity hiện tại
                        } else {
                            Toast.makeText(Activity_CTDH_ChoXacNhan.this, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Order>> call, Throwable t) {
                        Toast.makeText(Activity_CTDH_ChoXacNhan.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_CTDH_ChoXacNhan.this, Activity_DonHang.class));
            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("order_ten");
        address = intent.getStringExtra("order_diachi");
        phone = intent.getStringExtra("order_sdt");
        payMent = intent.getStringExtra("order_pttt");
        tt = intent.getStringExtra("order_ttdh");
        tcp = intent.getDoubleExtra("order_tongTien", 0.0);

        // Set data to TextViews
        tvName.setText(name);
        tvAdress.setText(address);
        tvPhone.setText(phone);
        tvPayMent.setText(payMent);
        tvTT.setText(tt);
        tvTCP.setText(String.format("%.2f", tcp));

        sanPhamList = new ArrayList<>();
        sanPhamList = (ArrayList<ProductItemCart>) intent.getSerializableExtra("order_sp");
        thanhToanAdapter = new ThanhToanAdapter(this, sanPhamList);
        rcvCTSP_ChoXacNhan.setLayoutManager(new LinearLayoutManager(this));
        rcvCTSP_ChoXacNhan.setAdapter(thanhToanAdapter);

    }
}