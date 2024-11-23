package com.example.restaurantguideapp.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantguideapp.R;
import com.example.restaurantguideapp.models.Restaurant;
import com.example.restaurantguideapp.utils.AppDatabase;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddRestaurantActivity extends AppCompatActivity {

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        database = AppDatabase.getInstance(this);

        EditText inputName = findViewById(R.id.input_name);
        EditText inputAddress = findViewById(R.id.input_address);
        EditText inputPhone = findViewById(R.id.input_phone);
        EditText inputDescription = findViewById(R.id.input_description);
        EditText inputTags = findViewById(R.id.input_tags);

        Button btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String address = inputAddress.getText().toString();
            String phone = inputPhone.getText().toString();
            String description = inputDescription.getText().toString();
            String tags = inputTags.getText().toString();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Name and Address are required", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setPhone(phone);
                restaurant.setDescription(description);
                restaurant.setTags(tags);

                // Fetch GPS coordinates
                fetchCoordinates(address, restaurant);

                database.restaurantDao().insert(restaurant);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Restaurant Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private void fetchCoordinates(String address, Restaurant restaurant) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (!addresses.isEmpty()) {
                Address location = addresses.get(0);
                restaurant.setLatitude(location.getLatitude());
                restaurant.setLongitude(location.getLongitude());
                // Debugging log
                System.out.println("Coordinates fetched: " + location.getLatitude() + ", " + location.getLongitude());
            } else {
                // Debugging log for empty results
                System.out.println("No coordinates found for address: " + address);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Debugging log for exceptions
            System.out.println("Geocoder failed: " + e.getMessage());
        }
    }

}
