package com.example.datn_md03_ungdungmuabangiaysneakzone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Activity.MainActivity;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.NotificationAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.RetrofitClient;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Thongbao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final Set<String> shownNotificationIds = new HashSet<>(); // Lưu ID thông báo đã hiển thị
    private static final long REFRESH_INTERVAL = 1000; // Thời gian tải lại dữ liệu
    public static final String CHANNEL_ID = "my_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongbaods);

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("Tentaikhoan", "");

        if (email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ánh xạ view
        btnExit = findViewById(R.id.btnExit);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(thongbaoList, (thongbao, position) -> {
            if (thongbao != null) {
                markAsRead(thongbao.getId(), position);
            }
        });

        // Xử lý sự kiện long click để xóa thông báo
//        notificationAdapter.setOnNotificationLongClickListener((notificationId, position) -> {
//            if (notificationId != null && !notificationId.isEmpty()) {
//                deleteNotification(notificationId, position);
//            } else {
//                Toast.makeText(this, "ID thông báo không hợp lệ!", Toast.LENGTH_SHORT).show();
//            }
//        });

        btnExit.setOnClickListener(view -> {
            Intent intent = new Intent(ThongbaodsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        recyclerView.setAdapter(notificationAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> loadNotifications(email));
        loadNotifications(email);

        // Tải lại dữ liệu định kỳ
        handler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                checkForNewNotifications();
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        };
        handler.post(refreshRunnable);
    }

    // Tải thông báo cho RecyclerView
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

    // Hiển thị thông báo trên thanh thông báo
    private void showNotification(Thongbao thongbao) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String deletedIdsString = sharedPreferences.getString("DeletedNotificationIds", "");
        Set<String> deletedIds = new HashSet<>(Arrays.asList(deletedIdsString.split(",")));

        // Kiểm tra nếu thông báo đã bị xóa, không hiển thị lại
        if (deletedIds.contains(thongbao.getId())) {
            return; // Không hiển thị thông báo đã bị xóa
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Thông báo",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(thongbao.getTitle())
                .setContentText(thongbao.getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(thongbao.getId().hashCode(), notification);

        // Lưu lại thông báo đã hiển thị
        shownNotificationIds.add(thongbao.getId());
    }

    // Lưu thông báo đã bị xóa vào SharedPreferences
    private void saveDeletedNotificationId(String notificationId) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String deletedIdsString = sharedPreferences.getString("DeletedNotificationIds", "");
        if (!deletedIdsString.contains(notificationId)) {
            deletedIdsString = deletedIdsString.isEmpty() ? notificationId : deletedIdsString + "," + notificationId;
        }

        editor.putString("DeletedNotificationIds", deletedIdsString);
        editor.apply();
    }

    // Kiểm tra thông báo mới trên API
    private void checkForNewNotifications() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("Tentaikhoan", "");

        if (email.isEmpty()) {
            return;
        }

        String deletedIdsString = sharedPreferences.getString("DeletedNotificationIds", "");
        List<String> deletedIds = new ArrayList<>(Arrays.asList(deletedIdsString.split(",")));

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Thongbao>> call = apiService.getNotifications(email);

        call.enqueue(new Callback<List<Thongbao>>() {
            @Override
            public void onResponse(Call<List<Thongbao>> call, Response<List<Thongbao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Thongbao> thongbaoList = response.body();

                    for (Thongbao thongbao : thongbaoList) {
                        if (deletedIds.contains(thongbao.getId())) {
                            continue;
                        }

                        if (!thongbao.isRead() && !shownNotificationIds.contains(thongbao.getId())) {
                            showNotification(thongbao);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Thongbao>> call, Throwable t) {
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
                    shownNotificationIds.remove(id);
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

        Call<Void> call = apiService.deleteNotification
                (id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    thongbaoList.remove(position);
                    notificationAdapter.notifyItemRemoved(position);
                    shownNotificationIds.remove(id);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(refreshRunnable);
        }
    }
    @Override
    public void onBackPressed() {
        // Ngăn không cho quay lại màn hình trước
    }
}
