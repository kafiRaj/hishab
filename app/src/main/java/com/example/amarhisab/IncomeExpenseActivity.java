package com.example.amarhisab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncomeExpenseActivity extends AppCompatActivity {
    private EditText etAmount;
    private TextView tv_income_type, tvIncomeExpenseDate;
    private Button btnSave;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Spinner spinnerIncomeExpense, spinnerIncomeType;
    String finalDate;

    Date date;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_income_expense);


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


        // Add Drawer Toggle for Toolbar (Hamburger Menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        // Initialize Views
        spinnerIncomeExpense = findViewById(R.id.spinner_income_expense);
        tv_income_type = findViewById(R.id.tv_income_type);
        tvIncomeExpenseDate = findViewById(R.id.tv_income_expense_date);
        etAmount = findViewById(R.id.et_amount);
        btnSave = findViewById(R.id.btn_save);

        // Set the current date as default in the TextView
        String currentDate = getCurrentDate();
        tvIncomeExpenseDate.setText(currentDate);


        // Open DatePickerDialog when tvMonthYear is clicked
        tvIncomeExpenseDate.setOnClickListener(v -> showMonthYearPickerDialog());

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Fetch sections from the database
        List<IncomeExpenseSectionItem> sections = dbHelper.getAllItems();

        sections.add(0, new IncomeExpenseSectionItem(-1, "আয়/ব্যায় এর খাত নির্বাচন করুন", null, null)); // Assuming -1 is an invalid ID for your data


        // Create an ArrayAdapter for the spinner
        ArrayAdapter<IncomeExpenseSectionItem> incomeSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sections);
        incomeSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        spinnerIncomeExpense.setAdapter(incomeSectionAdapter);

// Optional: Add a listener to handle the placeholder selection
        spinnerIncomeExpense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IncomeExpenseSectionItem selectedItem = (IncomeExpenseSectionItem) parent.getItemAtPosition(position);
                if (selectedItem.getId() == -1) {

                   // Toast.makeText(IncomeExpenseActivity.this, "দয়া করে সব তথ্য পূরণ করুন!", Toast.LENGTH_SHORT).show();

                } else {
                    IncomeExpenseSectionItem selectedName = (IncomeExpenseSectionItem) parent.getItemAtPosition(position);
                    String name = selectedName.getName();  // Get the section value

                    // Fetch the related income_type for the selected section
                    String incomeType = dbHelper.getIncomeTypeByName(name);

                    // Set the value in the TextView (display income type)
                    tv_income_type.setText(incomeType);  // Display the income type in the TextView
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected (optional)
            }
        });


        // Save Button Click Listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIncomeExpense();

            }
        });
    }


    private void showMonthYearPickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH); // Get the current day

        // DatePickerDialog for selecting date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Format the selected date as "Month Year"
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, monthOfYear);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Format to show in TextView
                    String formattedDate = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                            .format(selectedDate.getTime());
                    tvIncomeExpenseDate.setText(formattedDate); // Set the formatted date to the TextView
                },
                currentYear, currentMonth, currentDay); // Default date is set to the current date

        // Optionally, set a maximum date to today
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Show the dialog
        datePickerDialog.show();
    }


    private String getCurrentDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(calendar.getTime());

    }


    private void saveIncomeExpense() {
        // Get values from the form
        String category = spinnerIncomeExpense.getSelectedItem().toString();
        // String type = spinnerIncomeType.getSelectedItem().toString();
        String amount = etAmount.getText().toString();

        IncomeExpenseSectionItem selectedCategory = (IncomeExpenseSectionItem) spinnerIncomeExpense.getSelectedItem();
        int categoryId = selectedCategory.getId();

        // Fetch the related income_type for the selected section
        String label = tv_income_type.getText().toString();
        String selectedDate = tvIncomeExpenseDate.getText().toString();
        SectionType selectedType = SectionType.fromLabel(label);
        String type = selectedType.name();


        // Validate inputs
        if (category.isEmpty() || selectedDate.isEmpty() || type.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "অনুগ্রহ করে সব তথ্য পূরণ করুন!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse and format the monthYear value
        Locale bengaliLocale = new Locale("bn", "BD");
        SimpleDateFormat inputFormatBengali = new SimpleDateFormat("dd MMMM yyyy", bengaliLocale);
        SimpleDateFormat inputFormatEnglish = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        try {

            // First, try parsing with Bengali locale
            try {
                date = inputFormatEnglish.parse(selectedDate);
            } catch (ParseException e) {
                // If it fails, try with English locale
                date = inputFormatBengali.parse(selectedDate);

            }

            // Convert to desired format
            finalDate = outputFormat.format(date);

        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }


        // Insert data into the database
        boolean isInserted = dbHelper.insertIncomeExpense(categoryId, type, amount, finalDate);

        if (isInserted) {
            Toast.makeText(this, "তথ্য সফলভাবে সেভ করা হয়েছে!", Toast.LENGTH_LONG).show();
            clearForm();

            // Navigate to IncomeExpenseGridView
            Intent intent = new Intent(IncomeExpenseActivity.this, IncomeExpenseGridView.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "দুঃখিত! তথ্য সেভ করা যায়নি।", Toast.LENGTH_LONG).show();
        }
    }

    private void clearForm() {
        spinnerIncomeExpense.setSelection(0);
        tv_income_type.setText("");
        etAmount.setText("");
    }
}


