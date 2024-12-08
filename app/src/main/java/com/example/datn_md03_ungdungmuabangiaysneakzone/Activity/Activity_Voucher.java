package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.VoucherAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Voucher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_Voucher extends AppCompatActivity {

    ImageButton img_back_voucher;
    RecyclerView rcv_voucher;
    VoucherAdapter voucherAdapter;
    private List<Voucher> voucherList;
    private ApiService apiService;
    String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img_back_voucher = findViewById(R.id.img_back_voucher);
        rcv_voucher = findViewById(R.id.rcv_voucher);
        rcv_voucher.setLayoutManager(new LinearLayoutManager(this));
        img_back_voucher.setOnClickListener(v -> {
            finish();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email
        apiService = RetrofitClient.getClient().create(ApiService.class);

        fetchVouchers();
    }

    private void fetchVouchers() {
      apiService.getListVouchers().enqueue(new Callback<Response<ArrayList<Voucher>>>() {
          @Override
          public void onResponse(Call<Response<ArrayList<Voucher>>> call, retrofit2.Response<Response<ArrayList<Voucher>>> response) {
              if(response.isSuccessful() && response.body() != null){
                  voucherList = response.body().getData();
                  voucherAdapter = new VoucherAdapter(voucherList, new VoucherAdapter.OnVoucherClickListener() {
                      @Override
                      public void onVoucherClick(Voucher maVoucher) {

                      }
                  }, false);
                  rcv_voucher.setAdapter(voucherAdapter);
              }
          }

          @Override
          public void onFailure(Call<Response<ArrayList<Voucher>>> call, Throwable t) {
              Toast.makeText(Activity_Voucher.this, "loi", Toast.LENGTH_SHORT).show();
          }
      });
    }
}