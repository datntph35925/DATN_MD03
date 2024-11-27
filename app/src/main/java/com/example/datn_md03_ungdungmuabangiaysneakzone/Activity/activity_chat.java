package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import android.os.Handler;

public class activity_chat extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private String currentUserId;
    private EditText editTextMessage;
    private Button buttonSend;

    private Handler handler = new Handler();  // Khai báo handler
    private Runnable fetchMessagesRunnable;   // Khai báo runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUserId = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                .getString("Tentaikhoan", null);

        if (currentUserId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        chatAdapter = new ChatAdapter(chatMessages, position -> deleteMessage(position));
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Lấy danh sách tin nhắn ngay khi vào Activity
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

        // Khởi tạo Runnable để gọi fetchChatMessages liên tục
        fetchMessagesRunnable = new Runnable() {
            @Override
            public void run() {
                fetchChatMessages();
                handler.postDelayed(this, 100); // Lặp lại sau 3 giây
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(fetchMessagesRunnable); // Bắt đầu vòng lặp khi Activity hiển thị
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(fetchMessagesRunnable); // Dừng vòng lặp khi Activity không còn hiển thị
    }

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

    private void sendMessage(String content) {
        ApiService apiService = RetrofitClient.getApiService();

        Map<String, String> messageData = new HashMap<>();
        messageData.put("TentaiKhoan", currentUserId);
        messageData.put("message", content);

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

    private void deleteMessage(int position) {
        ChatMessage message = chatMessages.get(position);

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
