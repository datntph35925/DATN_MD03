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
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Review;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ReviewResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ViewHolder> {

    private Context context;
    private List<ProductItemCart> productItemList;

    private boolean isOrderDelivered;

    ApiService apiService;
    String email;

    private Map<String, Boolean> reviewedProducts = new HashMap<>();

    public ThanhToanAdapter(Context context, List<ProductItemCart> productItemList, boolean isOrderDelivered) {
        this.context = context;
        this.productItemList = productItemList;
        this.isOrderDelivered = isOrderDelivered;
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

        Glide.with(context).load(item.getHinhAnh().get(0)).into(holder.imgProduct);

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", ""); // Retrieve the emai

        // Sử dụng Handler để đảm bảo kiểm tra đánh giá sau khi email đã sẵn sàng

        if (isOrderDelivered) {
            holder.edDanhGia.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.btnDanhGia.setVisibility(View.GONE);// Hoặc View.INVISIBLE
        } else {
            holder.edDanhGia.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.btnDanhGia.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                checkIfReviewed(item.getMaSanPham(), holder);
            }, 1000);  // Delay một chút để đảm bảo email đã sẵn sàng


            holder.btnDanhGia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reviewText = holder.edDanhGia.getText().toString().trim();
                    float rating = holder.ratingBar.getRating();

                    if (reviewText.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập nội dung đánh giá", Toast.LENGTH_SHORT).show();
                    } else if (rating == 0) {
                        Toast.makeText(context, "Vui lòng chọn mức đánh giá", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tạo đối tượng Review
                        Review review = new Review();
                        review.setBinhLuan(reviewText);
                        review.setDanhGia(rating);
                        review.setMaSanPham(item.getMaSanPham()); // Giả sử bạn có mã sản phẩm trong ProductItemCart
                        review.setTentaikhoan(email); // Thay thế bằng tên tài khoản người dùng hiện tại

                        addReviewToApi(holder,review);

                    }
                }
            });
        }
    }

    private void checkIfReviewed(String productId, ViewHolder holder) {
        apiService = RetrofitClient.getClient().create(ApiService.class);// Khởi tạo service API

         apiService.getReviewsForProductAndUser(productId, email).enqueue(new Callback<ReviewResponse<List<Review>>>() {
             @Override
             public void onResponse(Call<ReviewResponse<List<Review>>> call, Response<ReviewResponse<List<Review>>> response) {
                 if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                     ReviewResponse<List<Review>> apiResponse = response.body();
                     List<Review> reviews = apiResponse.getData();

                     if (reviews != null && !reviews.isEmpty()) {
                         holder.edDanhGia.setVisibility(View.GONE);
                         holder.ratingBar.setVisibility(View.GONE);
                         holder.btnDanhGia.setVisibility(View.GONE);
                         Toast.makeText(context, "Bạn đã đánh giá sản phẩm này trước đó", Toast.LENGTH_SHORT).show();
                     } else {
                         holder.edDanhGia.setVisibility(View.VISIBLE);
                         holder.ratingBar.setVisibility(View.VISIBLE);
                         holder.btnDanhGia.setVisibility(View.VISIBLE);
                     }
                 } else {
                     Toast.makeText(context, "Lỗi khi kiểm tra đánh giá, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<ReviewResponse<List<Review>>> call, Throwable t) {
                 Toast.makeText(context, "Lỗi kết nối mạng. Không thể kiểm tra đánh giá.", Toast.LENGTH_SHORT).show();
                 Log.e("ThanhToanAdapter", "Error checking review: " + t.getMessage());
             }
         });
    }

    private void addReviewToApi(ViewHolder holder, Review review) {
        apiService.addReview(email,review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {
                    // Cập nhật trạng thái đã đánh giá cho sản phẩm
                    Toast.makeText(context, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    holder.edDanhGia.setVisibility(View.GONE);
                    holder.ratingBar.setVisibility(View.GONE);
                    holder.btnDanhGia.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "Đánh giá không thành công. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi gửi đánh giá", Toast.LENGTH_SHORT).show();
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
