package com.example.datn_md03_ungdungmuabangiaysneakzone;

import android.app.Service;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class MyNotificationListenerService extends NotificationListenerService {

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Lấy ID thông báo bị xóa
        String notificationId = sbn.getKey();

        // Lưu danh sách ID thông báo bị xóa vào SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Lưu nhiều ID thông báo (nếu cần thiết)
        String deletedIds = sharedPreferences.getString("DeletedNotificationIds", "");
        deletedIds = deletedIds + "," + notificationId; // Thêm ID mới vào danh sách
        editor.putString("DeletedNotificationIds", deletedIds);
        editor.apply();
    }

}
