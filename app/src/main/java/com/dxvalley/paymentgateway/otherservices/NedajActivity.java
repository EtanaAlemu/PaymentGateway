package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.ServicesActivity;
import com.dxvalley.paymentgateway.otherservices.adapter.NedajAdapter;
import com.dxvalley.paymentgateway.otherservices.model.Fuel;
import com.dxvalley.paymentgateway.util.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.Cipher;

public class NedajActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MY_APP";
    private static final String PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1mBW45vx9C2msYztwWcu+2y9bbUw60kVHcJxE7j4kzLFXJwxPCFK0PsEoO1lk3LJHf83mpfK3FrR2BSnqmbxZR9PJQg8I2Tr7UsOBSL09uRTj3phVrk/FrqDYsul1f/L5v/eTA3tuHysWveP8QLbVgxW3Uwl+CNw2CszEtwnbQZsMq9ZB8HNaUCeWC+OiWlETMQoj1rhTFe2oVH7J+5MhQR1ZbLFxZWdGdwtrNjJ62AaVAPKYEUUky/PbztRvdyhnbz69KKyGwiPGj/GyodxrA3jKjPz+m0oRM9/14VQ6trpliVXDWf+qTU3C10kUovc8si3J3yA3GkwMQBwCSq0uQIDAQAB";
    private static final String PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDWYFbjm/H0LaaxjO3BZy77bL1ttTDrSRUdwnETuPiTMsVcnDE8IUrQ+wSg7WWTcskd/zeal8rcWtHYFKeqZvFlH08lCDwjZOvtSw4FIvT25FOPemFWuT8WuoNiy6XV/8vm/95MDe24fKxa94/xAttWDFbdTCX4I3DYKzMS3CdtBmwyr1kHwc1pQJ5YL46JaURMxCiPWuFMV7ahUfsn7kyFBHVlssXFlZ0Z3C2s2MnrYBpUA8pgRRSTL89vO1G93KGdvPr0orIbCI8aP8bKh3GsDeMqM/P6bShEz3/XhVDq2umWJVcNZ/6pNTcLXSRSi9zyyLcnfIDcaTAxAHAJKrS5AgMBAAECggEAGWSyBiYLxAPAUhnCc/1X+FpKGfuzfrtab/r/T/nCIBWMgUrTHi9HYHfLdZCJTqzzG8WGfZ5rXiy33KOdtLaa84KA6n8OOJcUxzfzwjIA2Fn1d/JkI1SwH24acBJ1Y2L4cTVvzIx90zOZavEWMNNIzemKRI5nlPwFuuVrGFnRpb08XNQvil1MXfKQWNRuLcN+r1EHkpKqPfLDjhK5621JhrKrwgWqkPaK5XgTf1uKkwL+ZySScRbUVAhJG/sSZ1IwMRl/KzSHOhax7qEi+TfHouOWGxG+RqlNvDrL2C/DMGoLU+fGeMxYq9iXlapGdzBVhswkpF7kR9AvSmg5qxFyrQKBgQD8x16T9ziI1wXpZO0FJ8+c67Ufd5C1bSpEG1YfCTLTBi9HnoQrMb9T9ymyfgI/u6qQi1HRztyLYlIunPtUWqHZjQo5uB0m/vhB5P/n92lJIU86S4cKEoyRGG/iedBLXlDkA1RVhMB1rYT2nZWfPhjMXPPauvXONV3AADO1Jgzo8wKBgQDZG7DrGhJlFA/TvNr0rovSWrQfKuiVDEovyI4UcIqSQvkb0NSOr16EoCRzWmY4+pj3bYPIslfm2ZaoO3kxLnJt/uam1OsIPJ1PcJKfZfjmNAqBvNAnl6ATFUWoz0J6+Vr44QjKYKBdFaH1XWT6/XidQrUfaoeqawLkz4AwJLyWowKBgGu1M+qOe8tq+7zgYVJCDWfK06lt0/5KXqkYkNC2pa5fQ0QcGishjmnjtiO1J5Yqi9n9U0a4AydtJKFyCHGAENjXDRVdCybzm6rQPe6EcJtVkyG+zvKOxtCIfhwdVZDXxlXxyTyLTUqXPkGrEfcBiaWCsfFwmo5cFO9b5qx4YyXbAoGAINbgbt9VsvZS4ospb2NLgPj5T9GUtp7SReIHI65WN4Nr3Lo8vIxoNpVmjhA5cBrvslVdXqkjRKba8/1y+m51HpA04T1Jg8hvXwm/E98/w8pRYIhnz+VOcDSCgeM/wgwfp4+aXco513qjMdL7qD9Y1Ci37tWVScDAAk4krKOR5xMCgYEA+QXicf1ROljFhR93cChXOUILbl0HL1/iF2wzSg/bBe4soW42Hwd5CblKZqziUSf7GvIB3j8pLVqHnHQMgaTEPUe+qGBMcxVQg7zUzw/gleeuEkchp3YsjCiqqcVFxOH+8WQk4+gxEo9onYXmYnzpFThQVFEYjqzbXpVnQyCGxws=";
    private static String fuelName;
    private static float priceLiter;
    RecyclerView fuelRecyclerView;
    NedajAdapter nedajAdapter;
    ArrayList<Fuel> fuels;

    TextInputEditText mAmount;
    TextInputEditText mLiter;
    TextInputEditText mPlateNumber;
    Spinner mPlateCode;
    Spinner mPlateRegion;

    String username;
    String amount;
    String liter;
    String plateCode;
    String plateNumber;
    String plateRegion;
    LinearLayout rootView;

    LinearLayout fuelLayout;
    SwipeRefreshLayout reloadLayout;
    Button reload;
    private String url;

    private RequestQueue mRequestQueue;
    private StringRequest mRequest;
    private Context mContext;

    String fname;
    String lname;
    String merchantId;
    String agentId;

    String[] code;

    LinearLayout otpRoot;

    boolean sendOtpStatus = false;
    boolean confirmOtpStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedaj);
        mAmount = findViewById(R.id.amount);
        mLiter = findViewById(R.id.liter);
        mPlateNumber = findViewById(R.id.plate_number);
        mPlateCode = findViewById(R.id.plate_code);
        mPlateRegion = findViewById(R.id.plate_number_region);
        Button mQr = findViewById(R.id.qr);
        Button mOtp = findViewById(R.id.otp);
        rootView = findViewById(R.id.root_view);
        fuelLayout = findViewById(R.id.fuel_layout);
        reloadLayout = findViewById(R.id.reload_layout);
        reload = findViewById(R.id.reload);

        TextView mWelcome = findViewById(R.id.welcome);

        fuelRecyclerView = findViewById(R.id.fuel_rv);

        code = getResources().getStringArray(R.array.plate_code_data);

        // on below line getting data from shared preferences.
        // creating a master key for encryption of shared preferences.
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // Initialize/open an instance of EncryptedSharedPreferences on below line.
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    // passing a file name to share a preferences
                    "UserInfo",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            // on below line creating a variable
            // to get the data from shared prefs.
            fname = sharedPreferences.getString("fname", "");
            lname = sharedPreferences.getString("lname", "");
            merchantId = sharedPreferences.getString("merchant_id", "-1");
            agentId = String.valueOf(sharedPreferences.getInt("agent_id", -1));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        url = getString(R.string.base_url) + "/api/user/" + merchantId + "/latest_nedaj_prices";
        mWelcome.setText("Welcome " + fname + " " + lname);
        mAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                float amount = 0f;
                if (s.length() > 0)
                    amount = Float.parseFloat(s.toString());
                if (mAmount.isFocused())
                    mLiter.setText(String.valueOf(amount / priceLiter));
            }
        });
        mLiter.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                float liter = 0f;
                if (s.length() > 0)
                    liter = Float.parseFloat(s.toString());

                if (mLiter.isFocused())
                    mAmount.setText(String.valueOf(priceLiter * liter));
            }
        });

        // Spinner click listener
        mPlateCode.setOnItemSelectedListener(this);
        mPlateRegion.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<CharSequence> plateCodeAdapter = ArrayAdapter.createFromResource(this, R.array.plate_code_data, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> plateRegionAdapter = ArrayAdapter.createFromResource(this, R.array.plate_region_data, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        plateCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plateRegionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mPlateCode.setAdapter(plateCodeAdapter);
        mPlateRegion.setAdapter(plateRegionAdapter);

        mPlateCode.setSelection(2);
        mPlateRegion.setSelection(1);

        mQr.setOnClickListener(this);
        mOtp.setOnClickListener(this);
        reload.setOnClickListener(this);

        // Initialize contacts
        fuels = new ArrayList<>();

        reloadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFuels();
                reloadLayout.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFuels();

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
                if (!Objects.requireNonNull(mAmount.getText()).toString().isEmpty())
                    amount = Float.parseFloat(String.valueOf(mAmount.getText()));
                mLiter.setText(String.valueOf(amount / priceLiter));

            }
        });
        // Attach the adapter to the recyclerview to populate items
        fuelRecyclerView.setAdapter(nedajAdapter);

        fuelRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // Set layout manager to position the items
        fuelRecyclerView.setLayoutManager(gridLayoutManager);


    }

    public void loadFuels() {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        fuels = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i(TAG, "Response :" + response);

                pDialog.cancel();
                JSONArray mJsonArray = null;
                try {
                    mJsonArray = new JSONArray(response);
                    //Checking whether the JSON array has some value or not

                    Log.i(TAG, "JSONArray :" + mJsonArray);
                    //Iterating JSON array
                    for (int i = 0; i < mJsonArray.length(); i++) {

                        JSONObject currentItem = mJsonArray.getJSONObject(i);

                        //Adding each element of JSON array into ArrayList
                        fuels.add(new Fuel(currentItem.getString("nedaj_type"), Float.parseFloat(currentItem.getString("prices"))));

                    }

                    fuelLayout.setVisibility(View.VISIBLE);
                    reloadLayout.setVisibility(View.GONE);

                    nedajAdapter.notifyDataSetChanged();

                } catch (JSONException e) {

                    Log.i(TAG, "JSONException :" + e.getMessage());
                    fuelLayout.setVisibility(View.GONE);
                    reloadLayout.setVisibility(View.VISIBLE);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error :" + error.toString());

                pDialog.cancel();

                fuelLayout.setVisibility(View.GONE);
                reloadLayout.setVisibility(View.VISIBLE);


            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(mRequest);

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
        int id = view.getId();
        String[] plateRegionValues;
        String[] plateCodeValues;
        switch (id) {
            case R.id.qr:

                plateRegionValues = getResources().getStringArray(R.array.plate_region_data);
                plateCodeValues = getResources().getStringArray(R.array.plate_code_data);
                amount = mAmount.getText().toString();
                liter = mLiter.getText().toString();
                plateRegion = plateRegionValues[mPlateRegion.getSelectedItemPosition()];
                plateCode = plateCodeValues[mPlateCode.getSelectedItemPosition()];
                plateNumber = mPlateNumber.getText().toString();


                if (plateCode.equals(plateCodeValues[5]) || plateCode.equals(plateCodeValues[6]) || plateCode.equals(plateCodeValues[7]))
                    plateRegion = "";
                else
                    plateRegion = plateRegionValues[mPlateRegion.getSelectedItemPosition()];

                if (amount.isEmpty() || liter.isEmpty() || plateNumber.isEmpty() || plateCode.isEmpty() || plateRegion.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(rootView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    qrDialog();
                }
                break;
            case R.id.otp:

                plateRegionValues = getResources().getStringArray(R.array.plate_region_data);
                plateCodeValues = getResources().getStringArray(R.array.plate_code_data);
                amount = mAmount.getText().toString();
                liter = mLiter.getText().toString();
                plateRegion = plateRegionValues[mPlateRegion.getSelectedItemPosition()];
                plateCode = plateCodeValues[mPlateCode.getSelectedItemPosition()];
                plateNumber = mPlateNumber.getText().toString();


                if (plateCode.equals(plateCodeValues[5]) || plateCode.equals(plateCodeValues[6]) || plateCode.equals(plateCodeValues[7]))
                    plateRegion = "";
                else
                    plateRegion = plateRegionValues[mPlateRegion.getSelectedItemPosition()];

                if (amount.isEmpty() || liter.isEmpty() || plateNumber.isEmpty() || plateCode.isEmpty() || plateRegion.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(rootView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    otpDialog();
                }
                break;
            case R.id.reload:
                loadFuels();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (item.equals(code[5]) || item.equals(code[6]) || item.equals(code[7]))
            mPlateRegion.setEnabled(false);
        else
            mPlateRegion.setEnabled(true);

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void qrDialog() {
        String text = "{\n\"MerchantId\": \"" + merchantId + "\"," +
                "\n\"AgentId\": \"" + agentId + "\"," +
                "\n\"Amount\": \"" + amount + "\"," +
                "\n\"Liter\": \"" + liter + "\"," +
                "\n\"FuelType\": \"" + fuelName + "\"," +
                "\n\"PlateRegion\": \"" + plateRegion + "\"," +
                "\n\"PlateCode\": \"" + plateCode + "\"," +
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
            Hashtable hints = new Hashtable(); //important
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //important


//                BitMatrix bitMatrix = multiFormatWriter.encode(Base64.getEncoder().encodeToString(cipherText), BarcodeFormat.QR_CODE, 1000, 1000);
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 1000, 1000, hints);
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
    TextView time,message,debAmount,txId;
    Button sendOtp,confirmOtp,done;
    LinearLayout sendOtpView,confirmOtpView,paymentResult;
    TextInputEditText phoneNum,otpNum,tipCode;
    private void otpDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View otpDialogView = factory.inflate(R.layout.otp_dialog, null);
        final AlertDialog otpDialog = new AlertDialog.Builder(this).create();
        otpDialog.setView(otpDialogView);
         phoneNum = otpDialogView.findViewById(R.id.phone_num);
        otpNum = otpDialogView.findViewById(R.id.otp);
        tipCode = otpDialogView.findViewById(R.id.tip_code);
         sendOtp = otpDialogView.findViewById(R.id.send_otp);
         confirmOtp = otpDialogView.findViewById(R.id.confirm_otp);
         done = otpDialogView.findViewById(R.id.done);
         sendOtpView = otpDialogView.findViewById(R.id.send_otp_view);
         confirmOtpView = otpDialogView.findViewById(R.id.confirm_otp_view);
         paymentResult = otpDialogView.findViewById(R.id.payment_result);
         time = otpDialogView.findViewById(R.id.TRANSACTION_DATE);
         message = otpDialogView.findViewById(R.id.Message);
         debAmount = otpDialogView.findViewById(R.id.DEBITAMOUNT);
         txId = otpDialogView.findViewById(R.id.transactionID);
        otpRoot = otpDialogView.findViewById(R.id.root);

        confirmOtpView.setVisibility(View.GONE);
        paymentResult.setVisibility(View.GONE);

        sendOtp.setOnClickListener(v1 -> {
            sendOtp.setEnabled(false);
            String phone = phoneNum.getText().toString();

            sendOtp(phone, otpDialogView);




        });

        confirmOtp.setOnClickListener(v1 -> {
            confirmOtp.setEnabled(false);
            String otp = otpNum.getText().toString();
            String phone = phoneNum.getText().toString();
            confirmOtp(phone, otpDialogView, otp);


        });

        done.setOnClickListener(view -> {
            otpDialog.cancel();
        });


        otpDialog.setCancelable(false);
        otpDialog.setCanceledOnTouchOutside(false);
        otpDialog.show();

    }

    private void sendOtp(String phoneNum, View layout) {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        mRequestQueue = Volley.newRequestQueue(this);

        String url = " http://192.168.137.1:5000/api/sendOtp";
        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, sendOtpRequest(phoneNum), new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                Log.i(TAG, "Response :" + response);
                pDialog.cancel();
                try {
                    confirmOtpView.setVisibility(View.VISIBLE);
                    confirmOtp.setEnabled(true);
                    sendOtpView.setVisibility(View.GONE);
                    JSONObject jsonObject = (JSONObject) response;
                    String status = jsonObject.get("status").toString();

                    if (status.equals("Success")) {
                        sendOtpStatus = true;
                        Log.w(TAG, "status: " + sendOtpStatus);
                    }

                } catch (Exception e) {

                    Log.e(TAG, "Response Error: " + e.getMessage());
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendOtp.setEnabled(true);
                Log.i(TAG, "Error :" + error.toString());

                pDialog.cancel();

                Context mContext = layout.getContext();

                if (error instanceof NoConnectionError) {

                    ConnectivityManager cm = (ConnectivityManager) mContext
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = null;
                    if (cm != null) {
                        activeNetwork = cm.getActiveNetworkInfo();
                    }
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        Snackbar snackbar = Snackbar.make(layout, "Server is not connected to internet.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(layout, "Your device is not connected to internet.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException

                ) {
                    Snackbar snackbar = Snackbar.make(layout, "Your device is not connected to internet.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Snackbar snackbar = Snackbar.make(layout, "Bad Request.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Snackbar snackbar = Snackbar.make(layout, "Parse Error (because of invalid json or xml).", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error.getCause() instanceof OutOfMemoryError) {

                    Snackbar snackbar = Snackbar.make(layout, "Out Of Memory Error.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof AuthFailureError) {

                    Snackbar snackbar = Snackbar.make(layout, "server couldn't find the authenticated request.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {

                    Snackbar snackbar = Snackbar.make(layout, "Server is not responding.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {

                    Snackbar snackbar = Snackbar.make(layout, "Connection timeout error", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {

                    Snackbar snackbar = Snackbar.make(layout, "An unknown error occurred.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;

                Log.i(TAG, "Send OTP Status Code : " + mStatusCode);
                return super.parseNetworkResponse(response);
            }
        };


// 2 min timeout
        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(mRequest);

    }
    String Time;
    String Message;
    String debitAmount;
    String transactionID;
    private void confirmOtp(String phoneNum, View layout, String otp) {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.137.1:5000/api/agent/payment";

        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, url, confirmOtpRequest(phoneNum, otp), new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                Log.i(TAG, "Response :" + response);

                pDialog.cancel();
                try {
                    JSONObject jsonObject = (JSONObject) response;
                     Time = jsonObject.get("TRANSACTION_DATE").toString();
                     Message = jsonObject.get("Message").toString();
                     debitAmount = jsonObject.get("DEBITAMOUNT").toString();
                     transactionID = jsonObject.get("transactionID").toString();

                        time.setText(Time);
                        message.setText(Message);
                        debAmount.setText(debitAmount);
                        txId.setText(transactionID);
                        confirmOtpView.setVisibility(View.GONE);
                        paymentResult.setVisibility(View.VISIBLE);




                } catch (Exception e) {

                    Log.e(TAG, "confirmOtp Response Error: " + e.getMessage());

                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.cancel();

                Log.i(TAG, "Error :" + error.toString());

                sendOtpView.setVisibility(View.VISIBLE);
                sendOtp.setEnabled(true);
                confirmOtpView.setVisibility(View.GONE);
                paymentResult.setVisibility(View.GONE);

                Context mContext = layout.getContext();

                if (error instanceof NoConnectionError) {

                    ConnectivityManager cm = (ConnectivityManager) mContext
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = null;
                    if (cm != null) {
                        activeNetwork = cm.getActiveNetworkInfo();
                    }
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        Snackbar snackbar = Snackbar.make(layout, "Server is not connected to internet.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(layout, "Your device is not connected to internet.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException

                ) {
                    Snackbar snackbar = Snackbar.make(layout, "Your device is not connected to internet.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Snackbar snackbar = Snackbar.make(layout, "Bad Request.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Snackbar snackbar = Snackbar.make(layout, "Parse Error (because of invalid json or xml).", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error.getCause() instanceof OutOfMemoryError) {

                    Snackbar snackbar = Snackbar.make(layout, "Out Of Memory Error.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof AuthFailureError) {

                    Snackbar snackbar = Snackbar.make(layout, "server couldn't find the authenticated request.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {

                    Snackbar snackbar = Snackbar.make(layout, "Server is not responding.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {

                    Snackbar snackbar = Snackbar.make(layout, "Connection timeout error", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {

                    Snackbar snackbar = Snackbar.make(layout, "An unknown error occurred.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;

                Log.i(TAG, "Confirm OTP Status Code : " + mStatusCode);
                return super.parseNetworkResponse(response);
            }
        };
// 2 min timeout
        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(mRequest);

    }


    private JSONObject sendOtpRequest(String phoneNum) {

        JSONObject requestObject = new JSONObject();


        try {
            requestObject.put("Mobile", phoneNum);


        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(), "JSON Error :" + e, Toast.LENGTH_LONG).show();//display the response on screen

        }
        Log.i(TAG, "request: " + requestObject);
        return requestObject;
    }

    private JSONObject confirmOtpRequest(String phoneNum, String otp) {

        JSONObject requestObject = new JSONObject();


        try {
            String messageId = UUID.randomUUID().toString();
            requestObject.put("merchantId", merchantId);
            requestObject.put("agentId", agentId);
            requestObject.put("fuelType", fuelName);
            requestObject.put("debitAccount", "1022200021158");
            requestObject.put("debitAmount", amount);
            requestObject.put("confirmationOtpNumber", otp);
            requestObject.put("phoneNumber", phoneNum);
            requestObject.put("tipCode", tipCode.getText().toString());
            requestObject.put("messageId", messageId.substring(24, 36));

        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(), "JSON Error :" + e, Toast.LENGTH_LONG).show();//display the response on screen

        }
        Log.i(TAG, "request: " + requestObject);
        return requestObject;
    }

    public static KeyPair generateRSAKkeyPair()
            throws Exception {
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
            throws Exception {
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
            throws Exception {
        Cipher cipher
                = Cipher.getInstance("RSA");

        cipher.init(Cipher.DECRYPT_MODE,
                publicKey);
        byte[] result
                = cipher.doFinal(cipherText);

        return new String(result);
    }

}