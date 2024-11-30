package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment.Fragment_ChoXacNhan;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment.Fragment_DaGiao;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment.Fragment_DaHuy;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment.Fragment_DangGiao;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ViewPage2_DonHang extends FragmentStateAdapter {

    private List<Order> dangGiaoOrders;
    private List<Order> choXacNhanOrders;

    public Adapter_ViewPage2_DonHang(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_ChoXacNhan(); // "Chờ xác nhận"
            case 1:
                return new Fragment_DangGiao(); // "Đang giao"
            case 2:
                return new Fragment_DaGiao(); // "Đã hoàn thành"
            case 3:
                return new Fragment_DaHuy(); // "Đã hủy"
            default:
                return new Fragment_ChoXacNhan(); // Default to "Chờ xác nhận"
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
