package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.otherservices.adapter.NedajAdapter;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
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
import java.util.Objects;

import javax.crypto.Cipher;

public class NedajActivity extends AppCompatActivity {

    private static  String fuelName ;
    private static float priceLiter ;
    RecyclerView fuelRecyclerView;
    NedajAdapter nedajAdapter;
    ArrayList<Fuel> fuels;

    TextInputEditText mAmount ;
    TextInputEditText mLiter ;
    TextInputEditText mPlateNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedaj);
        mAmount = findViewById(R.id.amount);
        mLiter = findViewById(R.id.liter);
        mPlateNumber = findViewById(R.id.plate_number);
        Button mProceed = findViewById(R.id.proceed);

        fuelRecyclerView = findViewById(R.id.fuel_rv);

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


        mProceed.setOnClickListener(v -> {
            String text = "{\n\"MerchantId\": \"" + username + "\"," +
                    "\n\"AgentId\": \"" + username + "\"," +
                    "\n\"Amount\": \"" + mAmount.getText() + "\"," +
                    "\n\"Liter\": \"" + mLiter.getText() + "\"," +
                    "\n\"FuelType\": \""+fuelName+"\"," +
                    "\n\"PlateNumber\": \"" + mPlateNumber.getText() + "\"\n}";
            KeyPair keypair
                    = null;
            byte[] cipherText
                    = new byte[0];
            try {
                keypair = generateRSAKkeyPair();

                cipherText = do_RSAEncryption(
                text,
                keypair.getPrivate());
                String encryptedText = do_RSAEncryption(
                        text,
                        keypair.getPrivate()).toString();
                String decryptedText
                        = do_RSADecryption(
                        cipherText,
                        keypair.getPublic());

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

                System.out.println("PUBLIC:   "+Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded()));
                System.out.println("PRIVATE:  "+Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded()));
//                encrypted.setText("ENCRYPTED:  "+Base64.getEncoder().encodeToString(cipherText) );
//                decrypted.setText("DECRYPTED:  "+decryptedText);


                mFinish.setOnClickListener(v1 -> {
                    //your business logic
                    qrDialog.dismiss();
                    mAmount.setText(null);
                    mLiter.setText(null);
                    mPlateNumber.setText(null);
                });
                BitMatrix bitMatrix = multiFormatWriter.encode(Base64.getEncoder().encodeToString(cipherText), BarcodeFormat.QR_CODE, 1000, 1000);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
                qrDialog.setCancelable(false);
                qrDialog.setCanceledOnTouchOutside(false);
                qrDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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