package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Review;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ViewHolder> {

    private Context context;
    private List<ProductItemCart> productItemList;
    private boolean isOrderDelivered;
    private ApiService apiService;
    private String email;

    public ThanhToanAdapter(Context context, List<ProductItemCart> productItemList, boolean isOrderDelivered) {
        this.context = context;
        this.productItemList = productItemList;
        this.isOrderDelivered = isOrderDelivered;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        this.email = sharedPreferences.getString("Tentaikhoan", "");
    }

    @NonNull
    @Override
    public ThanhToanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhToanAdapter.ViewHolder holder, int position) {
        ProductItemCart item = productItemList.get(position);
        holder.tvProductName.setText(item.getTenSP());
        holder.tvProductQuantity.setText(String.format("Số lượng: %d", item.getSoLuongGioHang()));
        holder.tvProductPrice.setText(String.format("$%.2f", item.getGia()));
        holder.tvProductSize.setText(String.format("Size: %d", item.getSize()));

        // Load ảnh sản phẩm
        if (item.getHinhAnh() != null && !item.getHinhAnh().isEmpty()) {
            String baseUrl = "http://160.191.50.148:3000/";
            String imageUrl = item.getHinhAnh().get(0).trim();

            // Kiểm tra URL đầy đủ hay tương đối
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                imageUrl = baseUrl + imageUrl;
            }

            Log.d("ThanhToanAdapter", "Full Image URL: " + imageUrl);

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.nice_shoe)
                    .error(R.drawable.errorr)
                    .into(holder.imgProduct);
        } else {
            Glide.with(context)
                    .load(R.drawable.errorr)
                    .into(holder.imgProduct);
        }

        // Xử lý hiển thị đánh giá
        if (isOrderDelivered) {
            holder.edDanhGia.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.btnDanhGia.setVisibility(View.GONE);
        } else {
            checkIfReviewed(item.getMaSanPham(), holder);

            holder.btnDanhGia.setOnClickListener(view -> {
                String reviewText = holder.edDanhGia.getText().toString().trim();
                float rating = holder.ratingBar.getRating();

                if (reviewText.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập nội dung đánh giá", Toast.LENGTH_SHORT).show();
                } else if (rating == 0) {
                    Toast.makeText(context, "Vui lòng chọn mức đánh giá", Toast.LENGTH_SHORT).show();
                } else {
                    Review review = new Review();
                    review.setBinhLuan(reviewText);
                    review.setDanhGia(rating);
                    review.setMaSanPham(item.getMaSanPham());
                    review.setTentaikhoan(email);

                    addReviewToApi(holder, review);
                }
            });
        }
    }

    private void checkIfReviewed(String productId, ViewHolder holder) {
        apiService.getReviewsForProductAndUser(productId, email).enqueue(new Callback<ReviewResponse<List<Review>>>() {
            @Override
            public void onResponse(Call<ReviewResponse<List<Review>>> call, Response<ReviewResponse<List<Review>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Review> reviews = response.body().getData();
                    if (reviews != null && !reviews.isEmpty()) {
                        holder.edDanhGia.setVisibility(View.GONE);
                        holder.ratingBar.setVisibility(View.GONE);
                        holder.btnDanhGia.setVisibility(View.GONE);
                    } else {
                        holder.edDanhGia.setVisibility(View.VISIBLE);
                        holder.ratingBar.setVisibility(View.VISIBLE);
                        holder.btnDanhGia.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("ThanhToanAdapter", "Failed to check review.");
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse<List<Review>>> call, Throwable t) {
                Log.e("ThanhToanAdapter", "Error checking review: " + t.getMessage());
            }
        });
    }

    private void addReviewToApi(ViewHolder holder, Review review) {
        apiService.addReview(email, review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    holder.edDanhGia.setVisibility(View.GONE);
                    holder.ratingBar.setVisibility(View.GONE);
                    holder.btnDanhGia.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "Đánh giá không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi gửi đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItemList != null ? productItemList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice, tvProductSize;
        ImageView imgProduct;
        EditText edDanhGia;
        Button btnDanhGia;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.NameProduct_dt);
            tvProductQuantity = itemView.findViewById(R.id.tvSL_dt);
            tvProductPrice = itemView.findViewById(R.id.priceOrder_dt);
            tvProductSize = itemView.findViewById(R.id.tvSize_dt);
            imgProduct = itemView.findViewById(R.id.img_Order_Dt);
            edDanhGia = itemView.findViewById(R.id.edDanhGia);
            btnDanhGia = itemView.findViewById(R.id.btnDanhGia);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
