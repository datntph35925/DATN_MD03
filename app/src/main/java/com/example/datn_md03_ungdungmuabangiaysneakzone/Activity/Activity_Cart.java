package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.CartAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.Cart_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_Cart extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter_Demo cartAdapter;
    private List<Cart_Demo> cartItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
    }

    private void updateTotalCost() {
        // Logic để tính tổng chi phí và cập nhật giao diện
    }
}