package com.example.amarhisab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class IncomeExpenseGridView extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView userNameTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_income_expense_grid_view);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("আমার হিসাব");
            toolbar.setTitleTextColor(Color.WHITE);

        }
// Database Helper Initialization
        dbHelper = new DatabaseHelper(this);

// Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Get the logged-in user ID from SharedPreferences

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userid", -1);
        String userName = sharedPreferences.getString("name", "Guest");

        // Retrieve the user's name from the database
        if (userId != -1) {
            String savedUserName = dbHelper.getLoggedInUserName(String.valueOf(userId));

            if (savedUserName != null) {
                // Update the Navigation Drawer header with the user's name
                View headerView = navigationView.getHeaderView(0);
                userNameTextView = headerView.findViewById(R.id.tv_nav_header_name);
                userNameTextView.setText(savedUserName);
            }
        }

        // Add Drawer Toggle for Toolbar (Hamburger Menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Initialize NavigationMenuHandler
        NavigationMenuHelper menuHelper = new NavigationMenuHelper(this, drawerLayout);
        menuHelper.setupNavigationMenu(navigationView);

//<!-- end of menu function -->

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_income_expense_grid_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<IncomeExpenseItem> incomeExpenseItemList = dbHelper.getAllIncomeExpenseItems();


        // Set adapter
        IncomeExpenseRecylerViewAdapter adapter = new IncomeExpenseRecylerViewAdapter(incomeExpenseItemList);
        recyclerView.setAdapter(adapter);

        Button addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeExpenseGridView.this, IncomeExpenseActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
