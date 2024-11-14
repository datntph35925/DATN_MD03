package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.ThanhToanAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.Cart_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_ThanhToan extends AppCompatActivity {

    KichThuoc kichThuoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);
        LinearLayout imgChooseAddress = findViewById(R.id.lrlAddress);

        imgChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ThanhToan.this, ShowListLocationActivity.class);
                startActivity(intent);
            }
        });
        // Get the data from the Intent
        String productName = getIntent().getStringExtra("productName");
        double price = getIntent().getDoubleExtra("price", 0);
        String imageUrl = getIntent().getStringExtra("image");
        int size = getIntent().getIntExtra("size", 0); // Get the size
        int quantity = getIntent().getIntExtra("quantity", 1); // Get the quantity

        // Use this data to populate the RecyclerView
//        List<Product> productList = new ArrayList<>();
//        Product product = new Product(price, Arrays.asList(imageUrl), "", null, "", "", "", productName, "", false);
//        product.setKichThuoc(size); // Assuming you add this field in your Product model
//        product.setSelectedQuantity(quantity); // Assuming you add this field in your Product model
//        productList.add(product);
//
//        // Set up RecyclerView
//        RecyclerView recyclerView = findViewById(R.id.rcvOrder_dt);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        ThanhToanAdapter adapter = new ThanhToanAdapter(this, productList, size, quantity);
//        recyclerView.setAdapter(adapter);

    }
}