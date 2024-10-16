package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.SanPhamAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initRecyclerview();
        

    }

    private void initRecyclerview() {
        ArrayList<SanPham> items = new ArrayList<>();
        items.add(new SanPham("Giày Nike Jordan","nike",1000));
        items.add(new SanPham("Giày Nike Jordan","nike2",2000));
        items.add(new SanPham("Giày Nike Jordan","nike3",3000));
        items.add(new SanPham("Giày Nike Jordan","nike4",2000));
        binding.SanPhamPhoBienView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.SanPhamPhoBienView.setAdapter(new SanPhamAdapter(items));
    }
}