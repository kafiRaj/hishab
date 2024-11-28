package com.example.amarhisab;

public class BudgetItem {
    private int id;
    private String monthYear;
    private String amount;
    private String comments;

    public BudgetItem(int id, String monthYear, String amount, String comments) {
        this.id = id;
        this.monthYear = monthYear;
        this.amount = amount;
        this.comments = comments;
    }

    // Getters and setters (optional)
    public int getId() {
        return id;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public String getAmount() {
        return amount;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
                "id=" + id +
                ", monthYear='" + monthYear + '\'' +
                ", amount='" + amount + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
