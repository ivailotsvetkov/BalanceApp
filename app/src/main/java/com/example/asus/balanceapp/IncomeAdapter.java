package com.example.asus.balanceapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asus on 5.4.2018 г..
 */

public class IncomeAdapter extends ArrayAdapter<Income> {
    public IncomeAdapter(Activity content, ArrayList<Income> exercises) {
        super(content, 0, exercises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.content_income, parent, false);
        }
        Income currentIncome = getItem(position);

        TextView price = (TextView) listItemView.findViewById(R.id.textPrice);
        price.setText("$" + currentIncome.getPrice());
        TextView name = (TextView) listItemView.findViewById(R.id.textName);
        name.setText(currentIncome.getName());
        return listItemView;
    }
}
