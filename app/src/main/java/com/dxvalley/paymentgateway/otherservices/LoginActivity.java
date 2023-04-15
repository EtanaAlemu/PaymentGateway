package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.ServicesActivity;
import com.dxvalley.paymentgateway.retailservices.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.login);
        TextInputEditText mUsername, mPassword;
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        LinearLayout layout = findViewById(R.id.layout);
        login.setOnClickListener(view -> {
            if (mUsername.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {

                Snackbar snackbar = Snackbar.make(layout,"Please enter username and password",Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {

                // on below line we are setting data in our shared preferences.
                // creating a master key for encryption of shared preferences.
                String masterKeyAlias = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
                        // on below line we are storing data in shared preferences file.
                        sharedPreferences.edit().putString("username", mUsername.getText().toString()).apply();

                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                // Storing data into SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MerchantInfo", MODE_PRIVATE);
                // Creating an Editor object to edit(write to the file)
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                // Storing the key and its value as the data fetched from edittext
                myEdit.putString("username", mUsername.getText().toString());
                myEdit.apply();
                }

                startActivity(new Intent(LoginActivity.this, PackagesActivity.class));
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.service_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.services:
                startActivity(new Intent(this, ServicesActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}