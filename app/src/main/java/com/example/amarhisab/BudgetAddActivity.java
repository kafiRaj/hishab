package com.example.amarhisab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.Locale;

public class BudgetAddActivity extends AppCompatActivity {
    private TextView tvMonthYear;
    private Button btnSave;
    private EditText etComments, etAmount;
    private DatabaseHelper dbHelper;

    private String formattedMonthYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_budget_add);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("আমার হিসাব");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_back);
        }

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        tvMonthYear = findViewById(R.id.tv_month_year);
        etAmount = findViewById(R.id.et_amount);
        etComments = findViewById(R.id.et_comments);
        btnSave = findViewById(R.id.btn_save);

        // Set default text for tvMonthYear
        tvMonthYear.setText("বাজেট মাস নির্বাচন করুন");

        // Open DatePickerDialog when tvMonthYear is clicked
        tvMonthYear.setOnClickListener(v -> showMonthYearPickerDialog());

        // Handle Save button click
        btnSave.setOnClickListener(v -> saveData());

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void saveData() {
        String monthYear = tvMonthYear.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String comments = etComments.getText().toString().trim();

        // Check if the fields are empty
        if (monthYear.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "অনুগ্রহ করে সব তথ্য পূরণ করুন!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse and format the monthYear value
        Locale bengaliLocale = new Locale("bn", "BD");
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM yyyy", bengaliLocale);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

        try {
            // Parse the Bengali date string
            Date date = inputFormat.parse(monthYear);
            formattedMonthYear = outputFormat.format(date); // Format to required pattern
        } catch (java.text.ParseException e) {
            Toast.makeText(this, "তারিখের ফরম্যাট সঠিক নয়!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert data into the database
        boolean isInserted = dbHelper.insertBudget(formattedMonthYear, amount, comments);

        if (isInserted) {
            Toast.makeText(this, "সফলভাবে তথ্য সেভ করা হয়েছে!", Toast.LENGTH_SHORT).show();
            clearFields();
            Intent intent = new Intent(BudgetAddActivity.this, BudgetGridView.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "দুঃখিত! তথ্য সেভ করা যায়নি।", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        tvMonthYear.setText("বাজেট মাস নির্বাচন করুন");
        etAmount.setText("");
        etComments.setText("");
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
                    tvMonthYear.setText(selectedMonthYear);
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

    @Override
    public void onBackPressed() {
        // Perform custom logic here, e.g., show confirmation dialog
        super.onBackPressed(); // Call this to handle default back press
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
