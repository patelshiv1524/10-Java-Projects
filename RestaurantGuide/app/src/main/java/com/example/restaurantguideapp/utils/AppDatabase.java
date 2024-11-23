package com.example.restaurantguideapp.utils;

import android.content.Context;
import android.text.Editable;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.restaurantguideapp.models.Restaurant;

@Database(entities = {Restaurant.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract RestaurantDAO restaurantDao();

    // Migration from version 4 to 5 (adding Review table)
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS Review (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "restaurantId INTEGER NOT NULL, " +
                    "comment TEXT, " +
                    "timestamp INTEGER, " +
                    "FOREIGN KEY(restaurantId) REFERENCES Restaurant(id))");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_db")
                    .addMigrations(MIGRATION_4_5)
                    .build();
        }
        return instance;
    }

    public Editable reviewDAO() {
        return null;
    }
}
