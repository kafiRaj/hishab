package com.example.amarhisab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class IncomeSectionActivity extends AppCompatActivity {
    private Spinner spinnerIncomeSection;
    private EditText etIncomeName, etComments;
    private Button btnSubmit;
    private DatabaseHelper dbHelper;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.acitivity_income_expense_section);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("আমার হিসাব");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_back);
        }


        // Find AutoCompleteTextView by its ID
        spinnerIncomeSection = findViewById(R.id.spinner_income_section);
        ArrayAdapter<SectionType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SectionType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncomeSection.setAdapter(adapter);
        spinnerIncomeSection.setSelection(0, false);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);


        // Initialize views
        etIncomeName = findViewById(R.id.et_income_name);
        etComments = findViewById(R.id.et_comments);
        spinnerIncomeSection = findViewById(R.id.spinner_income_section);
        btnSubmit = findViewById(R.id.btn_submit);



        // Set up button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get form data
                String name = etIncomeName.getText().toString().trim();
                String comments = etComments.getText().toString().trim();
                String section = spinnerIncomeSection.getSelectedItem().toString().trim();

                // Validate form data
                if (name.isEmpty()) {
                    etIncomeName.setError("একটি আয়/ব্যায় খাতের নাম লিখুন!");
                    return;
                }

                if (section.equals("ধরন নির্বাচন করুন")) { // Hint value check
                    Toast.makeText(IncomeSectionActivity.this, "একটি খাত নির্বাচন করুন!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Insert data into the database
                boolean isInserted = dbHelper.insertIncomeExpenseSection(name, section, comments);

                // Show success or failure message
                if (isInserted) {
                    Toast.makeText(IncomeSectionActivity.this, "সফলভাবে তথ্য সেভ করা হয়েছে!", Toast.LENGTH_LONG).show();
                    clearForm();

                    // Navigate to IncomeExpenseGridView
                    Intent intent = new Intent(IncomeSectionActivity.this, IncomeSectionGridView.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(IncomeSectionActivity.this, "দু:খিত! তথ্য সেভ করা যায়নি!", Toast.LENGTH_LONG).show();
                }
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void clearForm() {
        etIncomeName.setText("");
        etComments.setText("");
        spinnerIncomeSection.setSelection(0); // Reset to hint value
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

