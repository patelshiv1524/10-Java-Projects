package com.example.restaurantguideapp.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantguideapp.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Team member details (you can customize these)
        String teamDetails = "Team Members:\n\n"
                + "1. Shivkumar Patel - Project Manager\n"
                + "2. Shivkumar Patel - Lead Developer\n"
                + "3. Shivkumar Patel - UI/UX Designer\n"
                + "4. Shivkumar Patel - Quality Assurance";

        // Set the text in the About section
        TextView aboutTextView = findViewById(R.id.text_about);
        aboutTextView.setText(teamDetails);
    }
}
