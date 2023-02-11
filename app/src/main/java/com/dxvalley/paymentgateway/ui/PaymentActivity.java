package com.dxvalley.paymentgateway.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dxvalley.paymentgateway.R;
import com.google.android.material.textfield.TextInputEditText;

public class PaymentActivity extends AppCompatActivity {

    TextView mTotalAmount;
    Button mSendOTP, mProceed;
    TextInputEditText mPhoneNumber, mOTP, mPasscode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mTotalAmount = findViewById(R.id.total_amount);
        mSendOTP = findViewById(R.id.send_otp);
        mProceed = findViewById(R.id.proceed);
        mPhoneNumber = findViewById(R.id.phone_num);
        mOTP = findViewById(R.id.otp);
        mPasscode = findViewById(R.id.passcode);

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

        mSendOTP.setOnClickListener(view -> {
            if(mPhoneNumber.getText().toString().isEmpty())
                Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Sending OTP...", Toast.LENGTH_SHORT).show();
        });


        mProceed.setOnClickListener(view -> {
            if(mPhoneNumber.getText().toString().isEmpty())
                Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            else if (mOTP.getText().toString().isEmpty()) {
                Toast.makeText(this, "OTP is required", Toast.LENGTH_SHORT).show();
            }
            else if (mPasscode.getText().toString().isEmpty()) {
                Toast.makeText(this, "Passcode is required", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show();
        });
    }
}