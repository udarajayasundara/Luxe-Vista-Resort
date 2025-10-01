package com.example.luxevistaresort;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServiceItemDetails extends AppCompatActivity {

    private ServiceData service;
    private ImageView ivDetailImage;
    private TextView tvTitle, tvDescription, tvPrice;
    private DatePicker datePicker;
    private Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_item_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Grab passed ServiceData
        service = (ServiceData) getIntent().getSerializableExtra("service_data");
        if (service == null) {
            Toast.makeText(this, "No service data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Bind views
        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvTitle = findViewById(R.id.ItemDetailsTitle);
        tvDescription = findViewById(R.id.ItemDetailsDescription);
        datePicker = findViewById(R.id.ItemDetailsDatePicker);
        tvPrice = findViewById(R.id.ItemDetailsPrice);
        btnBook = findViewById(R.id.btnBookNow);

        // 3. Populate UI
        Glide.with(this)
                .load(service.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(ivDetailImage);

        tvTitle.setText(service.getName());
        tvDescription.setText(service.getDescription());
        tvPrice.setText(String.format("LKR %d", service.getPrice()));
        // 4. Book Now logic
        btnBook.setOnClickListener(v -> saveBooking());
    }

    private void saveBooking() {
        // Get date from datepicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Months are zero-based
        int year = datePicker.getYear();
        String dateStr = String.format("%02d/%02d/%04d", day, month, year);

        // Get current user ID and service ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String serviceId = service.getId();

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if the selected date is already booked for this service
        db.collection("booking")
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("date", dateStr)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No existing booking found, proceed to save the new booking
                            Map<String, Object> booking = new HashMap<>();
                            booking.put("userId", userId);
                            booking.put("serviceId", serviceId);
                            booking.put("serviceName", service.getName());
                            booking.put("date", dateStr);
                            booking.put("price", service.getPrice());
                            booking.put("timestamp", Timestamp.now());

                            db.collection("booking")
                                    .add(booking)
                                    .addOnSuccessListener(docRef -> {
                                        Toast.makeText(this, "Service booked successfully!", Toast.LENGTH_LONG).show();
                                        finish(); // Close the activity
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            // Booking already exists, show a toast
                            Toast.makeText(this, "This date is already booked for this service.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Handle query failure
                        Toast.makeText(this, "Error checking booking: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}