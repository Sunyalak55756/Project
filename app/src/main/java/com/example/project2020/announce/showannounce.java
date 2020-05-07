package com.example.project2020.announce;

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


import com.example.project2020.ListItem4adapt.listItem2;
import com.example.project2020.ListItem4adapt.listItem5;
import com.example.project2020.MainActivity;
import com.example.project2020.R;
import com.example.project2020.info.showinfo;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class showannounce extends AppCompatActivity {

    adaptannounce adapter;
    private double Latitude;
    private double Longitude;
    private Bitmap imgBitMap;
    private  List<listItem5> listItems;
    private  ArrayList<String> type1 = new ArrayList<String>();
    private  ArrayList<String> breed1 = new ArrayList<String>();
    private  ArrayList<String> color1 = new ArrayList<String>();   private  ArrayList<String> image1 = new ArrayList<String>();
    private  ArrayList<String> marking1 = new ArrayList<String>();

    private  ArrayList<String> name = new ArrayList<String>();
    private  ArrayList<String> email1 = new ArrayList<String>();
    private  ArrayList<String> tel = new ArrayList<String>();
    private  ArrayList<String> date = new ArrayList<String>();
    private  ArrayList<String> summary = new ArrayList<String>();
    listItem5 xxx;
    private  ArrayList<String> nameuser = new ArrayList<String>();
    private  ArrayList<String> id = new ArrayList<String>();
    ArrayList<HashMap<String, String>> location1 = null;
    ArrayList<ArrayList <HashMap<String, String>>> store = null;
    private static final String MY_PREFS = "my_prefs";
    private byte[] decodedString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show1);
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        listItems = new ArrayList<>();


        //////////////ดึงข้อมูล////////////////////

        String email = shared.getString("email",null);
        ArrayList<HashMap<String, String>> location = null;
        String url2 = "http://sniperkla.lnw.mn/getannounce.php"; // ชื่อ host เพื่อติดต่อกับ php เพื่อดึงข้อมูล DB
        try {
            JSONArray data = new JSONArray(getHttpGet(url2));

            location = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map1;

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map1 = new HashMap<String, String>();
                map1.clear();
                map1.put("id", c.getString("id"));
                map1.put("email", c.getString("email"));
                map1.put("summary", c.getString("summary"));
                map1.put("tel", c.getString("tel"));
                map1.put("date", c.getString("date"));
                map1.put("name", c.getString("name"));
                map1.put("nameuser", c.getString("nameuser"));
                location.add(map1);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int x = 0;

        for(int i=0;i<location.size();i++){
            id.add(location.get(i).get("id").toString());
            email1.add(location.get(i).get("email").toString());
            summary.add(location.get(i).get("summary").toString());
            tel.add(location.get(i).get("tel").toString());
            date.add(location.get(i).get("date").toString());
            name.add(location.get(i).get("name").toString());
            nameuser.add(location.get(i).get("nameuser").toString());

        }
        Collections.reverse(email1);
        Collections.reverse(id);
        Collections.reverse(summary);
        Collections.reverse(tel);
        Collections.reverse(date);
        Collections.reverse(name);
        Collections.reverse(nameuser);
      //      ImageView xx = (ImageView) findViewById(R.id.xxx);
        //    xx.setImageBitmap((Bitmap.createScaledBitmap(imgBitMap, 250, 250, false)));
        location1 = new ArrayList<HashMap<String, String>>();
        for (int i=0;i<location.size();i++) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = "http://sniperkla.lnw.mn/getpet2.php";
                HttpPost httppost = new HttpPost(url);
                try {

                    List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                    nameValuePairList.add(new BasicNameValuePair("email", email1.get(i)));
                    nameValuePairList.add(new BasicNameValuePair("name", name.get(i)));
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
                    HashMap<String, String> map1;
                    for (int j= 0; j < data.length(); j++) {
                        JSONObject c = data.getJSONObject(j);
                        byte[] decodedString = Base64.decode(c.getString("image"), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        xxx = new listItem5((name.get(i)),c.getString("breed"),c.getString("type"),c.getString("color"),c.getString("marking"),imgBitMap);
                        listItems.add(xxx);
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

        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(showannounce.this));
        adapter = new adaptannounce(listItems,showannounce.this);
        recyclerView.addItemDecoration(new DividerItemDecoration(showannounce.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
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
        Intent intent = new Intent(showannounce.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }

}

