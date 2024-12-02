package com.example.amarhisab;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
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

import com.google.android.material.navigation.NavigationView;

public class ReportActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvMonthSelection, tvReportMonth, tvBudget, tvTotalIncome, tvTotalExpense, tvBalance, userNameTextView;
    private Button reportShowBtn;

    private DatabaseHelper dbHelper;

    public ReportActivity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_report);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("আমার হিসাব");
            toolbar.setTitleTextColor(Color.WHITE);

        }

        // Initialize database helper
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

        tvMonthSelection = findViewById(R.id.tv_report_month_selection);
        tvReportMonth = findViewById(R.id.tv_report_month);
        tvBudget = findViewById(R.id.tv_report_budget);
        tvTotalIncome = findViewById(R.id.tv_report_income);
        tvTotalExpense = findViewById(R.id.tv_report_expense);
        tvBalance = findViewById(R.id.tv_report_balance);
        reportShowBtn = findViewById(R.id.btn_show_report);


        // Set default text for tvMonthYear
        tvMonthSelection.setText("মাস নির্বাচন করুন");

        // Open DatePickerDialog when tvMonthYear is clicked
        tvMonthSelection.setOnClickListener(v -> showMonthYearPickerDialog());

        reportShowBtn.setOnClickListener(v -> showReport());

    }

    private void showReport() {

        String selectedMonthYear = tvMonthSelection.getText().toString();
        double totalBudget = dbHelper.getTotalBudgetForMonth(selectedMonthYear);
        double totalIncome = dbHelper.getTotalIncomeForMonth(selectedMonthYear);
        double totalExpense = dbHelper.getTotalExpenseForMonth(selectedMonthYear);
        double balance = totalIncome - totalExpense;


        tvReportMonth.setText(": " +selectedMonthYear);
        tvBudget.setText(": " + totalBudget);
        tvTotalIncome.setText(": " + totalIncome);
        tvTotalExpense.setText(": " + totalExpense);
        tvBalance.setText(": " + balance);

        if(totalExpense>totalBudget){
            tvTotalExpense.setTextColor(Color.RED);
        }else{
            tvTotalExpense.setTextColor(Color.GREEN);
        }
    }

    private void showMonthYearPickerDialog() {

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);

        // DatePickerDialog for selecting month and year
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Only set month and year (ignore day)
                    String selectedMonthYear = getMonthYearString(monthOfYear, year);
                    tvMonthSelection.setText(selectedMonthYear);
                },
                currentYear, currentMonth, 1); // Default date is set to the 1st of the current month

        // Hide the day selection (remove calendar view for API 34+)
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // Limit to today's date

        // Set the title and show the dialog
        datePickerDialog.setTitle("Select Month and Year");
        datePickerDialog.show();
    }

    private String getMonthYearString(int month, int year) {
        // Array of month names
        String[] months = getResources().getStringArray(R.array.months);
        return months[month] + " " + year;
    }
}
