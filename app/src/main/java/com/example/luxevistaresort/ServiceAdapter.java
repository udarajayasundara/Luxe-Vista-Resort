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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private ArrayList<ServiceData> serviceData;
    private Context context;

    public ServiceAdapter(ArrayList<ServiceData> serviceData, Context context) {
        this.serviceData = serviceData;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceData currentService = serviceData.get(position);
        holder.RecyclerCaption.setText(currentService.getName());
        holder.recyclerDescription.setText(currentService.getDescription());
        holder.recyclerCategory.setText(currentService.getCategory());
        holder.recyclerDuration.setText(String.format("%d minutes", currentService.getDuration()));
        holder.recyclerPrice.setText(String.format("LKR %d", currentService.getPrice()));
        Glide.with(context)
                .load(currentService.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.RecyclerImage);

        // Add click listener to launch ServiceItemDetails
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceItemDetails.class);
            intent.putExtra("service_data", currentService);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return serviceData.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        ImageView RecyclerImage;
        TextView RecyclerCaption, recyclerDescription, recyclerCategory, recyclerDuration, recyclerPrice;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            RecyclerImage = itemView.findViewById(R.id.recyclerImage);
            RecyclerCaption = itemView.findViewById(R.id.recyclerCaption);
            recyclerDescription = itemView.findViewById(R.id.recyclerdescription);
            recyclerCategory = itemView.findViewById(R.id.recyclercategory);
            recyclerDuration = itemView.findViewById(R.id.recyclerduration);
            recyclerPrice = itemView.findViewById(R.id.recyclerprice);
        }
    }
}