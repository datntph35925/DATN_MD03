package com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.Activity_CTDH_DaGiao;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.Activity_CTDH_HuyDH;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.OrderAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Fragment_DaHuy extends Fragment {
    private List<Order> huyList;
    private RecyclerView rcvHuy;
    private OrderAdapter hoaDonAdapter;
    String email;

    public Fragment_DaHuy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_da_giao, container, false);

        huyList = new ArrayList<>();
        rcvHuy = view.findViewById(R.id.rcv_done);
        rcvHuy.setLayoutManager(new LinearLayoutManager(getContext()));


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        hoaDonAdapter = new OrderAdapter(getContext(),huyList);
        rcvHuy.setAdapter(hoaDonAdapter);
        hoaDonAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Order order = huyList.get(position);

                Intent intent = new Intent(getContext(), Activity_CTDH_HuyDH.class);
                intent.putExtra("order_sp", (Serializable) order.getSanPham());
                intent.putExtra("order_ten", order.getTenNguoiNhan());
                intent.putExtra("order_diachi", order.getDiaChiGiaoHang());
                intent.putExtra("order_sdt", order.getSoDienThoai());
                intent.putExtra("order_pttt", order.getPhuongThucThanhToan());
                intent.putExtra("order_ttdh", order.getTrangThai());
                intent.putExtra("order_tongTien", order.getTongTien());
                startActivity(intent);
            }
        });

        fetchHuyOrders();
        return view;
    }

    private void fetchHuyOrders() {
        RetrofitClient.getApiService().getOrderById(email).enqueue(new Callback<Response<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Order>>> call, retrofit2.Response<Response<ArrayList<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().getData();
                    // Chắc chắn danh sách đã được khởi tạo
                    if (huyList != null) {
                        huyList.clear(); // Xóa tất cả các phần tử cũ
                    } else {
                        huyList = new ArrayList<>();  // Nếu danh sách vẫn null, khởi tạo lại
                    }// Clear current list

                    // Add only orders with "Đang giao" status
                    for (Order order : orders) {
                        if ("Hủy".equals(order.getTrangThai())) {
                            huyList.add(order);
                        }
                    }

                    if (huyList.isEmpty()) {
                        Toast.makeText(getContext(), "No orders in 'Huy' status", Toast.LENGTH_SHORT).show();
                    }

                    hoaDonAdapter.notifyDataSetChanged(); // Update RecyclerView
                } else {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Order>>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}