package com.example.amarhisab;

import android.content.Context;

public class IncomeExpenseItem {
    private String income_expense_section, income_type, amount;


    private String comments;

    private int id;

    public IncomeExpenseItem(int id, String income_expense_section, String income_type, String amount) {
        this.id = id;
        this.income_expense_section = income_expense_section;
        this.income_type = income_type;
        this.amount = amount;
    }

    public int getId(){
        return id;
    }

    public String getIncomeExpenseSection() {
        return income_expense_section;
    }

    public String getIncomeType() {
        SectionType sectionType = SectionType.valueOf(income_type);
        return sectionType.getLabel();
    }

    public String getAmount(){
        return amount;
    }

    @Override
    public String toString() {
        return income_type; // Or any field to display in the spinner
    }
}
