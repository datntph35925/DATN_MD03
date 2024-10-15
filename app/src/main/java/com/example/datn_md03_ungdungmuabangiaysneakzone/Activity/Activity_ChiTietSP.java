package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.Dialog;
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

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGia_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGiaAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_ChiTietSP extends AppCompatActivity {

    private RecyclerView recyclerReviews;
    private DanhGiaAdapter_Demo reviewsAdapter;

    Button btnMuaHang;
    private int num;

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
    }

    //Demo
    private void diaLogToBuy() {
        Dialog dialog = new Dialog(Activity_ChiTietSP.this);
        dialog.setContentView(R.layout.dialog_themgiohang);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bogoc);
        // kéo dialog xuống dưới màn hình
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;

        TextView tvSoLuongCon = dialog.findViewById(R.id.tvSoLuong_toBuy);
        TextView tvNum = dialog.findViewById(R.id.tv_num_toBuy);

        ImageView imgPlus = dialog.findViewById(R.id.img_plus_toBuy);
        ImageView imgMinus = dialog.findViewById(R.id.img_minus_toBuy);
        Button btnBuy = dialog.findViewById(R.id.btnBuy);
        num = 1;
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1){
                    num--;
                    tvNum.setText(num+"");
                }
            }
        });

        int selectedProductQuantity = 200;
        imgPlus.setOnClickListener(view -> {
            if (num < selectedProductQuantity){
                num++;
                tvNum.setText(num+"");
            }else {
                Toast.makeText(Activity_ChiTietSP.this, "Sản phẩm chỉ có " + selectedProductQuantity + " cái", Toast.LENGTH_SHORT).show();
            }
        });

        tvSoLuongCon.setText("100");

        btnBuy.setOnClickListener(view -> {
            Toast.makeText(Activity_ChiTietSP.this, "Đặt mua thành công", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }
    private List<DanhGia_Demo> getDummyReviews() {
        List<DanhGia_Demo> reviews = new ArrayList<>();
        reviews.add(new DanhGia_Demo("Hồng hài nhi", 5, "Giày đẹp quá shop ơi...."));
        reviews.add(new DanhGia_Demo("Ngư ma vương", 5, "GIAYDEP..."));
        reviews.add(new DanhGia_Demo("Nhị lang thần", 5, "Có tiền để mua giầy nhưnghfyfghkhxgdfgvhkjhcxzfsdghvcx yufghjvhcfvgbjk uyuftygyuhiugyfygyuh gufythg không thể mua được em :P"));
        return reviews;
    }
}