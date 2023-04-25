package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.ServicesActivity;
import com.dxvalley.paymentgateway.otherservices.adapter.NedajAdapter;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;

public class NedajActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private final String PUBLIC =  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1mBW45vx9C2msYztwWcu+2y9bbUw60kVHcJxE7j4kzLFXJwxPCFK0PsEoO1lk3LJHf83mpfK3FrR2BSnqmbxZR9PJQg8I2Tr7UsOBSL09uRTj3phVrk/FrqDYsul1f/L5v/eTA3tuHysWveP8QLbVgxW3Uwl+CNw2CszEtwnbQZsMq9ZB8HNaUCeWC+OiWlETMQoj1rhTFe2oVH7J+5MhQR1ZbLFxZWdGdwtrNjJ62AaVAPKYEUUky/PbztRvdyhnbz69KKyGwiPGj/GyodxrA3jKjPz+m0oRM9/14VQ6trpliVXDWf+qTU3C10kUovc8si3J3yA3GkwMQBwCSq0uQIDAQAB";
    private final String PRIVATE =  "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDWYFbjm/H0LaaxjO3BZy77bL1ttTDrSRUdwnETuPiTMsVcnDE8IUrQ+wSg7WWTcskd/zeal8rcWtHYFKeqZvFlH08lCDwjZOvtSw4FIvT25FOPemFWuT8WuoNiy6XV/8vm/95MDe24fKxa94/xAttWDFbdTCX4I3DYKzMS3CdtBmwyr1kHwc1pQJ5YL46JaURMxCiPWuFMV7ahUfsn7kyFBHVlssXFlZ0Z3C2s2MnrYBpUA8pgRRSTL89vO1G93KGdvPr0orIbCI8aP8bKh3GsDeMqM/P6bShEz3/XhVDq2umWJVcNZ/6pNTcLXSRSi9zyyLcnfIDcaTAxAHAJKrS5AgMBAAECggEAGWSyBiYLxAPAUhnCc/1X+FpKGfuzfrtab/r/T/nCIBWMgUrTHi9HYHfLdZCJTqzzG8WGfZ5rXiy33KOdtLaa84KA6n8OOJcUxzfzwjIA2Fn1d/JkI1SwH24acBJ1Y2L4cTVvzIx90zOZavEWMNNIzemKRI5nlPwFuuVrGFnRpb08XNQvil1MXfKQWNRuLcN+r1EHkpKqPfLDjhK5621JhrKrwgWqkPaK5XgTf1uKkwL+ZySScRbUVAhJG/sSZ1IwMRl/KzSHOhax7qEi+TfHouOWGxG+RqlNvDrL2C/DMGoLU+fGeMxYq9iXlapGdzBVhswkpF7kR9AvSmg5qxFyrQKBgQD8x16T9ziI1wXpZO0FJ8+c67Ufd5C1bSpEG1YfCTLTBi9HnoQrMb9T9ymyfgI/u6qQi1HRztyLYlIunPtUWqHZjQo5uB0m/vhB5P/n92lJIU86S4cKEoyRGG/iedBLXlDkA1RVhMB1rYT2nZWfPhjMXPPauvXONV3AADO1Jgzo8wKBgQDZG7DrGhJlFA/TvNr0rovSWrQfKuiVDEovyI4UcIqSQvkb0NSOr16EoCRzWmY4+pj3bYPIslfm2ZaoO3kxLnJt/uam1OsIPJ1PcJKfZfjmNAqBvNAnl6ATFUWoz0J6+Vr44QjKYKBdFaH1XWT6/XidQrUfaoeqawLkz4AwJLyWowKBgGu1M+qOe8tq+7zgYVJCDWfK06lt0/5KXqkYkNC2pa5fQ0QcGishjmnjtiO1J5Yqi9n9U0a4AydtJKFyCHGAENjXDRVdCybzm6rQPe6EcJtVkyG+zvKOxtCIfhwdVZDXxlXxyTyLTUqXPkGrEfcBiaWCsfFwmo5cFO9b5qx4YyXbAoGAINbgbt9VsvZS4ospb2NLgPj5T9GUtp7SReIHI65WN4Nr3Lo8vIxoNpVmjhA5cBrvslVdXqkjRKba8/1y+m51HpA04T1Jg8hvXwm/E98/w8pRYIhnz+VOcDSCgeM/wgwfp4+aXco513qjMdL7qD9Y1Ci37tWVScDAAk4krKOR5xMCgYEA+QXicf1ROljFhR93cChXOUILbl0HL1/iF2wzSg/bBe4soW42Hwd5CblKZqziUSf7GvIB3j8pLVqHnHQMgaTEPUe+qGBMcxVQg7zUzw/gleeuEkchp3YsjCiqqcVFxOH+8WQk4+gxEo9onYXmYnzpFThQVFEYjqzbXpVnQyCGxws=";
    private static  String fuelName ;
    private static float priceLiter ;
    RecyclerView fuelRecyclerView;
    NedajAdapter nedajAdapter;
    ArrayList<Fuel> fuels;

    TextInputEditText mAmount ;
    TextInputEditText mLiter ;
    TextInputEditText mPlateNumber;
    Spinner mPlateCode;
    Spinner mPlateRegion;

    String username;
    String amount;
    String liter;
    String plateCode;
    String plateNumber;
    String plateRegion;
    NestedScrollView rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedaj);
        mAmount = findViewById(R.id.amount);
        mLiter = findViewById(R.id.liter);
        mPlateNumber = findViewById(R.id.plate_number);
        mPlateCode = findViewById(R.id.plate_code);
        mPlateRegion = findViewById(R.id.plate_number_region);
        Button mProceed = findViewById(R.id.proceed);
        rootView = findViewById(R.id.root_view);

        fuelRecyclerView = findViewById(R.id.fuel_rv);

        // on below line getting data from shared preferences.
        // creating a master key for encryption of shared preferences.
        String masterKeyAlias = null;
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
                    mLiter.setText(String.valueOf(amount/priceLiter));
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
                    mAmount.setText(String.valueOf(priceLiter*liter));
            }
        });



        // Spinner click listener
        mPlateCode.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mPlateCode.setAdapter(dataAdapter);

        mProceed.setOnClickListener(this);

        // Initialize contacts
        fuels = new ArrayList<Fuel>();
        fuels.add(new Fuel("Regular",60.0f));
        fuels.add(new Fuel("Diesel",70.0f));
        setUpAdapter();
    }

    public void setUpAdapter() {
        // Create adapter passing in the sample user data
        nedajAdapter = new NedajAdapter(fuels, new NedajItemClickListener() {
            @Override
            public void onItemClick(Fuel fuel) {

                fuelName = fuel.getType();
                priceLiter = fuel.getPrice();
                float amount = 0f;
                if(!Objects.requireNonNull(mAmount.getText()).toString().isEmpty())
                    amount = Float.parseFloat(String.valueOf(mAmount.getText()));
                mLiter.setText(String.valueOf(amount/priceLiter));

            }
        });
        // Attach the adapter to the recyclerview to populate items
        fuelRecyclerView.setAdapter(nedajAdapter);
        int spanCount;
        int orientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount=2;
        } else {
            spanCount=2;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        // Set layout manager to position the items
        fuelRecyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onResume() {
        super.onResume();
        nedajAdapter.notifyDataSetChanged();
    }



    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.nedaj_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.report:
                startActivity(new Intent(NedajActivity.this, NedajTransactionActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
//        if(view.getId() == R.id.proceed){
            amount = mAmount.getText().toString();
            liter = mLiter.getText().toString();
//            plateRegion = mPlateRegion.getText().toString();
//            plateCode = mPlateCode.getText().toString();
            plateNumber = mPlateNumber.getText().toString();

            if(amount.isEmpty()||liter.isEmpty()||plateNumber.isEmpty()||plateCode.isEmpty()||plateRegion.isEmpty()){
                Snackbar snackbar = Snackbar
                        .make(rootView, "Fill all fields", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                proceed();
            }

//        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    private void proceed(){
        String text = "{\n\"MerchantId\": \"" + username + "\"," +
                "\n\"AgentId\": \"" + username + "\"," +
                "\n\"Amount\": \"" + amount + "\"," +
                "\n\"Liter\": \"" + liter + "\"," +
                "\n\"FuelType\": \""+fuelName+"\"," +
                "\n\"PlateRegion\": \""+plateRegion+"\"," +
                "\n\"PlateCode\": \""+plateCode+"\"," +
                "\n\"PlateNumber\": \"" + plateNumber + "\"\n}";
        KeyPair keypair
                = null;
        byte[] cipherText
                = new byte[0];
        try {
            keypair = generateRSAKkeyPair();

            cipherText = do_RSAEncryption(
                    text,
                    keypair.getPrivate());
//                String encryptedText = do_RSAEncryption(
//                        text,
//                        keypair.getPrivate()).toString();
//                String decryptedText
//                        = do_RSADecryption(
//                        cipherText,
//                        keypair.getPublic());

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            LayoutInflater factory = LayoutInflater.from(this);
            final View qrDialogView = factory.inflate(R.layout.qr_dialog, null);
            final AlertDialog qrDialog = new AlertDialog.Builder(this).create();
            qrDialog.setView(qrDialogView);
            ImageView imageView = qrDialogView.findViewById(R.id.qr);
            Button mFinish = qrDialogView.findViewById(R.id.finish);


//                TextView pubKey = qrDialogView.findViewById(R.id.pubKey);
//                TextView prvKey = qrDialogView.findViewById(R.id.prvKey);
//                TextView encrypted = qrDialogView.findViewById(R.id.encrypted);
//                TextView decrypted = qrDialogView.findViewById(R.id.decrypted);

//                System.out.println("PUBLIC:   "+Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded()));
//                System.out.println("PRIVATE:  "+Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded()));
//                encrypted.setText("ENCRYPTED:  "+Base64.getEncoder().encodeToString(cipherText) );
//                decrypted.setText("DECRYPTED:  "+decryptedText);


            mFinish.setOnClickListener(v1 -> {
                //your business logic
                qrDialog.dismiss();
                mAmount.setText(null);
                mLiter.setText(null);
                mPlateNumber.setText(null);
//                mPlateCode.setText(null);
//                mPlateRegion.setText(null);
            });
//                BitMatrix bitMatrix = multiFormatWriter.encode(Base64.getEncoder().encodeToString(cipherText), BarcodeFormat.QR_CODE, 1000, 1000);
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            qrDialog.setCancelable(false);
            qrDialog.setCanceledOnTouchOutside(false);
            qrDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static KeyPair generateRSAKkeyPair()
            throws Exception
    {
        SecureRandom secureRandom
                = new SecureRandom();
        KeyPairGenerator keyPairGenerator
                = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(
                2048, secureRandom);
        return keyPairGenerator
                .generateKeyPair();
    }

    public static byte[] do_RSAEncryption(
            String plainText,
            PrivateKey privateKey)
            throws Exception
    {
        Cipher cipher
                = Cipher.getInstance("RSA");

        cipher.init(
                Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(
                plainText.getBytes());
    }
    public static String do_RSADecryption(
            byte[] cipherText,
            PublicKey publicKey)
            throws Exception
    {
        Cipher cipher
                = Cipher.getInstance("RSA");

        cipher.init(Cipher.DECRYPT_MODE,
                publicKey);
        byte[] result
                = cipher.doFinal(cipherText);

        return new String(result);
    }

}