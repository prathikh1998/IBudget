package com.example.ibudget.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "payee")
public class Payee {
    @PrimaryKey(autoGenerate = true)
    public int payeeId;

    public String name;

    public String address;

    public String phoneNumber;

    public String accountNumber;

    public Double billAmount;

    public String billDueDate;

    public String websiteUrl;

    public String email;

    public String termLength;

    public String type;

    public String tags;

    public String notes;

    public Payee(String name, String address, String phoneNumber, String accountNumber,
                 Double billAmount, String billDueDate, String websiteUrl, String email,
                 String termLength, String type, String tags, String notes) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.billAmount = billAmount;
        this.billDueDate = billDueDate;
        this.websiteUrl = websiteUrl;
        this.email = email;
        this.termLength = termLength;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
    }
}
