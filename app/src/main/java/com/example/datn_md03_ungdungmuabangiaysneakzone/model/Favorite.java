package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.ActivityLocation;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import retrofit2.Call;
import retrofit2.Callback;

public class Favorite {
    private String MaSanPham;
    private String Tentaikhoan;
    private Product SanPham;

    public Product getSanPham() {
        return SanPham;
    }

    public void setSanPham(Product sanPham) {
        SanPham = sanPham;
    }

    public Favorite() {
    }

    public Favorite(String maSanPham, String tentaikhoan) {
        MaSanPham = maSanPham;
        Tentaikhoan = tentaikhoan;
    }

    public String getMaSanPham() {
        return MaSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        MaSanPham = maSanPham;
    }

    public String getTentaikhoan() {
        return Tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        Tentaikhoan = tentaikhoan;
    }
}

//private void showAddLocationDialog() {
//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    View view = LayoutInflater.from(this).inflate(R.layout.dialog_diachi, null);
//    builder.setView(view);
//    AlertDialog dialog = builder.create();
//
//    EditText edtTenNguoiNhan = view.findViewById(R.id.edt_tendiachi);
//    EditText edtDiaChi = view.findViewById(R.id.edt_diachi);
//    EditText edtSdt = view.findViewById(R.id.edt_sodienthoai);
//
//    Button btnCancel = view.findViewById(R.id.btn_cancel_diaSp);
//    Button btnSave = view.findViewById(R.id.btn_save_diaSp);
//
//    btnCancel.setOnClickListener(v -> dialog.dismiss());
//
//    btnSave.setOnClickListener(v -> {
//        String tenNguoiNhan = edtTenNguoiNhan.getText().toString().trim();
//        String diaChi = edtDiaChi.getText().toString().trim();
//        String sdt = edtSdt.getText().toString().trim();
//
//        if (tenNguoiNhan.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
//            Toast.makeText(ActivityLocation.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Tạo đối tượng Location
//        Location newLocation = new Location();
//        newLocation.setTenNguoiNhan(tenNguoiNhan);
//        newLocation.setDiaChi(diaChi);
//        newLocation.setSdt(sdt);
//
//        // Gọi API thêm địa chỉ
//        Call<Location> call = apiService.addLocation(currentUserId, newLocation);
//        call.enqueue(new Callback<Location>() {
//            @Override
//            public void onResponse(Call<Location> call, retrofit2.Response<Location> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    locationArrayList.add(response.body());
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(ActivityLocation.this, "Thêm địa chỉ thành công!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                } else {
//                    Toast.makeText(ActivityLocation.this, "Không thể thêm địa chỉ!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Location> call, Throwable t) {
//                Toast.makeText(ActivityLocation.this, "Lỗi khi thêm địa chỉ!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    });
//
//    dialog.show();
//}
