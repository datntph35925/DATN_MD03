package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.List;

public class NotificationAdapter_Demo extends RecyclerView.Adapter<NotificationAdapter_Demo.NotificationViewHolder> {
    private List<NotificationItem_Demo> notificationList;

    public NotificationAdapter_Demo(List<NotificationItem_Demo> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter_Demo.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter_Demo.NotificationViewHolder holder, int position) {
        NotificationItem_Demo notification = notificationList.get(position);
        holder.notificationTitle.setText(notification.getTitle());
        holder.notificationPrice.setText(notification.getPrice());
        holder.notificationTime.setText(notification.getTime());
        holder.notificationImage.setImageResource(notification.getImageResId());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView notificationTitle, notificationPrice, notificationTime;
        ImageView notificationImage;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.tv_notification_title);
            notificationPrice = itemView.findViewById(R.id.tv_notification_price);
            notificationTime = itemView.findViewById(R.id.tv_notification_time);
            notificationImage = itemView.findViewById(R.id.img_notification_product);
        }
    }
}
