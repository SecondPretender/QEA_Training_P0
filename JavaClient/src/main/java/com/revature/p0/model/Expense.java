package com.revature.p0.model;

import java.util.Date;

public class Expense {
    /*

                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL NOT NULL,
                description VARCHAR(255) NOT NULL,
                date VARCHAR(255) NOT NULL,
                user_id INTEGER,
                FOREIGN KEY(user_id) REFERENCES USER(id) ON DELETE CASCADE

     */

    private int id;
    private double amount;
    private String description;
    private String date;
    private String name;
    private int userId;

    public Expense(int id, double amount, String description, String date, String name) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.name = name;
        this.userId = -1;
    }

    public Expense(int id, double amount, String description, String date, int userId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.userId = userId;
    }
    public Expense(double amount, String description, String date, int userId){
        this.id = -1;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.userId = userId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
