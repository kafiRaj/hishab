package com.example.amarhisab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvNewUser;

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the ActionBar if it's still showing
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // Set the layout for the Login Activity
        setContentView(R.layout.login_page);

        // User Login

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // Initialize database
        db = openOrCreateDatabase("Amarhisab.db", MODE_PRIVATE, null);

        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "সব তথ্য পূরণ করুন!", Toast.LENGTH_LONG).show();
                } else {
                    login(username, password);
                }
            }
        });

        // Forgot password text click listener (Optional)
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "পাসওয়ার্ড রিসেট ফিচার শীঘ্রই আসছে!", Toast.LENGTH_SHORT).show();
            }
        });



        //User Registration for New Users
        tvNewUser = findViewById(R.id.tv_new_user);
        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });


    }

    private void login(String username, String password) {
        // Query to check the user credentials
        Cursor cursor = db.rawQuery(
                "SELECT name FROM users WHERE userid = ? AND password = ?",
                new String[]{username, password}
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Login successful
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            cursor.close();


            // Navigate to a new activity (e.g., DashboardActivity)
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            //intent.putExtra("NAME", name); // Add name to Intent
            startActivity(intent);
            finish(); // Close the login activity
        } else {
            // Login failed
            Toast.makeText(this, "ভুল ব্যবহারকারী আইডি বা পাসওয়ার্ড!", Toast.LENGTH_LONG).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
