package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
        currentUserId = sharedPreferences.getString("Tentaikhoan", "").trim().toLowerCase();
        Log.d("CurrentUser", "Tài khoản hiện tại: " + currentUserId);// Retrieve the email
        apiService = RetrofitClient.getClient().create(ApiService.class);

        fetchVouchers();
    }

    private void fetchVouchers() {
      apiService.getListVouchers().enqueue(new Callback<Response<ArrayList<Voucher>>>() {
          @Override
          public void onResponse(Call<Response<ArrayList<Voucher>>> call, retrofit2.Response<Response<ArrayList<Voucher>>> response) {
              if(response.isSuccessful() && response.body() != null){
                 // voucherList = response.body().getData();
                  List<Voucher> allVouchers = response.body().getData();
                  Log.d("VoucherList", "Dữ liệu trả về từ API: " + allVouchers.toString());

                  // Debug dữ liệu từng voucher để kiểm tra giá trị usedBy
                  for (Voucher voucher : allVouchers) {
                      Log.d("VoucherDebug", "Mã Voucher: " + voucher.getMaVoucher() + ", UsedBy: " + voucher.getUsedBy());
                  }
                  // Lọc danh sách voucher: loại bỏ những voucher đã sử dụng bởi currentUserId
                  voucherList = new ArrayList<>();
                  for (Voucher voucher : allVouchers) {
                      List<String> usedByList = voucher.getUsedBy();

                      Log.d("VoucherFilter", "usedByList: " + usedByList + ", CurrentUser: " + currentUserId);

                      // So sánh có xử lý khoảng trắng và không phân biệt hoa thường
                      if (usedByList == null || usedByList.isEmpty() ||
                              !containsIgnoreCase(usedByList, currentUserId.trim().toLowerCase())) {
                          voucherList.add(voucher); // Thêm voucher nếu currentUserId không tồn tại trong usedBy
                      }
                  }

                  Log.d("VoucherFiltered", "Số lượng voucher sau lọc: " + voucherList.size());
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
              Toast.makeText(Activity_Voucher.this, "loi:" +t.getMessage(), Toast.LENGTH_SHORT).show();
              Log.d("Voucher", "Full: " + t.getMessage());
          }
      });
    }

    private boolean containsIgnoreCase(List<String> list, String target) {
        if (list == null || target == null) return false;

        for (String item : list) {
            if (item != null && item.trim().toLowerCase().equals(target)) {
                return true; // Trùng khớp
            }
        }
        return false;
    }
}