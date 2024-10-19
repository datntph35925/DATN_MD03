package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
import com.example.datn_md03_ungdungmuabangiaysneakzone.databinding.ViewholderPupListBinding;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.Viewholder> { // Đã sửa
    ArrayList<SanPham> items;
    Context context;
    ViewholderPupListBinding binding;

    public SanPhamAdapter(ArrayList<SanPham> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Đã sửa
        binding = ViewholderPupListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new Viewholder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {  // Đã sửa
        binding.txtTieuDe.setText(items.get(position).getTenSanPham());
        binding.txtGia.setText("" + items.get(position).getGia());

        int drawableResource = holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl()
                ,"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResource)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(binding.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {  // Đã sửa
        public Viewholder(@NonNull ViewholderPupListBinding binding) {
            super(binding.getRoot());
        }
    }
}
