package com.example.asus.balanceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {


    private ArrayList<Expense> expensesList;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        gestureDetectorCompat = new GestureDetectorCompat(this, new ExpenseActivity.My3rdGestureListener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadData();
        final ListView listView = (ListView) findViewById(R.id.listExpenses);
        final ExpenseAdapter expenseAdapter = new ExpenseAdapter(this, expensesList);
        listView.setAdapter(expenseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Object o = expenseAdapter.getItem(position);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
                final View view1 = getLayoutInflater().inflate(R.layout.edit_dialog, null);
                Button mEdit = view1.findViewById(R.id.btnEdit);
                Button mDelete = view1.findViewById(R.id.btnDelete);
                builder.setView(view1);
                final AlertDialog dialogTop = builder.create();
                dialogTop.show();
                mEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogTop.dismiss();
                        final Expense e = (Expense) o;
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExpenseActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.income_dialog, null);
                        final EditText mName = (EditText) mView.findViewById(R.id.inputName);
                        final EditText mPrice = (EditText) mView.findViewById(R.id.inputPrice);
                        mName.setText(e.getName());
                        mPrice.setText(String.valueOf(e.getPrice()));
                        Button mSave = mView.findViewById(R.id.btnSave);
                        Button mCancel = mView.findViewById(R.id.btnCancel);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();
                        mSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mName.getText() != null && mPrice.getText() != null) {
                                    String name = mName.getText().toString();
                                    double price = 0;
                                    String priceString = mPrice.getText().toString();
                                    try {
                                        price = Double.parseDouble(priceString);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    expensesList.set(expenseAdapter.getPosition(e), new Expense(name, price));
                                    expenseAdapter.notifyDataSetChanged();
                                    dialog.dismiss();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Input something, please", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expenseAdapter.remove(expenseAdapter.getItem(position));
                        dialogTop.dismiss();
                    }
                });
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExpenseActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.income_dialog, null);
                final EditText mName = (EditText) mView.findViewById(R.id.inputName);
                final EditText mPrice = (EditText) mView.findViewById(R.id.inputPrice);

                Button mSave = mView.findViewById(R.id.btnSave);
                Button mCancel = mView.findViewById(R.id.btnCancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mName.getText() != null && mPrice.getText() != null) {
                            String name = mName.getText().toString();
                            double price = 0;
                            String priceString = mPrice.getText().toString();
                            try {
                                price = Double.parseDouble(priceString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            expenseAdapter.add(new Expense(name, price));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Input something, please", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {

        saveData();
        super.onPause();
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(expensesList);
        editor.putString("key", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("key", null);
        Type type = new TypeToken<ArrayList<Expense>>() {
        }.getType();
        expensesList = gson.fromJson(json, type);
        if (expensesList == null) {
            expensesList = new ArrayList<>();
        }
    }

    private double sumOfAllExpenses() {
        double sum = 0;
        for (int i = 0; i < expensesList.size(); i++) {
            sum += expensesList.get(i).getPrice();
        }
        return sum;
    }


    class My3rdGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe right' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {


            if (event2.getX() > event1.getX()) {
                Toast.makeText(getBaseContext(),
                        "Swipe right",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ExpenseActivity.this, BalanceActivity.class);
                intent.putExtra("key2", sumOfAllExpenses());
                startActivity(intent);
            }

            return true;
        }
    }

}
