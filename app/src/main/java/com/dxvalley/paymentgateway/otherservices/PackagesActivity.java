package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.ServicesActivity;

public class PackagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);
//        Button mHaji = findViewById(R.id.haji);
//        Button mUmrah = findViewById(R.id.umrah);
        Button mNedaj = findViewById(R.id.nedaj);

//        mUmrah.setOnClickListener(view -> {
//            startActivity(new Intent(PackagesActivity.this, UmraActivity.class));
//        });
//
//        mHaji.setOnClickListener(view -> {
//            startActivity(new Intent(PackagesActivity.this, HajiActivity.class));
//        });

        mNedaj.setOnClickListener(view -> {
            startActivity(new Intent(PackagesActivity.this, NedajActivity.class));
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