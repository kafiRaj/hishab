package com.example.amarhisab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BudgetGridView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light theme even if the system theme is dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_budget_grid_view);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_budget_grid_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<BudgetItem> budgetItemList = dbHelper.getAllBudgetItems();

        BudgetRecylerViewAdapter adapter = new BudgetRecylerViewAdapter(budgetItemList);
        recyclerView.setAdapter(adapter);


        Button addBtn = findViewById(R.id.btn_add);

    addBtn.setOnClickListener(v -> showBudgetAdd());
    }

    private void showBudgetAdd() {
        Intent intent = new Intent(BudgetGridView.this, BudgetAddActivity.class);
        startActivity(intent);
        finish();
    }

}
