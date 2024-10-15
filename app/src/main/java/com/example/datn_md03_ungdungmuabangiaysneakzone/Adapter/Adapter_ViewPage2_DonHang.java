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

public class Adapter_ViewPage2_DonHang extends FragmentStateAdapter {


    public Adapter_ViewPage2_DonHang(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new Fragment_ChoXacNhan();
                break;
            case 1:
                fragment = new Fragment_DangGiao();
                break;
            case 2:
                fragment = new Fragment_DaGiao();
                break;
            case 3:
                fragment = new Fragment_DaHuy();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
