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
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;

import java.util.ArrayList;
import java.util.List;

public class TopSellingAdapter extends RecyclerView.Adapter<TopSellingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> productArrayList;
    private static final String BASE_URL = "http://160.191.50.148:3000/";

    public TopSellingAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_selling, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.shoeName.setText(product.getName());
        holder.shoePrice.setText(String.valueOf(product.getPrice()) + "đ");
        holder.tvQuantitySold.setText("Đã bán: " + product.getQuantitySold());

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            String imageUrl = product.getImages().get(0);
            String[] imageUrls = imageUrl.split(",");  // Tách theo dấu phẩy

            String image = imageUrls[0].trim();  // Loại bỏ khoảng trắng dư thừa
            String finalImageUrl = BASE_URL + image;

            // Check if image URL is relative, if so, prepend the base URL
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                imageUrl = BASE_URL + imageUrl;
            }

            // Log the full image URL for debugging
            Log.d("Top10", "Full Image URL: " + finalImageUrl);

            // Use Glide to load the image into the ImageView
            Glide.with(context)
                    .load(finalImageUrl)
                    .placeholder(R.drawable.nice_shoe)  // Placeholder image while loading
                    .error(R.drawable.nike2)           // Error image if loading fails
                    .into(holder.shoeImage);          // Load the image into the ImageView
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Activity_ChiTietSP.class);
                in.putExtra("id", product.getProductId());
                in.putExtra("name", product.getName());
                in.putExtra("price", product.getPrice());
                in.putStringArrayListExtra("image", (ArrayList<String>) product.getImages());
                in.putExtra("description", product.getMoTa());

                Log.d("Top10", "Full ID: " + product.getProductId());
                Log.d("Top10", "Full Name URL: " + product.getName());
                Log.d("Top10", "Full hinh URL: " + product.getImages());
                Log.d("Top10", "Full MoTa URL: " + product.getMoTa());

                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView shoeImage;
        TextView shoeName, shoePrice, tvQuantitySold;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image_SPPB);
            shoeName = itemView.findViewById(R.id.shoe_name_SPPB);
            shoePrice = itemView.findViewById(R.id.shoe_price_SPPB);
            tvQuantitySold = itemView.findViewById(R.id.tvQuantitySold);
        }
    }
}
