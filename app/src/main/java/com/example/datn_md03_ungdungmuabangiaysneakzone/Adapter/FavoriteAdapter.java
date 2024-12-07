package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.Activity_ChiTietSP;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Favorite;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductYeuThich;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favoriteList;
    private Context context;

    // Constructor
    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }
    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spphobien, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Favorite favorite = favoriteList.get(position);
        Product product = favorite.getSanPham();

        holder.shoeName.setText(product.getTenSP());
        holder.shoePrice.setText(String.valueOf(product.getGiaBan()) + "đ");
        // Load image using Glide (assuming HinhAnh is a URL)
        if (product.getHinhAnh() != null && !product.getHinhAnh().isEmpty()) {
            Glide.with(context)
                    .load(product.getHinhAnh().get(0))  // Load the first image if available
                    .into(holder.shoeImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Activity_ChiTietSP.class);
                in.putExtra("id", product.getId());
                in.putExtra("name", product.getTenSP());
                in.putExtra("price", product.getGiaBan());
                in.putExtra("image", product.getHinhAnh().get(0));
                in.putExtra("description", product.getMoTa());
                in.putExtra("yeuthich", false);
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size(): 0;
    }


    public class FavoriteViewHolder extends RecyclerView.ViewHolder{
        ImageView shoeImage;
        TextView shoeName, shoePrice;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image_SPPB);
            shoeName = itemView.findViewById(R.id.shoe_name_SPPB);
            shoePrice = itemView.findViewById(R.id.shoe_price_SPPB);
        }
    }
}
