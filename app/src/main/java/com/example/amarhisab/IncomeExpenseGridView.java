package com.example.amarhisab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class IncomeExpenseGridView extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_income_expense_grid_view);

//        // Set up the toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("আমার হিসাব");
//            toolbar.setTitleTextColor(Color.WHITE);
//
//        }

        // Initialize DrawerLayout and NavigationView
//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);

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
