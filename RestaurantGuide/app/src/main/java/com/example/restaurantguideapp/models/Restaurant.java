package com.example.restaurantguideapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurants")
public class Restaurant {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String address;
    private String phone;
    private String description;
    private String tags;
    private double latitude;
    private double longitude;
    private float rating; // Average rating
    private int ratingCount; // Total number of ratings
    private float ratingSum; // Cumulative sum of ratings

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public float getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(float ratingSum) {
        this.ratingSum = ratingSum;
    }
}
