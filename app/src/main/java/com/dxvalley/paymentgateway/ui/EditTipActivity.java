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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.db.ItemDatabase;
import com.dxvalley.paymentgateway.db.TipDatabase;
import com.dxvalley.paymentgateway.models.Item;
import com.dxvalley.paymentgateway.models.Tip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class EditTipActivity extends AppCompatActivity {

    static List<Tip> items;
    static TipDatabase database ;
    int id;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tip);

        RadioGroup radioGroup = findViewById(R.id.unit_radio);
        RadioButton percent = findViewById(R.id.by_percent);
        RadioButton fixed = findViewById(R.id.by_fixed);
        TextInputEditText value = findViewById(R.id.tip_value);
        Button saveTip = findViewById(R.id.save);
        Button cancelTip = findViewById(R.id.cancel);
        mContext = this;

        int id;
        boolean isByPercent;
        float sValue;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id = 0;
                isByPercent = true;
                sValue = 0f;
            } else {
                id = extras.getInt("id");
                isByPercent = extras.getBoolean("isByPercent");
                sValue= extras.getFloat("value");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("id");
            isByPercent= (boolean) savedInstanceState.getSerializable("isByPercent");
            sValue= (float) savedInstanceState.getSerializable("value");
        }

        radioGroup.check(isByPercent?percent.getId():fixed.getId());
        value.setText(String.valueOf(sValue));

        saveTip.setOnClickListener(view -> {
            boolean mIsByPercent = percent.isChecked();
            float mValue = Float.parseFloat(value.getText().toString());
            Tip tip = new Tip(id,mIsByPercent,mValue);
            updateTip(tip);
        });

        cancelTip.setOnClickListener(view -> {
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
                        .setTitle("Delete Tip")
                        .setMessage("Are you sure you want to delete this tip?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                Tip tip = new Tip(id,false,0f);
                                deleteTip(tip);
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
    private void updateTip(Tip tip) {

        class UpdateTip extends AsyncTask<Void, Void, List<Tip>> {
            @Override
            protected List<Tip> doInBackground(Void... voids) {

                database = TipDatabase.getInstance(getApplicationContext());
                items = database.tipDao().getTipList();

                database.tipDao().updateTip(tip);

                return items;
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

        UpdateTip updateTip = new UpdateTip();
        updateTip.execute();
    }

    private void deleteTip(Tip tip) {

        class DeleteTip extends AsyncTask<Void, Void, List<Tip>> {
            @Override
            protected List<Tip> doInBackground(Void... voids) {

                database = TipDatabase.getInstance(getApplicationContext());
                items = database.tipDao().getTipList();

                database.tipDao().deleteTip(tip);

                return items;
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

        DeleteTip deleteTip = new DeleteTip();
        deleteTip.execute();
    }
}