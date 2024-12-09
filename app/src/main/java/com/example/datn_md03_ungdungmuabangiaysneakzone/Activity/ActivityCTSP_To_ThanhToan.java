package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextWatcher;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCTSP_To_ThanhToan extends AppCompatActivity {

    private TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductSize, tvTotalPrice, tvPaymentMethods;
    private TextView tvNameLocation, tvLocation, tvPhoneLocation, tvVoucher;
    private ImageView imgProduct, imgAddress;
    private LinearLayout lrlAddress, lraddressGone;
    private Button btnOrder, btnXoaVoucher;

    EditText edtVoicher;
    private Order order;
    private String name, address, phone, email, maVoucher, giaTri, loaiVoucher;
    private ApiService apiService;
    private ProductItemCart productItem;

    String result;
    String[] imageList;
    private double originalTotalCost;
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

        lrlAddress.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityCTSP_To_ThanhToan.this, ShowListLocationActivity.class);
            startActivityForResult(intent, 100);
        });

        lraddressGone.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityCTSP_To_ThanhToan.this, ShowListLocationActivity.class);
            startActivityForResult(intent, 100);
        });

        edtVoicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCTSP_To_ThanhToan.this, Activity_ShowList_Voucher.class);
                startActivityForResult(intent, 101);
            }
        });
        // Xử lý chọn phương thức thanh toán
        poppuGetListPayment();

        // Xử lý khi bấm nút "Đặt hàng"
        btnOrder.setOnClickListener(view -> handleOrder());

        layMaDonHang();
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
        edtVoicher = findViewById(R.id.edtVoicher);
        tvVoucher = findViewById(R.id.txtTax);
        btnXoaVoucher = findViewById(R.id.btnXoaVoucher);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        order = new Order();
    }

    private void removeVoucherAndRecalculate() {
        // Xóa voucher
        edtVoicher.setText("");  // Xóa mã voucher
        tvVoucher.setText("");  // Xóa hiển thị giá trị voucher

        // Tính lại tổng tiền mà không có voucher
        double totalCost = originalTotalCost;  // Lấy tổng tiền ban đầu

        // Cập nhật lại giao diện
        tvTotalPrice.setText(String.format("%.2f", totalCost));  // Hiển thị tổng tiền ban đầu
        Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Voucher đã được xóa. Tổng tiền đã khôi phục!", Toast.LENGTH_SHORT).show();
    }


    private void displayProductInfo(ProductItemCart productItem) {
        tvProductName.setText(productItem.getTenSP());
        tvProductPrice.setText(String.format("%.2f", productItem.getGia()));
        tvProductQuantity.setText(String.format("Quantity: %d", productItem.getSoLuongGioHang()));
        tvProductSize.setText(String.format("Size: %d", productItem.getSize()));
        tvTotalPrice.setText(String.format("%.2f", productItem.getTongTien()));

        originalTotalCost = productItem.getTongTien();

        List<String> productImages = productItem.getHinhAnh();
        if (productImages != null && !productImages.isEmpty()) {
            String firstImage = productImages.get(0);
            String[] imageUrls = firstImage.split(",");  // Tách theo dấu phẩy

            // Lấy URL đầu tiên
            String imageUrl = imageUrls[0].trim();  // Loại bỏ khoảng trắng dư thừa

            String baseUrl = "http://10.0.2.2:3000/"; // Thay thế bằng base URL thực tế
            String firstImageUrl  = baseUrl + imageUrl;
            // Log lại URL để kiểm tra
            Log.d("CartAdapter", "Primeira imagem: " + imageUrl);

            // Tải ảnh từ URL đầu tiên
            Glide.with(this)
                    .load(firstImageUrl )
                    .placeholder(R.drawable.nice_shoe) // Hình mặc định khi đang tải
                    .error(R.drawable.nike2) // Hình mặc định khi lỗi
                    .into(imgProduct);
            productItem.setHinhAnh(Collections.singletonList(firstImageUrl));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 100: // Khi quay về từ ShowListLocationActivity
                    name = data.getStringExtra("nameLocation");
                    address = data.getStringExtra("location");
                    phone = data.getStringExtra("phoneLocation");

                    // Cập nhật UI địa chỉ
                    if (name != null && address != null && phone != null) {
                        lrlAddress.setVisibility(View.GONE);
                        lraddressGone.setVisibility(View.VISIBLE);
                        tvNameLocation.setText(name);
                        tvPhoneLocation.setText(phone);
                        tvLocation.setText(address);
                    }
                    break;

                case 101: // Khi quay về từ Activity_ShowList_Voucher
                    maVoucher = data.getStringExtra("maVoucher");
                    loaiVoucher = data.getStringExtra("loaiVoucher");
                    double giaTriDouble = data.getDoubleExtra("giaTri", 0.0);

                    btnXoaVoucher.setVisibility(View.VISIBLE);
                    btnXoaVoucher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeVoucherAndRecalculate();
                            btnXoaVoucher.setVisibility(View.GONE);
                        }
                    });

                    // Định dạng giá trị voucher
                    if (giaTriDouble % 1 == 0) {
                        giaTri = String.valueOf((int) giaTriDouble);
                    } else {
                        giaTri = String.valueOf(giaTriDouble);
                    }

                    // Hiển thị loại voucher
                    if ("Giảm giá theo %".equals(loaiVoucher)) {
                        tvVoucher.setText("-" + giaTri + "%");
                    } else if ("Giảm giá cố định".equals(loaiVoucher)) {
                        tvVoucher.setText("-" + giaTri + " VNĐ");
                    } else {
                        tvVoucher.setText(giaTri);
                    }

                    applyVoucher(loaiVoucher, giaTriDouble);

                    // Cập nhật UI mã voucher
                    if (maVoucher != null) {
                        edtVoicher.setText(maVoucher);
                    }
                    break;
            }
        }
    }

    private void applyVoucher(String loaiVoucher, double giaTriDouble) {
        double totalPrice = originalTotalCost;  // Lấy tổng tiền ban đầu

        // Kiểm tra loại voucher và tính toán lại tổng tiền
        if ("Giảm giá theo %".equals(loaiVoucher)) {
            totalPrice -= totalPrice * (giaTriDouble / 100);  // Giảm theo phần trăm
        } else if ("Giảm giá cố định".equals(loaiVoucher)) {
            totalPrice -= giaTriDouble;  // Giảm theo số tiền cố định
        }

        // Đảm bảo tổng tiền không âm
        totalPrice = Math.max(0, totalPrice);

        // Hiển thị tổng tiền mới
        tvTotalPrice.setText(String.format("%.2f", totalPrice));
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
        order.setVoucher(edtVoicher.getText().toString());
        order.setMaDonHang(result);

        double finalTotalPrice = Double.parseDouble(tvTotalPrice.getText().toString());
        order.setTongTien(finalTotalPrice);  // Cập nhật tổng tiền vào đơn hàng
        // Thêm email vào order
        order.setTentaikhoan(email);

        if (tvPaymentMethods.getText().toString().equals("Thanh toán qua ngân hàng")) {
            // Xử lý thanh toán qua ngân hàng
            order.setMaDonHang(result);
            double amount = productItem.getTongTien(); // Tổng tiền
//            String description = email + " - " + phone; // Tên tài khoản và số điện thoại
            String description = order.getMaDonHang();
            String accountName = "Mua Ban Giay SneakZone"; // Tên tài khoản ngân hàng

            //lấy mã đơn hàng chuyền vào
            // Tạo URL mã QR
            String qrUrl = generateVietQRUrl(amount, description, accountName);

            Log.d("qUrl", "Current Quantity: " + qrUrl);
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
                    Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Voucher đã được dùng hoặc hết hạn sử dụng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(ActivityCTSP_To_ThanhToan.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
