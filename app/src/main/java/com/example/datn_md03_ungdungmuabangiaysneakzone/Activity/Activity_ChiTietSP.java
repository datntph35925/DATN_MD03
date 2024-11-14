package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SizeAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGia_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGiaAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Activity_ChiTietSP extends AppCompatActivity {

    private RecyclerView recyclerReviews, rcvSizes;
    private DanhGiaAdapter_Demo reviewsAdapter;

    Button btnMuaHang;
    private int num,selectedProductQuantity, selectSize = -1;
    TextView tvName, tvGia, tvMoTa;
    ImageView imgSP, imgBack;
    ApiService apiService;

    ArrayList kichThuocArrayList;
    SizeAdapter sizeAdapter;
    String maSP;
    int soLuongTon;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_sp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getClient().create(ApiService.class);
        Intent i = getIntent();
        maSP =i.getStringExtra("id");
        String tenSP =i.getStringExtra("name");
        String moTa =i.getStringExtra("description");
        double gia =i.getDoubleExtra("price",0);
        String hinh =i.getStringExtra("image");

        imgBack = findViewById(R.id.imgBack);
        tvName = findViewById(R.id.tvTen_SPCT);
        tvGia = findViewById(R.id.tvGia_SPCT);
        tvMoTa = findViewById(R.id.tvMoTa_SPCT);
        imgSP = findViewById(R.id.img_SPCT);
        tvName.setText(tenSP);
        tvGia.setText(gia+"$");
        tvMoTa.setText(moTa);


        Glide.with(this).load(hinh).into(imgSP);

        kichThuocArrayList = new ArrayList();

        recyclerReviews = findViewById(R.id.rcvDanhGia_SPCT);
        btnMuaHang = findViewById(R.id.btnMuaHang_CTSP);

        // Set up RecyclerView for reviews
        recyclerReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new DanhGiaAdapter_Demo(getDummyReviews());
        recyclerReviews.setAdapter(reviewsAdapter);

        // Set up button Mua Hang click listener

        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogToBuy();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_ChiTietSP.this, MainActivity.class));
            }
        });
    }

    //Demo
    private void diaLogToBuy() {
        Dialog dialog = new Dialog(Activity_ChiTietSP.this);
        dialog.setContentView(R.layout.dialog_themgiohang);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bogoc);

        // Kéo dialog xuống dưới màn hình
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;

        TextView tvSoLuongCon = dialog.findViewById(R.id.tvSoLuong_toBuy);
        TextView tvNum = dialog.findViewById(R.id.tv_num_toBuy);
        rcvSizes = dialog.findViewById(R.id.rvOpsionSize);
        rcvSizes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ImageView imgPlus = dialog.findViewById(R.id.img_plus_toBuy);
        ImageView imgMinus = dialog.findViewById(R.id.img_minus_toBuy);
        Button btnBuy = dialog.findViewById(R.id.btnBuy);
        num = 1;

        // Gọi API để lấy thông tin sản phẩm và kích thước
        Call<Response<Product>> call = apiService.getProductById(maSP);
        call.enqueue(new Callback<Response<Product>>() {
            @Override
            public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        product = response.body().getData();
                        ArrayList<KichThuoc> kichThuocArrayList = (ArrayList<KichThuoc>) product.getKichThuoc();

                        // Hiển thị danh sách kích thước trong RecyclerView
                        sizeAdapter = new SizeAdapter(Activity_ChiTietSP.this, kichThuocArrayList);
                        sizeAdapter.setSizeAdapterListener(new SizeAdapter.SizeAdapterListener() {
                            @Override
                            public void onSizeSelected(KichThuoc kichThuoc) {
                                selectSize = kichThuoc.getSize();
                                 soLuongTon = kichThuoc.getSoLuongTon(); // Get the soLuongTon for the selected size

                                num = 1;
                                tvNum.setText(String.valueOf(num));
                                // Update the TextView with the soLuongTon
                                tvSoLuongCon.setText(String.valueOf(soLuongTon));
                            }
                        });
                        rcvSizes.setAdapter(sizeAdapter);
                    } else {
                        Toast.makeText(Activity_ChiTietSP.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Activity_ChiTietSP.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<Product>> call, Throwable t) {
                Toast.makeText(Activity_ChiTietSP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1) {
                    num--;
                    tvNum.setText(String.valueOf(num));
                }
            }
        });


        imgPlus.setOnClickListener(view -> {
            if (num < soLuongTon) {
                num++;
                tvNum.setText(String.valueOf(num));
            } else {
                Toast.makeText(Activity_ChiTietSP.this, "Sản phẩm chỉ có " + soLuongTon + " đôi", Toast.LENGTH_SHORT).show();
            }
        });


        btnBuy.setOnClickListener(view -> {
            Intent intent = new Intent(Activity_ChiTietSP.this, Activity_ThanhToan.class);
            intent.putExtra("productName", product.getTenSP());
            intent.putExtra("price", product.getGiaBan());
            intent.putExtra("image", product.getHinhAnh().get(0)); // Pass the first image URL
            intent.putExtra("size", selectSize); // Pass the selected size
            intent.putExtra("quantity", num); // Pass the quantity to buy
            startActivity(intent);
        });

        dialog.show();
    }

    private List<DanhGia_Demo> getDummyReviews() {
        List<DanhGia_Demo> reviews = new ArrayList<>();
        reviews.add(new DanhGia_Demo("Hồng hài nhi", 5, "Giày đẹp quá shop ơi...."));
        reviews.add(new DanhGia_Demo("Ngư ma vương", 5, "GIAYDEP..."));
        reviews.add(new DanhGia_Demo("Nhị lang thần", 5, "Có tiền để mua giầy nhưnghfyfghkhxgdfgvhkjhcxzfsdghvcx yufghjvhcfvgbjk uyuftygyuhiugyfygyuh gufythg không thể mua được em :P"));
        reviews.add(new DanhGia_Demo("Nhị lang thần", 5, "Có tiền để mua giầy nhưnghfyfghkhxgdfgvhkjhcxzfsdghvcx yufghjvhcfvgbjk uyuftygyuhiugyfygyuh gufythg không thể mua được em :P"));
        return reviews;
    }
}