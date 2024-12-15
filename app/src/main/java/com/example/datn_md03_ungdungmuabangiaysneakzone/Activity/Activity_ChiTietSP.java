    package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

    import android.app.Dialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Gravity;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.Button;
    import android.widget.ImageButton;
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
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.ReviewAdapter;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SizeAdapter;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGia_Demo;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGiaAdapter_Demo;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Cart;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.CustomerAccount;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.FavoriteAdd;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItem;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Review;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;

    public class Activity_ChiTietSP extends AppCompatActivity {
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

        ProductItem proitem;
        ReviewAdapter reviewAdapter;
        RecyclerView rcvReview;
        private ArrayList<Review> reviewList ;

        RecyclerView rcvSizes;

        String[] imageList; // Danh sách ảnh slideshow
        int currentImageIndex = 0;

        ImageView imageViewSlider; // ImageView cho slideshow
        ImageButton btnPrev, btnNext;

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
            hinh = String.valueOf(i.getStringArrayListExtra("image"));
            hinh = hinh.replace("[", "").replace("]", "").trim(); // Loại bỏ dấu ngoặc vuông nếu có
            imageList = hinh.split(","); // Chia chuỗi thành mảng URL


            proitem = new ProductItem();
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
            imageViewSlider = findViewById(R.id.imageViewSlider);
            btnPrev = findViewById(R.id.btnPrev); // Nút "Trước đó"
            btnNext = findViewById(R.id.btnNext); // Nút "Tiếp theo"

            tvName = findViewById(R.id.tvTen_SPCT);
            tvGia = findViewById(R.id.tvGia_SPCT);
            tvMoTa = findViewById(R.id.tvMoTa_SPCT);
    //        imgSP = findViewById(R.id.img_SPCT);
            btnYeuThich = findViewById(R.id.btnYeuThich_CTSP);
            btnYeuThichRed = findViewById(R.id.btnYeuThich_CTSP_red);
            btnGioHang = findViewById(R.id.btnGioHang_CTSP);


            tvName.setText(tenSP);
            tvGia.setText(giaSP+"VNĐ");
            tvMoTa.setText(moTa);

            kichThuocArrayList = new ArrayList();
            accountArrayList = new ArrayList();

            rcvReview = findViewById(R.id.rcvDanhGia_SPCT);
            btnMuaHang = findViewById(R.id.btnMuaHang_CTSP);
            rcvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


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
                    onBackPressed();
                }
            });

            btnYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  onAddToFavoriteClicked();
                }
            });

            btnYeuThichRed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRemoveFromFavoriteClicked();
                }
            });
            reviewList = new ArrayList<>(); // Khởi tạo danh sách
            reviewAdapter = new ReviewAdapter(reviewList, this); // Tạo adapter
            rcvReview.setAdapter(reviewAdapter); // Gán adapter

            getFavoriteStatus(email, maSP);
            loadReviewsByProduct();
            showImage();

            // Xử lý sự kiện cho nút "Trước đó"
            // Xử lý sự kiện slideshow
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImageIndex--; // Giảm chỉ số hình ảnh
                    if (currentImageIndex < 0) {
                        currentImageIndex = imageList.length - 1; // Quay lại hình ảnh cuối cùng nếu đã hết
                    }
                    showImage(); // Hiển thị hình ảnh mới
                }
            });

            // Xử lý sự kiện cho nút "Tiếp theo"
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImageIndex++; // Tăng chỉ số hình ảnh
                    if (currentImageIndex >= imageList.length) {
                        currentImageIndex = 0; // Quay lại hình ảnh đầu tiên nếu đã hết
                    }
                    showImage(); // Hiển thị hình ảnh mới
                }
            });
        }

        private void showImage() {
            // Kiểm tra xem danh sách hình ảnh có hợp lệ không
            if (imageList != null && imageList.length > 0) {
                // Lấy URL của ảnh hiện tại
                String currentImageUrl = imageList[currentImageIndex].trim();

                // Ghép với URL base nếu cần
                String baseUrl = "http://160.191.50.148:3000/"; // Thay thế bằng base URL thực tế
                String fullImageUrl = baseUrl + currentImageUrl;

                Log.d("CTSP", "Full Image URL: " + fullImageUrl);

                // Kiểm tra URL có hợp lệ không
                if (!currentImageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(fullImageUrl)  // Tải ảnh từ URL
                            .into(imageViewSlider);  // Đặt ảnh vào ImageView
                } else {
                    Glide.with(this)
                            .load(R.drawable.nice_shoe)  // Hình ảnh mặc định nếu không có URL
                            .into(imageViewSlider);
                }
            } else {
                // Nếu không có URL hợp lệ, tải ảnh mặc định
                Glide.with(this)
                        .load(R.drawable.nice_shoe)  // Hình ảnh mặc định
                        .into(imageViewSlider);
            }

        }

        private void loadReviewsByProduct() {
         Call<Response<ArrayList<Review>>> call = apiService.getReviewsByProduct(maSP);
         call.enqueue(new Callback<Response<ArrayList<Review>>>() {
             @Override
             public void onResponse(Call<Response<ArrayList<Review>>> call, retrofit2.Response<Response<ArrayList<Review>>> response) {
                 if (response.isSuccessful() && response.body() != null) {
                     ArrayList<Review> data = response.body().getData();
                     if (data != null) {
                         reviewList.clear();
                         reviewList.addAll(data);
                         reviewAdapter.notifyDataSetChanged();
                     } else {
                         Log.d("API Response", "No reviews available");
                         Toast.makeText(Activity_ChiTietSP.this, "No reviews found for this product.", Toast.LENGTH_SHORT).show();
                     }
                 } else {
                     Log.e("API Response", "Failed to load reviews.");
                     Toast.makeText(Activity_ChiTietSP.this, "Failed to load reviews.", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<Response<ArrayList<Review>>> call, Throwable t) {

             }
         });
        }

        // Xử lý khi nhấn nút thêm vào yêu thích
        public void onAddToFavoriteClicked() {
            updateFavoriteStatus(email, maSP, true);  // Thêm vào yêu thích
        }

        // Xử lý khi nhấn nút xóa khỏi yêu thích
        public void onRemoveFromFavoriteClicked() {
            updateFavoriteStatus(email, maSP,false);  // Xóa khỏi yêu thích
        }

        // Lấy trạng thái yêu thích
        private void getFavoriteStatus(String tentaikhoan, String sanPhamId) {
            ApiService apiService = RetrofitClient.getApiService();
            apiService.getFavoriteStatus(tentaikhoan, sanPhamId).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Response response1 = response.body();
                        // Kiểm tra nếu sản phẩm là yêu thích
                        if (response1.isFavorite()) {
                            // Sản phẩm là yêu thích, hiển thị nút đỏ và ẩn nút yêu thích
                            btnYeuThich.setVisibility(View.GONE);
                            btnYeuThichRed.setVisibility(View.VISIBLE);
                        } else {
                            // Sản phẩm chưa yêu thích, hiển thị nút yêu thích và ẩn nút đỏ
                            btnYeuThich.setVisibility(View.VISIBLE);
                            btnYeuThichRed.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(Activity_ChiTietSP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Cập nhật trạng thái yêu thích (thêm hoặc xóa)
        private void updateFavoriteStatus(String tentaikhoan, String sanPhamId, boolean isFavorite) {
            FavoriteAdd request = new FavoriteAdd(isFavorite);
            ApiService apiService = RetrofitClient.getApiService();
            apiService.updateFavoriteStatus(tentaikhoan, sanPhamId, request).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if(response.isSuccessful() && response.body() != null){
                        if (isFavorite) {
                            Toast.makeText(Activity_ChiTietSP.this, "Sản phẩm đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_ChiTietSP.this, "Sản phẩm đã bị xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        }
                        getFavoriteStatus(email, maSP);
                    } else {
                        Toast.makeText(Activity_ChiTietSP.this, "Lỗi khi cập nhật danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(Activity_ChiTietSP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

                btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectSize == -1) {
                            Toast.makeText(Activity_ChiTietSP.this, "Vui lòng chọn kích thước", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (soLuongTon == 0) {
                            Toast.makeText(Activity_ChiTietSP.this, "Kích cỡ hiện tại đã hết hàng. Vui lòng chọn kích cỡ khác!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // Prepare product data to send
                            List<String> hinhanh = Collections.singletonList(hinh);

                            productItem.setMaSanPham(maSP);
                            productItem.setTenSP(tenSP);
                            productItem.setSoLuongGioHang(num);
                            productItem.setSize(selectSize);
                            productItem.setGia(giaSP);
                            productItem.setTongTien(giaSP * num);
                            productItem.setHinhAnh(hinhanh);
                            productItem.setSoLuongTon(soLuongTon);

                            // Pass data to Activity_ThanhToan
                            Intent intent = new Intent(Activity_ChiTietSP.this, ActivityCTSP_To_ThanhToan.class);
                            intent.putExtra("productItem", (Serializable) productItem);
                            startActivity(intent);
                            dialog.dismiss();
                        }
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
                           List<String> hinhanh = Collections.singletonList(hinh);

                           productItem.setMaSanPham(maSP);
                           productItem.setTenSP(tenSP);
                           productItem.setSoLuongGioHang(num);
                           productItem.setSize(selectSize);
                           productItem.setGia(giaSP);
                           productItem.setTongTien(giaSP * num);
                           productItem.setHinhAnh(hinhanh);
                           productItem.setSoLuongTon(soLuongTon);

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
    }