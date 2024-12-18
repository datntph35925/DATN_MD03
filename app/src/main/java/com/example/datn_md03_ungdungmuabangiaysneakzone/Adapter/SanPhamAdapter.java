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
    import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.Activity_ChiTietSP;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.SP_PhoBienAdapterDemo;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Favorite;
    import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.List;

    public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.Viewholder> { // Đã sửa
        Context context;
        ArrayList<Product> productArrayList;
        private static final String BASE_URL = "http://160.191.50.148:3000/";

        public SanPhamAdapter(Context context, ArrayList<Product> productArrayList) {
            this.context = context;
            this.productArrayList = productArrayList;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Đã sửa
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spphobien, parent, false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {  // Đã sửa
            Product product = productArrayList.get(position);
            holder.shoeName.setText(product.getTenSP());
            holder.shoePrice.setText(String.valueOf(product.getGiaBan()) + "VNĐ");

            if (product.getHinhAnh() != null && !product.getHinhAnh().isEmpty()) {
                String imageUrl = product.getHinhAnh().get(0).trim();

                // Check if image URL is relative, if so, prepend the base URL
                if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                    imageUrl = BASE_URL + imageUrl;
                }

                // Log the full image URL for debugging
                Log.d("SanPhamAdapter", "Full Image URL: " + imageUrl);

                // Use Glide to load the image into the ImageView
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.nice_shoe)  // Placeholder image while loading
                        .error(R.drawable.nike2)           // Error image if loading fails
                        .into(holder.shoeImage);          // Load the image into the ImageView
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context, Activity_ChiTietSP.class);
                    in.putExtra("id", product.getId());
                    in.putExtra("name", product.getTenSP());
                    in.putExtra("price", product.getGiaBan());
                    in.putStringArrayListExtra("image", (ArrayList<String>) product.getHinhAnh());
                    in.putExtra("description", product.getMoTa());
                    in.putExtra("yeuthich", false);
                    context.startActivity(in);
                    Log.d("SanPhamAdapter", "Full Image URL: " + product.getHinhAnh());
                }
            });
        }

        @Override
        public int getItemCount() {
            return productArrayList != null ? productArrayList.size():0;
        }

        public static class Viewholder extends RecyclerView.ViewHolder {  // Đã sửa
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
