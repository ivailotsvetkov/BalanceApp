package com.example.asus.balanceapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class BalanceActivity extends Activity {


    private GestureDetectorCompat gestureDetectorCompat;

    private TextView textViewIncome;
    private TextView textViewExpense;
    private TextView textViewBalance;
    private double income;
    private double expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_balance);
        gestureDetectorCompat = new GestureDetectorCompat(this, new My2ndGestureListener());
        // Get the Intent that started this activity and extract the string

        Intent intent = getIntent();

        income = intent.getDoubleExtra("key", 0);

        expense = intent.getDoubleExtra("key2", 0);

        loadData();
        double balance = income - expense;
        // Capture the layout's TextView and set the string as its text
        textViewBalance = findViewById(R.id.balanceText);
        textViewExpense = findViewById(R.id.expenseText);
        textViewIncome = findViewById(R.id.incomeText);
        textViewBalance.setText("$" + balance);
        textViewIncome.setText("$" + income);
        textViewExpense.setText("$" + expense);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences("prefs", MODE_PRIVATE);
        String incomeS = sharedPref.getString("income", null);
        String expenseS = sharedPref.getString("expense", null);

        if (income == 0) {
            if (incomeS != null) {
                income = Double.parseDouble(incomeS);
            }
        }
        if (expense == 0) {
            if (expenseS != null) {
                expense = Double.parseDouble(expenseS);
            }
        }
    }

    private class My2ndGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe right' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

         /*
         Toast.makeText(getBaseContext(),
          event1.toString() + "\n\n" +event2.toString(),
          Toast.LENGTH_SHORT).show();
         */

            if (event2.getX() > event1.getX()) {
                Toast.makeText(getBaseContext(),
                        "Swipe right",
                        Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String s = String.valueOf(expense);
                editor.putString("expense", s);
                editor.apply();
                Intent intent = new Intent(BalanceActivity.this, IncomeActivity.class);
                startActivity(intent);

            }
            if (event2.getX() < event1.getX()) {
                Toast.makeText(getBaseContext(),
                        "Swipe left",
                        Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String s = String.valueOf(income);
                editor.putString("income", s);
                editor.apply();
                Intent intent = new Intent(BalanceActivity.this, ExpenseActivity.class);
                startActivity(intent);
            }
            return true;
        }
    }
}





