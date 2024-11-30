package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.LocationAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Location;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.LocationRequest;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ActivityLocation extends AppCompatActivity {

    private RecyclerView rcvLocation;
    private LocationAdapter adapter;
    private ApiService apiService;
    private String currentUserId ; // ID người dùng hiện tại
    ArrayList<Location> locationArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email
        locationArrayList = new ArrayList<>();
        rcvLocation = findViewById(R.id.rcv_location);
        rcvLocation.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        getListLocationById();
        findViewById(R.id.btn_add_location).setOnClickListener(v -> showAddLocationDialog());
    }

    private void getListLocationById() {
        // Gọi API để lấy danh sách địa chỉ từ server
        Call<Response<ArrayList<Location>>> call = apiService.getListLocationById(currentUserId);
        call.enqueue(new Callback<Response<ArrayList<Location>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Location>>> call, retrofit2.Response<Response<ArrayList<Location>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationArrayList = response.body().getData();
                    adapter = new LocationAdapter(ActivityLocation.this, locationArrayList, new LocationAdapter.Callback() {
                        @Override
                        public void editAddress(Location address) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLocation.this);
                            View view = LayoutInflater.from(ActivityLocation.this).inflate(R.layout.dialog_diachi, null);
                            builder.setView(view);
                            AlertDialog dialog = builder.create();

                            EditText edtTenNguoiNhan = view.findViewById(R.id.edt_tendiachi);
                            EditText edtDiaChi = view.findViewById(R.id.edt_diachi);
                            EditText edtSdt = view.findViewById(R.id.edt_sodienthoai);
                            TextView tvTitle = view.findViewById(R.id.tv_title_sp); // Từ layout `dialog_diachi`
                            tvTitle.setText("Sửa địa chỉ");
                            // Hiển thị thông tin hiện tại
                            edtTenNguoiNhan.setText(address.getTenNguoiNhan());
                            edtDiaChi.setText(address.getDiaChi());
                            edtSdt.setText(address.getSdt());

                            Button btnSave = view.findViewById(R.id.btn_save_diaSp);
                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String tenNguoiNhan = edtTenNguoiNhan.getText().toString().trim();
                                    String diaChi = edtDiaChi.getText().toString().trim();
                                    String sdt = edtSdt.getText().toString().trim();

                                    // Kiểm tra đầu vào
                                    if (tenNguoiNhan.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                                        Toast.makeText(ActivityLocation.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // Cập nhật đối tượng
                                    address.setTenNguoiNhan(tenNguoiNhan);
                                    address.setDiaChi(diaChi);
                                    address.setSdt(sdt);

                                    // Gọi API cập nhật
                                    Call<Response<Location>> call = apiService.updateLocation(currentUserId, address);
                                    call.enqueue(new Callback<Response<Location>>() {
                                        @Override
                                        public void onResponse(Call<Response<Location>> call, retrofit2.Response<Response<Location>> response) {
                                           if(response.isSuccessful() && response.body() != null){
                                               adapter.notifyDataSetChanged();
                                               Toast.makeText(ActivityLocation.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                               dialog.dismiss();
                                           }
                                        }

                                        @Override
                                        public void onFailure(Call<Response<Location>> call, Throwable t) {
                                            Toast.makeText(ActivityLocation.this, "Lỗi khi cập nhật địa chỉ!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            Button btnCancel = view.findViewById(R.id.btn_cancel_diaSp);
                            btnCancel.setOnClickListener(v -> dialog.dismiss());

                            dialog.show();
                        }

                        @Override
                        public void deleteAddress(Location address) {
                          new AlertDialog.Builder(ActivityLocation.this)
                                  .setTitle("Xác nhận xóa")
                                  .setMessage("Bạn có chắc muốn xóa địa chỉ này")
                                  .setPositiveButton("Có", (dialog, which) -> {
                                      deleteLocation(address);
                                  })
                                  .setNegativeButton("Không", null)
                                  .show();

                        }
                    });
                    rcvLocation.setAdapter(adapter);
                } else {
                    Toast.makeText(ActivityLocation.this, "Không thể lấy địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Location>>> call, Throwable t) {
                Toast.makeText(ActivityLocation.this, "Looi", Toast.LENGTH_SHORT).show();
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
                  adapter.notifyDataSetChanged();
                    Toast.makeText(ActivityLocation.this, "Địa chỉ đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityLocation.this, "Không thể xóa địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<LocationRequest>> call, Throwable t) {
                Toast.makeText(ActivityLocation.this, "Lỗi xóa địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAddLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_diachi, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText edtTenNguoiNhan = view.findViewById(R.id.edt_tendiachi);
        EditText edtDiaChi = view.findViewById(R.id.edt_diachi);
        EditText edtSdt = view.findViewById(R.id.edt_sodienthoai);

        Button btnCancel = view.findViewById(R.id.btn_cancel_diaSp);
        Button btnSave = view.findViewById(R.id.btn_save_diaSp);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String tenNguoiNhan = edtTenNguoiNhan.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();

            if (tenNguoiNhan.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                Toast.makeText(ActivityLocation.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Location
            Location newLocation = new Location();
            newLocation.setTenNguoiNhan(tenNguoiNhan);
            newLocation.setDiaChi(diaChi);
            newLocation.setSdt(sdt);

            // Gọi API thêm địa chỉ
            Call<Location> call = apiService.addLocation(currentUserId, newLocation);
            call.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, retrofit2.Response<Location> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        locationArrayList.add(response.body());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ActivityLocation.this, "Thêm địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ActivityLocation.this, "Không thể thêm địa chỉ!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    Toast.makeText(ActivityLocation.this, "Lỗi khi thêm địa chỉ!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

}