package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.NotificationAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.NotificationItem_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;

public class Activity_Notifications extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter_Demo adapter;
    private ArrayList<NotificationItem_Demo> notificationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.rcv_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fake data
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem_Demo("We Have New Products With Offers1", "$364.95   $260.00", "6 min ago", R.drawable.nice_shoe));
        notificationList.add(new NotificationItem_Demo("We Have New Products With Offers2", "$364.95   $260.00", "26 min ago", R.drawable.nice_shoe));
        notificationList.add(new NotificationItem_Demo("We Have New Products With Offers3", "$364.95   $260.00", "4 day ago", R.drawable.nice_shoe));

        adapter = new NotificationAdapter_Demo(notificationList);
        recyclerView.setAdapter(adapter);
    }
}