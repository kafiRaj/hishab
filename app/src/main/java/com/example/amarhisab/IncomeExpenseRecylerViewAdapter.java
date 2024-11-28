package com.example.amarhisab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncomeExpenseRecylerViewAdapter extends RecyclerView.Adapter<IncomeExpenseRecylerViewAdapter.ViewHolder> {

    // Data source for the RecyclerView
    private List<IncomeExpenseItem> incomeExpenseItemList;

    // Constructor
    public IncomeExpenseRecylerViewAdapter(List<IncomeExpenseItem> incomeExpenseItemList) {
        this.incomeExpenseItemList = incomeExpenseItemList;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIncomeSection, tvIncomeType, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIncomeSection = itemView.findViewById(R.id.tv_income_section);
            tvIncomeType = itemView.findViewById(R.id.tv_income_type);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }



    @NonNull
    @Override
    public IncomeExpenseRecylerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.income_expense_table_view, parent, false);
        return new IncomeExpenseRecylerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeExpenseRecylerViewAdapter.ViewHolder holder, int position) {
        IncomeExpenseItem incomeExpenseItem = incomeExpenseItemList.get(position);
        holder.tvIncomeSection.setText(incomeExpenseItem.getIncomeExpenseSection());
        holder.tvIncomeType.setText(incomeExpenseItem.getIncomeType());
        holder.tvAmount.setText(incomeExpenseItem.getAmount());
    }

    @Override
    public int getItemCount() {
        return incomeExpenseItemList.size();
    }
}
