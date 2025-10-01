package com.example.luxevistaresort;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class RoomItemDetails extends AppCompatActivity {

    private RoomData room;
    private ImageView ivDetailImage;
    private TextView tvTitle, tvDescription, tvPrice;
    private Spinner spinnerType;
    private DatePicker datePicker;
    private Button btnBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_item_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // 1. Grab passed RoomData
        room = (RoomData) getIntent().getSerializableExtra("room_data");
        if (room == null) {
            Toast.makeText(this, "No room data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Bind views
        ivDetailImage   = findViewById(R.id.ivDetailImage);
        tvTitle         = findViewById(R.id.ItemDetailsTitle);
        tvDescription   = findViewById(R.id.ItemDetailsDescription);
        spinnerType     = findViewById(R.id.ItemDetailsSpinner);
        datePicker      = findViewById(R.id.ItemDetailsDatePicker);
        tvPrice         = findViewById(R.id.ItemDetailsPrice);
        btnBook         = findViewById(R.id.btnBookNow); // update your XML id to this if needed

        // 3. Populate UI
        Glide.with(this)
                .load(room.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(ivDetailImage);

        tvTitle.setText(room.getName());
        tvDescription.setText(room.getDescription());
        tvPrice.setText(String.format("LKR %d", room.getPrice()));
        // 4. Book Now logic
        btnBook.setOnClickListener(v -> saveBooking());
    }

    /** Gather all fields and write a new document under "booking". */
    private void saveBooking() {
        // Get selected type from spinner
        String selectedType = spinnerType.getSelectedItem().toString();

        // Get date from datepicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Months are zero-based
        int year = datePicker.getYear();
        String dateStr = String.format("%02d/%02d/%04d", day, month, year);

        // Get current user ID and room ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String roomId = room.getId();

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if the selected combination is already booked
        db.collection("booking")
                .whereEqualTo("roomId", roomId)
                .whereEqualTo("date", dateStr)
                .whereEqualTo("type", selectedType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No existing booking found, proceed to save the new booking
                            Map<String, Object> booking = new HashMap<>();
                            booking.put("userId", userId);
                            booking.put("roomId", roomId);
                            booking.put("roomName", room.getName());
                            booking.put("type", selectedType);
                            booking.put("date", dateStr);
                            booking.put("price", room.getPrice());
                            booking.put("timestamp", Timestamp.now());

                            db.collection("booking")
                                    .add(booking)
                                    .addOnSuccessListener(docRef -> {
                                        Toast.makeText(this, "Booked successfully!", Toast.LENGTH_LONG).show();
                                        finish(); // Close the activity
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            // Booking already exists, show a toast
                            Toast.makeText(this, "This date and type are already booked for this room.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Handle query failure
                        Toast.makeText(this, "Error checking booking: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
