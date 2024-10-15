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

public class SP_PhoBienAdapterDemo extends RecyclerView.Adapter<SP_PhoBienAdapterDemo.ViewHolder> {
   private  ArrayList<SP_PhoBien_Demo> list;

    public SP_PhoBienAdapterDemo(ArrayList<SP_PhoBien_Demo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SP_PhoBienAdapterDemo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spphobien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SP_PhoBienAdapterDemo.ViewHolder holder, int position) {
        SP_PhoBien_Demo shoe = list.get(position);
        holder.shoeName.setText(shoe.getName());
        holder.shoePrice.setText("$" + shoe.getPrice());
        holder.shoeImage.setImageResource(shoe.getImageResId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView shoeImage;
        TextView shoeName, shoePrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image_SPPB);
            shoeName = itemView.findViewById(R.id.shoe_name_SPPB);
            shoePrice = itemView.findViewById(R.id.shoe_price_SPPB);

        }
    }
}
