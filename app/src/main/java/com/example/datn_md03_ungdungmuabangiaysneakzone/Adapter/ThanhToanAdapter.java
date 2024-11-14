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

import java.util.ArrayList;
import java.util.List;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ViewHolder> {

    private ArrayList<Product> productArrayList;
    Context context;
    private int size;
    private int quantity;

    public ThanhToanAdapter(){
        this.productArrayList = productArrayList;
    }
    @NonNull
    @Override
    public ThanhToanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhToanAdapter.ViewHolder holder, int position) {
        Product product = productArrayList.get(position);

        holder.tvProductName.setText(product.getTenSP());
        holder.tvPrice.setText("$ " + product.getGiaBan());
        Glide.with(context).load(product.getHinhAnh().get(0)).into(holder.imgProduct);

        holder.tvSize.setText("Size: " + size); // Display the selected size
        holder.tvSL.setText("Quantity: " + quantity); // Display the quantity
    }

    @Override
    public int getItemCount() {
        return productArrayList != null ? productArrayList.size() : 0;
    }

    public Product getProduct(int position) {
        if (position >= 0 && position < productArrayList.size()) {
            return productArrayList.get(position);
        }
        return null;
    }

    public void setProductList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName, tvSize, tvPrice, tvSL, tvColor,idPR,idSl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_Order_Dt);
            tvProductName = itemView.findViewById(R.id.NameProduct_dt);
            tvSize = itemView.findViewById(R.id.tvSize_dt);
            tvColor = itemView.findViewById(R.id.tvColor_dt);
            tvPrice = itemView.findViewById(R.id.priceOrder_dt);
            tvSL = itemView.findViewById(R.id.tvSL_dt);
            idPR = itemView.findViewById(R.id.idPR);
            idSl = itemView.findViewById(R.id.idSL);
        }
    }
}
