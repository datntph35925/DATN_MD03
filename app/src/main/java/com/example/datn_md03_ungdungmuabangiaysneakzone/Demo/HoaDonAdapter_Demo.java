package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.List;

public class HoaDonAdapter_Demo extends RecyclerView.Adapter<HoaDonAdapter_Demo.HoaDonViewHolder> {

    private Context context;
    private List<HoaDon_Demo> hoaDonList;

    public HoaDonAdapter_Demo(Context context, List<HoaDon_Demo> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
    }

    @NonNull
    @Override
    public HoaDonAdapter_Demo.HoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donhang, parent, false);
        return new HoaDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonAdapter_Demo.HoaDonViewHolder holder, int position) {
        HoaDon_Demo hoaDon = hoaDonList.get(position);

        holder.tvNameByer.setText("Tên người mua: " + hoaDon.getTenNguoiMua());
        holder.tvAdresByer.setText("Địa chỉ: " + hoaDon.getDiaChi());
        holder.tvPhoneByer.setText("SĐT: " + hoaDon.getSdt());
        holder.tvSoLuong.setText("Số lượng SP: " + hoaDon.getSoLuongSP());
        holder.tvTotal.setText("Tổng cộng: " + hoaDon.getTongTien() + " VND");
        holder.tvDate.setText("Ngày: " + hoaDon.getDate());
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public class HoaDonViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameByer, tvAdresByer, tvPhoneByer, tvSoLuong, tvTotal, tvDate, tvPaid;
        ImageView imgByer;

        public HoaDonViewHolder(@NonNull View itemView) {
            super(itemView);

            imgByer = itemView.findViewById(R.id.img_byer);
            tvNameByer = itemView.findViewById(R.id.tv_nameByer);
            tvAdresByer = itemView.findViewById(R.id.adresByer);
            tvPhoneByer = itemView.findViewById(R.id.phoneByer);
            tvSoLuong = itemView.findViewById(R.id.soluong);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPaid = itemView.findViewById(R.id.tv_paid);
        }
    }
}
