package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Voucher;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<Voucher> voucherList;
    private SimpleDateFormat dateFormat;
    private boolean isOrderDelivered;

    private OnVoucherClickListener onVoucherClickListener;
    private Voucher selectedVoucher;

    public VoucherAdapter(List<Voucher> voucherList, OnVoucherClickListener onVoucherClickListener, boolean isOrderDelivered) {
        this.voucherList = voucherList;
        this.onVoucherClickListener = onVoucherClickListener;
        this.isOrderDelivered = isOrderDelivered;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.selectedVoucher = null; // Ban đầu chưa có voucher nào được chọn
    }

    @NonNull
    @Override
    public VoucherAdapter.VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.VoucherViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);

        // Set data to views
        holder.tvMaVoucher.setText("Mã Voucher: " + voucher.getMaVoucher());
        holder.tvLoaiVoucher.setText("Loại Voucher: " + voucher.getLoaiVoucher());
        // Hiển thị giá trị voucher tùy theo loại
        if ("Giảm giá theo %".equals(voucher.getLoaiVoucher())) {
            holder.tvGiaTri.setText("Giá trị: " + (int)voucher.getGiaTri() + "%");
        } else if ("Giảm giá cố định".equals(voucher.getLoaiVoucher())) {
            holder.tvGiaTri.setText("Giá trị: " + voucher.getGiaTri() + " VND");
        } else {
            holder.tvGiaTri.setText("Giá trị: " + voucher.getGiaTri());
        }

        if (voucher.getNgayBatDau() != null || voucher.getNgayKetThuc() != null) {
            String formattedDateBD = dateFormat.format(voucher.getNgayBatDau());
            String formattedDateKT = dateFormat.format(voucher.getNgayKetThuc());
            if (holder.tvNgayBatDau != null && holder.tvNgayKetThuc != null) {
                holder.tvNgayBatDau.setText(formattedDateBD);
                holder.tvNgayKetThuc.setText(formattedDateKT);
            }
        }
        if(isOrderDelivered){
            holder.itemView.setOnClickListener(v -> {
                if (onVoucherClickListener != null ) {
                   // Cập nhật lại danh sách sau khi thay đổi
                    onVoucherClickListener.onVoucherClick(voucher);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return voucherList != null ? voucherList.size() : 0;
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaVoucher, tvLoaiVoucher, tvGiaTri, tvNgayBatDau, tvNgayKetThuc;
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaVoucher = itemView.findViewById(R.id.tvMaVoucher);
            tvGiaTri = itemView.findViewById(R.id.tvGiaTri);
            tvMaVoucher = itemView.findViewById(R.id.tvMaVoucher);
            tvLoaiVoucher = itemView.findViewById(R.id.tvLoaiVoucher);
            tvGiaTri = itemView.findViewById(R.id.tvGiaTri);
            tvNgayBatDau = itemView.findViewById(R.id.tv_NgayBatDau);
            tvNgayKetThuc = itemView.findViewById(R.id.tv_NgayKetThuc);
        }
    }

    // Interface cho sự kiện click
    public interface OnVoucherClickListener {
        void onVoucherClick(Voucher maVoucher);
    }
}
