package com.example.amarhisab;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class UserActivity extends AppCompatActivity {
    private EditText etUsername, etMobile, etUserId, etPassword;
    private Button btnRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the ActionBar if it's still showing
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // Set the layout for the Login Activity
        setContentView(R.layout.activity_user);


        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        etUsername = findViewById(R.id.et_username);
        etMobile = findViewById(R.id.et_mobile);
        etUserId = findViewById(R.id.et_userid);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_login);

        // Handle Register Button Click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Get User Input
        String name = etUsername.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String userid = etUserId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate Input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(userid) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "সব তথ্য পূরণ করুন", Toast.LENGTH_LONG).show();
            return;
        }

        // Insert User into Database
        boolean isInserted = databaseHelper.insertUser(name, mobile, userid, password);

        if (isInserted) {
            Toast.makeText(this, "নিবন্ধন সফল হয়েছে", Toast.LENGTH_LONG).show();
            // Redirect to Login Activity
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "নিবন্ধন ব্যর্থ হয়েছে", Toast.LENGTH_LONG).show();
        }


    }

}
