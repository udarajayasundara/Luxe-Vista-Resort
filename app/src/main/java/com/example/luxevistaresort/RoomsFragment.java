package com.example.luxevistaresort;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RoomData> allRoomData; // Holds all rooms from Firestore
    private ArrayList<RoomData> roomData; // Holds filtered rooms for RecyclerView
    private MyAdapter adapter;
    private String selectedType = "All"; // Default filter is "All"

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        allRoomData = new ArrayList<>();
        roomData = new ArrayList<>();
        adapter = new MyAdapter(roomData, requireContext());
        recyclerView.setAdapter(adapter);

        // Set up Spinner
        Spinner spinner = view.findViewById(R.id.spinner_filter);
        List<String> types = Arrays.asList("All", "suite", "standard", "family", "deluxe");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Spinner selection listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = types.get(position);
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "All";
                applyFilter();
            }
        });

        // Load data from Firestore
        FirebaseFirestore.getInstance()
                .collection("room")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) return;
                    allRoomData.clear();
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

                        RoomData room = new RoomData();
                        room.setId(doc.getId());
                        room.setName(doc.getString("name"));
                        room.setDescription(doc.getString("description"));
                        room.setImageUrl(doc.getString("imageUrl"));
                        room.setType(doc.getString("type")); // Set the type field
                        room.setPrice(priceValue);
                        allRoomData.add(room);
                    }
                    applyFilter(); // Apply filter after fetching data
                });

        return view;
    }

    // Filter rooms based on selected type
    private void applyFilter() {
        roomData.clear();
        if (selectedType.equals("All")) {
            roomData.addAll(allRoomData);
        } else {
            for (RoomData room : allRoomData) {
                if (room.getType() != null && room.getType().equals(selectedType)) {
                    roomData.add(room);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}