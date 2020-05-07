package com.example.project2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class qrcodegen extends AppCompatActivity {
    private Bitmap bitmap;
    private static final String MY_PREFS = "my_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

///////////////////////// gen code//////////////////////
        String path = shared.getString("path","null");
        String html = "http://sniperkla.lnw.mn/htmlgenerated/"+path+".php";
        Toast.makeText(getApplicationContext(), ""+html, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_qrcodegen);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("name");
        TextView name;
        name = findViewById(R.id.name);
        name.setText(value);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(html, BarcodeFormat.QR_CODE ,400, 400);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrcode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

        Button save ;
        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaStore.Images.Media.insertImage(
                        getContentResolver(), bitmap,
                        ""+shared.getString("pathpet","null"),
                        "qrcode");
                Toast.makeText(getApplicationContext(), "บันทึกรูปภาพแล้ว", Toast.LENGTH_SHORT).show();
                finish();

            }
        });




    }
}


