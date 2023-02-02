package com.dxvalley.paymentgateway.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.db.ItemDatabase;
import com.dxvalley.paymentgateway.models.Item;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    static List<Item> items;
    static ItemDatabase database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText price = findViewById(R.id.price);
        Button saveItem = findViewById(R.id.save);
        Button cancelItem = findViewById(R.id.cancel);

        saveItem.setOnClickListener(view -> {
            String mName = name.getText().toString();
            String mPrice = price.getText().toString();
            Item item = new Item(R.drawable.coffee,mName,mPrice);
            createItemList(item);
        });

        cancelItem.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add Item");
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
    private void createItemList(Item mItem) {

        class CreateItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                database = ItemDatabase.getInstance(getApplicationContext());
                items = database.itemDao().getItemList();

                database.itemDao().insertItem(mItem);

                return items;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
//                Intent settingIntent = new Intent(AddItemActivity.this, SettingActivity.class);
//                settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(settingIntent);
                finish();
            }
        }

        CreateItems savedTasks = new CreateItems();
        savedTasks.execute();
    }
}