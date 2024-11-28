package com.example.amarhisab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    private TextView mainTitle, description;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private boolean doubleBackToLogout = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_dashboard);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("আমার হিসাব");
            toolbar.setTitleTextColor(Color.WHITE);

        }

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Handle item click in the Navigation Drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Toast.makeText(DashboardActivity.this, "হোম", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_income) {
                    Toast.makeText(DashboardActivity.this, "আয় এর তথ্য", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_expense) {
                    Toast.makeText(DashboardActivity.this, "ব্যায় এর তথ্য", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_summary) {
                    Toast.makeText(DashboardActivity.this, "সামারী রিপোর্ট", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_settings) {
                    Toast.makeText(DashboardActivity.this, "সেটিংস", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(DashboardActivity.this, "প্রোফাইল", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_about) {
                    Toast.makeText(DashboardActivity.this, "অ্যাপ সম্পর্কিত", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    handleLogout();
                    return true;
                } else {
                    return false;
                }

                // Close the drawer after selection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        // Add Drawer Toggle for Toolbar (Hamburger Menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Card Activity

        // Find cards by their IDs
        MaterialCardView incomeSectionCard = findViewById(R.id.card_income_section);
        MaterialCardView addIncomeCard = findViewById(R.id.card_add_income);
        MaterialCardView addBudgetCard = findViewById(R.id.card_add_expense);
        MaterialCardView reportsCard = findViewById(R.id.card_reports);


        // Click function on Income Section Card
        incomeSectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, IncomeSectionGridView.class);
                startActivity(intent);
            }
        });


        // Click Function on add Income Card

        addIncomeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, IncomeExpenseGridView.class);
                startActivity(intent);
            }
        });

        // Click Function on Budget Card

        addBudgetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, BudgetGridView.class);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToLogout) {
                // Perform logout logic
                handleLogout();
            } else {
                // If back pressed once, show message to ask for second press
                doubleBackToLogout = true;
                Toast.makeText(this, "লগআউট হওয়ার জন্য পুনরায় ব্যাক বাটন চাপুন!", Toast.LENGTH_SHORT).show();

                // Reset doubleBackToLogout flag after 2 seconds
                new Handler().postDelayed(() -> doubleBackToLogout = false, 2000);
            }
        }
    }

    private void handleLogout() {
        // Clear user session or preferences if needed
        // Example: SharedPreferences.clear();

        Toast.makeText(this, "লগআউট সফল হয়েছ।", Toast.LENGTH_LONG).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears back stack
        startActivity(intent);
        finish(); // Close DashboardActivity
    }
}
