package com.example.restaurantguideapp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantguideapp.R;
import com.example.restaurantguideapp.adapters.RestaurantAdapter;
import com.example.restaurantguideapp.models.Restaurant;
import com.example.restaurantguideapp.utils.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getInstance(this);

        EditText searchBar = findViewById(R.id.search_bar);

        // Load all restaurants initially
        loadRestaurants();

        // Add search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadRestaurants() {
        new Thread(() -> {
            restaurantList = database.restaurantDao().getAllRestaurants();

            runOnUiThread(() -> {
                adapter = new RestaurantAdapter(this, restaurantList);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private void filterRestaurants(String query) {
        List<Restaurant> filteredList = new ArrayList<>();

        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getName().toLowerCase().contains(query.toLowerCase())
                    || restaurant.getTags().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }

        adapter.updateList(filteredList);
    }
}
