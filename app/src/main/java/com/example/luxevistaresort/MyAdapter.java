package com.example.luxevistaresort;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<RoomData> roomData;
    private Context context;

    public MyAdapter(ArrayList<RoomData> roomData, Context context) {
        this.roomData = roomData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rooms_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.RecyclerCaption.setText(roomData.get(position).getName());
        holder.RecyclerPrice.setText(String.format("LKR %d", roomData.get(position).getPrice()));
        Glide.with(context).load(roomData.get(position).getImageUrl()).into(holder.RecyclerImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RoomItemDetails.class);
            intent.putExtra("room_data", roomData.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView RecyclerImage;
        TextView RecyclerCaption, RecyclerPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            RecyclerImage = itemView.findViewById(R.id.recyclerImage);
            RecyclerCaption = itemView.findViewById(R.id.recyclerCaption);
            RecyclerPrice = itemView.findViewById(R.id.recyclerprice);
        }
    }
}