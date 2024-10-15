package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.List;

public class CartAdapter_Demo extends RecyclerView.Adapter<CartAdapter_Demo.CartViewHolder_Demo> {
    private List<Cart_Demo> cartItems;

    public CartAdapter_Demo(List<Cart_Demo> cartItems) {
        this.cartItems = cartItems;
    }
    @NonNull
    @Override
    public CartAdapter_Demo.CartViewHolder_Demo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder_Demo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter_Demo.CartViewHolder_Demo holder, int position) {
        Cart_Demo item = cartItems.get(position);

        // Set product image, name, price, and quantity
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        // holder.itemImage.setImage... // sử dụng thư viện Glide or Picasso

        // Handle quantity increase/decrease
        holder.btnIncrease.setOnClickListener(v -> {
            // Logic to increase quantity
        });

        holder.btnDecrease.setOnClickListener(v -> {
            // Logic to decrease quantity
        });

        // Handle delete item
        holder.btnDelete.setOnClickListener(v -> {
            // Logic to delete the item from cart
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder_Demo extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity;
        ImageView btnIncrease, btnDecrease, btnDelete;
        public CartViewHolder_Demo(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.cart_item_image);
            itemName = itemView.findViewById(R.id.cart_item_name);
            itemPrice = itemView.findViewById(R.id.cart_item_price);
            itemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
