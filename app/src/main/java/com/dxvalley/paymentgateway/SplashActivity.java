package com.dxvalley.paymentgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dxvalley.paymentgateway.otherservices.LoginActivity;
import com.dxvalley.paymentgateway.retailservices.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /****** Create Thread that will sleep for 5 seconds****/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    // After 5 seconds redirect to another intent
                    String userPerf = checkUserPreferences();
                    Intent i;
                    if(userPerf.equals("Retail Service")){
                    i=new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);}
                    else if (userPerf.equals("Other Service")) {

                        i=new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                    } else{
                        i=new Intent(getBaseContext(),ServicesActivity.class);
                        startActivity(i);
                    }

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }

    private String checkUserPreferences(){
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ServicePerf",MODE_PRIVATE);


        // The value will be default as empty string because for the very
        // first time when the app is opened, there is nothing to show
        String userPerf = sharedPreferences.getString("User Perf", "");
        return userPerf;
    }
}