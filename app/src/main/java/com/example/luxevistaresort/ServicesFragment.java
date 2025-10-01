package com.example.luxevistaresort;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ServiceData> serviceData;
    ServiceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        serviceData = new ArrayList<>();
        adapter = new ServiceAdapter(serviceData, requireContext());
        recyclerView.setAdapter(adapter);

        // Load data from Firestore
        FirebaseFirestore.getInstance()
                .collection("service")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) return;
                    serviceData.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        ServiceData service = new ServiceData();
                        service.setId(doc.getId()); // Set the document ID
                        service.setName(doc.getString("name"));
                        service.setDescription(doc.getString("description"));
                        service.setCategory(doc.getString("category"));
                        service.setImageUrl(doc.getString("imageUrl"));
                        service.setDuration(doc.getLong("duration") != null ? doc.getLong("duration") : 0L);
                        service.setPrice(doc.getLong("price") != null ? doc.getLong("price") : 0L);
                        serviceData.add(service);
                    }
                    adapter.notifyDataSetChanged();
                });

        return view;
    }
}