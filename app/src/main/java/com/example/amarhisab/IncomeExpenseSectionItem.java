package com.example.amarhisab;

public class IncomeExpenseSectionItem {
    private String name;
    private String section;

    private String comments;

    private int id;

    public IncomeExpenseSectionItem(int id, String name, String section, String comments) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.comments = comments;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public String getComments(){
        return comments;
    }

    @Override
    public String toString() {
        return name; // Or any field you want to display in the spinner
    }
}
