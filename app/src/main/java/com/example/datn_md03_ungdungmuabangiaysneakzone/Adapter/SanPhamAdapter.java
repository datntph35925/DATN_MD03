package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.SP_PhoBienAdapterDemo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spphobien, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {  // Đã sửa
        SanPham shoe = items.get(position);
        holder.shoeName.setText(shoe.getTenSanPham());
        holder.shoePrice.setText("$" + shoe.getGia());
        holder.shoeImage.setImageResource(shoe.getPicUrl());

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
        ImageView shoeImage;
        TextView shoeName, shoePrice;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image_SPPB);
            shoeName = itemView.findViewById(R.id.shoe_name_SPPB);
            shoePrice = itemView.findViewById(R.id.shoe_price_SPPB);
        }
    }
}
