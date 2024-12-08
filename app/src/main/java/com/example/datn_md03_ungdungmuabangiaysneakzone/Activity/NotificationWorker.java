package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Hiển thị thông báo
        showNotification("Thông báo nền", "Ứng dụng của bạn đã nhận được thông báo!");

        return Result.success();
    }

    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "background_channel",
                    "Thông báo nền",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "background_channel")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}

