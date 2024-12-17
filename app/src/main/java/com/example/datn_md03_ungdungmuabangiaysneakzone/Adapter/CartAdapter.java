package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<ProductItemCart> cartItems;
    private OnCartItemActionListener actionListener;
    private Set<ProductItemCart> selectedItems = new HashSet<>();

    public CartAdapter(Context context, ArrayList<ProductItemCart> cartItems, OnCartItemActionListener actionListener) {
        this.context = context;
        this.cartItems = cartItems;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductItemCart item = cartItems.get(position);

        holder.cartItemName.setText(item.getTenSP());
        holder.cartItemPrice.setText("VND" + item.getGia());
        holder.cartItemQuantity.setText(String.valueOf(item.getSoLuongGioHang()));
        holder.cartItemSize.setText(String.valueOf(item.getSize()));

        // Hiển thị ảnh sản phẩm
        if (item.getHinhAnh() != null && !item.getHinhAnh().isEmpty()) {
            String imageUrl = item.getHinhAnh().get(0);

            // Nếu URL là tương đối, ghép với baseUrl
            if (!imageUrl.startsWith("http")) {
                String baseUrl = "http://160.191.50.148:3000/";
                imageUrl = baseUrl + imageUrl;
            }

            // Log URL để kiểm tra
            Log.d("CartAdapter", "Full Image URL: " + imageUrl);

            // Tải ảnh với Glide
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.nice_shoe) // Hình mặc định khi đang tải
                    .error(R.drawable.nike2) // Hình lỗi nếu không tải được
                    .into(holder.cartItemImage);
        } else {
            // Hiển thị ảnh mặc định nếu không có ảnh
            Glide.with(context)
                    .load(R.drawable.errorr) // Hình mặc định
                    .into(holder.cartItemImage);
        }

        // Xử lý tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            if (item.getSoLuongGioHang() < item.getSoLuongTon()) {
                item.setSoLuongGioHang(item.getSoLuongGioHang() + 1);
                holder.cartItemQuantity.setText(String.valueOf(item.getSoLuongGioHang()));
                notifyItemChanged(position);
                if (actionListener != null) {
                    actionListener.onIncreaseQuantity(item);
                }
            } else {
                Toast.makeText(context, "Số lượng đã đạt tối đa trong kho", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getSoLuongGioHang() > 1) {
                item.setSoLuongGioHang(item.getSoLuongGioHang() - 1);
                holder.cartItemQuantity.setText(String.valueOf(item.getSoLuongGioHang()));
                notifyItemChanged(position);
                if (actionListener != null) {
                    actionListener.onDecreaseQuantity(item);
                }
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý xóa sản phẩm
        holder.btnDelete.setOnClickListener(view -> {
            if (actionListener != null) {
                actionListener.onDeleteItem(item);
            }
        });

        // Xử lý checkbox
        holder.checkGH.setOnCheckedChangeListener(null);
        holder.checkGH.setChecked(item.isChecked());
        holder.checkGH.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            if (actionListener != null) {
                actionListener.onItemChecked(item, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkGH;
        ImageView cartItemImage, btnIncrease, btnDecrease, btnDelete;
        TextView cartItemName, cartItemPrice, cartItemQuantity, cartItemSize;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkGH = itemView.findViewById(R.id.checkGH);
            cartItemImage = itemView.findViewById(R.id.cart_item_image);
            cartItemName = itemView.findViewById(R.id.cart_item_name);
            cartItemPrice = itemView.findViewById(R.id.cart_item_price);
            cartItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            cartItemSize = itemView.findViewById(R.id.cart_item_size);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnCartItemActionListener {
        void onIncreaseQuantity(ProductItemCart item);

        void onDecreaseQuantity(ProductItemCart item);

        void onDeleteItem(ProductItemCart item);

        void onItemChecked(ProductItemCart item, boolean isChecked);
    }
}
