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

import java.util.List;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_chat_admin_nhan, parent, false);
            return new AdminViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_chat_message_khachhang, parent, false);
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

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessageReceived);
        }

        void bind(ChatMessage message) {
            textViewMessage.setText(message.getMessage());
        }
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        ImageButton buttonDelete;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessageSent);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteMessage);
        }

        void bind(ChatMessage message) {
            textViewMessage.setText(message.getMessage());
            buttonDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onMessageDelete(getAdapterPosition());
                }
            });
        }
    }

    public interface OnMessageDeleteListener {
        void onMessageDelete(int position);
    }
}
