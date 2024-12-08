package com.example.restaurantguideapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantguideapp.R;
import com.example.restaurantguideapp.models.Restaurant;
import com.example.restaurantguideapp.utils.AppDatabase;

public class EditRestaurantActivity extends AppCompatActivity {

    private AppDatabase database;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        // Initialize the database
        database = AppDatabase.getInstance(this);

        // Retrieve the restaurant ID passed from the intent
        int restaurantId = getIntent().getIntExtra("restaurant_id", -1);

        if (restaurantId == -1) {
            Toast.makeText(this, "Error: No restaurant ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch the restaurant from the database
        new Thread(() -> {
            restaurant = database.restaurantDao().getRestaurantById(restaurantId);

            runOnUiThread(() -> {
                if (restaurant == null) {
                    Toast.makeText(this, "Error: Restaurant not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                // Populate input fields with restaurant details
                EditText inputName = findViewById(R.id.input_name);
                EditText inputAddress = findViewById(R.id.input_address);
                EditText inputPhone = findViewById(R.id.input_phone);
                EditText inputDescription = findViewById(R.id.input_description);
                EditText inputTags = findViewById(R.id.input_tags);

                inputName.setText(restaurant.getName());
                inputAddress.setText(restaurant.getAddress());
                inputPhone.setText(restaurant.getPhone());
                inputDescription.setText(restaurant.getDescription());
                inputTags.setText(restaurant.getTags());

                // Make fields read-only if they should not be edited
                inputName.setEnabled(false); // Name cannot be changed
                inputAddress.setEnabled(false); // Address cannot be changed

                // Save button logic
                Button btnSave = findViewById(R.id.btn_save);
                btnSave.setOnClickListener(v -> {
                    String phone = inputPhone.getText().toString();
                    String description = inputDescription.getText().toString();
                    String tags = inputTags.getText().toString();

                    if (phone.isEmpty() || description.isEmpty() || tags.isEmpty()) {
                        Toast.makeText(this, "Please fill out all editable fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(() -> {
                        // Update only editable fields
                        restaurant.setPhone(phone);
                        restaurant.setDescription(description);
                        restaurant.setTags(tags);

                        // Update in the database
                        database.restaurantDao().update(restaurant);

                        runOnUiThread(() -> {
                            Toast.makeText(this, "Restaurant updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }).start();
                });
            });
        }).start();
    }
}
