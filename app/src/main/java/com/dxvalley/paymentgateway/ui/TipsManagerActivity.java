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

import com.dxvalley.paymentgateway.ItemClickListener;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.adapter.TipAdapter;
import com.dxvalley.paymentgateway.db.TipDatabase;
import com.dxvalley.paymentgateway.models.Item;
import com.dxvalley.paymentgateway.models.Tip;

import java.util.ArrayList;
import java.util.List;

public class TipsManagerActivity extends AppCompatActivity implements ItemClickListener {


    static List<Tip> tips;
    static TipDatabase database ;
    LinearLayout mNoTipView;
    LinearLayout mProgressView;
    TipAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_manager);

        mNoTipView = findViewById(R.id.no_item);
        mProgressView =findViewById(R.id.progress);

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById(R.id.tips_rv);

        // Initialize contacts
        tips = new ArrayList<Tip>();
        setUpAdapter();
        getSavedTips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedTips();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Tips");
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
                Intent addTipIntent = new Intent(this, AddTipActivity.class);
                addTipIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addTipIntent);
//                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpAdapter() {
        // Create adapter passing in the sample user data
        adapter = new TipAdapter(tips);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter.setClickListener(this); // Bind the listener

    }

    private void getSavedTips() {

        class GetSavedTips extends AsyncTask<Void, Void, List<Tip>> {
            @Override
            protected List<Tip> doInBackground(Void... voids) {

                database = TipDatabase.getInstance(getApplicationContext());
                tips = database.tipDao().getTipList();
                return tips;
            }

            @Override
            protected void onPostExecute(List<Tip> tips) {
                super.onPostExecute(tips);
                mNoTipView.setVisibility(tips.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTips savedTips = new GetSavedTips();
        savedTips.execute();
    }


    @Override
    public void onClick(View view, int position, String adapter) {
        // The onClick implementation of the RecyclerView item click
        final Tip tip = tips.get(position);
        Intent i = new Intent(this, EditTipActivity.class);
        i.putExtra("id", tip.getId());
        i.putExtra("isByPercent", tip.isByPercent());
        i.putExtra("value", tip.getValue());
        startActivity(i);
    }

}
