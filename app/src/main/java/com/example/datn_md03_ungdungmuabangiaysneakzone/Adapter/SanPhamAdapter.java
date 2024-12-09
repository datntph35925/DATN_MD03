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
            holder.shoePrice.setText(String.valueOf(product.getGiaBan()) + "đ");

            if (product.getHinhAnh() != null && !product.getHinhAnh().isEmpty()) {
                // Lấy URL ảnh đầu tiên trong danh sách
                String imageUrl = product.getHinhAnh().get(0);

                // Nếu URL là tương đối, ghép nó với địa chỉ base URL của server
                String baseUrl = "http://10.0.2.2:3000/"; // Thay thế bằng địa chỉ thực tế của server
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
                }
            });
        }

        @Override
        public int getItemCount() {
            return productArrayList.size();
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
