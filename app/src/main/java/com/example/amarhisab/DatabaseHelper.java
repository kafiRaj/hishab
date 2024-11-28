package com.example.amarhisab;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "Amarhisab.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names for users
    public static final String TABLE_USERS = "users";

    // Column Names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_PASSWORD = "password";


    // Table Name for Income/Expense Section
    public static final String TABLE_INCOME_EXPENSE_SECTION = "income_expense_section";
    public static final String COLUMN_IE_ID = "id";
    public static final String COLUMN_IE_NAME = "name";
    public static final String COLUMN_IE_SECTION = "section";
    public static final String COLUMN_IE_COMMENTS = "comments";


    // Table Name for income_expense
    public static final String TABLE_INCOME_EXPENSE = "income_expense";

    // Columns for income_expense table
    public static final String COLUMN_INCOME_EXPENSE_ID = "id";
    public static final String COLUMN_INCOME_EXPENSE_SECTION = "income_expense_section";
    public static final String COLUMN_INCOME_TYPE = "income_type";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_INCOME_EXPENSE_DATE = "income_expense_date";


    // Table Name for Budget
    public static final String TABLE_BUDGET = "budget";

    // Columns for budget table
    public static final String COLUMN_BUDGET_ID = "id";
    public static final String COLUMN_MONTH_YEAR = "month_year";
    public static final String COLUMN_BUDGET_AMOUNT = "amount";
    public static final String COLUMN_BUDGET_COMMENTS = "comments";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_MOBILE + " TEXT, "
                    + COLUMN_USERID + " TEXT UNIQUE, "
                    + COLUMN_PASSWORD + " TEXT)";
            db.execSQL(CREATE_TABLE);

            String CREATE_INCOME_EXPENSE_SECTION_TABLE = "CREATE TABLE " + TABLE_INCOME_EXPENSE_SECTION + "("
                    + COLUMN_IE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_IE_NAME + " TEXT, "
                    + COLUMN_IE_SECTION + " TEXT, "
                    + COLUMN_IE_COMMENTS + " TEXT)";
            db.execSQL(CREATE_INCOME_EXPENSE_SECTION_TABLE);

            String CREATE_INCOME_EXPENSE_TABLE = "CREATE TABLE " + TABLE_INCOME_EXPENSE + "("
                    + COLUMN_INCOME_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_INCOME_EXPENSE_SECTION + " INTEGER, "
                    + COLUMN_INCOME_TYPE + " TEXT, "
                    + COLUMN_AMOUNT + " TEXT, "
                    + COLUMN_INCOME_EXPENSE_DATE + "TEXT)";
            db.execSQL(CREATE_INCOME_EXPENSE_TABLE);

            String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + "("
                    + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_MONTH_YEAR + " TEXT, "
                    + COLUMN_BUDGET_AMOUNT + " TEXT, "
                    + COLUMN_BUDGET_COMMENTS + " TEXT)";
            db.execSQL(CREATE_BUDGET_TABLE);

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating tables: " + e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) { // Assuming the `budget` table is added in version 2
            String CREATE_BUDGET_TABLE = "CREATE TABLE budget (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "month_year TEXT, " +
                    "amount TEXT, " +
                    "comments TEXT)";
            db.execSQL(CREATE_BUDGET_TABLE);

        }else if(oldVersion < 3){

            db.execSQL("ALTER TABLE " + TABLE_INCOME_EXPENSE + " ADD COLUMN " + COLUMN_INCOME_EXPENSE_DATE + " TEXT");

        }

    }


    // Insert User into Database
    public boolean insertUser(String name, String mobile, String userid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MOBILE, mobile);
        values.put(COLUMN_USERID, userid);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // Return true if insertion was successful
    }


    // Insert Income/Expense section into Database
    public boolean insertIncomeExpenseSection(String name, String section, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IE_NAME, name);
        values.put(COLUMN_IE_SECTION, section);
        values.put(COLUMN_IE_COMMENTS, comments);

        long result = db.insert(TABLE_INCOME_EXPENSE_SECTION, null, values);
        return result != -1; // Return true if insertion was successful
    }

    // Insert Income/Expense into Database
    public boolean insertIncomeExpense(int catgoryId, String type, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_EXPENSE_SECTION, catgoryId);
        values.put(COLUMN_INCOME_TYPE, type);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_INCOME_EXPENSE_DATE, date);

        long result = db.insert(TABLE_INCOME_EXPENSE, null, values);
        return result != -1; // Return true if insertion was successful
    }

    // Insert Budget Data
    public boolean insertBudget(String monthYear, String amount, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month_year", monthYear); // Add column in database
        values.put("amount", amount);
        values.put("comments", comments);

        long result = db.insert("budget", null, values); // Adjust table name as necessary
        return result != -1; // Return true if insertion was successful
    }




    //Retrieve Income/Expense Section Data for grid view
    public List<IncomeExpenseSectionItem> getAllItems() {
        List<IncomeExpenseSectionItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, section, comments FROM income_expense_section", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String section = cursor.getString(2);
                String comments = cursor.getString(3);
                itemList.add(new IncomeExpenseSectionItem(id, name, section, comments));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    // Retrieve Income/Expense Section Data for spinner
    public List<String> getIncomeExpenseSections() {
        List<String> sections = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM income_expense_section", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String sectionName = cursor.getString(cursor.getColumnIndex("name"));
                sections.add(sectionName);
            }
            cursor.close();
        }
        return sections;
    }


    //Retrieve Income/Expense Data for grid view
    public List<IncomeExpenseItem> getAllIncomeExpenseItems() {
        List<IncomeExpenseItem> incomeExpenseItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT ie.id, ies.name AS section_name, ie.income_type, ie.amount " +
                "FROM income_expense ie " +
                "JOIN income_expense_section ies ON ie.income_expense_section = ies.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String income_expense_section = cursor.getString(1); // Fetched section name
                String income_type = cursor.getString(2);
                String amount = cursor.getString(3);

                // Pass the resolved section name to the constructor
                incomeExpenseItemList.add(new IncomeExpenseItem(id, income_expense_section, income_type, amount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return incomeExpenseItemList;
    }


    // Fetch income types based on selected income_expense_section
    public String getIncomeTypeByName(String iesName) {
        String incomeType = "Not Found";
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the income_expense_section table where section ID matches
        String query = "SELECT section FROM income_expense_section WHERE name = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{iesName});

        if (cursor.moveToFirst()) {
            incomeType = cursor.getString(0);  // Get income type value
        }
        cursor.close();
        db.close();

        return incomeType;
    }

    public List<BudgetItem> getAllBudgetItems() {
        List<BudgetItem> budgetItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM budget"; // Adjust table name as necessary
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id")); // Assuming an ID column exists
                String monthYear = cursor.getString(cursor.getColumnIndexOrThrow("month_year"));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));

                // Create a new BudgetItem and add it to the list
                budgetItems.add(new BudgetItem(id, monthYear, amount, comments));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return budgetItems;
    }



}
