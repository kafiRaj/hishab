package com.example.amarhisab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the ActionBar if it's still showing
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Get reference to the login button
        Button startBtn = findViewById(R.id.btn_start);

        // Set an OnClickListener on the button to navigate to LoginActivity
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to open the LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }


}
