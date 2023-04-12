package com.dxvalley.paymentgateway.otherservices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dxvalley.paymentgateway.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class NedajActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nedaj);

        TextInputEditText mAmount = findViewById(R.id.amount);
        Button mProceed = findViewById(R.id.proceed);
// Retrieving the value using its keys the file name must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MerchantInfo", Context.MODE_PRIVATE);
// first time when the app is opened, there is nothing to show
        String username = sh.getString("username", "");
        mProceed.setOnClickListener(v -> {
            String text="{\n\"MerchantId\": \""+username+"\",\n\"Amount\": \""+mAmount.getText()+"\"\n}"; // Whatever you need to encode in the QR code
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {

                LayoutInflater factory = LayoutInflater.from(this);
                final View qrDialogView = factory.inflate(R.layout.qr_dialog, null);

                final AlertDialog qrDialog = new AlertDialog.Builder(this).create();
                qrDialog.setView(qrDialogView);
                ImageView imageView = qrDialogView.findViewById(R.id.qr);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //your business logic
                        qrDialog.dismiss();
                    }
                });
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
                qrDialog.show();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }
}