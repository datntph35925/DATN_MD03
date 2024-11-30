package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.ThanhToanAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.Cart_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ThanhToan extends AppCompatActivity {

    private RecyclerView rcvThanhToan;
    private TextView tvTotalCost, tvPayMent;
    KichThuoc kichThuoc;
    ArrayList<ProductItemCart> selectedCartItems;

    private TextView tvNameLocation, tvLocation, tvPhoneLocation;
    private ImageView imgProduct, imgAddress;
    LinearLayout lrlAddress, lraddressGone;
    private ArrayList<String> maSPList; // Mảng chứa các maSP
    Button btnOrder;
    String email, name, address, phone;
    ApiService apiService;
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);

        rcvThanhToan = findViewById(R.id.rcvOrder_dt);
        tvTotalCost = findViewById(R.id.txtTotal);
        tvPayMent = findViewById(R.id.txtPayment);
        tvNameLocation = findViewById(R.id.txtName_TT);
        tvPhoneLocation = findViewById(R.id.txtPhone_TT);
        tvLocation = findViewById(R.id.txtAddress_TT);
        imgAddress = findViewById(R.id.imgChooseAddress);
        lrlAddress = findViewById(R.id.lraddress);
        lraddressGone = findViewById(R.id.idlr_gone);

        btnOrder = findViewById(R.id.btnOrder);
        poppuGetListPayment();

        selectedCartItems = (ArrayList<ProductItemCart>) getIntent().getSerializableExtra("selectedCartItems");
        apiService = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        maSPList = new ArrayList<>();
        maSPList = getIntent().getStringArrayListExtra("maSPList");

        // Kiểm tra nếu maSPList không null và có ít nhất một sản phẩm
        if (maSPList != null && !maSPList.isEmpty()) {
            Log.d("Activity_ThanhToan", "Danh sách maSP: " + maSPList.toString());
        }

        imgAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_ThanhToan.this, ShowListLocationActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
            ThanhToanAdapter adapter = new ThanhToanAdapter(this, selectedCartItems);
            rcvThanhToan.setLayoutManager(new LinearLayoutManager(this));
            rcvThanhToan.setAdapter(adapter);

            double totalCost = 0;
            for (ProductItemCart item : selectedCartItems) {
                totalCost += item.getGia() * item.getSoLuongGioHang();
            }
            tvTotalCost.setText(String.format("$%.2f", totalCost));
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalCost = Double.parseDouble(tvTotalCost.getText().toString().replace("$", "").trim());

                order = new Order();
                order.setSelectedProducts(maSPList);
                order.setTenNguoiNhan(name);
                order.setDiaChiGiaoHang(address);
                order.setSoDienThoai(phone);
                order.setPhuongThucThanhToan(tvPayMent.getText().toString());
                order.setTongTien(totalCost);

                Call<Order> call = apiService.createOrderFromCart(email, order);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()) {
                            // Đơn hàng được tạo thành công
                            startActivity(new Intent(Activity_ThanhToan.this, MainActivity.class));
                            Toast.makeText(Activity_ThanhToan.this, "Order thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý lỗi khi tạo đơn hàng
                            Toast.makeText(Activity_ThanhToan.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(Activity_ThanhToan.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra requestCode và resultCode
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Nhận dữ liệu trả về
             name = data.getStringExtra("nameLocation");
             address = data.getStringExtra("location");
             phone = data.getStringExtra("phoneLocation");

            // Cập nhật dữ liệu vào TextView
            lrlAddress.setVisibility(View.GONE);
            lraddressGone.setVisibility(View.VISIBLE);

            tvNameLocation.setVisibility(View.VISIBLE);
            tvPhoneLocation.setVisibility(View.VISIBLE);
            tvLocation.setVisibility(View.VISIBLE);

            tvNameLocation.setText(name);
            tvPhoneLocation.setText(phone);
            tvLocation.setText(address);

        }
    }

    private void poppuGetListPayment() {
        String[] listPayment = {"Thanh toán khi nhận hàng (COD)", "Thanh toán với PayPal"};
        tvPayMent.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(Activity_ThanhToan.this, tvPayMent);
            for (String address : listPayment) {
                popupMenu.getMenu().add(address);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String selectedAddress = item.getTitle().toString();
                    tvPayMent.setText(selectedAddress);
                    return true;
                }
            });
            popupMenu.show();
        });
    }
}