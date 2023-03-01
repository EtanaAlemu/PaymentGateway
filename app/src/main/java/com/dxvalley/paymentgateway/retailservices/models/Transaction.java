package com.dxvalley.paymentgateway.retailservices.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction_db")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "phoneNumber")
    String phoneNumber;
    @ColumnInfo(name = "transactionId")
    String transactionId;
    @ColumnInfo(name = "amount")
    String amount;
    @ColumnInfo(name = "date")
    String date;

    public Transaction(String phoneNumber, String transactionId, String amount, String date) {
        this.phoneNumber = phoneNumber;
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
    }

    @Ignore
    public Transaction() {

    }

    @Ignore
    public Transaction(int id, String phoneNumber, String transactionId, String amount, String date) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
