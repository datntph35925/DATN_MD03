package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.DiaChi;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.List;

public class DiaChiAdapter extends RecyclerView.Adapter<DiaChiAdapter.ViewHolder> {
    private Context context;
    private List<DiaChi> lstSP;



    public DiaChiAdapter(Context context, List<DiaChi> lstSP) {
        this.context = context;
        this.lstSP = lstSP;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diachi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {

        DiaChi diaChi = lstSP.get(position);


        holder.tendiachi.setText("Tên người nhận: " + diaChi.getTen() );
        holder.diachi.setText("Địa chỉ: " + diaChi.getDiaChi());
        holder.sdtdiachi.setText("Số điện thoại: " + diaChi.getSoDT());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa item tại vị trí được nhấn
                lstSP.remove(position);
                // Cập nhật lại RecyclerView sau khi xóa
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, lstSP.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstSP != null ? lstSP.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tendiachi,diachi,sdtdiachi;
        ImageButton btn_delete;
        ImageView images;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tendiachi = itemView.findViewById(R.id.tenDiaChi);
            diachi = itemView.findViewById(R.id.diachi);
            sdtdiachi = itemView.findViewById(R.id.sdtdiachi);

            btn_delete = itemView.findViewById(R.id.btn_delete_diachi);


        }
    }
}
