package com.example.project2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project2020.info.showinfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class Addpet extends AppCompatActivity {
    private static final String MY_PREFS = "my_prefs";
    Button button;
    Button update;
    private String imgstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText type = (EditText) findViewById(R.id.type);
        final EditText breed = (EditText) findViewById(R.id.breed);
        final EditText color = (EditText) findViewById(R.id.color);
        final EditText marking = (EditText) findViewById(R.id.marking);



        update = findViewById(R.id.upload);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });
        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ///////update status pet จริงๆจังๆ ////////
                List<NameValuePair> nameValuePairList1 = new ArrayList<>();
                nameValuePairList1.add(new BasicNameValuePair("email", shared.getString("email", "pass")));
                nameValuePairList1.add(new BasicNameValuePair("status", "true"));
                editor.putString("statuspet", "true");
                editor.commit();
                if (imgstr == null||name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "กรอกข้อมูลไม่ครบถ้วน", Toast.LENGTH_SHORT).show();
                }
                else

                {
                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        String url = "http://sniperkla.lnw.mn/editstatus.php";
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList1, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        InputStream is = httpEntity.getContent();
                        Toast.makeText(getApplicationContext(), "ส่งค่าแล้ว", Toast.LENGTH_SHORT).show();
                        is.close();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                    }

                    ////////////////////////////////////////////

                    String name1 = name.getText().toString();
                    String type1 = type.getText().toString();
                    String breed1 = breed.getText().toString();
                    String color1 = color.getText().toString();
                    String marking1 = marking.getText().toString();
                    String email1 = shared.getString("email", "null");
                    try {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("email", email1));
                        nameValuePairList.add(new BasicNameValuePair("name", name1));
                        nameValuePairList.add(new BasicNameValuePair("type", type1));
                        nameValuePairList.add(new BasicNameValuePair("breed", breed1));
                        nameValuePairList.add(new BasicNameValuePair("color", color1));
                        nameValuePairList.add(new BasicNameValuePair("marking", marking1));
                        nameValuePairList.add(new BasicNameValuePair("image", imgstr));
                        HttpClient httpClient = new DefaultHttpClient();
                        String url = "http://sniperkla.lnw.mn/addpet.php";
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        InputStream is = httpEntity.getContent();
                        Toast.makeText(getApplicationContext(), "ส่งค่าแล้ว", Toast.LENGTH_SHORT).show();
                        is.close();
                        Intent intent = new Intent(Addpet.this, showinfo.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });


    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data); // Toast.makeText(getApplicationContext(), "ถ่ายละนะ" , Toast.LENGTH_SHORT).show();
        try {
            Uri selectedImage = data.getData();
            Bitmap bitmap ;
            switch (requestCode) {
//////////////////////////////////////ดึงรูป ไปใส่ดาต้าเบส///////////////////////////////
                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {


                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        Bitmap resized = Bitmap. createScaledBitmap ( bitmap , 250 , 250 , true ) ;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.PNG, 50, baos);

                        byte[] b = baos.toByteArray();

                        String image_str = Base64.encodeToString(b,Base64.DEFAULT);
                        imgstr = image_str;


                    } else if (resultCode == Activity.RESULT_CANCELED) {

                    }

            }
        } catch (Exception e) {

        }
    }

}




