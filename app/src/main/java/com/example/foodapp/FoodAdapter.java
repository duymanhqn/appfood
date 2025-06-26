package com.example.foodapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.foodapp.apiImages.ImageModel.MonAn;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MonAnViewHolder> {

    private Context context;
    private List<MonAn> monAnList;

    public FoodAdapter(Context context, List<MonAn> monAnList) {
        this.context = context;
        this.monAnList = monAnList;
    }

    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monan, parent, false);
        return new MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAn monAn = monAnList.get(position);
        holder.tvName.setText(monAn.getTenmAn());
        holder.tvPrice.setText(monAn.getGia() + " " + monAn.getDVT());
        holder.tvDescription.setText(monAn.getMota());

        String imageUrl = "http://10.0.2.2:8888/images/" + monAn.getHinhanh();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.home)
                .error(R.drawable.home)
                .into(holder.imgFood);

        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detailfood.class);
            intent.putExtra("tenmAn", monAn.getTenmAn());
            intent.putExtra("gia", monAn.getGia());
            intent.putExtra("dvt", monAn.getDVT());
            intent.putExtra("mota", monAn.getMota());
            intent.putExtra("hinhanh", monAn.getHinhanh());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return monAnList.size();
    }

    public static class MonAnViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice, tvDescription;
        Button btnView;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
