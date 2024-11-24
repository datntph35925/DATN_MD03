package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Product;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.ArrayList;
import java.util.List;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ViewHolder> {

    private Context context;
    private List<ProductItemCart> productItemList;

    public ThanhToanAdapter(Context context, List<ProductItemCart> productItemList) {
        this.context = context;
        this.productItemList = productItemList;
    }
    @NonNull
    @Override
    public ThanhToanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhToanAdapter.ViewHolder holder, int position) {
        ProductItemCart item = productItemList.get(position);
        holder.tvProductName.setText(item.getTenSP());
        holder.tvProductQuantity.setText(String.format("Số lượng: %d", item.getSoLuongGioHang()));
        holder.tvProductPrice.setText(String.format("$%.2f", item.getGia()));
        holder.tvProductSize.setText(String.format("Size: %d", item.getSize()));

        Glide.with(context).load(item.getHinhAnh().get(0)).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productItemList != null ? productItemList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice, tvProductSize;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.NameProduct_dt);
            tvProductQuantity = itemView.findViewById(R.id.tvSL_dt);
            tvProductPrice = itemView.findViewById(R.id.priceOrder_dt);
            tvProductSize = itemView.findViewById(R.id.tvSize_dt);
            imgProduct = itemView.findViewById(R.id.img_Order_Dt);
        }
    }
}
