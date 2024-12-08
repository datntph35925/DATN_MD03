package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.DanhGiaAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviewsList;
    private Context context;
    private SimpleDateFormat dateFormat;
    public ReviewAdapter(ArrayList<Review> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhgia, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = reviewsList.get(position);

        holder.username.setText(review.getHoten());
        holder.comment.setText(review.getBinhLuan());
        holder.ratingBar.setRating(review.getDanhGia());
        // Set date
        // Chuyển đổi thời gian theo định dạng
        if (review.getTimestamp() != null) {
            String formattedDate = dateFormat.format(review.getTimestamp());
            if (holder.ngayThang != null) {
                holder.ngayThang.setText(formattedDate);
            }
        }

        // Kiểm tra URL của ảnh đại diện
        if (review.getAnhDaiDien() != null && !review.getAnhDaiDien().isEmpty()) {
            // Tạo URL đầy đủ nếu cần (giả sử ảnh lưu trên server nội bộ)
            String imageUrl = review.getAnhDaiDien().startsWith("http")
                    ? review.getAnhDaiDien()
                    : "http://10.0.2.2:3000/" + review.getAnhDaiDien();

            // Tải ảnh bằng Glide
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl) // URL hoặc đường dẫn ảnh
                    .placeholder(R.drawable.btn_4) // Hình mặc định khi tải
                    .into(holder.avatar);
        } else {
            // Nếu không có URL ảnh, hiển thị ảnh mặc định
            holder.avatar.setImageResource(R.drawable.btn_4);
        }
    }


    @Override
    public int getItemCount() {
        return reviewsList != null ? reviewsList.size() : 0;
      // return reviewsList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView username, comment, ngayThang;
        RatingBar ratingBar;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgAvatar);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            ngayThang = itemView.findViewById(R.id.NgayThang);
            ratingBar = itemView.findViewById(R.id.start);
        }
    }
}
