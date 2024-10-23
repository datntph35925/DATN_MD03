package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.CartAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.Cart_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Activity_Cart extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter_Demo cartAdapter;
    private List<Cart_Demo> cartItems;

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

        // Dữ liệu giỏ hàng
        cartItems = new ArrayList<>();

        // Thêm các sản phẩm vào giỏ hàng (giả lập dữ liệu)
        cartItems.add(new Cart_Demo("Nike Air Max", 64.95, 1, R.drawable.nice_shoe));
        cartItems.add(new Cart_Demo("Nike Air Max 200", 64.95, 1, R.drawable.nice_shoe));
        cartItems.add(new Cart_Demo("Nike Club Max", 64.95, 1, R.drawable.nice_shoe));


        cartAdapter = new CartAdapter_Demo(cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

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

    private void updateTotalCost() {
        // Logic để tính tổng chi phí và cập nhật giao diện
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
        Intent intent = new Intent(Activity_Cart.this, Activity_ThanhToan.class);
        startActivity(intent);
    }
}