package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Location;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Response;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> locationList;
    Callback callback;
    Context context;

    // Constructor để nhận dữ liệu và userId


    public LocationAdapter(Context context, List<Location> locationList, Callback callback) {
        this.context = context;
        this.locationList = locationList;
        this.callback = callback;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diachi, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.tenNguoiNhan.setText(location.getTenNguoiNhan());
        holder.diaChi.setText(location.getDiaChi());
        holder.sdt.setText(location.getSdt());

        // Xử lý sự kiện "Cập nhật"
        holder.btnEdit.setOnClickListener(v -> {
            // Tạo một form để nhập lại thông tin cập nhật, hoặc bạn có thể gọi activity mới để sửa
            // Cập nhật địa chỉ
             callback.editAddress(location);
        });

        // Xử lý sự kiện "Xóa"
        holder.btnDelete.setOnClickListener(v -> {
            // Xóa địa chỉ
            callback.deleteAddress(location);
        });

    }

    @Override
    public int getItemCount() {
        return locationList != null ? locationList.size(): 0;
    }



    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tenNguoiNhan, diaChi, sdt;
        ImageView btnEdit, btnDelete;
        CardView cardView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            tenNguoiNhan = itemView.findViewById(R.id.tv_name);
            diaChi = itemView.findViewById(R.id.tv_location);
            sdt = itemView.findViewById(R.id.tv_phone);
            btnEdit = itemView.findViewById(R.id.img_updateAddress);
            btnDelete = itemView.findViewById(R.id.img_deleteAddress);
        }
    }

    public interface Callback{
        void editAddress(Location address);
        void deleteAddress(Location address);

    }
}
