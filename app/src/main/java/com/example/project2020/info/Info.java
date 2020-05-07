package com.example.project2020.info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project2020.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Info extends AppCompatActivity {
    private static final String MY_PREFS = "my_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ////////////////////////////////


        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

        Toast.makeText(getApplicationContext(), "ข้อมูลแสดง", Toast.LENGTH_SHORT).show();



/*            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://sniperkla.lnw.mn/getblob.php";
            HttpPost httppost = new HttpPost(url);
            try {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("email", "admin"));
                HttpClient httpClient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                is.close();


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("email", "admin"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            */

            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://sniperkla.lnw.mn/getblob.php";
            HttpPost httppost = new HttpPost(url);
            try {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("email", shared.getString("email", null)));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                is.close();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclient.execute(httppost, responseHandler);
                byte[] decodedString = Base64.decode((responseBody), Base64.DEFAULT);
                Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                ImageView xx = (ImageView) findViewById(R.id.xxx);
                xx.setImageBitmap((Bitmap.createScaledBitmap(imgBitMap, 250, 250, false)));

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


    }
    public static String getHttpGet(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();

    }
}


