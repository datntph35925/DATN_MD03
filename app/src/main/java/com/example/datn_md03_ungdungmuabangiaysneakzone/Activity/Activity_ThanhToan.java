package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.RemoveItemsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ThanhToan extends AppCompatActivity {

    private RecyclerView rcvThanhToan;
    private TextView tvTotalCost, tvPayMent, tvVoucher;
    KichThuoc kichThuoc;
    ArrayList<ProductItemCart> selectedCartItems;

    private TextView tvNameLocation, tvLocation, tvPhoneLocation;
    private ImageView imgProduct, imgAddress;
    LinearLayout lrlAddress, lraddressGone;
    private ArrayList<String> maSPList;
    private ArrayList<Integer> sizeList;// Mảng chứa các maSP
    AppCompatButton btnOrder ;
    String email, name, address, phone, result, giaTri;
    ApiService apiService;
    Order order;

    EditText edVoicher;
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
        edVoicher = findViewById(R.id.edtVoicher);
        tvVoucher = findViewById(R.id.txtTax);

        btnOrder = findViewById(R.id.btnOrder);
        poppuGetListPayment();

        selectedCartItems = (ArrayList<ProductItemCart>) getIntent().getSerializableExtra("selectedCartItems");
        apiService = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        maSPList = new ArrayList<>();
        sizeList = new ArrayList<>();

        layMaDonHang();
        sizeList = getIntent().getIntegerArrayListExtra("sizeList");
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

        lrlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_ThanhToan.this, ShowListLocationActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        lraddressGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_ThanhToan.this, ShowListLocationActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
            ThanhToanAdapter adapter = new ThanhToanAdapter(this, selectedCartItems, true);
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
                handleOrder();
            }
        });

        edVoicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_ThanhToan.this, Activity_ShowList_Voucher.class);
                startActivityForResult(intent, 101);
            }
        });

    }

    private void handleOrder() {
        order = new Order();
        order.setSelectedProducts(maSPList);
        order.setTenNguoiNhan(name);
        order.setDiaChiGiaoHang(address);
        order.setSoDienThoai(phone);
        order.setPhuongThucThanhToan(tvPayMent.getText().toString());
        order.setVoucher(edVoicher.getText().toString());
        order.setMaDonHang(result);

        // Thêm email vào order
        order.setTentaikhoan(email);
        double totalCost = Double.parseDouble(tvTotalCost.getText().toString().replace("$", "").trim());
        order.setTongTien(totalCost);

        if (tvPayMent.getText().toString().equals("Thanh toán qua ngân hàng")) {
            // Xử lý thanh toán qua ngân hàng
            order.setMaDonHang(result);
            double amount = order.getTongTien(); // Tổng tiền
//            String description = email + " - " + phone; // Tên tài khoản và số điện thoại
            String description = order.getMaDonHang();
            String accountName = "Mua Ban Giay SneakZone"; // Tên tài khoản ngân hàng

            //lấy mã đơn hàng chuyền vào
            // Tạo URL mã QR
            String qrUrl = generateVietQRUrl(amount, description, accountName);

            Log.d("qUrl", "Current Quantity: " + qrUrl);
            // Mở Activity hiển thị QR Code
            Intent intent = new Intent(Activity_ThanhToan.this, QRCodeCartActivity.class);
            intent.putExtra("order", order);  // Truyền đối tượng order
            intent.putExtra("qrUrl", qrUrl);  // Truyền mã QR
            startActivity(intent);

        } else if (tvPayMent.getText().toString().equals("Thanh toán khi nhận hàng (COD)")) {
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
        double totalCost = Double.parseDouble(tvTotalCost.getText().toString().replace("$", "").trim());

        order = new Order();
        order.setSelectedProducts(maSPList);
        order.setTenNguoiNhan(name);
        order.setDiaChiGiaoHang(address);
        order.setSoDienThoai(phone);
        order.setPhuongThucThanhToan(tvPayMent.getText().toString());
        order.setTongTien(totalCost);
        order.setVoucher(edVoicher.getText().toString());
        order.setMaDonHang(result);

        Call<Order> call = apiService.createOrderFromCart(email, order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    // Đơn hàng được tạo thành công
                    removeCartItem();
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

    private void removeCartItem(){
        List<RemoveItemsRequest.products> productItems = new ArrayList<>();
        for (int i = 0; i < maSPList.size(); i++) {
            List<String> productIdList = Collections.singletonList(maSPList.get(i)); // Mã sản phẩm
            List<Integer> sizeList = Collections.singletonList(this.sizeList.get(i)); // Kích thước

            // Thêm vào danh sách yêu cầu
            productItems.add(new RemoveItemsRequest.products(productIdList, sizeList));
        }
        RemoveItemsRequest request = new RemoveItemsRequest(productItems);
        apiService.removeItems(email, request).enqueue(new Callback<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>>() {
            @Override
            public void onResponse(Call<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>> call, Response<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_ThanhToan.this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    Log.d("API_RESPONSE", "Success: " + response.code());
                } else {
                    Toast.makeText(Activity_ThanhToan.this, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                    Log.e("API_RESPONSE", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response<RemoveItemsRequest>> call, Throwable t) {
                Toast.makeText(Activity_ThanhToan.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }


    private void layMaDonHang() {
// Kiểm tra email có hợp lệ không
        if (email != null && email.length() >= 4) {
            // Lấy 4 ký tự đầu tiên của email
            String firstFourChars = email.substring(0, 4).toUpperCase();;

            // Tạo đối tượng Random
            Random random = new Random();

            // Tạo 4 chữ số ngẫu nhiên
            StringBuilder randomDigits = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                randomDigits.append(random.nextInt(10)); // Thêm một chữ số ngẫu nhiên (0-9)
            }

            // Ghép 4 ký tự đầu tiên với 4 chữ số ngẫu nhiên
            result = firstFourChars + randomDigits.toString();

            // In ra kết quả
            Log.d("RandomString", "Kết quả: " + result);
        } else {
            Log.d("Error", "Email không hợp lệ hoặc quá ngắn.");
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
        } else if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            // Nhận thông tin voucher
            String maVoucher = data.getStringExtra("maVoucher");
            String loaiVoucher = data.getStringExtra("loaiVoucher");
            double giaTriVoucher = data.getDoubleExtra("giaTri", 0.0);

            // Hiển thị mã voucher
            edVoicher.setText(maVoucher);

            // Hiển thị loại voucher
            // Định dạng giá trị voucher
            if (giaTriVoucher % 1 == 0) {
                giaTri = String.valueOf((int) giaTriVoucher);
            } else {
                giaTri = String.valueOf(giaTriVoucher);
            }

            // Hiển thị loại voucher
            if ("Giảm giá theo %".equals(loaiVoucher)) {
                tvVoucher.setText("-" + giaTri + "%");
            } else if ("Giảm giá cố định".equals(loaiVoucher)) {
                tvVoucher.setText("-" + giaTri + " VNĐ");
            } else {
                tvVoucher.setText(giaTri);
            }
            // Tính toán lại tổng tiền
            double totalCost = 0;
            for (ProductItemCart item : selectedCartItems) {
                totalCost += item.getGia() * item.getSoLuongGioHang();
            }

            if ("Giảm giá theo %".equals(loaiVoucher)) {
                totalCost -= totalCost * (giaTriVoucher / 100);
            } else if ("Giảm giá cố định".equals(loaiVoucher)) {
                totalCost -= giaTriVoucher;
            }

            // Đảm bảo tổng tiền không âm
            totalCost = Math.max(0, totalCost);

            // Hiển thị tổng tiền mới
            tvTotalCost.setText(String.format("$%.2f", totalCost));
        }
    }

    private void poppuGetListPayment() {
        String[] listPayment = {"Thanh toán khi nhận hàng (COD)", "Thanh toán qua ngân hàng"};
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