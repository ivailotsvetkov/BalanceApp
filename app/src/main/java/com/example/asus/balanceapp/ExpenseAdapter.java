package com.example.asus.balanceapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asus on 7.4.2018 г..
 */

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    public ExpenseAdapter(Activity content, ArrayList<Expense> exercises) {
        super(content, 0, exercises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.content_expense, parent, false);
        }
        Expense currentExpense = getItem(position);

        TextView price = (TextView) listItemView.findViewById(R.id.textPriceExpense);
        price.setText("$" + currentExpense.getPrice());
        TextView name = (TextView) listItemView.findViewById(R.id.textNameExpense);
        name.setText(currentExpense.getName());
        return listItemView;
    }
}
