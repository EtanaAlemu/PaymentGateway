package com.dxvalley.paymentgateway.retailservices.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.dxvalley.paymentgateway.util.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PAYMENT";
    TextView mTotalAmount;
    Button mPay;
    TextInputEditText mPhoneNumber,  mPasscode;
    private String url ;


    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mTotalAmount = findViewById(R.id.total_amount);
        mPay = findViewById(R.id.pay);
        mPhoneNumber = findViewById(R.id.phone_num);
        mPasscode = findViewById(R.id.passcode);
        url = getString(R.string.payment_api);

        float mAmount;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mAmount = 0f;
            } else {
                mAmount= extras.getFloat("amount");
            }
        } else {
            mAmount= (float) savedInstanceState.getSerializable("amount");
        }
        mTotalAmount.setText(String.valueOf(mAmount));



        mPay.setOnClickListener(view -> {
            if(mPhoneNumber.getText().toString().isEmpty())
                Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            
            else if (mPasscode.getText().toString().isEmpty()) {
                Toast.makeText(this, "Passcode is required", Toast.LENGTH_SHORT).show();
            }else{

                String mobile = mPhoneNumber.getText().toString();
                String text = mPhoneNumber.getText().toString();
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


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i(TAG,"Error :" + error.toString());
                        Toast.makeText(PaymentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
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
        });

    }

    private JSONObject request(){

        JSONObject requestObject =new JSONObject();


        try{
            requestObject.put("mobile","0943830108");
            requestObject.put("text", "test");

        }catch(JSONException e){

            Log.i(TAG,"Error :" + e.toString());
        }
        return requestObject;
    }
}