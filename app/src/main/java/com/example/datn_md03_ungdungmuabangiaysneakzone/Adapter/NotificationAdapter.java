package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Thongbao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Thongbao> notificationList;
    private SimpleDateFormat dateFormat;
    private OnNotificationClickListener clickListener;
    private OnNotificationLongClickListener longClickListener; // Thêm listener cho long click

    // Constructor
    public NotificationAdapter(List<Thongbao> notificationList, OnNotificationClickListener clickListener) {
        this.notificationList = notificationList != null ? notificationList : new ArrayList<>();
        this.clickListener = clickListener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    // Setter cho long click listener
    public void setOnNotificationLongClickListener(OnNotificationLongClickListener longClickListener) {
        this.longClickListener = longClickListener; // Gán listener vào biến longClickListener
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_notification
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Thongbao thongbao = notificationList.get(position);

        if (thongbao != null) {
            if (holder.tvTitle != null && thongbao.getTitle() != null) {
                holder.tvTitle.setText(thongbao.getTitle());
            }

            if (holder.tvMessage != null && thongbao.getMessage() != null) {
                holder.tvMessage.setText(thongbao.getMessage());
            }

            // Chuyển đổi thời gian theo định dạng
            if (thongbao.getTimestamp() != null) {
                String formattedDate = dateFormat.format(thongbao.getTimestamp());
                if (holder.tvTimestamp != null) {
                    holder.tvTimestamp.setText(formattedDate);
                }
            }

            // Kiểm tra trạng thái "đọc" và thay đổi màu nền
            if (thongbao.isRead()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3")); // Màu xám cho đã đọc
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF")); // Màu trắng cho chưa đọc
            }

            // Lắng nghe sự kiện click vào thông báo
            holder.itemView.setOnClickListener(v -> {
                if (!thongbao.isRead()) {
                    thongbao.setRead(true); // Đánh dấu đã đọc
                    clickListener.onNotificationClick(thongbao, position); // Gọi listener
                    notifyItemChanged(position); // Cập nhật RecyclerView
                }
            });

            // Lắng nghe sự kiện long click để xóa thông báo
            holder.itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    Log.d("NotificationAdapter", "Long clicked notification ID: " + thongbao.getId());
                    longClickListener.onLongClickNotification(thongbao.getId(), position); // Gọi phương thức long-click
                }
                return true; // Đánh dấu sự kiện đã được xử lý
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public void updateNotifications(List<Thongbao> newNotifications) {
        if (newNotifications != null) {
            notificationList.clear();
            notificationList.addAll(newNotifications);
            notifyDataSetChanged();
        }
    }

    // ViewHolder cho RecyclerView
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvTimestamp;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }

    // Interface để lắng nghe sự kiện click và long click
    public interface OnNotificationClickListener {
        void onNotificationClick(Thongbao thongbao, int position);
    }

    public interface OnNotificationLongClickListener {
        void onLongClickNotification(String notificationId, int position); // Phương thức để xóa thông báo
    }
}
