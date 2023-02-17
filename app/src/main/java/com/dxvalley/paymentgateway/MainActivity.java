package com.dxvalley.paymentgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dxvalley.paymentgateway.adapter.ItemAdapter;
import com.dxvalley.paymentgateway.adapter.TipAdapter;
import com.dxvalley.paymentgateway.db.ItemDatabase;
import com.dxvalley.paymentgateway.db.TipDatabase;
import com.dxvalley.paymentgateway.models.Item;
import com.dxvalley.paymentgateway.models.Tip;
import com.dxvalley.paymentgateway.ui.PaymentActivity;
import com.dxvalley.paymentgateway.ui.SettingActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener , ItemClickListener{
    public static final String ITEM_ADAPTER = "ITEM ADAPTER";
    public static final String TIP_ADAPTER = "TIP ADAPTER";

    private Context mContext;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    static List<Item> items;
    static ItemDatabase itemDatabase ;
    static List<Tip> tips;
    static TipDatabase tipDatabase ;
    LinearLayout mNoItemView;
    ItemAdapter itemAdapter;
    RecyclerView itemRecyclerView;
    TipAdapter tipAdapter;
    RecyclerView tipRecyclerView;
    TextInputEditText mAmount;
    TextInputLayout amountLayout;
    Button mPay;

    boolean isTipEnabled = false;
    float tipValue = 0f;
    boolean isByPercent = true;
    float amount = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        mAmount = findViewById(R.id.amount);
        amountLayout = findViewById(R.id.amount_layout);

        amountLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = 0f;
                mAmount.setText("0");
                isTipEnabled = false;
            }
        });

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

        // Lookup the recyclerview in activity layout
        itemRecyclerView = findViewById(R.id.list_item);
        tipRecyclerView = findViewById(R.id.list_tips);
        mPay = findViewById(R.id.pay);
        mPay.setOnClickListener(view -> {
            float amount = Float.parseFloat(Objects.requireNonNull(mAmount.getText()).toString());
            if(amount!=0){
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            intent.putExtra("amount", amount);
            startActivity(intent);}
            else{
                Toast.makeText(mContext, "Amount can't be 0.0 Birr", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize contacts
        items = new ArrayList<Item>();
        tips = new ArrayList<Tip>();
        setUpAdapter();
        getSavedItems();
        getSavedTips();

    }

    public void setUpAdapter() {
        // Create adapter passing in the sample user data
        itemAdapter = new ItemAdapter(items);
        // Attach the adapter to the recyclerview to populate items
        itemRecyclerView.setAdapter(itemAdapter);
        // Set layout manager to position the items
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        itemAdapter.setClickListener(this); // Bind the listener

        // Create adapter passing in the sample user data
        tipAdapter = new TipAdapter(tips);
        // Attach the adapter to the recyclerview to populate items
        tipRecyclerView.setAdapter(tipAdapter);
        // Set layout manager to position the items
        tipRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        tipAdapter.setClickListener(this); // Bind the listener
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSavedItems();
        itemAdapter.notifyDataSetChanged();
        getSavedTips();
        tipAdapter.notifyDataSetChanged();
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


    private void getSavedItems() {

        class GetSavedItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                itemDatabase = ItemDatabase.getInstance(getApplicationContext());
                items = itemDatabase.itemDao().getItemList();
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

    private void getSavedTips() {

        class GetSavedTips extends AsyncTask<Void, Void, List<Tip>> {
            @Override
            protected List<Tip> doInBackground(Void... voids) {

                tipDatabase = TipDatabase.getInstance(getApplicationContext());
                tips = tipDatabase.tipDao().getTipList();
                return tips;
            }

            @Override
            protected void onPostExecute(List<Tip> tips) {
                super.onPostExecute(tips);
                setUpAdapter();
            }
        }

        GetSavedTips savedTips = new GetSavedTips();
        savedTips.execute();
    }

    @Override
    public void onClick(View view, int position, String adapter) {


        if(Objects.equals(adapter, ITEM_ADAPTER)) {
            final Item item = items.get(position);
            amount += Float.parseFloat(item.getPrice());

        } else {
            final Tip tip = tips.get(position);
            isTipEnabled = true;
            tipValue = tip.getValue();
            isByPercent = tip.isByPercent();
        }
        updatePrice();
    }

    private void updatePrice(){

        if(isTipEnabled){
            float totalAmount;
            if(isByPercent)
                totalAmount = amount + (tipValue * amount / 100);
            else
                totalAmount = amount + tipValue;
            mAmount.setText(String.valueOf(totalAmount));
        } else{
            mAmount.setText(String.valueOf(amount));
        }

    }
}