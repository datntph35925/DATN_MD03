package com.example.datn_md03_ungdungmuabangiaysneakzone.Demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;
import java.util.List;

public class YeuThichAdapter_Demo extends RecyclerView.Adapter<YeuThichAdapter_Demo.ShoeViewHolder> {
    @NonNull

    private ArrayList<YeuThich_Demo> shoeList;

    public YeuThichAdapter_Demo(ArrayList<YeuThich_Demo> shoeList) {
        this.shoeList = shoeList;
    }
    public YeuThichAdapter_Demo.ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yeuthich, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YeuThichAdapter_Demo.ShoeViewHolder holder, int position) {
        YeuThich_Demo shoe = shoeList.get(position);
        holder.shoeName.setText(shoe.getName());
        holder.shoePrice.setText("$" + shoe.getPrice());
        holder.shoeImage.setImageResource(shoe.getImageResId());
    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    public class ShoeViewHolder extends RecyclerView.ViewHolder {
        ImageView shoeImage;
        TextView shoeName, shoePrice;
        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image_YT);
            shoeName = itemView.findViewById(R.id.shoe_name_YT);
            shoePrice = itemView.findViewById(R.id.shoe_price_YT);
        }
    }
}
