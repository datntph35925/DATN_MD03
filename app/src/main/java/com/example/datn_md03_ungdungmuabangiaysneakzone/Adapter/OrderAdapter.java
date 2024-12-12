package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> hoaDonList;

    private OnItemClickListener onItemClickListener;
    private SimpleDateFormat dateFormat;

    public OrderAdapter(Context context, List<Order> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donhang, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order order = hoaDonList.get(position);
        holder.tvTenNguoiNhan.setText("Người nhận: " +  order.getTenNguoiNhan());
        holder.tvDiaChi.setText("Địa chỉ: " +order.getDiaChiGiaoHang());
        holder.tvSoDienThoai.setText("Số điện thoại: " + order.getSoDienThoai());
        if (order.getNgayDatHang() != null) {
            String formattedDate = dateFormat.format(order.getNgayDatHang());
            if (holder.tvDate != null) {
                holder.tvDate.setText(formattedDate);
            }
        }
        holder.tvTongTien.setText("Tổng tiền: " + String.format("%.2f", order.getTongTien()));

        holder.tvSoLuong.setText("Tổng số lượng: " + String.valueOf(order.getTongSoLuong()));
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenNguoiNhan, tvDiaChi, tvSoDienThoai, tvDate, tvTongTien, tvSoLuong;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvTenNguoiNhan = itemView.findViewById(R.id.tv_nameByer);
            tvDiaChi = itemView.findViewById(R.id.adresByer);
            tvSoDienThoai = itemView.findViewById(R.id.phoneByer);
            tvTongTien = itemView.findViewById(R.id.tvTotal);
            tvSoLuong = itemView.findViewById(R.id.soluong);
            tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }

}
