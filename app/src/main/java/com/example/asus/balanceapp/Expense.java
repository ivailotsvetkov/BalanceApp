package com.example.asus.balanceapp;

/**
 * Created by Asus on 7.4.2018 г..
 */

public class Expense {
    private String name;
    private double price;

    public Expense(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }


}
