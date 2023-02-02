package com.dxvalley.paymentgateway.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.db.ItemDatabase;
import com.dxvalley.paymentgateway.db.TipDatabase;
import com.dxvalley.paymentgateway.models.Item;
import com.dxvalley.paymentgateway.models.Tip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AddTipActivity extends AppCompatActivity {

    static List<Tip> tips;
    static TipDatabase database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        RadioGroup radioGroup = findViewById(R.id.unit_radio);
        RadioButton percent = findViewById(R.id.by_percent);
        RadioButton fixed = findViewById(R.id.by_fixed);
        TextInputEditText value = findViewById(R.id.tip_value);
        Button saveTip = findViewById(R.id.save);
        Button cancelTip = findViewById(R.id.cancel);

        saveTip.setOnClickListener(view -> {

            float mValue = Float.parseFloat(value.getText().toString());
            boolean isByPercent = percent.isChecked();
            Tip tip = new Tip(isByPercent,mValue);
            createTip(tip);
        });

        cancelTip.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add Tip");
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
    private void createTip(Tip mTip) {

        class CreateTips extends AsyncTask<Void, Void, List<Tip>> {
            @Override
            protected List<Tip> doInBackground(Void... voids) {

                database = TipDatabase.getInstance(getApplicationContext());
                tips = database.tipDao().getTipList();

                database.tipDao().insertTip(mTip);

                return tips;
            }

            @Override
            protected void onPostExecute(List<Tip> tips) {
                super.onPostExecute(tips);
//                Intent settingIntent = new Intent(AddItemActivity.this, SettingActivity.class);
//                settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(settingIntent);
                finish();
            }
        }

        CreateTips savedTasks = new CreateTips();
        savedTasks.execute();
    }
}