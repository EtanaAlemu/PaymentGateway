package com.dxvalley.paymentgateway.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dxvalley.paymentgateway.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TextView mManageItems = findViewById(R.id.manage_items);
        mManageItems.setOnClickListener(view -> {
            startActivity(new Intent(this, ItemManagerActivity.class));
        });
        TextView mManageTips = findViewById(R.id.manage_tips);
        mManageTips.setOnClickListener(view -> {
            startActivity(new Intent(this, TipsManagerActivity.class));
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Setting");
        ab.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}