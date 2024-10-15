package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.YeuThichAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.YeuThich_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;

public class Activity_YeuThich extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_yeu_thich);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.rcvYeuThich_YT);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<YeuThich_Demo> shoeList = new ArrayList<>();
        shoeList.add(new YeuThich_Demo("Nike Jordan", 58.7, R.drawable.nice_shoe));
        shoeList.add(new YeuThich_Demo("Nike Air Max", 37.8, R.drawable.nice_shoe));
        shoeList.add(new YeuThich_Demo("Nike Club Max", 47.7, R.drawable.nice_shoe));
        shoeList.add(new YeuThich_Demo("Nike Air Max", 57.6, R.drawable.nice_shoe));

        YeuThichAdapter_Demo adapter = new YeuThichAdapter_Demo(shoeList);
        recyclerView.setAdapter(adapter);
    }
}