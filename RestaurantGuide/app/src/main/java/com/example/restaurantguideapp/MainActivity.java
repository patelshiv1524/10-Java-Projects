package com.example.restaurantguideapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.restaurantguideapp.activities.AddRestaurantActivity;
import com.example.restaurantguideapp.activities.RestaurantListActivity;
import com.example.restaurantguideapp.activities.AboutActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize CardViews
        CardView cardAddRestaurant = findViewById(R.id.card_add_restaurant);
        CardView cardViewRestaurants = findViewById(R.id.card_view_restaurants);
        CardView cardAboutUs = findViewById(R.id.card_about_us);

        // Set onClickListeners for navigation
        cardAddRestaurant.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });

        cardViewRestaurants.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
            startActivity(intent);
        });

        cardAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void fetchNearbyRestaurants() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;

        }
    }
}
