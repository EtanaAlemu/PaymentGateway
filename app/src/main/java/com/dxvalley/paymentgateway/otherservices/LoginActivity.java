package com.dxvalley.paymentgateway.otherservices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dxvalley.paymentgateway.R;
import com.dxvalley.paymentgateway.ServicesActivity;
import com.dxvalley.paymentgateway.retailservices.models.DeviceConfig;
import com.dxvalley.paymentgateway.util.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class LoginActivity extends AppCompatActivity {

    private String url = "http://192.168.14.43:5000/api/agent/login";
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.login);
        TextInputEditText mUsername, mPassword;
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        LinearLayout layout = findViewById(R.id.layout);
        login.setOnClickListener(view -> {
            if (mUsername.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {
                Snackbar snackbar = Snackbar.make(layout,"Please enter username and password",Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                login(mUsername.getText().toString(),mPassword.getText().toString());
            }
        });


    }

    private void login(String username, String password) {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mRequest = new JsonObjectRequest(Request.Method.POST, url, request(username, password), new Response.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Object response) {
//                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

                Log.i("MayApp", "Response :" + response);

                pDialog.cancel();
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    String token = jsonObject.get("token").toString();
                    String[] chunks = token.split("\\.");
                    Base64.Decoder decoder = Base64.getUrlDecoder();

                    String header = new String(decoder.decode(chunks[0]));
                    String payload = new String(decoder.decode(chunks[1]));
                    JSONObject obj = null;
                    try {

                        obj = new JSONObject(payload);

                        Log.d("My App", obj.toString());

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + payload + "\"");
                    }
                    Gson gson= new Gson();
                    User user = gson.fromJson(jsonObject.toString(),User.class);



                    loginProceed(user);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Response Error :" + response, Toast.LENGTH_LONG).show();//display the response on screen
                    throw new RuntimeException(e);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("MyApp", "Error :" + error.toString());
                Toast.makeText(getApplicationContext(), "Volley Error :" + error, Toast.LENGTH_LONG).show();//display the response on screen

                pDialog.cancel();

            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Toast.makeText(getApplicationContext(), "Status Code :" + mStatusCode, Toast.LENGTH_LONG).show();//display the response on screen

                return super.parseNetworkResponse(response);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(mRequest);


    }

    private void loginProceed(User user){

        // on below line we are setting data in our shared preferences.
        // creating a master key for encryption of shared preferences.
        String masterKeyAlias = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
                // on below line we are storing data in shared preferences file.
                sharedPreferences.edit().putString("agent_id", user.agent.agent_id).apply();
                sharedPreferences.edit().putString("email_address", user.agent.email_address).apply();
                sharedPreferences.edit().putString("user_id", user.agent.user_id).apply();
                sharedPreferences.edit().putString("fname", user.agent.fname).apply();
                sharedPreferences.edit().putString("lname", user.agent.lname).apply();
                sharedPreferences.edit().putString("role", user.agent.role).apply();

            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {

            // Storing data into SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            // Creating an Editor object to edit(write to the file)
            // Storing the key and its value as the data fetched from edittext

            sharedPreferences.edit().putString("agent_id", user.agent.agent_id).apply();
            sharedPreferences.edit().putString("email_address", user.agent.email_address).apply();
            sharedPreferences.edit().putString("user_id", user.agent.user_id).apply();
            sharedPreferences.edit().putString("fname", user.agent.fname).apply();
            sharedPreferences.edit().putString("lname", user.agent.lname).apply();
            sharedPreferences.edit().putString("role", user.agent.role).apply();

        }

        startActivity(new Intent(LoginActivity.this, NedajActivity.class));
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.service_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.services:
                startActivity(new Intent(this, ServicesActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private JSONObject request(String username, String password){

        JSONObject requestObject =new JSONObject();


        try{
            requestObject.put("email",username);
            requestObject.put("password", password);


        }catch(JSONException e){

            Toast.makeText(getApplicationContext(),"JSON Error :" + e, Toast.LENGTH_LONG).show();//display the response on screen

        }
        return requestObject;
    }
}

class User{
    Agent agent;
}

class Agent{
    String agent_id;
    String fname;
    String lname;
    String email_address;
    String phone_number;
    String role;
    String user_id;
}