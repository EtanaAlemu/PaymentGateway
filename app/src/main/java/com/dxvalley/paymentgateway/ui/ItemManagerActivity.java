package com.dxvalley.paymentgateway.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dxvalley.paymentgateway.adapter.ItemAdapter;
import com.dxvalley.paymentgateway.ItemClickListener;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.db.ItemDatabase;
import com.dxvalley.paymentgateway.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemManagerActivity extends AppCompatActivity

        implements ItemClickListener {

    static List<Item> items;
    static ItemDatabase database ;
    LinearLayout mNoItemView;
    LinearLayout mProgressView;
    ItemAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_manager);

        mNoItemView = findViewById(R.id.no_item);
        mProgressView =findViewById(R.id.progress);

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById(R.id.list_item);

        // Initialize contacts
        items = new ArrayList<Item>();
        setUpAdapter();
        getSavedItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Items");
        ab.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add:
                Intent addItemIntent = new Intent(this, AddItemActivity.class);
                addItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addItemIntent);
//                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
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
    public void onClick(View view, int position, String adapter) {
        // The onClick implementation of the RecyclerView item click
        final Item item = items.get(position);
        Intent i = new Intent(this, EditItemActivity.class);
        i.putExtra("id", item.getId());
        i.putExtra("name", item.getName());
        i.putExtra("image", item.getImage());
        i.putExtra("price", item.getPrice());
        startActivity(i);
    }

}
