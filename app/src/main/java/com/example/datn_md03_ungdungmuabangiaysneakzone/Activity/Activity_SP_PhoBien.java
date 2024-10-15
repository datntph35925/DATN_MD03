package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.SP_PhoBienAdapterDemo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.SP_PhoBien_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.YeuThichAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;

public class Activity_SP_PhoBien extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sp_pho_bien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.rcvSPPhoBien_SPPB);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<SP_PhoBien_Demo> list = new ArrayList<>();
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan1", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan2", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan3", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan4", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan5", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan6", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan7", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan8", 58.7));
        list.add(new SP_PhoBien_Demo(R.drawable.nice_shoe, "Nike Jordan9", 58.7));


        SP_PhoBienAdapterDemo adapter = new SP_PhoBienAdapterDemo(list);
        recyclerView.setAdapter(adapter);
    }
}