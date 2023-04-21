package com.dxvalley.paymentgateway.otherservices.model;

import android.text.format.Time;
import android.widget.TextView;

public class NedajTransaction {
    String amount;
    String fuelType;
    String transactionId;
    String messageId;
    String time;

    public NedajTransaction(String amount, String fuelType, String transactionId, String messageId, String time) {
        this.amount = amount;
        this.fuelType = fuelType;
        this.transactionId = transactionId;
        this.messageId = messageId;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
