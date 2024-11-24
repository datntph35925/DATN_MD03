    package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatButton;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.CartAdapter;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SanPhamAdapter;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.CartAdapter_Demo;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.Cart_Demo;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Cart;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.DeleteCartRequest;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.google.android.material.navigation.NavigationBarView;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    import retrofit2.Call;
    import retrofit2.Callback;
//nam anh depzai
    public class Activity_Cart extends AppCompatActivity {

        private RecyclerView cartRecyclerView;
        ArrayList<ProductItemCart> productItemCarts;
        CartAdapter cartAdapter;
        ApiService apiService;
        ArrayList<Cart> cartArrayList;
        private Set<ProductItemCart> selectedItems = new HashSet<>();
        String email;
        TextView tvTotalCost;

        ImageView imgBack_cart;

        BottomNavigationView bottomNavigationView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_cart);
            bottomNavigationView = findViewById(R.id.bottomnavigation);
            setBottomNavigationView();
            cartRecyclerView = findViewById(R.id.rcv_cart);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            tvTotalCost = findViewById(R.id.cart_total_price);
            imgBack_cart = findViewById(R.id.imgBack_cart);

            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

            apiService = RetrofitClient.getClient().create(ApiService.class);
            productItemCarts = new ArrayList<>();
            cartArrayList = new ArrayList<>();

            // Dữ liệu giỏ hàng

            loadDuLieuCart();
            // Update total price
            updateTotalCost();

            AppCompatButton checkoutButton = findViewById(R.id.checkout_button);
            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   ThanhToanActivity();
                }
            });

        }

        private void loadDuLieuCart() {
            apiService.getListCartById(email).enqueue(new Callback<Response<Cart>>() {
                @Override
                public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Cart cart = response.body().getData();
                        productItemCarts = cart.getSanPham();

                        cartAdapter = new CartAdapter(Activity_Cart.this, productItemCarts, new CartAdapter.OnCartItemActionListener() {
                            @Override
                            public void onIncreaseQuantity(ProductItemCart item) {
                                // Tăng số lượng
                                updateCart();
                            }

                            @Override
                            public void onDecreaseQuantity(ProductItemCart item) {
                                // Giảm số lượng
                                    updateCart();

                            }

                            @Override
                            public void onDeleteItem(ProductItemCart item) {
                                // Gọi API để xóa sản phẩm
                                Log.d("CartItem", "masp: " + item.getMaSanPham());
                                Log.d("CartItem", "size: " + item.getSize());
                                Log.d("CartItem", "mail: " + email);

                                DeleteCartRequest request = new DeleteCartRequest(item.getMaSanPham(), item.getSize());
                                apiService.removeCartItem(email, request)
                                        .enqueue(new Callback<Response<DeleteCartRequest>>() {
                                            @Override
                                            public void onResponse(Call<Response<DeleteCartRequest>> call, retrofit2.Response<Response<DeleteCartRequest>> response) {
                                                if (response.isSuccessful()) {
                                                    // Xóa sản phẩm khỏi selectedItems
                                                    selectedItems.remove(item);
                                                    // Cập nhật danh sách sản phẩm và RecyclerView
                                                    productItemCarts.remove(item);
                                                    cartAdapter.notifyDataSetChanged();
                                                    updateTotalCost();
                                                    Toast.makeText(Activity_Cart.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Activity_Cart.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Response<DeleteCartRequest>> call, Throwable t) {
                                                Toast.makeText(Activity_Cart.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onItemChecked(ProductItemCart item, boolean isChecked) {
                                if (isChecked) {
                                   selectedItems.add(item);
                                } else {
                                   selectedItems.remove(item);
                                }
                                updateTotalCost(); // Tính lại tổng tiền khi trạng thái checkbox thay đổi
                            }
                        });
                        cartRecyclerView.setAdapter(cartAdapter);
                    } else {
                        Toast.makeText(Activity_Cart.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<Cart>> call, Throwable t) {
                    Toast.makeText(Activity_Cart.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void updateCart() {
            cartAdapter.notifyDataSetChanged();
            updateTotalCost();
        }

        public void updateTotalCost() {
            // Logic để tính tổng chi phí và cập nhật giao diện
            double totalCost = 0;

            // Duyệt qua danh sách sản phẩm trong giỏ hàng
            for (ProductItemCart item : selectedItems) {
                totalCost += item.getGia() * item.getSoLuongGioHang();
            }

            // Cập nhật UI (Giả sử bạn có một TextView để hiển thị tổng tiền)
            tvTotalCost.setText(String.format("$%.2f", totalCost));
        }


        public void setBottomNavigationView() {
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.getItemId() == R.id.trangchu){
                        startActivity(new Intent(Activity_Cart.this, MainActivity.class));
                    } else if (item.getItemId() == R.id.yeuthich) {
                        startActivity(new Intent(Activity_Cart.this, Activity_YeuThich.class));
                    } else if (item.getItemId() == R.id.hoso) {
                        startActivity(new Intent(Activity_Cart.this, Activity_Profile.class));
                    }
                    return true;
                }
            });
            bottomNavigationView.setSelectedItemId(R.id.giohang);

        }

        private void ThanhToanActivity() {
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Activity_Cart.this, Activity_ThanhToan.class);
            ArrayList<ProductItemCart> selectedCartItems = new ArrayList<>(selectedItems); // Convert Set to ArrayList
            intent.putExtra("selectedCartItems", (Serializable) selectedCartItems);
            startActivity(intent);
        }
    }