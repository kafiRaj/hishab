package com.example.amarhisab;

import android.content.Context;
import android.icu.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class IncomeExpenseItem {
    private String income_expense_section, income_type, amount, date;


    private String comments;

    private int id;

    public IncomeExpenseItem(int id, String income_expense_section, String income_type, String amount, String date) {
        this.id = id;
        this.income_expense_section = income_expense_section;
        this.income_type = income_type;
        this.amount = amount;
        this.date = date;
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

    public String getDate() {
        String date = this.date; // Assume date is in "yyyy-MM-dd" format
        String formattedDate = "";

        try {
            // Parse the input date string
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date parsedDate = originalFormat.parse(date);

            // Format the date to Bengali locale
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yy", new Locale("bn", "BD"));
            formattedDate = targetFormat.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing errors
        }

        return formattedDate; // Return formatted date in Bengali
    }


    @Override
    public String toString() {
        return income_type; // Or any field to display in the spinner
    }
}
