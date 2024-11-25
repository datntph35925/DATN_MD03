package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.ChatAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_chat extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private String currentUserId; // Tên tài khoản người dùng hiện tại
    private EditText editTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Lấy tài khoản từ SharedPreferences
        currentUserId = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                .getString("Tentaikhoan", null);

        if (currentUserId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ view
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // Thiết lập RecyclerView
        chatAdapter = new ChatAdapter(chatMessages, position -> deleteMessage(position));
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Lấy danh sách tin nhắn
        fetchChatMessages();

        // Xử lý nút gửi tin nhắn
        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
            } else {
                Toast.makeText(this, "Tin nhắn không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Lấy danh sách tin nhắn từ server.
     */
    private void fetchChatMessages() {
        ApiService apiService = RetrofitClient.getApiService();

        Log.d("DEBUG", "Fetching messages for TentaiKhoan: " + currentUserId);

        apiService.getMessages(currentUserId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatMessages.clear();
                    chatMessages.addAll(response.body());
                    chatAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(activity_chat.this, "Không có tin nhắn nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Toast.makeText(activity_chat.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Gửi tin nhắn tới server.
     */
    private void sendMessage(String content) {
        ApiService apiService = RetrofitClient.getApiService();

        // Tạo Map chứa dữ liệu tin nhắn
        Map<String, String> messageData = new HashMap<>();
        messageData.put("TentaiKhoan", currentUserId); // Gán tài khoản hiện tại làm người gửi
        messageData.put("message", content); // Gán nội dung tin nhắn

        // Gửi tin nhắn qua API
        apiService.sendMessage(messageData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    editTextMessage.setText(""); // Xóa nội dung đã nhập
                    fetchChatMessages(); // Tải lại danh sách tin nhắn
                } else {
                    Toast.makeText(activity_chat.this, "Không thể gửi tin nhắn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity_chat.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Xóa tin nhắn.
     */
    private void deleteMessage(int position) {
        ChatMessage message = chatMessages.get(position);

        // Debug log để kiểm tra giá trị
        Log.d("DEBUG", "Message ID: " + message.getId());
        Log.d("DEBUG", "TentaiKhoan: " + currentUserId);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.deleteMessage(message.getId(), currentUserId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    chatMessages.remove(position);
                    chatAdapter.notifyItemRemoved(position);
                    Toast.makeText(activity_chat.this, "Đã xóa tin nhắn!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity_chat.this, "Không thể xóa tin nhắn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity_chat.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
