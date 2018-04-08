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

public class IncomeActivity extends AppCompatActivity {


    private  ArrayList<Income> incomesList;
    private GestureDetectorCompat gestureDetectorCompat;

    public  double sumOfAllIncomes() {
        double sum = 0;
        for (int i = 0; i < incomesList.size(); i++) {
            sum += incomesList.get(i).getPrice();
        }
        return sum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        gestureDetectorCompat = new GestureDetectorCompat(this, new IncomeActivity.My1stGestureListener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadData();
        final ListView listView = (ListView) findViewById(R.id.listIncomes);
        final IncomeAdapter incomeAdapter = new IncomeAdapter(this, incomesList);
        listView.setAdapter(incomeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Object o = incomeAdapter.getItem(position);

                final AlertDialog.Builder builder = new AlertDialog.Builder(IncomeActivity.this);
                final View view1 = getLayoutInflater().inflate(R.layout.edit_dialog, null);
                Button mEdit = view1.findViewById(R.id.btnEdit);
                Button mDelete = view1.findViewById(R.id.btnDelete);
                builder.setView(view1);
                final AlertDialog dialog1 = builder.create();
                dialog1.show();
                mEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        final Income e = (Income) o;
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(IncomeActivity.this);
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

                                    incomesList.set(incomeAdapter.getPosition(e), new Income(name, price));
                                    incomeAdapter.notifyDataSetChanged();
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
                        incomeAdapter.remove(incomeAdapter.getItem(position));
                        dialog1.dismiss();
                    }
                });
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(IncomeActivity.this);
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
                            incomeAdapter.add(new Income(name, price));
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
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(incomesList);
        editor.putString("key2", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("key2", null);
        Type type = new TypeToken<ArrayList<Income>>() {
        }.getType();
        incomesList = gson.fromJson(json, type);
        if (incomesList == null) {
            incomesList = new ArrayList<>();
        }
    }

    class My1stGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe right' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {


            if (event2.getX() < event1.getX()) {
                Toast.makeText(getBaseContext(),
                        "Swipe left",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(IncomeActivity.this, BalanceActivity.class);
                intent.putExtra("key", sumOfAllIncomes());
                startActivity(intent);
            }

            return true;
        }
    }


}

