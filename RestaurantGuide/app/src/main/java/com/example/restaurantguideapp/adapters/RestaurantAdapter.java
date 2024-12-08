package com.example.restaurantguideapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantguideapp.R;
import com.example.restaurantguideapp.activities.EditRestaurantActivity;
import com.example.restaurantguideapp.activities.RestaurantDetailActivity;
import com.example.restaurantguideapp.models.Restaurant;
import com.example.restaurantguideapp.utils.AppDatabase;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private final Context context;
    private final AppDatabase database; // Database instance for delete operation

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.database = AppDatabase.getInstance(context); // Initialize database instance
    }

    public void updateList(List<Restaurant> updatedList) {
        this.restaurantList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        // Set restaurant details in the view holder
        holder.textName.setText(restaurant.getName());
        holder.textAddress.setText(restaurant.getAddress());
        holder.textTags.setText(restaurant.getTags());
        holder.ratingBar.setRating(restaurant.getRating());
        holder.textNumericRating.setText(String.format("%.1f/5", restaurant.getRating()));

        // Navigate to RestaurantDetailActivity on item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra("name", restaurant.getName());
            intent.putExtra("address", restaurant.getAddress());
            intent.putExtra("phone", restaurant.getPhone());
            intent.putExtra("description", restaurant.getDescription());
            intent.putExtra("tags", restaurant.getTags());
            intent.putExtra("latitude", restaurant.getLatitude());
            intent.putExtra("longitude", restaurant.getLongitude());
            intent.putExtra("rating", restaurant.getRating());
            intent.putExtra("ratingCount", restaurant.getRatingCount());
            intent.putExtra("ratingSum", restaurant.getRatingSum());
            context.startActivity(intent);
        });

        // Edit Button functionality
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditRestaurantActivity.class);
            intent.putExtra("restaurant_id", restaurant.getId()); // Pass restaurant ID
            context.startActivity(intent);
        });

        // Delete Button functionality
        holder.btnDelete.setOnClickListener(v -> {
            new Thread(() -> {
                // Delete restaurant from database
                database.restaurantDao().delete(restaurant);
                restaurantList.remove(position); // Remove item from the list

                // Notify adapter and show toast on UI thread
                ((AppCompatActivity) context).runOnUiThread(() -> {
                    notifyDataSetChanged();
                    Toast.makeText(context, "Restaurant deleted!", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAddress, textTags, textNumericRating;
        RatingBar ratingBar;
        ImageButton btnEdit, btnDelete; // Edit and Delete buttons

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textAddress = itemView.findViewById(R.id.text_address);
            textTags = itemView.findViewById(R.id.text_tags);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            textNumericRating = itemView.findViewById(R.id.text_numeric_rating);
            btnEdit = itemView.findViewById(R.id.btn_edit); // Edit button
            btnDelete = itemView.findViewById(R.id.btn_delete); // Delete button
        }
    }
}
