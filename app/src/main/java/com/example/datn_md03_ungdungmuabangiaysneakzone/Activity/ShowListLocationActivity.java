package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.DiaChiAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.LocationAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.DiaChi;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Location;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.LocationRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ShowListLocationActivity extends AppCompatActivity {
    private RecyclerView rcvDiaChi;
    private DiaChiAdapter diaChiAdapter;
    private FloatingActionButton btnAdd;
    private GestureDetector gestureDetector;
    ApiService apiService;
    private String currentUserId ; // ID người dùng hiện tại
    ArrayList<Location> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_location);

        rcvDiaChi = findViewById(R.id.rcv_diachi);
        locationArrayList = new ArrayList<>();
        rcvDiaChi.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getClient().create(ApiService.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email
        getListLocationById();
//        setupRecyclerView();
       // setupGestureDetector();

//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAddDiaChiDialog();
//            }
//        });
    }

//    private void setupRecyclerView() {
//        diaChiAdapter = new DiaChiAdapter(ShowListLocationActivity.this, diaChiList, new DiaChiAdapter.Callback() {
//            @Override
//            public void clickItem(Location address) {
//                Intent intent = new Intent(ShowListLocationActivity.this, MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("nameLocation", address.getTenNguoiNhan());
//                bundle.putString("location", address.getDiaChi());
//                bundle.putString("phoneLocation", address.getSdt());
//
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//
//            @Override
//            public void deleteAddress(Location address) {
//
//            }
//        });
//        rcvDiaChi.setLayoutManager(new LinearLayoutManager(this));
//        rcvDiaChi.setAdapter(diaChiAdapter);
//    }

    private void getListLocationById() {
        // Gọi API để lấy danh sách địa chỉ từ server
        Call<Response<ArrayList<Location>>> call = apiService.getListLocationById(currentUserId);
        call.enqueue(new Callback<Response<ArrayList<Location>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Location>>> call, retrofit2.Response<Response<ArrayList<Location>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationArrayList = response.body().getData();
                    diaChiAdapter =  new DiaChiAdapter(ShowListLocationActivity.this, locationArrayList, new DiaChiAdapter.Callback() {
                        @Override
                        public void clickItem(Location Address) {
                            Intent intent = new Intent(ShowListLocationActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("nameLocation", Address.getTenNguoiNhan());
                            bundle.putString("location", Address.getDiaChi());
                            bundle.putString("phoneLocation", Address.getSdt());

                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void deleteAddress(Location address) {

                        }

                        @Override
                        public void editAddress(Location address) {

                        }
                    });
                    rcvDiaChi.setAdapter(diaChiAdapter);
                } else {
                    Toast.makeText(ShowListLocationActivity.this, "Không thể lấy địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Location>>> call, Throwable t) {
                Toast.makeText(ShowListLocationActivity.this, "Looi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLocation(Location location){
        LocationRequest request = new LocationRequest(location.get_id());
        Call<Response<LocationRequest>> call = apiService.removeLocation(currentUserId, request);
        call.enqueue(new Callback<Response<LocationRequest>>() {
            @Override
            public void onResponse(Call<Response<LocationRequest>> call, retrofit2.Response<Response<LocationRequest>> response) {
                if(response.isSuccessful() && response.body() != null){
                    locationArrayList.remove(location);
                    diaChiAdapter.notifyDataSetChanged();
                    Toast.makeText(ShowListLocationActivity.this, "Địa chỉ đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowListLocationActivity.this, "Không thể xóa địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<LocationRequest>> call, Throwable t) {
                Toast.makeText(ShowListLocationActivity.this, "Lỗi xóa địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}