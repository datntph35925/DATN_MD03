package com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.Activity_CTDH_ChoXacNhan;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.OrderAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.HoaDonAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.HoaDon_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Fragment_ChoXacNhan extends Fragment {

    public Fragment_ChoXacNhan() {
        // Required empty public constructor
    }

    private RecyclerView rcvWait;
    private OrderAdapter hoaDonAdapter;
    private List<Order> hoaDonList;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cho_xac_nhan, container, false);
        // Inflate the layout for this fragment
        rcvWait = view.findViewById(R.id.rcv_wait);
        rcvWait.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the email

        hoaDonList = new ArrayList<>();
        hoaDonAdapter = new OrderAdapter(getContext(), hoaDonList);
        rcvWait.setAdapter(hoaDonAdapter);

        hoaDonAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               Order order = hoaDonList.get(position);


               Intent intent = new Intent(getContext(), Activity_CTDH_ChoXacNhan.class);
               intent.putExtra("order_id", order.getId());
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

        fetchOrder();

        return view;
    }

    private void fetchOrder(){
        RetrofitClient.getApiService().getOrderById(email).enqueue(new Callback<Response<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Order>>> call, retrofit2.Response<Response<ArrayList<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().getData();
                    hoaDonList.clear();  // Clear current list

                    // Add only orders with "Chờ xử lý" status
                    for (Order order : orders) {
                        if ("Chờ xử lý".equals(order.getTrangThai())) {
                            hoaDonList.add(order);
                        }
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