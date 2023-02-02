package com.dxvalley.paymentgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener , ItemClickListener{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    static List<Item> items;
    static ItemDatabase database ;
    LinearLayout mNoItemView;
    LinearLayout mProgressView;
    ItemAdapter adapter;
    RecyclerView recyclerView;
    TextInputEditText mAmount;
    CardView mTip;
    boolean isTipEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        mAmount = findViewById(R.id.amount);
        mTip = findViewById(R.id.tip);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.nav_drawer);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }


        mNoItemView = findViewById(R.id.no_item);
        mProgressView =findViewById(R.id.progress);

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById(R.id.list_item);

        mTip.setOnClickListener(view -> {
            isTipEnabled = !isTipEnabled;

            float amount = Float.parseFloat(mAmount.getText().toString());
            amount += 0.05*amount;
            mAmount.setText(String.valueOf(amount));
        });

        // Initialize contacts
        items = new ArrayList<Item>();
        setUpAdapter();
        getSavedTasks();

    }

    public void setUpAdapter() {
        // Create adapter passing in the sample user data
        adapter = new ItemAdapter(items);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter.setClickListener(this); // Bind the listener
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSavedItems();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
//                finish();
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }


    private void getSavedTasks() {

        class GetSavedItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                database = ItemDatabase.getInstance(getApplicationContext());
                items = database.itemDao().getItemList();
                return items;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                mNoItemView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedItems savedItems = new GetSavedItems();
        savedItems.execute();
    }


    private void getSavedItems() {

        class GetSavedItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                database = ItemDatabase.getInstance(getApplicationContext());
                items = database.itemDao().getItemList();
                return items;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                mNoItemView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedItems savedTasks = new GetSavedItems();
        savedTasks.execute();
    }

    @Override
    public void onClick(View view, int position) {
        final Item item = items.get(position);
        float amount = Float.parseFloat(mAmount.getText().toString());
        amount += Float.parseFloat(item.price);
        mAmount.setText(String.valueOf(amount));

    }
}