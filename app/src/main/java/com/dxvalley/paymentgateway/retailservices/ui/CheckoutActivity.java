package com.dxvalley.paymentgateway.retailservices.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.dxvalley.paymentgateway.retailservices.CheckoutItemInterface;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.retailservices.adapter.CheckoutItemAdapter;
import com.dxvalley.paymentgateway.retailservices.db.ItemDatabase;
import com.dxvalley.paymentgateway.retailservices.models.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements CheckoutItemInterface {

    static ItemDatabase itemDatabase;
    List<Item> items;

    List<Item> retrievedItems;
    CheckoutItemAdapter itemAdapter;
    ArrayList<Integer> itemsId;

    float amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Bundle extras = getIntent().getExtras();
        RecyclerView itemRecyclerView = findViewById(R.id.item_recycler);
        items = new ArrayList<>();
        retrievedItems = new ArrayList<>();

        itemAdapter = new CheckoutItemAdapter(items);
        // Attach the adapter to the recyclerview to populate items
        itemRecyclerView.setAdapter(itemAdapter);
        // Set layout manager to position the items
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        itemAdapter.setCount(this);
//        ArrayList<String> itemsId = extras.getStringArrayList("items");
        itemsId = extras.getIntegerArrayList("items");
        amount = extras.getFloat("amount");

        Integer[] arr = new Integer[itemsId.size()];
        arr = itemsId.toArray(arr);
        int id[] = new int[]{};
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
             id = Arrays.stream(arr).mapToInt(i->i).toArray();
        }

        getItemById(id);

        TextView finalAmount = findViewById(R.id.final_amount);
        TextView totalAmount = findViewById(R.id.total_amount);
        finalAmount.setText(String.valueOf(amount));
        totalAmount.setText(String.valueOf(amount));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getItemById(int[] id) {

        class GetItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                itemDatabase = ItemDatabase.getInstance(getApplicationContext());
                retrievedItems = itemDatabase.itemDao().getItem(id);
                return retrievedItems;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
//                items.add(retrievedItems.get(0));
                itemAdapter.setItems(retrievedItems);
                itemAdapter.notifyDataSetChanged();
//                Toast.makeText(CheckoutActivity.this, String.valueOf(retrievedItems.get(0).getName()), Toast.LENGTH_SHORT).show();
            }
        }

        GetItems items = new GetItems();
        items.execute();
    }

    @Override
    public int setCount( int position) {

        return Collections.frequency(itemsId, retrievedItems.get(position).getId());
    }
}