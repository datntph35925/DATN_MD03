package com.example.datn_md03_ungdungmuabangiaysneakzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Thongbao> thongbaoList = new ArrayList<>(); // Khởi tạo danh sách rỗng
    private SwipeRefreshLayout swipeRefreshLayout; // Khai báo SwipeRefreshLayout
    private String email; // Biến để lưu email

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
        email = sharedPreferences.getString("Tentaikhoan", ""); // Lấy email từ SharedPreferences

        if (email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy các view cần thiết
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);  // Khởi tạo SwipeRefreshLayout

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter với listener
        notificationAdapter = new NotificationAdapter(thongbaoList, new NotificationAdapter.OnNotificationClickListener() {
            @Override
            public void onNotificationClick(Thongbao thongbao, int position) {
                if (thongbao != null) {
                    // Xử lý khi người dùng click vào thông báo
                    Log.d("ThongbaodsActivity", "Clicked on notification: " + thongbao.getTitle());
                    markAsRead(thongbao.getId(), position);
                }
            }
        });

// Gọi phương thức setOnNotificationLongClickListener để xử lý sự kiện long-click
        notificationAdapter.setOnNotificationLongClickListener(new NotificationAdapter.OnNotificationLongClickListener() {
            @Override
            public void onLongClickNotification(String notificationId, int position) {
                if (notificationId != null && !notificationId.isEmpty()) {
                    // Xử lý khi người dùng long-click thông báo
                    Log.d("ThongbaodsActivity", "Long clicked notification with ID: " + notificationId);
                    deleteNotification(notificationId, position);
                }
            }
        });



        recyclerView.setAdapter(notificationAdapter);

        // Cấu hình sự kiện LongClick để xóa thông báo
        notificationAdapter.setOnNotificationLongClickListener((notificationId, position) -> {
            if (notificationId != null && !notificationId.isEmpty()) {
                Log.d("ThongbaodsActivity", "Xóa thông báo với ID: " + notificationId);
                deleteNotification(notificationId, position);
            } else {
                Log.e("ThongbaodsActivity", "ID thông báo không hợp lệ!");
                Toast.makeText(ThongbaodsActivity.this, "Không thể xóa thông báo: ID không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        // Gọi API để tải thông báo
        loadNotifications(email);

        // Cấu hình SwipeRefreshLayout để tải lại dữ liệu khi người dùng kéo xuống
        swipeRefreshLayout.setOnRefreshListener(() -> loadNotifications(email)); // Tải lại dữ liệu khi kéo xuống
    }

    // Phương thức gọi API để tải thông báo
    private void loadNotifications(String tentaikhoan) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Gửi yêu cầu GET đến API
        Call<List<Thongbao>> call = apiService.getNotifications(tentaikhoan);

        // Xử lý kết quả trả về từ API
        call.enqueue(new Callback<List<Thongbao>>() {
            @Override
            public void onResponse(Call<List<Thongbao>> call, Response<List<Thongbao>> response) {
                // Dừng animation của SwipeRefreshLayout khi dữ liệu đã được tải
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    thongbaoList = response.body(); // Cập nhật danh sách thông báo
                    Log.d("ThongbaodsActivity", "Danh sách thông báo đã được tải: " + thongbaoList.size());
                    notificationAdapter.updateNotifications(thongbaoList); // Cập nhật dữ liệu trong adapter
                } else {
                    Toast.makeText(ThongbaodsActivity.this, "Lỗi khi tải thông báo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Thongbao>> call, Throwable t) {
                // Dừng animation của SwipeRefreshLayout khi xảy ra lỗi
                swipeRefreshLayout.setRefreshing(false);

                // Hiển thị thông báo lỗi
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }

    // Phương thức để đánh dấu thông báo là đã đọc
    private void markAsRead(String id, int position) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Gửi yêu cầu PUT để đánh dấu thông báo là đã đọc
        Call<Void> call = apiService.markNotificationAsRead(id);

        // Xử lý kết quả trả về từ API
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật trạng thái thông báo là đã đọc
                    thongbaoList.get(position).setRead(true);  // Cập nhật trạng thái trong danh sách
                    notificationAdapter.notifyItemChanged(position); // Cập nhật RecyclerView
                    Log.d("ThongbaodsActivity", "Thông báo đã được đánh dấu là đã đọc: ID = " + id);
                    Toast.makeText(ThongbaodsActivity.this, "Thông báo đã được đánh dấu là đã đọc", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ThongbaodsActivity", "Lỗi khi đánh dấu thông báo là đã đọc: " + response.code());
                    Toast.makeText(ThongbaodsActivity.this, "Lỗi khi đánh dấu thông báo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ThongbaodsActivity", "Lỗi kết nối khi đánh dấu thông báo là đã đọc: " + t.getMessage());
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức để xóa thông báo
    private void deleteNotification(String id, int position) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Gửi yêu cầu DELETE đến API
        Call<Void> call = apiService.deleteNotification(id);

        // Xử lý kết quả trả về từ API
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa thông báo khỏi danh sách và cập nhật lại RecyclerView
                    thongbaoList.remove(position);
                    notificationAdapter.notifyItemRemoved(position);
                    Log.d("ThongbaodsActivity", "Thông báo đã được xóa: ID = " + id);
                    Toast.makeText(ThongbaodsActivity.this, "Thông báo đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ThongbaodsActivity", "Lỗi khi xóa thông báo: " + response.code());
                    Toast.makeText(ThongbaodsActivity.this, "Lỗi khi xóa thông báo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ThongbaodsActivity", "Lỗi kết nối khi xóa thông báo: " + t.getMessage());
                Toast.makeText(ThongbaodsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
