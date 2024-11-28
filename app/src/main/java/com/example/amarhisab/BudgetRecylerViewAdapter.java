package com.example.amarhisab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BudgetRecylerViewAdapter extends RecyclerView.Adapter<BudgetRecylerViewAdapter.ViewHolder> {

    // Data source for the RecyclerView
    private List<BudgetItem> budgetItemList;

    // Constructor
    public BudgetRecylerViewAdapter(List<BudgetItem> budgetItemList) {
        this.budgetItemList = budgetItemList;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonthName, tvBudgetAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonthName = itemView.findViewById(R.id.tv_month_name);
            tvBudgetAmount = itemView.findViewById(R.id.tv_budget_amount);
        }
    }



    @NonNull
    @Override
    public BudgetRecylerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.income_budget_table_view, parent, false);
        return new BudgetRecylerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetRecylerViewAdapter.ViewHolder holder, int position) {
        BudgetItem budgetItem = budgetItemList.get(position);

        String formattedDate = convertToBengaliMonthYear(budgetItem.getMonthYear());

        holder.tvMonthName.setText(formattedDate);
        holder.tvBudgetAmount.setText(budgetItem.getAmount());
    }

    private String convertToBengaliMonthYear(String monthYear) {
        // Define the input and output formats
        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM", Locale.getDefault());
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("MMMM yyyy", new Locale("bn", "BD"));

        try {
            // Parse the input string
            Date date = inputFormat.parse(monthYear);

            // Format it to Bengali month and year
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return monthYear; // Return original if parsing fails
        }

    }

    @Override
    public int getItemCount() {
        return budgetItemList.size();
    }
}
