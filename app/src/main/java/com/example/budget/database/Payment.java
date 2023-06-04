package com.example.ibudget.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "payment")
public class Payment {
    @PrimaryKey(autoGenerate = true)
    public int paymentId;

    public String payeePaidTo;

    public Double amount;

    public String date;

    public String notes;

    public Payment(String payeePaidTo, Double amount, String date, String notes) {
        this.payeePaidTo = payeePaidTo;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }
}
