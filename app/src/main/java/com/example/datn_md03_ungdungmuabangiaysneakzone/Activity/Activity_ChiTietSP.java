package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiResponse;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Cart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.example.datn_md03_ungdungmuabangiaysneakzone.untils.untils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Activity_ChiTietSP extends AppCompatActivity {

    private RecyclerView recyclerReviews, rcvSizes;
    private DanhGiaAdapter_Demo reviewsAdapter;

    Button btnMuaHang;
    ImageView btnYeuThich, btnYeuThichRed, btnGioHang;
    private int num,selectedProductQuantity, selectSize = -1;
    TextView tvName, tvGia, tvMoTa;
    ImageView imgSP, imgBack;
    ApiService apiService;
    ArrayList kichThuocArrayList;
    SizeAdapter sizeAdapter;
    String maSP;
    int soLuongTon;
    Product product;
    CustomerAccount customerAccount;
    ArrayList<CustomerAccount> accountArrayList;
    double priceText;
    String idMaTK;
    private String email, tenSP;

    private double giaSP;
    String hinh;

    ProductItemCart productItem;

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
        tenSP =i.getStringExtra("name");
        String moTa =i.getStringExtra("description");
        giaSP =i.getDoubleExtra("price",0);
        hinh =i.getStringExtra("image");

        product = new Product();
        productItem = new ProductItemCart();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        if (!email.isEmpty()) {
            // Email found, use it
            Log.d("SharedPreferences", "Retrieved Email: " + email);
        } else {
            // Email not found
            Toast.makeText(this, "Email not found. Please register or log in.", Toast.LENGTH_SHORT).show();
            Log.d("SharedPreferences", "Email not found");
        }
// hieuaddd

        imgBack = findViewById(R.id.imgBack);
        tvName = findViewById(R.id.tvTen_SPCT);
        tvGia = findViewById(R.id.tvGia_SPCT);
        tvMoTa = findViewById(R.id.tvMoTa_SPCT);
        imgSP = findViewById(R.id.img_SPCT);
        btnYeuThich = findViewById(R.id.btnYeuThich_CTSP);
        btnYeuThichRed = findViewById(R.id.btnYeuThich_Red_CTSP);
        btnGioHang = findViewById(R.id.btnGioHang_CTSP);


        tvName.setText(tenSP);
        tvGia.setText(giaSP+"$");
        tvMoTa.setText(moTa);

        String imageUrl = "https://www.chuphinhsanpham.vn/wp-content/uploads/2021/06/chup-hinh-giay-dincox-shoes-c-photo-studio-4.jpg";
        if(hinh == null){
            Glide.with(this)
                    .load(imageUrl)
                    .into(imgSP);
            Toast.makeText(this, "Lỗi ảnh", Toast.LENGTH_SHORT).show();
        } else {
            Glide.with(this).load(hinh).into(imgSP);
        }
        kichThuocArrayList = new ArrayList();
        accountArrayList = new ArrayList();

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
                diaLogToBuy(1);
            }
        });

        btnGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogToBuy(0);
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
    private void diaLogToBuy(int type) {
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

        if(type == 1){
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
        } else {
          btnBuy.setText("Thêm giỏ hàng");
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

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(selectSize == -1){
                       Toast.makeText(Activity_ChiTietSP.this, "Vui lòng chọn kích thước", Toast.LENGTH_SHORT).show();
                       return;
                   } else if (soLuongTon == 0) {
                       Toast.makeText(Activity_ChiTietSP.this, "Kích cỡ hiện tại đã hết hàng. Vui lòng chọn kích cỡ khác!", Toast.LENGTH_SHORT).show();
                       return;
                   } else {
                      Log.d("SharedPreferences", "Retrieved maSP: " + maSP);
                       Log.d("SharedPreferences", "Retrieved tenSP: " + tenSP);
                       Log.d("SharedPreferences", "Retrieved num: " + num);
                       Log.d("SharedPreferences", "Retrieved selectSize: " + selectSize);
                       Log.d("SharedPreferences", "Retrieved giaSP: " + giaSP);
                       Log.d("SharedPreferences", "Retrieved email: " + email);
                       Log.d("SharedPreferences", "Retrieved slt: " + soLuongTon);

                       List<String> hinhanh = Collections.singletonList(hinh);

                       productItem.setMaSanPham(maSP);
                       productItem.setTenSP(tenSP);
                       productItem.setSoLuongGioHang(num);
                       productItem.setSize(selectSize);
                       productItem.setGia(giaSP);
                       productItem.setTongTien(giaSP * num);
                       productItem.setHinhAnh(hinhanh);
                       productItem.setSoLuongTon(soLuongTon);

//                       // Tạo danh sách sản phẩm và thêm vào giỏ hàng
//                       ArrayList<ProductItemCart> productItems = new ArrayList<>();
//                       productItems.add(productItem);

//                       Intent intent = new Intent(Activity_ChiTietSP.this, Activity_Cart.class);
//                       intent.putExtra("cartItems", productItems); // productItems is your ArrayList<ProductItemCart>
//                       startActivity(intent);

                       Cart cart = new Cart();
                       cart.setProductId(maSP);
                       cart.setSize(selectSize);
                       cart.setQuantity(num);

                       Call<Response<ArrayList<Cart>>> call = apiService.addListCart(email, cart);
                       call.enqueue(new Callback<Response<ArrayList<Cart>>>() {
                           @Override
                           public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                              if(response.isSuccessful() && response.body() != null){
                                  Toast.makeText(Activity_ChiTietSP.this, "Thêm giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                  dialog.dismiss();
                              }
                           }

                           @Override
                           public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {
                               Toast.makeText(Activity_ChiTietSP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
                }
            });
        }



        dialog.show();
    }
//
//    private void setupFavoriteButtonListeners() {
//        btnYeuThich.setOnClickListener(view -> {
//            if (product != null) {
//                // Update the local cache for instant UI feedback
//                updateFavoriteStatusLocally(true);
//                // Push the update to the server asynchronously
//                updateTrangThaiYeuThich();
//                Toast.makeText(this, "Thêm danh sách yêu thích thành công", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Looix", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btnYeuThichRed.setOnClickListener(view -> {
//            if (product != null) {
//                // Update the local cache for instant UI feedback
//                updateFavoriteStatusLocally(false);
//                // Push the update to the server asynchronously
//                updateTrangThaiYeuThich();
//                Toast.makeText(this, "Đã xóa danh sách yêu thích thành công", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void updateFavoriteStatusLocally(boolean isFavorite) {
//        product.setTrangThaiYeuThich(isFavorite);
//        btnYeuThich.setVisibility(isFavorite ? View.GONE : View.VISIBLE);
//        btnYeuThichRed.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
//    }
//
//    private void updateTrangThaiYeuThich() {
//        if (product == null) return;
//
//        Call<Response<Product>> call = apiService.updateProductYeuThichById(maSP, false);
//        call.enqueue(new Callback<Response<Product>>() {
//            @Override
//            public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
//                if (!response.isSuccessful() || response.body() == null || response.body().getStatus() != 200) {
//                    // If the update fails, inform the user and revert changes if necessary
//                    Toast.makeText(Activity_ChiTietSP.this, "Không thể cập nhật trạng thái yêu thích", Toast.LENGTH_SHORT).show();
//                    updateFavoriteStatusLocally(!product.isTrangThaiYeuThich());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response<Product>> call, Throwable t) {
//                Toast.makeText(Activity_ChiTietSP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                updateFavoriteStatusLocally(!product.isTrangThaiYeuThich());
//            }
//        });
//    }


    private List<DanhGia_Demo> getDummyReviews() {
        List<DanhGia_Demo> reviews = new ArrayList<>();
        reviews.add(new DanhGia_Demo("Hồng hài nhi", 5, "Giày đẹp quá shop ơi...."));
        reviews.add(new DanhGia_Demo("Ngư ma vương", 5, "GIAYDEP..."));
        reviews.add(new DanhGia_Demo("Nhị lang thần", 5, "Có tiền để mua giầy nhưnghfyfghkhxgdfgvhkjhcxzfsdghvcx yufghjvhcfvgbjk uyuftygyuhiugyfygyuh gufythg không thể mua được em :P"));
        reviews.add(new DanhGia_Demo("Nhị lang thần", 5, "Có tiền để mua giầy nhưnghfyfghkhxgdfgvhkjhcxzfsdghvcx yufghjvhcfvgbjk uyuftygyuhiugyfygyuh gufythg không thể mua được em :P"));
        return reviews;
    }
}