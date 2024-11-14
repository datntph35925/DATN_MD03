package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SanPhamAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.SP_PhoBienAdapterDemo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.SP_PhoBien_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.YeuThichAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_SP_PhoBien extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imgBack, imgSearch;
    ApiService apiService;
    SanPhamAdapter productAdapter;
    ArrayList<Product> productArrayList;
    EditText edSearch;
    TextView tvTextView;
    private Boolean isVisibleSearch = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sp_pho_bien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.rcvSPPhoBien_SPPB);
        imgBack = findViewById(R.id.imgBack_SPPB);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_SP_PhoBien.this, MainActivity.class));
            }
        });
        apiService = RetrofitClient.getClient().create(ApiService.class);
        productArrayList = new ArrayList<>();

        timKiem();
        // Gọi API để lấy danh sách sản phẩm
        getListProducts();

    }

    private void timKiem() {
        ImageView imgSearch_SPPB = findViewById(R.id.imgSearch_SPPB);
        ImageView imgBack_SPPB = findViewById(R.id.imgBack_SPPB);
        TextView textView = findViewById(R.id.textView);
        edSearch = findViewById(R.id.edSearch);
        ConstraintLayout mainLayout = findViewById(R.id.main); // Layout chính để phát hiện nhấn ra ngoài

        imgSearch_SPPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBack_SPPB.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                imgSearch_SPPB.setVisibility(View.GONE);

                // Hiện TextInputLayout
                edSearch.setVisibility(View.VISIBLE);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
//                layoutParams.topMargin = 30; // Thêm 30dp khoảng cách
                layoutParams.bottomToTop = R.id.edSearch;
                recyclerView.setLayoutParams(layoutParams);
            }
        });


    }

    private void loadDuLieu(ArrayList<Product> list){
        productAdapter = new SanPhamAdapter(this, list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(productAdapter);
    }
    private void getListProducts() {
        Call<Response<ArrayList<Product>>> call = apiService.getListProducts();
        call.enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getStatus() == 200) {
                        productArrayList = response.body().getData();
                        loadDuLieu(productArrayList);
                    } else {
                        Toast.makeText(Activity_SP_PhoBien.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Activity_SP_PhoBien.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(Activity_SP_PhoBien.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}