package com.example.restaurantguideapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantguideapp.R;
import com.example.restaurantguideapp.activities.RestaurantDetailActivity;
import com.example.restaurantguideapp.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private final Context context;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
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

        holder.textName.setText(restaurant.getName());
        holder.textAddress.setText(restaurant.getAddress());
        holder.textTags.setText(restaurant.getTags());
        holder.ratingBar.setRating(restaurant.getRating());
        holder.textNumericRating.setText(String.format("%.1f/5", restaurant.getRating()));

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
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAddress, textTags, textNumericRating;
        RatingBar ratingBar;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textAddress = itemView.findViewById(R.id.text_address);
            textTags = itemView.findViewById(R.id.text_tags);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            textNumericRating = itemView.findViewById(R.id.text_numeric_rating); // Add TextView for numeric rating
        }
    }
}
