package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

        edSearch = findViewById(R.id.edSearch);
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

        setupSearch();
        // Gọi API để lấy danh sách sản phẩm
        getListProducts();

    }

    private void setupSearch() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              String key = charSequence.toString();
              if(!key.isEmpty()){
                  searchProducts(key);
              } else {
                  getListProducts();
              }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchProducts(String keyword) {
        Log.d("Search API", "Từ khóa tìm kiếm: " + keyword);

        Call<Response<ArrayList<Product>>> call = apiService.searchProducts(keyword);
        call.enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        ArrayList<Product> searchResults = response.body().getData();
                        if (!searchResults.isEmpty()) {
                            loadDuLieu(searchResults); // Hiển thị kết quả tìm kiếm
                        } else {
                            Toast.makeText(Activity_SP_PhoBien.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Activity_SP_PhoBien.this, "Không có kết quả!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Activity_SP_PhoBien.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(Activity_SP_PhoBien.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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