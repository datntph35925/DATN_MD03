package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.Activity_ChiTietSP;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Favorite;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final List<Favorite> favoriteList;
    private final Context context;
    private final String baseUrl = "http://160.191.50.148:3000/"; // Replace with your actual base URL

    // Constructor
    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spphobien, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Favorite favorite = favoriteList.get(position);
        Product product = favorite.getSanPham();

        holder.shoeName.setText(product.getTenSP());
        holder.shoePrice.setText(String.format("%,.0f đ", product.getGiaBan())); // Format price with thousand separator

        if (product.getHinhAnh() != null && !product.getHinhAnh().isEmpty()) {
            // Lấy URL ảnh đầu tiên trong danh sách
            String imageUrl = product.getHinhAnh().get(0);

            // Nếu URL là tương đối, ghép nó với địa chỉ base URL của server
            String baseUrl = "http://160.191.50.148:3000/"; // Thay thế bằng địa chỉ thực tế của server
            String fullImageUrl = baseUrl + imageUrl;  // Kết hợp URL server với URL ảnh

            // In ra log để kiểm tra URL
            Log.d("CartAdapter", "Full Image URL: " + fullImageUrl);

            // Sử dụng Glide để tải ảnh từ URL đầy đủ
            Glide.with(context)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.nice_shoe) // Ảnh placeholder khi chưa tải
                    .error(R.drawable.nike2) // Ảnh lỗi nếu không tải được
                    .into(holder.shoeImage); // Đưa ảnh vào ImageView
        }else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }

        // Set click listener for item
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, Activity_ChiTietSP.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getTenSP());
            intent.putExtra("price", product.getGiaBan());
            intent.putStringArrayListExtra("image", (ArrayList<String>) product.getHinhAnh());
            intent.putExtra("description", product.getMoTa());
            intent.putExtra("yeuthich", true); // Set true for favorites
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size() : 0;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
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