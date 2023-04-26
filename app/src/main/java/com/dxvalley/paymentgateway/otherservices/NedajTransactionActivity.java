package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.otherservices.adapter.NedajAdapter;
import com.dxvalley.paymentgateway.otherservices.adapter.TransactionAdapter;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
import com.dxvalley.paymentgateway.otherservices.model.NedajTransaction;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

        String fname;
        String lname;
        // on below line getting data from shared preferences.
        // creating a master key for encryption of shared preferences.
        String masterKeyAlias = null;
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

                // Initialize/open an instance of EncryptedSharedPreferences on below line.
                SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                        // passing a file name to share a preferences
                        "MerchantInfo",
                        masterKeyAlias,
                        getApplicationContext(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
                // on below line creating a variable
                // to get the data from shared prefs.
                fname = sharedPreferences.getString("fname", "");
                lname = sharedPreferences.getString("lname", "");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        transactions.add(new NedajTransaction("500","Regular","TX00923", "M09332", "12:00"));
        transactions.add(new NedajTransaction("1000","Diesel","TX00924", "M09333", "12:10"));
        transactions.add(new NedajTransaction("300","Regular","TX00925", "M09334", "12:20"));
        transactions.add(new NedajTransaction("200","Diesel","TX00926", "M09335", "12:22"));
        transactions.add(new NedajTransaction("800","Diesel","TX00927", "M09336", "1:00"));
        transactions.add(new NedajTransaction("1500","Regular","TX00928", "M09337", "2:30"));
        transactions.add(new NedajTransaction("2500","Diesel","TX00929", "M09338", "3:03"));
        setUpAdapter();
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