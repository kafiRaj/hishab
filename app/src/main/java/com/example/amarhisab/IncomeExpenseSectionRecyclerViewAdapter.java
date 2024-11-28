package com.example.amarhisab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncomeExpenseSectionRecyclerViewAdapter extends RecyclerView.Adapter<IncomeExpenseSectionRecyclerViewAdapter.ViewHolder> {

    // Data source for the RecyclerView
    private List<IncomeExpenseSectionItem> itemList;

    // Constructor
    public IncomeExpenseSectionRecyclerViewAdapter(List<IncomeExpenseSectionItem> itemList) {
        this.itemList = itemList;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSection, tvComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_income_name);
            tvSection = itemView.findViewById(R.id.tv_income_section);
            tvComments = itemView.findViewById(R.id.tv_income_comments);
        }
    }

    // Inflate the item layout and return the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.income_section_table_view, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomeExpenseSectionItem item = itemList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvSection.setText(item.getSection());

        String comments = item.getComments().toString();
        String displayText = comments.length() > 8 ? comments.substring(0, 8) + ".." : comments;
        holder.tvComments.setText(displayText);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return itemList.size();
    }



}
