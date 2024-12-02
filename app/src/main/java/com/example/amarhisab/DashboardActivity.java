package com.example.amarhisab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView mainTitle, description, tvDbBudget, tvDbIncome, tvDbExpense, userNameTextView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private boolean doubleBackToLogout = false;

    private GraphView graphView;
    private DatabaseHelper dbHelper;

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
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.getNavigationIcon().setTint(Color.WHITE);
        }

        ImageView profileImage = findViewById(R.id.toolbar_profile_image);

        Glide.with(this)
                .load(R.drawable.ic_profile) // Replace with your image URL or drawable
                .circleCrop()
                .into(profileImage);

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

        reportsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

//Summary View

        tvDbBudget = findViewById(R.id.tv_db_budget);
        tvDbIncome = findViewById(R.id.tv_db_income);
        tvDbExpense = findViewById(R.id.tv_db_expense);

        Locale locale = new Locale("bn", "BD");

        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM yyyy", locale);
        Date currentDate = new Date();
        String formattedDate = inputFormat.format(currentDate);

        String currentMonthsBudget = String.valueOf(dbHelper.getTotalBudgetForMonth(formattedDate));
        String currentMonthsIncome = String.valueOf(dbHelper.getTotalIncomeForMonth(formattedDate));
        String currentMonthsExpense = String.valueOf(dbHelper.getTotalExpenseForMonth(formattedDate));

        tvDbBudget.setText(currentMonthsBudget);
        tvDbIncome.setText(currentMonthsIncome);
        tvDbExpense.setText(currentMonthsExpense);

//Graph View

        graphView = findViewById(R.id.graph);

        graphViewMethod();

    }

    private void graphViewMethod() {

        List<String> months = new ArrayList<>();
        double[] incomeData = new double[6];
        double[] expenseData = new double[6];

// Initialize data arrays with zeroes (for months with no data)
        Arrays.fill(incomeData, 0);
        Arrays.fill(expenseData, 0);

        // Prepare arrays for income, expense data and months
        ArrayList<Double> incomeDataList = dbHelper.getLastSixMonthsIncomeData();
        ArrayList<Double> expenseDataList = dbHelper.getLastSixMonthsExpenseData();



        for (int i = 0; i < incomeDataList.size(); i++) {
            incomeData[i] = incomeDataList.get(i);
        }

        for (int i = 0; i < expenseDataList.size(); i++) {
            expenseData[i] = expenseDataList.get(i);
        }

        // Current date and last 5 months
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");

        // Get current month and the last five months
        for (int i = 0; i < 6; i++) {
            months.add(monthFormat.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, -1);  // Move to the previous month
        }


        // Set up the chart
        GraphHelper.setupBarChart(graphView, incomeData, expenseData, months);
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
