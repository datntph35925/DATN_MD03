package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.List;

public class DanhGiaAdapter_Demo extends RecyclerView.Adapter<DanhGiaAdapter_Demo.DanhGiaViewHolder> {

    private List<DanhGia_Demo> danhGias;
    public DanhGiaAdapter_Demo(List<DanhGia_Demo> reviews) {
        this.danhGias = reviews;
    }

    public DanhGiaAdapter_Demo.DanhGiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhgia, parent, false);
        return new DanhGiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhGiaAdapter_Demo.DanhGiaViewHolder holder, int position) {
        DanhGia_Demo review = danhGias.get(position);
        holder.username.setText(review.getUsername());
        holder.comment.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return danhGias.size();
    }

    public static class DanhGiaViewHolder extends RecyclerView.ViewHolder {
        TextView username, comment;
        public DanhGiaViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
