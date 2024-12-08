package com.example.datn_md03_ungdungmuabangiaysneakzone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.MainActivity;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.NotificationAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Thongbao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongbaodsActivity extends AppCompatActivity {

    private ImageButton btnExit;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Thongbao> thongbaoList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String email;
    private Handler handler;
    private Runnable refreshRunnable;

    // Thời gian tải lại dữ liệu (mili giây)
    private static final long REFRESH_INTERVAL = 1000; //

    // Khai báo CHANNEL_ID cho kênh thông báo
    public static final String CHANNEL_ID = "my_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.thongbaods);

        // Áp dụng insets cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", "");

        if (email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra quyền POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // Lấy các view cần thiết
        btnExit = findViewById(R.id.btnExit);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter với listener
        notificationAdapter = new NotificationAdapter(thongbaoList, (thongbao, position) -> {
            if (thongbao != null) {
                Log.d("ThongbaodsActivity", "Clicked on notification: " + thongbao.getTitle());
                markAsRead(thongbao.getId(), position);
            }
        });

        // Long-click listener để xóa thông báo
        notificationAdapter.setOnNotificationLongClickListener((notificationId, position) -> {
            if (notificationId != null && !notificationId.isEmpty()) {
                Log.d("ThongbaodsActivity", "Xóa thông báo với ID: " + notificationId);
                deleteNotification(notificationId, position);
            } else {
                Toast.makeText(ThongbaodsActivity.this, "Không thể xóa thông báo: ID không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        btnExit.setOnClickListener(view -> {
            Intent intent = new Intent(ThongbaodsActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đảm bảo không quay lại màn hình đăng nhập
        });

        recyclerView.setAdapter(notificationAdapter);

        // Cấu hình SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> loadNotifications(email));

        // Gọi API để tải thông báo ban đầu
        loadNotifications(email);

        // Khởi động Handler để tải dữ liệu định kỳ
        handler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                loadNotifications(email); // Gọi API tải dữ liệu
                handler.postDelayed(this, REFRESH_INTERVAL); // Lặp lại sau REFRESH_INTERVAL
            }
        };
        handler.post(refreshRunnable); // Bắt đầu tải dữ liệu
    }

    private void loadNotifications(String email) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Thongbao>> call = apiService.getNotifications(email);
        call.enqueue(new Callback<List<Thongbao>>() {
            @Override
            public void onResponse(Call<List<Thongbao>> call, Response<List<Thongbao>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    thongbaoList = response.body();
                    notificationAdapter.updateNotifications(thongbaoList);

                    // Hiển thị thông báo mới
                    for (Thongbao thongbao : thongbaoList) {
                        if (!thongbao.isRead()) {
                            showNotification(thongbao);
                        }
                    }
                } else {
                    Toast.makeText(ThongbaodsActivity.this, "Lỗi khi tải thông báo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Thongbao>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markAsRead(String id, int position) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.markNotificationAsRead(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    thongbaoList.get(position).setRead(true);
                    notificationAdapter.notifyItemChanged(position);
                    Toast.makeText(ThongbaodsActivity.this, "Thông báo đã được đánh dấu là đã đọc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteNotification(String id, int position) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.deleteNotification(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    thongbaoList.remove(position);
                    notificationAdapter.notifyItemRemoved(position);
                    Toast.makeText(ThongbaodsActivity.this, "Thông báo đã được xóa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotification(Thongbao thongbao) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (existingChannel == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Thông báo",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Thông báo từ ứng dụng");
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell) // Đảm bảo rằng đây là drawable hợp lệ
                .setContentTitle(thongbao.getTitle())
                .setContentText(thongbao.getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ngừng Handler khi Activity bị hủy
        if (handler != null) {
            handler.removeCallbacks(refreshRunnable);
        }
    }
}
