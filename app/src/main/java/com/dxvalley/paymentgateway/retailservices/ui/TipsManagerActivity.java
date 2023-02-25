package com.dxvalley.paymentgateway.retailservices.ui;

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

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.retailservices.TipItemClickListener;
import com.dxvalley.paymentgateway.retailservices.adapter.TipAdapter;
import com.dxvalley.paymentgateway.retailservices.db.TipDatabase;
import com.dxvalley.paymentgateway.retailservices.models.Tip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TipsManagerActivity extends AppCompatActivity implements TipItemClickListener {


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

        // Lookup the recyclerview in activity layout
        recyclerView = findViewById(R.id.tips_rv);

        FloatingActionButton addTip = findViewById(R.id.add_tip);
        addTip.setOnClickListener(view -> {

            Intent addTipIntent = new Intent(this, AddTipActivity.class);
            addTipIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(addTipIntent);
        });

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
//        getMenuInflater().inflate(R.menu.setting_menu, menu);

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
    public void onTipClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        final Tip tip = tips.get(position);
        Intent i = new Intent(this, EditTipActivity.class);
        i.putExtra("id", tip.getId());
        i.putExtra("isByPercent", tip.isByPercent());
        i.putExtra("value", tip.getValue());
        startActivity(i);
    }

}
