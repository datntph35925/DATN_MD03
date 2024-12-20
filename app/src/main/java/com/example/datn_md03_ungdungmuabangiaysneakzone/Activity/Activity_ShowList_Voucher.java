package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Intent;
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

public class Activity_ShowList_Voucher extends AppCompatActivity {
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
        setContentView(R.layout.activity_show_list_voucher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img_back_voucher = findViewById(R.id.img_back_Showvoucher);
        rcv_voucher = findViewById(R.id.rcv_Showvoucher);
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
                    //voucherList = response.body().getData();
                    List<Voucher> allVouchers = response.body().getData();
                    Log.d("VoucherList", "Dữ liệu trả về từ API: " + allVouchers.toString());

                    // Debug dữ liệu từng voucher để kiểm tra giá trị usedBy
                    for (Voucher voucher : allVouchers) {
                        Log.d("VoucherDebug", "Mã Voucher: " + voucher.getMaVoucher() + ", UsedBy: " + voucher.getUsedBy());
                    }
                    // Lọc danh sách voucher: loại bỏ những voucher đã sử dụng bởi currentUserId
                    voucherList = new ArrayList<>();
                    java.util.Date currentDate = new java.util.Date(); // Ngày hiện tại
                    for (Voucher voucher : allVouchers) {
                        List<String> usedByList = voucher.getUsedBy();
                        String trangThai = voucher.getTrangThai();
                        Log.d("VoucherFilter", "usedByList: " + usedByList + ", CurrentUser: " + currentUserId);

                        boolean isBeforeStartDate = voucher.getNgayBatDau() != null && voucher.getNgayBatDau().after(currentDate);
                        boolean isExpired = voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().before(currentDate);

                        if (isBeforeStartDate || isExpired) {
                            Log.d("VoucherFilter", "Voucher không hợp lệ: " + voucher.getMaVoucher());
                            continue;
                        }
                        // So sánh có xử lý khoảng trắng và không phân biệt hoa thường
                        if (usedByList == null || usedByList.isEmpty() ||
                                !containsIgnoreCase(usedByList, currentUserId.trim().toLowerCase()) && !"Không thể sử dụng".equalsIgnoreCase(trangThai)) {
                            voucherList.add(voucher); // Thêm voucher nếu currentUserId không tồn tại trong usedBy
                        }
                    }

                    Log.d("VoucherFiltered", "Số lượng voucher sau lọc: " + voucherList.size());
                    voucherAdapter = new VoucherAdapter(voucherList, new VoucherAdapter.OnVoucherClickListener() {
                        @Override
                        public void onVoucherClick(Voucher maVoucher) {
                            Intent intent = new Intent(Activity_ShowList_Voucher.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("maVoucher", maVoucher.getMaVoucher());
                            bundle.putDouble("giaTri", maVoucher.getGiaTri());
                            bundle.putString("loaiVoucher", maVoucher.getLoaiVoucher());

                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, true);
                    rcv_voucher.setAdapter(voucherAdapter);
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Voucher>>> call, Throwable t) {
                Toast.makeText(Activity_ShowList_Voucher.this, "loi", Toast.LENGTH_SHORT).show();
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