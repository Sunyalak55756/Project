package com.example.project2020;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project2020.utility.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class petmap extends FragmentActivity implements OnMapReadyCallback {
    ArrayList<HashMap<String, String>> location = null;
    private static final String MY_PREFS = "my_prefs";
    private GoogleMap mMap;
 private  GpsTracker gps;
    Spinner spinner;
    Button ok;
    String option="";
    CountDownTimer countDownTimer;
    String valuedis;
    ProgressDialog progressDialog;
    EditText distance;
    List<String> name = new ArrayList<String>();
    List<String> lat = new ArrayList<String>();
    List<String> lng = new ArrayList<String>();
    MarkerOptions userMarker;
    Marker myMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(petmap.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please while");
        progressDialog.show();
String x;
        setContentView(R.layout.activity_petmap);
        spinner=findViewById(R.id.spinner);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        distance=findViewById(R.id.distance);
        distance.setText(""+shared.getString("valuedistance","10"));
        ok=findViewById(R.id.ok);
        List<String> list = new ArrayList<String>();
        list.add("กรุณาเลือกข้อมูล");
        list.add("อาหารสัตว์เลี้ยง");
        list.add("คลินิกสัตว์");
        list.add("Pet groomer");
                if(shared.getString("option",null)!="")
                {
                    list.remove(0);
                    list.add(0,"location : "+shared.getString("option",null));
                }
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adaptertype = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list);
        spinner.setAdapter(adaptertype);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    option = "อาหารสัตว์เลี้ยง";
                    editor.putString("option", option);
                    editor.commit();
                   // Toast.makeText(getApplicationContext(), ""+shared.getString("option","null"), Toast.LENGTH_SHORT).show();
                }

                else if (position == 2) {
                    option = "คลินิกสัตว์";
                    editor.putString("option", option);
                    editor.commit();
                    //Toast.makeText(getApplicationContext(), ""+shared.getString("option","null"), Toast.LENGTH_SHORT).show();


                }
                else if (position == 3) {
                    option = "Pet groomer";
                    editor.putString("option", option);
                    editor.commit();
 //                   Toast.makeText(getApplicationContext(), ""+shared.getString("option","null"), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    valuedis=distance.getText().toString();
                    if(Float.parseFloat(valuedis)>50) {
                        Toast.makeText(getApplicationContext(), "less than 50 km", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.putString("valuedistance", valuedis);
                        editor.commit();
                        Intent intent = new Intent(petmap.this, petmap.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                }

            });






    //    Toast.makeText(getApplicationContext(), "" + urlx(), Toast.LENGTH_SHORT).show();






       // final List<String> lng = new ArrayList<String>();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    /*private void removeMarkers(){
        for (Marker marker:myMarker) {
            marker.remove();
        }
        mMarkers.clear();

    }*/
   @Override
    public void onMapReady(final GoogleMap googleMap) {
       gps = new GpsTracker(petmap.this);
       final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
       final SharedPreferences.Editor editor = shared.edit();
       StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
       stringBuilder.append("location=" + gps.getLatitude() + "," + gps.getLongitude());
       stringBuilder.append("&radius=" + Float.parseFloat(shared.getString("valuedistance", "0")) * 1000);
       stringBuilder.append("&keyword=" + "" + shared.getString("option", "null"));
       stringBuilder.append("&key=" + "AIzaSyBa2kLxum4zlcy7I_y97SSb5LnLvR_52XQ");
       String url = stringBuilder.toString();
       try {
           JSONObject data = new JSONObject(getHttpGet(url));
           JSONArray result = data.getJSONArray("results");

           location = new ArrayList<HashMap<String, String>>();
           HashMap<String, String> map1;

           for (int i = 0; i < result.length(); i++) {
               JSONObject c = result.getJSONObject(i);
               JSONObject v = c.getJSONObject("geometry").getJSONObject("location");
               map1 = new HashMap<String, String>();
               map1.clear();
               map1.put("lat", v.getString("lat"));
               map1.put("lng", v.getString("lng"));
               map1.put("name", c.getString("name")); // ข้อมูลจากฐานข้อมูลที่ต้องการดึง ชื่อตัวแปร
               location.add(map1);
               name.add(location.get(i).get("name"));
               lat.add(location.get(i).get("lat"));
               lng.add(location.get(i).get("lng"));
           }

       } catch (JSONException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       mMap = googleMap;
       Log.d("xxx",""+url);
       for (int i = 0; i < location.size(); i++) {
           name.add(location.get(i).get("name"));
           lat.add(location.get(i).get("lat"));
           lng.add(location.get(i).get("lng"));
           LatLng sydney = new LatLng(Double.valueOf(lat.get(i)), Double.valueOf(lng.get(i)));
           userMarker = new MarkerOptions().position(sydney).title(""+name.get(i));
           myMarker = mMap.addMarker(userMarker);
       }
       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 14));
       mMap.setMyLocationEnabled(true);
       countDownTimer = new CountDownTimer(2000,1000) {
           @Override
           public void onTick(long l) {
           }

           @Override
           public void onFinish() {
               progressDialog.dismiss();

           }
       }.start();

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
