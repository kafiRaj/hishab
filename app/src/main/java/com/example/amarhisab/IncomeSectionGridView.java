package com.example.amarhisab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncomeSectionGridView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_income_section_grid_view);

    // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_income_expense);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<IncomeExpenseSectionItem> itemList = dbHelper.getAllItems();


        // Set adapter
        IncomeExpenseSectionRecyclerViewAdapter adapter = new IncomeExpenseSectionRecyclerViewAdapter(itemList);
        recyclerView.setAdapter(adapter);

        Button addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeSectionGridView.this, IncomeSectionActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
