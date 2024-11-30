package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.DiaChi;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Address;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Location;

import java.util.List;

public class DiaChiAdapter extends RecyclerView.Adapter<DiaChiAdapter.ViewHolder> {
    private Context context;
    private List<Location> lstSP;
    private Callback callback;

    public DiaChiAdapter(Context context, List<Location> lstSP, Callback callback) {
        this.context = context;
        this.lstSP = lstSP;
        this.callback = callback;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diachi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Location diaChi = lstSP.get(position);

        holder.tendiachi.setText(" " + diaChi.getTenNguoiNhan() );
        holder.diachi.setText(" " + diaChi.getDiaChi());
        holder.sdtdiachi.setText(" " + diaChi.getSdt());

//        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Xóa item tại vị trí được nhấn
//                lstSP.remove(position);
//                // Cập nhật lại RecyclerView sau khi xóa
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, lstSP.size());
//            }
//        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              callback.clickItem(diaChi);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            // Tạo một form để nhập lại thông tin cập nhật, hoặc bạn có thể gọi activity mới để sửa
            // Cập nhật địa chỉ
            callback.editAddress(diaChi);
        });

        // Xử lý sự kiện "Xóa"
        holder.btn_delete.setOnClickListener(v -> {
            // Xóa địa chỉ
            callback.deleteAddress(diaChi);
        });

    }

    @Override
    public int getItemCount() {
        return lstSP != null ? lstSP.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tendiachi,diachi,sdtdiachi;
        ImageView btn_delete, btnEdit;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tendiachi = itemView.findViewById(R.id.tv_name);
            diachi = itemView.findViewById(R.id.tv_location);
            sdtdiachi = itemView.findViewById(R.id.tv_phone);
            cardView = itemView.findViewById(R.id.cv_itemLocation);
            btnEdit = itemView.findViewById(R.id.img_updateAddress);
            btn_delete = itemView.findViewById(R.id.img_deleteAddress);
        }
    }

    public interface Callback{
        void clickItem(Location Address);
        void deleteAddress(Location address);
        void editAddress(Location address);
    }
}