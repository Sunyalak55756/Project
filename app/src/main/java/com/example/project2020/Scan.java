package com.example.project2020;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;

import android.content.Intent;


import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import com.google.zxing.integration.android.IntentResult;

public class Scan extends AppCompatActivity {

    private Button scan_button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        final Activity activity = this;


        IntentIntegrator integrator = new IntentIntegrator(activity);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);

        //integrator.setOrientationLocked(true);

        //  integrator.setPrompt("Scan");

        integrator.setCameraId(0);

        integrator.setBeepEnabled(false);

        integrator.setBarcodeImageEnabled(false);

        integrator.initiateScan();



    }



    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        setContentView(R.layout.activity_webview);
        WebView webView = findViewById(R.id.webview);
        if(result != null){

            if(result.getContents()== null){

                Toast.makeText(this, "ยกเลิกการสแกน", Toast.LENGTH_LONG).show();
                finish();

            }

            else{
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView.loadUrl(result.getContents());
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
            }

        }

        else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }
}

