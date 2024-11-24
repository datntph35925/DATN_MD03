package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.List;

public class ActivityCTSP_To_ThanhToan extends AppCompatActivity {

    private TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductSize, tvTotalPrice, tvPaymentMethods;
    private ImageView imgProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ctsp_to_thanh_toan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvProductName = findViewById(R.id.tvName_od_ctsp);
        tvProductPrice = findViewById(R.id.tvPrice_od_ctsp);
        tvProductQuantity = findViewById(R.id.tvSL_od_ctsp);
        tvProductSize = findViewById(R.id.tvSize_od_ctsp);
        tvTotalPrice = findViewById(R.id.tvTotal_od_ctsp);
        imgProduct = findViewById(R.id.imgProduct_od_ctsp);
        tvPaymentMethods = findViewById(R.id.txtPayment_od_ctsp);
        poppuGetListPayment();

        // Retrieve passed product data
        ProductItemCart selectedProduct = (ProductItemCart) getIntent().getSerializableExtra("selectedProduct");

        if (selectedProduct != null) {
            // Display product data
            tvProductName.setText(selectedProduct.getTenSP());
            tvProductPrice.setText(String.format("$%.2f", selectedProduct.getGia()));
            tvProductQuantity.setText(String.format("Quantity: %d", selectedProduct.getSoLuongGioHang()));
            tvProductSize.setText(String.format("Size: %d", selectedProduct.getSize()));
            tvTotalPrice.setText(String.format("$%.2f", selectedProduct.getTongTien()));

            List<String> productImages = selectedProduct.getHinhAnh();
            if (productImages != null && !productImages.isEmpty()) {
                Glide.with(this).load(productImages.get(0)).into(imgProduct);
            }
        }
    }

    private void poppuGetListPayment() {
        String[] listPayment = {"Thanh toán khi nhận hàng", "Thanh toán với PayPal"};
        tvPaymentMethods.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(ActivityCTSP_To_ThanhToan.this, tvPaymentMethods);
            for (String address : listPayment) {
                popupMenu.getMenu().add(address);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String selectedAddress = item.getTitle().toString();
                    tvPaymentMethods.setText(selectedAddress);
                    return true;
                }
            });
            popupMenu.show();
        });
    }
}