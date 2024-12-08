package com.example.datn_md03_ungdungmuabangiaysneakzone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Thongbao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyForegroundService extends Service {

    private static final String CHANNEL_ID = "my_notification_channel";
    private String lastNotificationId = ""; // Lưu ID thông báo cuối cùng để kiểm tra
    private final Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();

        // Tạo kênh thông báo cho Foreground Service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Thông báo Foreground Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Hiển thị thông báo mặc định của Foreground Service
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Ứng dụng đang chạy")
                .setContentText("Dịch vụ Foreground đang hoạt động.")
                .setSmallIcon(R.drawable.bell)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);

        // Bắt đầu kiểm tra thông báo mới
        startRepeatingTask();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Nullable
    @Override
    public android.os.IBinder onBind(Intent intent) {
        return null; // Không cần giao tiếp với Activity
    }

    // Kiểm tra thông báo mới định kỳ
    private void startRepeatingTask() {
        runnable = new Runnable() {
            @Override
            public void run() {
                checkForNewNotifications(); // Kiểm tra thông báo mới
                handler.postDelayed(this, 1000); // Lặp lại sau 1 giây
            }
        };
        handler.post(runnable);
    }

    private void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }

    private void checkForNewNotifications() {
        // Lấy tài khoản từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("Tentaikhoan", "");

        if (email.isEmpty()) {
            return; // Không thực hiện nếu không có email
        }

        // Gọi API để kiểm tra thông báo mới
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Thongbao>> call = apiService.getNotifications(email);

        call.enqueue(new Callback<List<Thongbao>>() {
            @Override
            public void onResponse(Call<List<Thongbao>> call, Response<List<Thongbao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Thongbao> thongbaoList = response.body();

                    for (Thongbao thongbao : thongbaoList) {
                        // Hiển thị thông báo mới nếu chưa được hiển thị
                        if (!thongbao.isRead() && !thongbao.getId().equals(lastNotificationId)) {
                            showNotification(thongbao.getTitle(), thongbao.getMessage());
                            lastNotificationId = thongbao.getId(); // Lưu lại ID của thông báo
                            break; // Chỉ hiển thị một thông báo mới nhất
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Thongbao>> call, Throwable t) {
                // Xử lý lỗi (nếu cần)
            }
        });
    }

    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        // Sử dụng ID cố định cho mỗi thông báo mới
        notificationManager.notify(2, notification);
    }
}
