package com.example.project2020;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.project2020.announce.showannounce;
import com.example.project2020.info.showinfo;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ///////////ประกาศเพื่อเรียกใช้ตัวแปรแบบแชร์ได้///////////////
    private static final String MY_PREFS = "my_prefs";
    private byte[] decodedString;
    private  ArrayList<String> blob = new ArrayList<String>();
    //////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        ImageView addpet ;
        addpet = findViewById(R.id.addpet);

        ////////////เช็คปุ่ม add ถ้า ไม่ได้ลอกอิน จะเซตให้มองไม่เหน////////
        if(shared.getBoolean("check",false)==false)
        addpet.setVisibility(View.INVISIBLE);

        ///////////ประกาศเพื่อเรียกใช้ตัวแปรแบบแชร์ได้///////////////
        addpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Addpet.class);
                startActivity(intent);
            }
        });
        //////////////////////////////////////////////////////
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Dialog MyDialog = new Dialog(MainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_login);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        /////////////เปลี่ยนข้อความ header ของ nav /////////////
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.header);
        TextView navUsername1 = (TextView) headerView.findViewById(R.id.header1);
        ImageView navImage = (ImageView) headerView.findViewById(R.id.imageheader);
        Button edit = (Button) headerView.findViewById(R.id.edit);
        ////test////


        navUsername.setText("Welcome : " + shared.getString("user", "Noname"));
        navUsername1.setText("Email : "+shared.getString("email","Unknown"));
        /////////////เปลี่ยนข้อความ header ของ nav /////////////

////////เช็คการใช้งานครั้งแรก ของตัวแปร status pet //////////////
            if(shared.getBoolean("check",false)==(false)){
            editor.putString("statuspet", "normal");
            editor.commit();
        }

///////////ในส่วนของ ถ้้า ล๊อคอินแล้ว /////////////////
            if (shared.getBoolean("check", false) == true) {
                Button button2;
                byte[] x = null;
                decodedString = Base64.decode((shared.getString("editimg","null")), Base64.DEFAULT);
                x=decodedString;
                Bitmap imgb;
                imgb=BitmapFactory.decodeByteArray(x ,0, x.length);

                navImage.setImageBitmap(imgb);

             /*   String imageUrl = "https://docs.google.com/uc?id=1zrN0-jNBDyhLKXgJrGjRddO-hVG_LcfR";
                Picasso.get().load(imageUrl).into(navImage);*/
                button2 = (findViewById(R.id.login));
                button2.setText("Logout");
                button2.setTextSize(20);// เปลี่ยนปุ่มเป็น logout
                ///////// กดปุ่มจะทำการ log out เปลี่ยนค่าตัวแปรเป็นค่าตั้งต้น (เป็นยังไม่ได้ ลอกอิน)///////////////
                button2.setOnClickListener(new View.OnClickListener() {
                    /////////// เมื่อกดปุ่ม log out //////////
                    public void onClick(View v) {
                        editor.putBoolean("check", false); // ใส่ค่า check ให้เป็น false แทน true (กลับค่าเดิม)
                        editor.putString("user", "Annonymous");
                        editor.putString("statuspet", "normal");
                        editor.putString("email", "Unknown");
                        editor.putString("edittel", "0");
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "ออกจากระบบแล้ว", Toast.LENGTH_SHORT).show();
                        //System.out.print ("xxx"+shared.getBoolean("check",false));
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }); //หลังจากกดปุ่มแล้วจะ intent กลับมาหน้าเดิม และทำการ check ตัวแปร ที่ชื่อว่า check ว่าเป็น true หรือ false ซึ่งกรณีนี้จะเป็น false เพราะ กำหนดให้เป็น false
                //หากเปิดแอพมาอีกครั้งจะ อยู่ในสถานะที่ยังไม่ล๊อคอิน

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      Intent intent = new Intent(MainActivity.this,edituser.class);
                      startActivity(intent);
                    }
                });


            } else {

                final EditText username, bpassword;
                username = findViewById(R.id.email);
                bpassword = findViewById(R.id.password);

                navImage.setImageResource(R.mipmap.login);
                Button button1;
                button1 = (findViewById(R.id.login));

                button1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ArrayList<byte[]> x = new ArrayList<>();
                        ArrayList<HashMap<String, String>> location = null;
                        String url2 = "http://sniperkla.lnw.mn/testgetuser.php"; // ชื่อ host เพื่อติดต่อกับ php เพื่อดึงข้อมูล DB
                        try {
                            JSONArray data = new JSONArray(getHttpGet(url2));

                            location = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> map1;

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map1 = new HashMap<String, String>();
                                map1.clear();

                                map1.put("email", c.getString("email")); // ข้อมูลจากฐานข้อมูลที่ต้องการดึง ชื่อตัวแปร
                                map1.put("password", c.getString("password"));  // ข้อมูลจากฐานข้อมูลที่ต้องการดึง ชื่อตัวแปร
                                map1.put("name", c.getString("name")); // ข้อมูลจากฐานข้อมูลที่ต้องการดึง ชื่อตัวแปร
                                map1.put("tel", c.getString("tel"));
                                map1.put("dob", c.getString("dob"));
                                map1.put("statuspet", c.getString("statuspet"));
                                map1.put("image", c.getString("image"));
                                location.add(map1);


                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        final List<String> email = new ArrayList<String>();
                        final List<String> name = new ArrayList<String>();
                        final List<String> password = new ArrayList<String>();

                        final List<String> tel = new ArrayList<String>();
                        final List<String> dob = new ArrayList<String>();
                        final List<String> statuspet = new ArrayList<String>();
                        for (int i = 0; i < location.size(); i++) {

                            email.add(location.get(i).get("email").toString());
                            password.add(location.get(i).get("password").toString());
                            name.add(location.get(i).get("name").toString());
                            tel.add(location.get(i).get("tel").toString());
                            dob.add(location.get(i).get("dob").toString());
                            statuspet.add(location.get(i).get("statuspet").toString());
                            blob.add(location.get(i).get("image").toString());

                            /////////////////////////////////////////////////////////
                        }


                        final ArrayList<HashMap<String, String>> finalLocation = location;
                        MyDialog.show();
                        Button submit = (Button) MyDialog.findViewById(R.id.register);
                        submit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, signup.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });

                        ///////กดปุ่มลอกอิน ////////////
                        Button button1;
                        final EditText username = (EditText) MyDialog.findViewById(R.id.email);
                        final EditText bpassword = (EditText) MyDialog.findViewById(R.id.password);
                        button1 = (Button) MyDialog.findViewById(R.id.submit);
                        button1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                int pass = 0;
                                int count = 0;
                                final String user = username.getText().toString();
                                for (int i = 0; i < finalLocation.size(); i++) {

                                    ///////////เช็ค ว่า email pass ตรงกับฐานข้อมูลไหม///////
                                    if (email.get(i).equals(username.getText().toString()) && password.get(i).equals(bpassword.getText().toString())) {
                                        editor.putString("user", name.get(i));

                                        editor.putString("statuspet", statuspet.get(i));
                                        editor.putBoolean("check", true);
                                        editor.putString("email", email.get(i));
                                        editor.putString("editpass", password.get(i));
                                        editor.putString("editname", name.get(i));
                                        editor.putString("edittel",  tel.get(i));
                                        editor.putString("editdob",  dob.get(i));
                                        editor.putString("editimg", blob.get(i));
                                        editor.commit();
                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                        pass = 1;
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "ล๊อคอินสำเร็จ" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (pass == 0) {
                                    Toast.makeText(getApplicationContext(), "ชื่อผู้ใช้งานหรือรหัสผ่านผิด", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                // super.onBackPressed();

        }
    }





    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            Intent intent = new Intent(MainActivity.this, Schedule.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_info) {
            if (shared.getString("statuspet", "normal").equals("normal")) {
                Toast.makeText(getApplicationContext(), "ต้องสมัครสมาชิกก่อน" , Toast.LENGTH_SHORT).show();
            }
            else if (shared.getString("statuspet", "normal").equals("false")){

                Toast.makeText(getApplicationContext(), "คุณยังไม่มีข้อมูลของสัตว์เลี้ยง กรุณากรอกข้อมูลก่อน" , Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, showinfo.class);
                startActivity(intent);
            }
        }
         else if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this,petmap.class);
            startActivity(intent);

        } else if (id == R.id.nav_announce) {
            Intent intent = new Intent(MainActivity.this, showannounce.class);
            startActivity(intent);

        } else if (id == R.id.nav_scan) {
            Intent intent = new Intent(MainActivity.this, Scan.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), "5735512057@email.psu.ac.th" , Toast.LENGTH_LONG).show();
        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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


}
