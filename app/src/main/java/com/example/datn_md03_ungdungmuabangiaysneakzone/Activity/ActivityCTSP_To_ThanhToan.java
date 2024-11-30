package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItem;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCTSP_To_ThanhToan extends AppCompatActivity {

    private TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductSize, tvTotalPrice, tvPaymentMethods;
    private TextView tvNameLocation, tvLocation, tvPhoneLocation;
    private ImageView imgProduct, imgAddress;
    LinearLayout lrlAddress, lraddressGone;
    Button btnOrder;
    Order order;
    String name, address, phone, email;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ctsp_to_thanh_toan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvProductName = findViewById(R.id.tvName_od_ctsp);
        tvProductPrice = findViewById(R.id.tvPrice_od_ctsp);
        tvProductQuantity = findViewById(R.id.tvSL_od_ctsp);
        tvProductSize = findViewById(R.id.tvSize_od_ctsp);
        tvTotalPrice = findViewById(R.id.tvTotal_od_ctsp);
        imgProduct = findViewById(R.id.imgProduct_od_ctsp);
        imgAddress = findViewById(R.id.imgChooseAddress);
        tvPaymentMethods = findViewById(R.id.txtPayment_od_ctsp);
        tvNameLocation = findViewById(R.id.txtName_TT);
        tvPhoneLocation = findViewById(R.id.txtPhone_TT);
        tvLocation = findViewById(R.id.txtAddress_TT);
        btnOrder = findViewById(R.id.btnOrder_CTSP);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        lrlAddress = findViewById(R.id.lraddress);
        lraddressGone = findViewById(R.id.idlr_gone);
        poppuGetListPayment();
        order = new Order();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        imgAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCTSP_To_ThanhToan.this, ShowListLocationActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        // Retrieve passed product data
        ProductItemCart productItem = (ProductItemCart) getIntent().getSerializableExtra("productItem");

        if (productItem != null) {
            // Display product data
            tvProductName.setText(productItem.getTenSP());
            tvProductPrice.setText(String.format("$%.2f", productItem.getGia()));
            tvProductQuantity.setText(String.format("Quantity: %d", productItem.getSoLuongGioHang()));
            tvProductSize.setText(String.format("Size: %d", productItem.getSize()));
            tvTotalPrice.setText(String.format("$%.2f", productItem.getTongTien()));

            List<String> productImages = productItem.getHinhAnh();
            if (productImages != null && !productImages.isEmpty()) {
                Glide.with(this).load(productImages.get(0)).into(imgProduct);
            }
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String deliveryDate = "2024-12-25";  // Placeholder, could be dynamic

                order.setSanPham(Collections.singletonList(productItem));
                order.setTenNguoiNhan(name);
                order.setDiaChiGiaoHang(address);
                order.setSoDienThoai(phone);
                order.setPhuongThucThanhToan(tvPaymentMethods.getText().toString());

                Call<Order> call = apiService.createOrder(email, order);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()) {
                            // Đơn hàng được tạo thành công
                            startActivity(new Intent(ActivityCTSP_To_ThanhToan.this, MainActivity.class));
                            Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Order thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý lỗi khi tạo đơn hàng
                            Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        // Xử lý lỗi khi kết nối tới server thất bại
                        Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
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
        tvPaymentMethods.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(ActivityCTSP_To_ThanhToan.this, tvPaymentMethods);
            for (String address : listPayment) {
                popupMenu.getMenu().add(address);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String selectedAddress = item.getTitle().toString();
                    tvPaymentMethods.setText(selectedAddress);
                    return true;
                }
            });
            popupMenu.show();
        });
    }
}