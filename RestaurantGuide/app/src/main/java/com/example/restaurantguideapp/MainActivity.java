package com.example.restaurantguideapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantguideapp.activities.AddRestaurantActivity;
import com.example.restaurantguideapp.activities.RestaurantListActivity;
import com.example.restaurantguideapp.models.Restaurant;
import com.example.restaurantguideapp.utils.AppDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        database = AppDatabase.getInstance(this);

        // Input fields
        EditText inputName = findViewById(R.id.input_name);
        EditText inputAddress = findViewById(R.id.input_address);
        EditText inputPhone = findViewById(R.id.input_phone);
        EditText inputDescription = findViewById(R.id.input_description);
        EditText inputTags = findViewById(R.id.input_tags);

        // Buttons
        Button btnSave = findViewById(R.id.btn_save);
        Button btnShow = findViewById(R.id.btn_show);
        Button btnAddRestaurant = findViewById(R.id.btn_add_restaurant);
        Button btnShowRestaurants = findViewById(R.id.btn_show_restaurants);

        // Save Button Logic
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

                database.restaurantDao().insert(restaurant);

                runOnUiThread(() -> Toast.makeText(this, "Restaurant Saved!", Toast.LENGTH_SHORT).show());
            }).start();
        });

        // Show Button Logic (Toast for debugging)
        btnShow.setOnClickListener(v -> showRestaurants());

        // Navigate to AddRestaurantActivity
        btnAddRestaurant.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });

        // Navigate to RestaurantListActivity
        btnShowRestaurants.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
            startActivity(intent);
        });
    }

    private void showRestaurants() {
        new Thread(() -> {
            List<Restaurant> restaurants = database.restaurantDao().getAllRestaurants();

            runOnUiThread(() -> {
                if (restaurants.isEmpty()) {
                    Toast.makeText(this, "No restaurants found!", Toast.LENGTH_SHORT).show();
                } else {
                    for (Restaurant restaurant : restaurants) {
                        Toast.makeText(this, restaurant.getName() + " - " + restaurant.getAddress(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }).start();
    }
}
