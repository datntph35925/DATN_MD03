package com.example.datn_md03_ungdungmuabangiaysneakzone.Activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter.DiaChiAdapter;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.DiaChi;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShowListLocationActivity extends AppCompatActivity {
    private RecyclerView rcvDiaChi;
    private DiaChiAdapter diaChiAdapter;
    private List<DiaChi> diaChiList;
    private FloatingActionButton btnAdd;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_location);

        rcvDiaChi = findViewById(R.id.rcv_sp);
        btnAdd = findViewById(R.id.btn_add);
        diaChiList = new ArrayList<>();
        setupDiaChiList();
        setupRecyclerView();
        setupGestureDetector();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDiaChiDialog();
            }
        });
    }

    private void setupRecyclerView() {
        diaChiAdapter = new DiaChiAdapter(this, diaChiList);
        rcvDiaChi.setLayoutManager(new LinearLayoutManager(this));
        rcvDiaChi.setAdapter(diaChiAdapter);
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = rcvDiaChi.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = rcvDiaChi.getChildAdapterPosition(childView);
                    showEditDiaChiDialog(position);
                }
            }
        });

        rcvDiaChi.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector.onTouchEvent(e);
            }
        });
    }

    private void showEditDiaChiDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_diachi, null);
        builder.setView(dialogView);

        final EditText edtTenDiaChi = dialogView.findViewById(R.id.edt_tendiachi);
        final EditText edtDiaChi = dialogView.findViewById(R.id.edt_diachi);
        final EditText edtSoDT = dialogView.findViewById(R.id.edt_sodienthoai);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_diaSp);
        Button btnSave = dialogView.findViewById(R.id.btn_save_diaSp);

        DiaChi currentDiaChi = diaChiList.get(position);
        edtTenDiaChi.setText(currentDiaChi.getTen());
        edtDiaChi.setText(currentDiaChi.getDiaChi());
        edtSoDT.setText(String.valueOf(currentDiaChi.getSoDT()));

        AlertDialog dialog = builder.create();
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String ten = edtTenDiaChi.getText().toString();
            String diaChi = edtDiaChi.getText().toString();
            String soDTStr = edtSoDT.getText().toString();

            if (ten.isEmpty() || diaChi.isEmpty() || soDTStr.isEmpty()) {
                Toast.makeText(ShowListLocationActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int soDT = Integer.parseInt(soDTStr);
                DiaChi newDiaChi = new DiaChi(ten, diaChi, soDT);
                diaChiList.set(position, newDiaChi);
                diaChiAdapter.notifyItemChanged(position);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(ShowListLocationActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showAddDiaChiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_diachi, null);
        builder.setView(dialogView);

        final EditText edtTenDiaChi = dialogView.findViewById(R.id.edt_tendiachi);
        final EditText edtDiaChi = dialogView.findViewById(R.id.edt_diachi);
        final EditText edtSoDT = dialogView.findViewById(R.id.edt_sodienthoai);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_diaSp);
        Button btnSave = dialogView.findViewById(R.id.btn_save_diaSp);

        AlertDialog dialog = builder.create();
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String ten = edtTenDiaChi.getText().toString();
            String diaChi = edtDiaChi.getText().toString();
            String soDTStr = edtSoDT.getText().toString();

            if (ten.isEmpty() || diaChi.isEmpty() || soDTStr.isEmpty()) {
                Toast.makeText(ShowListLocationActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int soDT = Integer.parseInt(soDTStr);
                DiaChi newDiaChi = new DiaChi(ten, diaChi, soDT);
                diaChiList.add(newDiaChi);
                diaChiAdapter.notifyItemInserted(diaChiList.size() - 1);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(ShowListLocationActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void setupDiaChiList() {
        diaChiList.add(new DiaChi("Nguyen Van A", "123 Đường ABC", 123456789));
        diaChiList.add(new DiaChi("Nguyen Van B", "456 Đường XYZ", 987654321));
    }
}
