package com.dxvalley.paymentgateway.retailservices.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.retailservices.db.DeviceConfigDatabase;
import com.dxvalley.paymentgateway.retailservices.models.DeviceConfig;
import com.dxvalley.paymentgateway.retailservices.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeviceConfigActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mSecretKey, mClientId, mApiKey;
    Button mRegister;
    private String url;

    private static final String TAG = "DEVICE_CONFIG";

    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;

    String secretkey;
    String clientid;
    String apikey;
    String deviceId;

    Context mContext;

    boolean isFirstTime = true;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_config);
        mContext = this;
        mSecretKey = findViewById(R.id.secret_key);
        mClientId = findViewById(R.id.client_id);
        mApiKey = findViewById(R.id.api_key);
        mRegister = findViewById(R.id.register_btn);
        url = getString(R.string.device_cofig_api);
        mRegister.setOnClickListener(this);
        getDeviceConfig();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Device Configuration");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_btn:

                secretkey = mSecretKey.getText().toString();
                clientid = mClientId.getText().toString();
                apikey = mApiKey.getText().toString();
                if(secretkey.isEmpty()){
                    Toast.makeText(mContext, "Secret Key cant be empty", Toast.LENGTH_SHORT).show();
                }
                else if(clientid.isEmpty()){
                    Toast.makeText(mContext, "Client ID cant be empty", Toast.LENGTH_SHORT).show();}
                else if(apikey.isEmpty()){
                    Toast.makeText(mContext, "API Key cant be empty", Toast.LENGTH_SHORT).show();}
                else{
                    deviceRegister();
                }
                break;

        }
    }

    private void deviceRegister() {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mRequest = new JsonObjectRequest(Request.Method.POST, url,request(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

                Log.i(TAG,"Response :" + response.toString());
                pDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = (JSONObject) response;
                    if(jsonObject.get("status") == "success"){
                        if(isFirstTime)
                            addDeviceConfig(new DeviceConfig(clientid,secretkey,apikey,deviceId));
                        else
                            updateDeviceConfig(new DeviceConfig(mId,clientid,secretkey,apikey,deviceId));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
                pDialog.cancel();


                if(isFirstTime)
                    addDeviceConfig(new DeviceConfig(clientid,secretkey,apikey,deviceId));
                else
                    updateDeviceConfig(new DeviceConfig(mId,clientid,secretkey,apikey,deviceId));
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(mRequest);


        }

    static DeviceConfigDatabase database ;
    static List<DeviceConfig> config;
    private void addDeviceConfig(DeviceConfig mConfig) {
        class AddDeviceConfig extends AsyncTask<Void, Void, List<DeviceConfig>> {
            @Override
            protected List<DeviceConfig> doInBackground(Void... voids) {

                database = DeviceConfigDatabase.getInstance(getApplicationContext());
                config = database.configDao().getConfigList();
                database.configDao().insertConfig(mConfig);

                return config;
            }

            @Override
            protected void onPostExecute(List<DeviceConfig> configs) {
                super.onPostExecute(configs);
                finish();
            }
        }

        AddDeviceConfig savedTasks = new AddDeviceConfig();
        savedTasks.execute();
    }

    private void updateDeviceConfig(DeviceConfig mConfig) {

        class UpdateConfig extends AsyncTask<Void, Void, List<DeviceConfig>> {
            @Override
            protected List<DeviceConfig> doInBackground(Void... voids) {

                database = DeviceConfigDatabase.getInstance(getApplicationContext());
                database.configDao().updateConfig(mConfig);
                config = database.configDao().getConfigList();
                return config;
            }

            @Override
            protected void onPostExecute(List<DeviceConfig> items) {
                super.onPostExecute(items);
                finish();
            }
        }

        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.execute();
    }


    private void getDeviceConfig() {

        class GetDeviceConfig extends AsyncTask<Void, Void, List<DeviceConfig>> {
            @Override
            protected List<DeviceConfig> doInBackground(Void... voids) {

                database = DeviceConfigDatabase.getInstance(getApplicationContext());
                config = database.configDao().getConfigList();
                return config;
            }

            @Override
            protected void onPostExecute(List<DeviceConfig> configs) {
                super.onPostExecute(configs);
                if(!configs.isEmpty()) {

                    mId = configs.get(0).getId();
                    mClientId.setText(configs.get(0).getClientId());
                    mApiKey.setText(configs.get(0).getApiKey());
                    mSecretKey.setText(configs.get(0).getSecretId());
                    deviceId = configs.get(0).getDeviceId();
                    isFirstTime = false;
                }
            }
        }

        GetDeviceConfig saveConfigs = new GetDeviceConfig();
        saveConfigs.execute();
    }

    @SuppressLint("HardwareIds")
    private JSONObject request(){

        JSONObject requestObject =new JSONObject();


        try{
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            requestObject.put("secretkey",secretkey);
            requestObject.put("clientid", clientid);
            requestObject.put("apikey", apikey);
            requestObject.put("deviceid", deviceId);

            Log.i(TAG, "device Id: "+deviceId);

        }catch(JSONException e){


            Log.i(TAG,"Error :" + e.toString());
        }
        return requestObject;
    }
}