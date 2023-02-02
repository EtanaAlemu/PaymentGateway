package com.dxvalley.paymentgateway.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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

public class EditItemActivity extends AppCompatActivity {

    static List<Item> items;
    static ItemDatabase database ;
    int id;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText price = findViewById(R.id.price);
        Button saveItem = findViewById(R.id.save);
        Button cancelItem = findViewById(R.id.cancel);
        mContext = this;

        int sImage;
        String sName;
        String sPrice;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id = 0;
                sImage = 0;
                sName= null;
                sPrice= null;
            } else {
                id = extras.getInt("id");
                sImage = extras.getInt("image");
                sName= extras.getString("name");
                sPrice= extras.getString("price");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("id");
            sImage = (int) savedInstanceState.getSerializable("image");
            sName= (String) savedInstanceState.getSerializable("name");
            sPrice= (String) savedInstanceState.getSerializable("price");
        }

        name.setText(sName);
        price.setText(sPrice);

        saveItem.setOnClickListener(view -> {
            String mName = name.getText().toString();
            String mPrice = price.getText().toString();
            Item item = new Item(id,R.drawable.coffee,mName,mPrice);
            updateItemList(item);
        });

        cancelItem.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Edit Item");
        ab.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this item?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                Item item = new Item(id,0,null,null);
                                deleteItemList(item);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void updateItemList(Item item) {

        class UpdateItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                database = ItemDatabase.getInstance(getApplicationContext());
                items = database.itemDao().getItemList();

                database.itemDao().updateItem(item);

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

        UpdateItems updateItems = new UpdateItems();
        updateItems.execute();
    }

    private void deleteItemList(Item mItem) {

        class DeleteItems extends AsyncTask<Void, Void, List<Item>> {
            @Override
            protected List<Item> doInBackground(Void... voids) {

                database = ItemDatabase.getInstance(getApplicationContext());
                items = database.itemDao().getItemList();

                database.itemDao().deleteItem(mItem);

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

        DeleteItems deleteItems = new DeleteItems();
        deleteItems.execute();
    }
}