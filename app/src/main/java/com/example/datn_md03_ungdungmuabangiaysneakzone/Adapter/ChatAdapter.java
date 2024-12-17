package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ChatMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ADMIN = 0;
    private static final int TYPE_CUSTOMER = 1;

    private List<ChatMessage> messages;
    private OnMessageDeleteListener deleteListener;

    public ChatAdapter(List<ChatMessage> messages, OnMessageDeleteListener deleteListener) {
        this.messages = messages;
        this.deleteListener = deleteListener;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isAdminMessage() ? TYPE_ADMIN : TYPE_CUSTOMER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADMIN) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item_chat_admin_nhan, parent, false);
            return new AdminViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item_chat_message_khachhang, parent, false);
            return new CustomerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof AdminViewHolder) {
            ((AdminViewHolder) holder).bind(message);
        } else if (holder instanceof CustomerViewHolder) {
            ((CustomerViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // Phương thức định dạng thời gian
    private String formatTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "Không xác định";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Không xác định"; // Trả về giá trị mặc định nếu có lỗi
        }
    }

    // ViewHolder cho Admin
    class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage, textViewTime;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessageReceived);
            textViewTime = itemView.findViewById(R.id.textViewTimeReceived);
        }

        void bind(ChatMessage message) {
            textViewMessage.setText(message.getMessage());
            textViewTime.setText(formatTimestamp(message.getTimestamp()));
        }
    }

    // ViewHolder cho Khách hàng
    class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage, textViewTime;
        ImageButton buttonDelete;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessageSent);
            textViewTime = itemView.findViewById(R.id.textViewTimeSent);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteMessage);
        }

        void bind(ChatMessage message) {
            textViewMessage.setText(message.getMessage());
            textViewTime.setText(formatTimestamp(message.getTimestamp()));
            buttonDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onMessageDelete(getAdapterPosition());
                }
            });
        }
    }

    // Interface xóa tin nhắn
    public interface OnMessageDeleteListener {
        void onMessageDelete(int position);
    }
}
