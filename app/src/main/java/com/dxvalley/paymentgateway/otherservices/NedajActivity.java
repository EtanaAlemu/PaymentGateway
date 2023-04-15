package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dxvalley.paymentgateway.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class NedajActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private static final String[] fuel = {"Regular", "Diesel"};
    private static final float[] price = {65.00F, 70.00F};
    private int fuelTypeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedaj);

        TextInputEditText mAmount = findViewById(R.id.amount);
        TextInputEditText mLiter = findViewById(R.id.liter);
        TextInputEditText mPlateNumber = findViewById(R.id.plate_number);
        Button mProceed = findViewById(R.id.proceed);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NedajActivity.this,
                android.R.layout.simple_spinner_item, fuel);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // on below line getting data from shared preferences.
        // creating a master key for encryption of shared preferences.
        String masterKeyAlias = null;
        String username;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

                // Initialize/open an instance of EncryptedSharedPreferences on below line.
                SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                        // passing a file name to share a preferences
                        "MerchantInfo",
                        masterKeyAlias,
                        getApplicationContext(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
                // on below line creating a variable
                // to get the data from shared prefs.
                username = sharedPreferences.getString("username", "");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else
        {

            // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
            SharedPreferences sh = getSharedPreferences("MerchantInfo", Context.MODE_PRIVATE);
            // first time when the app is opened, there is nothing to show
            username = sh.getString("username", "");

        }

        mAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                float amount =0f;
                if(s.length()>0)
                    amount = Float.parseFloat(s.toString());
                if(mAmount.isFocused())
                    mLiter.setText(String.valueOf(amount/price[fuelTypeSelected]));
            }
        });
        mLiter.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                float liter =0f;
                if(s.length()>0)
                    liter = Float.parseFloat(s.toString());

                if(mLiter.isFocused())
                    mAmount.setText(String.valueOf(price[fuelTypeSelected]*liter));
            }
        });


        mProceed.setOnClickListener(v -> {
            String text = "{\n\"MerchantId\": \"" + username + "\"," +
                    "\n\"Name\": \"" + username + "\"," +
                    "\n\"Amount\": \"" + mAmount.getText() + "\"," +
                    "\n\"Liter\": \"" + mLiter.getText() + "\"," +
//                    "\n\"PlateNumber\": \"" + mPlateNumber.getText() + "\"," +
                    "\n\"PlateNumber\": \"" + mPlateNumber.getText() + "\"\n}";
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {

                LayoutInflater factory = LayoutInflater.from(this);
                final View qrDialogView = factory.inflate(R.layout.qr_dialog, null);

                final AlertDialog qrDialog = new AlertDialog.Builder(this).create();
                qrDialog.setView(qrDialogView);
                ImageView imageView = qrDialogView.findViewById(R.id.qr);
                imageView.setOnClickListener(v1 -> {
                    //your business logic
                    qrDialog.dismiss();
                });
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 1000, 1000);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
                qrDialog.show();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                fuelTypeSelected = 0;
                break;
            case 1:
                fuelTypeSelected = 1;
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}