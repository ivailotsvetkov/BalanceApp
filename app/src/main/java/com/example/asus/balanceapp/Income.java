package com.example.asus.balanceapp;

/**
 * Created by Asus on 5.4.2018 г..
 */

public class Income {


    private String name;
    private double price;

    public Income(String name, double price) {
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
