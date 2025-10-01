package com.example.luxevistaresort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private ArrayList<BookingData> bookingData;
    private Context context;

    public BookingAdapter(ArrayList<BookingData> bookingData, Context context) {
        this.bookingData = bookingData;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {
        BookingData currentBooking = bookingData.get(position);
        String displayName = currentBooking.getRoomName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = currentBooking.getServiceName();
        }
        holder.RecyclerCaption.setText(displayName != null ? displayName : "N/A");
        holder.RecyclerPrice.setText(String.format("LKR %d", currentBooking.getPrice()));
        holder.recyclerDate.setText(currentBooking.getDate() != null ? currentBooking.getDate() : "N/A");
        holder.recyclerType.setText(currentBooking.getType() != null ? currentBooking.getType() : "N/A");
        if (currentBooking.getTimestamp() != null) {
            Date date = currentBooking.getTimestamp().toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            holder.recyclerTimestamp.setText(sdf.format(date));
        } else {
            holder.recyclerTimestamp.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return bookingData.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView RecyclerCaption, RecyclerPrice, recyclerDate, recyclerType, recyclerTimestamp;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            RecyclerCaption = itemView.findViewById(R.id.recyclerCaption);
            RecyclerPrice = itemView.findViewById(R.id.recyclerprice);
            recyclerDate = itemView.findViewById(R.id.recyclerdate);
            recyclerType = itemView.findViewById(R.id.recyclertype);
            recyclerTimestamp = itemView.findViewById(R.id.recyclertimestamp);
        }
    }
}