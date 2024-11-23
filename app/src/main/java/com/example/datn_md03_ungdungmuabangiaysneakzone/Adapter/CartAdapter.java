package com.example.datn_md03_ungdungmuabangiaysneakzone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Domain.SanPham;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;
import com.example.datn_md03_ungdungmuabangiaysneakzone.api.ApiService;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.Cart;
import com.example.datn_md03_ungdungmuabangiaysneakzone.model.ProductItemCart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<ProductItemCart> cartItems;
    private OnCartItemActionListener actionListener;
    private ArrayList<Cart> cartArrayList;
    private ArrayList<SanPham> sanPhams;

    private Set<ProductItemCart> selectedItems = new HashSet<>();
    TextView tv_totalbill;
    ApiService apiService;

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
        holder.cartItemPrice.setText("$" + item.getGia());
        holder.cartItemQuantity.setText(String.valueOf(item.getSoLuongGioHang()));
        holder.cartItemSize.setText(String.valueOf(item.getSize()));
        if (item.getHinhAnh() != null && !item.getHinhAnh().isEmpty()) {
            // Lấy ảnh đầu tiên từ danh sách
            Glide.with(context)
                    .load(item.getHinhAnh().get(0))
                    .into(holder.cartItemImage);
        } else {
            // Nếu không có ảnh, tải ảnh mặc định
            Glide.with(context)
                    .load(R.drawable.errorr) // Thay bằng ảnh mặc định của bạn
                    .into(holder.cartItemImage);
        }

        // Handle click events
        holder.btnIncrease.setOnClickListener(v -> {
            Log.d("CartAdapter", "Current Quantity: " + item.getSoLuongGioHang());
            Log.d("CartAdapter", "Max Quantity: " + item.getSoLuongTon());
            Log.d("CartAdapter", "Size: " + item.getSize());

            if (item.getSoLuongGioHang() < item.getSoLuongTon()) { // Kiểm tra tồn kho
                item.setSoLuongGioHang(item.getSoLuongGioHang() + 1); // Tăng số lượng
                holder.cartItemQuantity.setText(String.valueOf(item.getSoLuongGioHang()));
                notifyItemChanged(position);// Cập nhật UI
                if (actionListener != null) {
                    actionListener.onIncreaseQuantity(item); // Gọi callback để cập nhật tổng tiền
                }
            } else {
                Toast.makeText(context, "Số lượng đã đạt tối đa trong kho", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getSoLuongGioHang() > 1) {
                item.setSoLuongGioHang(item.getSoLuongGioHang() - 1); // Giảm số lượng
                holder.cartItemQuantity.setText(String.valueOf(item.getSoLuongGioHang()));
                notifyItemChanged(position);// Cập nhật UI
                if (actionListener != null) {
                    actionListener.onDecreaseQuantity(item); // Gọi callback để cập nhật tổng tiền
                }
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(view -> {
            if (actionListener != null) {
                actionListener.onDeleteItem(item);
            }
        });

        holder.checkGH.setOnCheckedChangeListener(null); // Tránh gọi lại không cần thiết
        holder.checkGH.setChecked(item.isChecked()); // Set trạng thái hiện tại của checkbox
        holder.checkGH.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked); // Cập nhật trạng thái checkbox
            if (actionListener != null) {
                actionListener.onItemChecked(item, isChecked); // Gọi callback để xử lý
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
//            @Query("productId") String productId,
//            @Query("size") int size
