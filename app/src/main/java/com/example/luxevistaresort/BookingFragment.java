package com.example.luxevistaresort;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BookingFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<BookingData> bookingData;
    BookingAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the correct layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        bookingData = new ArrayList<>();
        adapter = new BookingAdapter(bookingData, requireContext());
        recyclerView.setAdapter(adapter);

        // Get current user's ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load data with filter for current user
        FirebaseFirestore.getInstance()
                .collection("booking")
                .whereEqualTo("userId", currentUserId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) return;
                    bookingData.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Object rawPrice = doc.get("price");
                        long priceValue;
                        if (rawPrice instanceof Number) {
                            priceValue = ((Number) rawPrice).longValue();
                        } else if (rawPrice instanceof String) {
                            priceValue = Long.parseLong((String) rawPrice);
                        } else {
                            priceValue = 0L;
                        }

                        BookingData booking = new BookingData();
                        booking.setRoomName(doc.getString("roomName"));
                        booking.setServiceName(doc.getString("serviceName"));
                        booking.setUserId(doc.getString("userId"));
                        booking.setPrice(priceValue);
                        booking.setDate(doc.getString("date"));
                        booking.setType(doc.getString("type"));
                        booking.setTimestamp(doc.getTimestamp("timestamp"));

                        bookingData.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                });

        return view;
    }
}