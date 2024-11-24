package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_ThanhToan extends AppCompatActivity {

    private RecyclerView rcvThanhToan;
    private TextView tvTotalCost, tvPayMent;
    KichThuoc kichThuoc;
    ArrayList<ProductItemCart> selectedCartItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);

        rcvThanhToan = findViewById(R.id.rcvOrder_dt);
        tvTotalCost = findViewById(R.id.txtTotal);
        tvPayMent = findViewById(R.id.txtPayment);
        poppuGetListPayment();

        selectedCartItems = (ArrayList<ProductItemCart>) getIntent().getSerializableExtra("selectedCartItems");

        if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
            ThanhToanAdapter adapter = new ThanhToanAdapter(this, selectedCartItems);
            rcvThanhToan.setLayoutManager(new LinearLayoutManager(this));
            rcvThanhToan.setAdapter(adapter);

            double totalCost = 0;
            for (ProductItemCart item : selectedCartItems) {
                totalCost += item.getGia() * item.getSoLuongGioHang();
            }
            tvTotalCost.setText(String.format("$%.2f", totalCost));
        }

    }

    private void poppuGetListPayment() {
        String[] listPayment = {"Thanh toán khi nhận hàng", "Thanh toán với PayPal"};
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