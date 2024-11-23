package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.KichThuoc;

import java.util.ArrayList;
import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {
    private Context context;
    private ArrayList<KichThuoc> kichThuocArrayList;
    private int selectedPosition = -1; // Theo dõi vị trí đã chọn
    private OnSizeClickListener listener;


    public ArrayList<KichThuoc> getKichThuocArrayList() {
        return kichThuocArrayList;
    }

    SizeAdapterListener sizeAdapterListener;

    public SizeAdapter(SizeAdapterListener sizeAdapterListener) {
        this.sizeAdapterListener = sizeAdapterListener;
    }

    public SizeAdapterListener getSizeAdapterListener() {
        return sizeAdapterListener;
    }

    public void setSizeAdapterListener(SizeAdapterListener sizeAdapterListener) {
        this.sizeAdapterListener = sizeAdapterListener;
    }

    public interface OnSizeClickListener {
        void onSizeClick(KichThuoc kichThuoc);
    }

    public SizeAdapter(Context context, ArrayList<KichThuoc> kichThuocArrayList) {
        this.context = context;
        this.kichThuocArrayList = kichThuocArrayList;
    }


    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_size, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        KichThuoc kichThuoc = kichThuocArrayList.get(position);
        holder.btnSize.setText(String.valueOf(kichThuoc.getSize()));

        // Thay đổi giao diện nút bấm khi được chọn
        holder.btnSize.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            // Notify listener of the selected size
            if (sizeAdapterListener != null) {
                sizeAdapterListener.onSizeSelected(kichThuoc); // Pass the selected size
            }
        });

        // Thay đổi giao diện nếu kích thước được chọn
        if (position == selectedPosition) {
            holder.btnSize.setBackgroundResource(R.drawable.btn_shop_click);
        } else {
            holder.btnSize.setBackgroundResource(R.drawable.btn_shop);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSizeClick(kichThuoc); // Gọi listener khi kích thước được chọn
            }
        });
    }

    @Override
    public int getItemCount() {
        return kichThuocArrayList.size();
    }

    public static class SizeViewHolder extends RecyclerView.ViewHolder {
        Button btnSize;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSize = itemView.findViewById(R.id.sizeTextView);
        }
    }

    public interface SizeAdapterListener {
        void onSizeSelected(KichThuoc kichThuoc);
    }


}
