package com.example.restaurantguideapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantguideapp.R;
import com.example.restaurantguideapp.models.Restaurant;
import com.example.restaurantguideapp.utils.AppDatabase;
import android.content.pm.PackageManager;
import java.util.List;
import java.util.ArrayList;


public class RestaurantDetailActivity extends AppCompatActivity {

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        database = AppDatabase.getInstance(this);

        // Retrieve restaurant details
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");
        String description = getIntent().getStringExtra("description");
        String tags = getIntent().getStringExtra("tags");
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);
        float rating = getIntent().getFloatExtra("rating", 0.0f);

        // Bind data to views
        TextView textName = findViewById(R.id.text_name);
        TextView textAddress = findViewById(R.id.text_address);
        TextView textPhone = findViewById(R.id.text_phone);
        TextView textDescription = findViewById(R.id.text_description);
        TextView textTags = findViewById(R.id.text_tags);
        RatingBar ratingBar = findViewById(R.id.detail_rating_bar);

        textName.setText(name);
        textAddress.setText(address);
        textPhone.setText(phone);
        textDescription.setText(description);
        textTags.setText(tags);
        ratingBar.setRating(rating);

        Button btnViewMap = findViewById(R.id.btn_view_map);
        Button btnGetDirections = findViewById(R.id.btn_get_directions);
        Button btnShare = findViewById(R.id.btn_share); // New Share Button

        // Map and Directions functionality
        btnViewMap.setOnClickListener(v -> {
            Uri locationUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, locationUri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        btnGetDirections.setOnClickListener(v -> {
            Uri directionsUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, directionsUri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        // Share functionality
        btnShare.setOnClickListener(v -> shareRestaurantDetails(name, address, tags, rating));

        // Rating functionality
        ratingBar.setOnRatingBarChangeListener((bar, newRating, fromUser) -> {
            if (fromUser) { // Only update if the user changes the rating
                new Thread(() -> {
                    Restaurant restaurant = database.restaurantDao().getRestaurantByName(name);

                    if (restaurant != null) {
                        restaurant.setRatingSum(restaurant.getRatingSum() + newRating);
                        restaurant.setRatingCount(restaurant.getRatingCount() + 1);
                        restaurant.setRating(restaurant.getRatingSum() / restaurant.getRatingCount());

                        database.restaurantDao().update(restaurant);

                        runOnUiThread(() -> {
                            Toast.makeText(this, "Rating updated!", Toast.LENGTH_SHORT).show();
                            ratingBar.setRating(restaurant.getRating());
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Restaurant not found!", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });
    }

    // Update your shareRestaurantDetails() method
    private void shareRestaurantDetails(String name, String address, String tags, float rating) {
        String shareText = "Check out this restaurant!\n\n" +
                "Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Tags: " + tags + "\n" +
                "Rating: " + rating + "/5\n\n" +
                "Shared via Flavor Guide!";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Create chooser intent to prioritize Email and Social Media apps
        Intent chooser = Intent.createChooser(shareIntent, "Share Restaurant Details");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, getSpecificShareTargets(shareIntent));

        startActivity(chooser);
    }

    // Helper method to prioritize specific apps
    private Intent[] getSpecificShareTargets(Intent baseIntent) {
        PackageManager packageManager = getPackageManager();
        List<Intent> targetIntents = new ArrayList<>();

        // Define specific package names for desired apps
        String[] preferredApps = {
                "com.google.android.gm",          // Gmail
                "com.facebook.katana",            // Facebook
                "com.twitter.android",            // Twitter
                "com.instagram.android"           // Instagram
        };

        for (String packageName : preferredApps) {
            try {
                Intent specificIntent = new Intent(baseIntent);
                specificIntent.setPackage(packageName);
                targetIntents.add(specificIntent);
            } catch (Exception e) {
                e.printStackTrace(); // Handle cases where app is not installed
            }
        }

        return targetIntents.toArray(new Intent[0]);
    }

}
