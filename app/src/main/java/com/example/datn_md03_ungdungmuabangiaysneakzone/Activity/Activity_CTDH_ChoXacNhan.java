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

import java.text.DecimalFormat;
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

        // Bật tính năng Edge-to-Edge để tối ưu giao diện
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ctdh_cho_xac_nhan);

        // Cấu hình khoảng cách padding để phù hợp với hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần giao diện
        rcvCTSP_ChoXacNhan = findViewById(R.id.rcvCTDH_ChoXacNhan);
        imgBack = findViewById(R.id.imageView4);
        tvName = findViewById(R.id.tvName_CTDH_ChoXacNhan);
        tvAdress = findViewById(R.id.tvAdress_CTDH_ChoXacNhan);
        tvPhone = findViewById(R.id.tvPhone_CTDH_ChoXacNhan);
        tvPayMent = findViewById(R.id.tvPayMent_CTDH_ChoXacNhan);
        tvTT = findViewById(R.id.tvTrangThai_CTDH_ChoXacNhan);
        tvTCP = findViewById(R.id.tvTCP_CTDH_ChoXacNhan);
        btnHuyMuaHang = findViewById(R.id.btnHuyDonHang);

        // Khởi tạo đối tượng đơn hàng
        order = new Order();

        // Xử lý sự kiện khi nhấn nút Hủy Đơn Hàng
        btnHuyMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy ID của đơn hàng từ Intent
                String orderId = getIntent().getStringExtra("order_id");
                Log.d("CartAdapter", "Current Quantity: " + orderId);

                // Tạo request để cập nhật trạng thái đơn hàng
                UpdateStatusRequest request = new UpdateStatusRequest("Hủy");

                // Gửi yêu cầu cập nhật trạng thái đơn hàng qua API
                RetrofitClient.getApiService().updateOrderStatus(orderId, request).enqueue(new Callback<Response<Order>>() {
                    @Override
                    public void onResponse(Call<Response<Order>> call, retrofit2.Response<Response<Order>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Hủy đơn hàng thành công
                            Toast.makeText(Activity_CTDH_ChoXacNhan.this, "Order canceled successfully", Toast.LENGTH_SHORT).show();

                            // Chuyển sang màn hình đơn hàng và mở tab "Đã Hủy"
                            Intent intent = new Intent(Activity_CTDH_ChoXacNhan.this, Activity_DonHang.class);
                            intent.putExtra("select_tab", 3);
                            startActivity(intent);
                            finish(); // Đóng Activity hiện tại
                        } else {
                            // Hủy đơn hàng thất bại
                            Toast.makeText(Activity_CTDH_ChoXacNhan.this, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Order>> call, Throwable t) {
                        // Xử lý lỗi khi gọi API thất bại
                        Toast.makeText(Activity_CTDH_ChoXacNhan.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Xử lý sự kiện khi nhấn nút quay lại
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển về màn hình danh sách đơn hàng
                startActivity(new Intent(Activity_CTDH_ChoXacNhan.this, Activity_DonHang.class));
            }
        });

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("order_ten");
        address = intent.getStringExtra("order_diachi");
        phone = intent.getStringExtra("order_sdt");
        payMent = intent.getStringExtra("order_pttt");
        tt = intent.getStringExtra("order_ttdh");
        tcp = intent.getDoubleExtra("order_tongTien", 0.0);

        // Hiển thị dữ liệu lên giao diện
        tvName.setText(name);
        tvAdress.setText(address);
        tvPhone.setText(phone);
        tvPayMent.setText(payMent);
        tvTT.setText(tt);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(tcp);
        tvTCP.setText(formattedPrice + " VNĐ");

        // Lấy danh sách sản phẩm từ Intent
        sanPhamList = new ArrayList<>();
        sanPhamList = (ArrayList<ProductItemCart>) intent.getSerializableExtra("order_sp");

        Log.d("Activity_CTDH_ChoXacNhan", "Full Image URL: " + sanPhamList.get(0).getHinhAnh().get(0));
        // Cấu hình RecyclerView để hiển thị danh sách sản phẩm
        thanhToanAdapter = new ThanhToanAdapter(this, sanPhamList, true);
        rcvCTSP_ChoXacNhan.setLayoutManager(new LinearLayoutManager(this));
        rcvCTSP_ChoXacNhan.setAdapter(thanhToanAdapter);
    }
}