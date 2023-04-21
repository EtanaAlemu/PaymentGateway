package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.otherservices.adapter.NedajAdapter;
import com.dxvalley.paymentgateway.otherservices.adapter.TransactionAdapter;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
import com.dxvalley.paymentgateway.otherservices.model.NedajTransaction;

import java.util.ArrayList;
import java.util.Objects;

public class NedajTransactionActivity extends AppCompatActivity {

    private TransactionAdapter transactionAdapter;
    RecyclerView transactionRecyclerView;

    ArrayList<NedajTransaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedaj_transaction);
        transactionRecyclerView = findViewById(R.id.transaction_rv);
        transactions = new ArrayList<>();

        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
    }

    public void setUpAdapter() {
        // Create adapter passing in the sample user data
        transactionAdapter = new TransactionAdapter(transactions);
        // Attach the adapter to the recyclerview to populate items
        transactionRecyclerView.setAdapter(transactionAdapter);

        // Set layout manager to position the items
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}