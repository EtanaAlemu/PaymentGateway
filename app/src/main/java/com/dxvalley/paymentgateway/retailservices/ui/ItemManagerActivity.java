package com.dxvalley.paymentgateway.retailservices.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dxvalley.paymentgateway.retailservices.ItemsItemClickListener;
import com.dxvalley.paymentgateway.retailservices.adapter.ItemAdapter;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.retailservices.db.ItemDatabase;
import com.dxvalley.paymentgateway.retailservices.models.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.raphaelebner.roomdatabasebackup.core.RoomBackup;

public class ItemManagerActivity extends AppCompatActivity

        implements ItemsItemClickListener {

    private static final String TAG = "MY APP";
    static List<Item> items;
    static ItemDatabase database ;
    LinearLayout mNoItemView;
    ItemAdapter adapter;
    RecyclerView recyclerView;
    RoomBackup roomBackup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_manager);

        mNoItemView = findViewById(R.id.no_item);

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById(R.id.list_item);

        FloatingActionButton addItem = findViewById(R.id.add_item);

        addItem.setOnClickListener(view -> {
            Intent addItemIntent = new Intent(this, AddItemActivity.class);
            addItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(addItemIntent);
        });
        // Initialize contacts
        items = new ArrayList<Item>();
        setUpAdapter();
        getSavedItems();

        roomBackup = new RoomBackup(this);
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

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }

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
            case R.id.action_backup:

                roomBackup.database(ItemDatabase.getInstance(this));
                roomBackup.enableLogDebug(true);
                roomBackup.backupIsEncrypted(false);
                roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_INTERNAL);
                roomBackup.maxFileCount(5);
                roomBackup.onCompleteListener((success, message, exitCode) -> {
                    Log.d(TAG, "success: " + success + ", message: " + message + ", exitCode: " + exitCode);
                    if (success) roomBackup.restartApp(new Intent(getApplicationContext(), ItemManagerActivity.class));
                });
                roomBackup.backup();
                break;

            case R.id.action_restore:

                roomBackup.database(ItemDatabase.getInstance(this));
                roomBackup.enableLogDebug(true);
                roomBackup.backupIsEncrypted(false);
                roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_INTERNAL);
                roomBackup.onCompleteListener((success, message, exitCode) -> {
                    Log.d(TAG, "success: " + success + ", message: " + message + ", exitCode: " + exitCode);
                    if (success) roomBackup.restartApp(new Intent(getApplicationContext(), ItemManagerActivity.class));
                });
                roomBackup.restore();
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

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

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                mNoItemView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                adapter.setItems(items);
                adapter.notifyDataSetChanged();
            }
        }

        GetSavedItems savedTasks = new GetSavedItems();
        savedTasks.execute();
    }


    @Override
    public void onItemClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        final Item item = items.get(position);
        Intent i = new Intent(this, EditItemActivity.class);
        i.putExtra("id", item.getId());
        i.putExtra("name", item.getName());
        i.putExtra("image", item.getImageName());
        i.putExtra("price", item.getPrice());

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)view, "item_image");
        startActivity(i, options.toBundle());
    }

}
