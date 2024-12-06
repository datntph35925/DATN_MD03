package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ActivityLocation extends AppCompatActivity {

    private RecyclerView rcvLocation;
    private LocationAdapter adapter;
    private ApiService apiService;
    private String currentUserId ; // ID người dùng hiện tại
    ArrayList<Location> locationArrayList;
    ImageButton img_back;
    Button btnLayViTri;

    private static final int LOCATION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;

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

        img_back = findViewById(R.id.img_back_Location);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLocation.this, Activity_Profile.class));
            }
        });
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Khởi tạo FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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

                            Button btnLayViTri = view.findViewById(R.id.btnLayViTri);
                            btnLayViTri.setOnClickListener(v -> requestLocationPermission(edtDiaChi));


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

        Button btnLayViTri = view.findViewById(R.id.btnLayViTri);
        btnLayViTri.setOnClickListener(v -> requestLocationPermission(edtDiaChi));


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

    private void requestLocationPermission(EditText edtDiaChi) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            getCurrentLocation(edtDiaChi);
        }
    }

    private void getCurrentLocation(EditText edtDiaChi) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            getAddressFromCoordinates(latitude, longitude, edtDiaChi);
                        } else {
                            Toast.makeText(this, "Không thể lấy vị trí hiện tại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi lấy vị trí!", Toast.LENGTH_SHORT).show());
        }
    }

    private void getAddressFromCoordinates(double latitude, double longitude, EditText edtDiaChi) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                edtDiaChi.setText(address);
            } else {
                Toast.makeText(this, "Không tìm thấy địa chỉ!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi chuyển đổi tọa độ!", Toast.LENGTH_SHORT).show();
        }
    }
}