package com.example.amarhisab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationMenuHelper {

    private final Context context;
    private final DrawerLayout drawerLayout;

    public NavigationMenuHelper(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    public void setupNavigationMenu(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationSelection(item.getItemId());
                drawerLayout.closeDrawers(); // Close the navigation drawer after selection
                return true;
            }
        });
    }

    private void handleNavigationSelection(int id) {
        if (id == R.id.nav_home) {
            context.startActivity(new Intent(context, DashboardActivity.class));
        } else if (id == R.id.nav_income) {
            context.startActivity(new Intent(context, IncomeExpenseGridView.class));
        } else if (id == R.id.nav_expense) {
            context.startActivity(new Intent(context, BudgetGridView.class));
        } else if (id == R.id.nav_summary) {
            context.startActivity(new Intent(context, ReportActivity.class));
        } else if (id == R.id.nav_settings) {
            context.startActivity(new Intent(context, DashboardActivity.class));
        } else if (id == R.id.nav_profile) {
            context.startActivity(new Intent(context, DashboardActivity.class));
        } else if (id == R.id.nav_about) {
            context.startActivity(new Intent(context, DashboardActivity.class));
        } else if (id == R.id.nav_logout) {
            handleLogout();
        }
    }

    private void handleLogout() {
        Toast.makeText(context, "লগআউট সফল হয়েছ।", Toast.LENGTH_LONG).show();

        // Clear user session or preferences if needed
        // Example: SharedPreferences.clear();

        // Redirect to LoginActivity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Close the current activity
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
