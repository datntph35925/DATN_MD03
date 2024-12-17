package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private String currentUserId;
    private EditText editTextMessage;
    private Button buttonSend;
    private ImageButton buttonBack;

    private Handler handler = new Handler();
    private Runnable fetchMessagesRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Lấy ID người dùng từ SharedPreferences
        currentUserId = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                .getString("Tentaikhoan", null);

        if (currentUserId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Liên kết các thành phần giao diện
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBack = findViewById(R.id.buttonBack);

        // Cấu hình RecyclerView
        chatAdapter = new ChatAdapter(chatMessages, position -> deleteMessage(position));
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        fetchChatMessages(); // Tải tin nhắn ban đầu

        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
            } else {
                Toast.makeText(this, "Tin nhắn không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(activity_chat.this, Activity_Profile.class);
            startActivity(intent);
            finish();
        });

        // Tải tin nhắn định kỳ
        fetchMessagesRunnable = new Runnable() {
            @Override
            public void run() {
                fetchChatMessages();
                handler.postDelayed(this, 1000);
            }
        };
        recyclerViewChat.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int rootViewHeight = recyclerViewChat.getRootView().getHeight();
            int visibleHeight = recyclerViewChat.getHeight();
            int heightDiff = rootViewHeight - visibleHeight;

            // Bàn phím được coi là xuất hiện khi chiều cao giảm > 200
            if (heightDiff > 200) {
                // Chỉ cuộn khi người dùng nhấn vào EditText
                if (editTextMessage.hasFocus()) {
                    recyclerViewChat.post(() -> {
                        if (chatMessages.size() > 0) {
                            recyclerViewChat.smoothScrollToPosition(chatMessages.size() - 1);
                        }
                    });
                }
            }
        });
        ConstraintLayout rootLayout = findViewById(R.id.rootLayout); // ID của root layout

//        rootLayout.setOnClickListener(v -> {
//            // Tắt focus khỏi EditText
//            editTextMessage.clearFocus();
//
//            // Ẩn bàn phím
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null) {
//                imm.hideSoftInputFromWindow(editTextMessage.getWindowToken(), 0);
//            }
//        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null && currentFocus instanceof EditText) {
                // Kiểm tra xem người dùng chạm bên ngoài EditText
                int[] location = new int[2];
                currentFocus.getLocationOnScreen(location);
                float x = event.getRawX() + currentFocus.getLeft() - location[0];
                float y = event.getRawY() + currentFocus.getTop() - location[1];

                if (x < currentFocus.getLeft() || x > currentFocus.getRight() ||
                        y < currentFocus.getTop() || y > currentFocus.getBottom()) {
                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                    currentFocus.clearFocus(); // Xóa focus khỏi EditText
                }
            }
        }
        return super.dispatchTouchEvent(event); // Tiếp tục xử lý các sự kiện khác
    }
    @Override
    protected void onStart() {
        super.onStart();
        handler.post(fetchMessagesRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(fetchMessagesRunnable);
    }

    private void fetchChatMessages() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getMessages(currentUserId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatMessages.clear();
                    chatMessages.addAll(response.body());
                    chatAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FETCH_MESSAGES", "Không có tin nhắn nào");
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
                    editTextMessage.setText("");
                    fetchChatMessages();
                } else {
                    Toast.makeText(activity_chat.this, "Gửi tin nhắn thất bại!", Toast.LENGTH_SHORT).show();
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
