package com.example.project2020.info;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.project2020.ListItem4adapt.listItem3;
import com.example.project2020.MainActivity;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class showinfo extends AppCompatActivity {

    adaptinfo adapter;
    private double Latitude;
    private double Longitude;
    private ArrayList<Bitmap> imgBitMap = new ArrayList<>();
    private  List<listItem3> listItems;
    private  ArrayList<String> name = new ArrayList<String>();
    private  ArrayList<String> blob = new ArrayList<String>();
    private static final String MY_PREFS = "my_prefs";
    private byte[] decodedString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        listItems = new ArrayList<>();


        //////////////ดึงข้อมูล////////////////////

        String email = shared.getString("email",null);
        ArrayList<HashMap<String, String>> location = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://sniperkla.lnw.mn/getpet.php";
            HttpPost httppost = new HttpPost(url);
            try {
                location = new ArrayList<HashMap<String, String>>();
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("email",email));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpClient httpClient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                is.close();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclient.execute(httppost, responseHandler);
                JSONArray data = new JSONArray(responseBody);
                location = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map1;
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map1 = new HashMap<String, String>();
                    map1.clear();
                    map1.put("name", c.getString("name"));
                    location.add(map1);
                    name.add(location.get(i).get("name").toString());
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpClient httpclient = new DefaultHttpClient();
        String url = "http://sniperkla.lnw.mn/getblob.php";
        HttpPost httppost = new HttpPost(url);
        ArrayList<byte[]> x = new ArrayList<>();
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
            JSONArray data = new JSONArray(responseBody);
            location = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map1;
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map1 = new HashMap<String, String>();
                map1.clear();
                map1.put("image", c.getString("image"));
                location.add(map1);
                blob.add(location.get(i).get("image").toString());
                decodedString = Base64.decode((blob.get(i)), Base64.DEFAULT);
                x.add(decodedString);
            }


            for (int i=0 ;i<location.size();i++) {
                imgBitMap.add(BitmapFactory.decodeByteArray(x.get(i), 0, x.get(i).length));
            }

      //      ImageView xx = (ImageView) findViewById(R.id.xxx);
        //    xx.setImageBitmap((Bitmap.createScaledBitmap(imgBitMap, 250, 250, false)));

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < location.size(); i++)
        {
            listItem3 xxx = new listItem3((name.get(i)),imgBitMap.get(i));
            listItems.add(xxx);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(showinfo.this));
        adapter = new adaptinfo(listItems,showinfo.this);
        recyclerView.addItemDecoration(new DividerItemDecoration(showinfo.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(showinfo.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
}

