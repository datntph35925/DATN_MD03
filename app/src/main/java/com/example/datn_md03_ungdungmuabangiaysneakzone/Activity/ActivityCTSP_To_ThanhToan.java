package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCTSP_To_ThanhToan extends AppCompatActivity {

    private TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductSize, tvTotalPrice, tvPaymentMethods;
    private TextView tvNameLocation, tvLocation, tvPhoneLocation;
    private ImageView imgProduct, imgAddress;
    private LinearLayout lrlAddress, lraddressGone;
    private Button btnOrder;

    private Order order;
    private String name, address, phone, email;
    private ApiService apiService;
    private ProductItemCart productItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctsp_to_thanh_toan);

        // Khởi tạo các thành phần giao diện
        initializeViews();

        // Lấy thông tin tài khoản người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Tên tài khoản người dùng

        // Lấy thông tin sản phẩm từ Intent
        productItem = (ProductItemCart) getIntent().getSerializableExtra("productItem");
        if (productItem != null) {
            displayProductInfo(productItem);
        }

        // Xử lý khi chọn địa chỉ giao hàng
        imgAddress.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityCTSP_To_ThanhToan.this, ShowListLocationActivity.class);
            startActivityForResult(intent, 100);
        });

        // Xử lý chọn phương thức thanh toán
        poppuGetListPayment();

        // Xử lý khi bấm nút "Đặt hàng"
        btnOrder.setOnClickListener(view -> handleOrder());
    }

    private void initializeViews() {
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
        lrlAddress = findViewById(R.id.lraddress);
        lraddressGone = findViewById(R.id.idlr_gone);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        order = new Order();
    }

    private void displayProductInfo(ProductItemCart productItem) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            name = data.getStringExtra("nameLocation");
            address = data.getStringExtra("location");
            phone = data.getStringExtra("phoneLocation");

            lrlAddress.setVisibility(View.GONE);
            lraddressGone.setVisibility(View.VISIBLE);

            tvNameLocation.setText(name);
            tvPhoneLocation.setText(phone);
            tvLocation.setText(address);
        }
    }

    private void poppuGetListPayment() {
        String[] listPayment = {"Thanh toán khi nhận hàng (COD)", "Thanh toán qua ngân hàng"};
        tvPaymentMethods.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(ActivityCTSP_To_ThanhToan.this, tvPaymentMethods);
            for (String payment : listPayment) {
                popupMenu.getMenu().add(payment);
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                tvPaymentMethods.setText(item.getTitle().toString());
                return true;
            });
            popupMenu.show();
        });
    }

    private void handleOrder() {
        // Cập nhật thông tin đơn hàng
        order.setSanPham(Collections.singletonList(productItem));
        order.setTenNguoiNhan(name);
        order.setDiaChiGiaoHang(address);
        order.setSoDienThoai(phone);
        order.setPhuongThucThanhToan(tvPaymentMethods.getText().toString());

        // Thêm email vào order
        order.setTentaikhoan(email);

        if (tvPaymentMethods.getText().toString().equals("Thanh toán qua ngân hàng")) {
            // Xử lý thanh toán qua ngân hàng
            double amount = productItem.getTongTien(); // Tổng tiền
            String description = email + " - " + phone; // Tên tài khoản và số điện thoại
            String accountName = "Mua Ban Giay SneakZone"; // Tên tài khoản ngân hàng

            // Tạo URL mã QR
            String qrUrl = generateVietQRUrl(amount, description, accountName);

            // Mở Activity hiển thị QR Code
            Intent intent = new Intent(ActivityCTSP_To_ThanhToan.this, QRCodeActivity.class);
            intent.putExtra("order", order);  // Truyền đối tượng order
            intent.putExtra("qrUrl", qrUrl);  // Truyền mã QR
            startActivity(intent);

        } else if (tvPaymentMethods.getText().toString().equals("Thanh toán khi nhận hàng (COD)")) {
            // Lưu đơn hàng vào database
            saveOrderToDatabase();
        }
    }

    private String generateVietQRUrl(double amount, String description, String accountName) {
        String bankId = "970423"; // BANK_ID
        String accountNo = "0384191830"; // ACCOUNT_NO
        String template = "print"; // TEMPLATE

        return "https://img.vietqr.io/image/" + bankId + "-" + accountNo + "-" + template + ".png"
                + "?amount=" + amount
                + "&addInfo=" + description
                + "&accountName=" + accountName;
    }

    private void saveOrderToDatabase() {
        Call<Order> call = apiService.createOrder(email, order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Order thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(ActivityCTSP_To_ThanhToan.this, MainActivity.class));
                } else {
                    Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
