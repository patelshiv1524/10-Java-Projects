package com.example.restaurantguideapp.utils;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.restaurantguideapp.models.Restaurant;
import java.util.List;

@Dao
public interface RestaurantDAO {

    @Insert
    void insert(Restaurant restaurant);

    @Update
    void update(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);

    @Query("SELECT * FROM restaurants")
    List<Restaurant> getAllRestaurants();

    @Query("SELECT * FROM restaurants WHERE name = :name LIMIT 1")
    Restaurant getRestaurantByName(String name);

}
