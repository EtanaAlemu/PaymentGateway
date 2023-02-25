package com.dxvalley.paymentgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.dxvalley.paymentgateway.otherservices.LoginActivity;
import com.dxvalley.paymentgateway.retailservices.MainActivity;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Button retailService = findViewById(R.id.retail_service);
        Button otherService = findViewById(R.id.other_service);
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ServicePerf",MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();


        retailService.setOnClickListener(view -> {

            // Storing the key and its value as the data fetched from edittext
            myEdit.putString("User Perf", "Retail Service");

            // Once the changes have been made, we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.apply();


            Intent i =new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            finish();
        });

        otherService.setOnClickListener(view -> {

            // Storing the key and its value as the data fetched from edittext
            myEdit.putString("User Perf", "Other Service");

            // Once the changes have been made, we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.apply();


            Intent i =new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
}