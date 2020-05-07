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

public class edituser extends AppCompatActivity {
    private static final String MY_PREFS = "my_prefs";
    public String imgstr;
    public List<NameValuePair> nameValuePairList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);

        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        final String email = shared.getString("email", "null");
        String name = shared.getString("editname", "null");
        String tel = shared.getString("edittel", "null");
        String dob = shared.getString("editdob", "null");
        String password = shared.getString("editpass", "null");

        Button upload;
        Button summit;
        final EditText ename = (EditText) findViewById(R.id.name);
        final EditText etel = (EditText) findViewById(R.id.tel);
        final EditText edob = (EditText) findViewById(R.id.dob);
        final EditText epass = (EditText) findViewById(R.id.password);
        upload = findViewById(R.id.upload);
        ename.setText(name);
        etel.setText(tel);
        edob.setText(dob);
        epass.setText(password);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });




        summit = findViewById(R.id.submit);
        summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgstr!=null){

                    nameValuePairList.add(new BasicNameValuePair("email", email));
                    nameValuePairList.add(new BasicNameValuePair("password", epass.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("name", ename.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("tel", etel.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("dob", edob.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("image",imgstr));

                }
                else
                {
                    nameValuePairList.add(new BasicNameValuePair("email", email));
                    nameValuePairList.add(new BasicNameValuePair("password", epass.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("name", ename.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("tel", etel.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("dob", edob.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("image",imgstr));
                    nameValuePairList.add(new BasicNameValuePair("image",shared.getString("editimg","null")));
                }

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    String url = "http://sniperkla.lnw.mn/editinfo.php";
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    InputStream is = httpEntity.getContent();
                    Toast.makeText(getApplicationContext(), "บันทึกการเปลี่ยนแปลงแล้ว", Toast.LENGTH_SHORT).show();
                    is.close();
                    editor.clear();
                    editor.commit();
                    //System.out.print ("xxx"+shared.getBoolean("check",false));
                    Intent intent = new Intent(edituser.this, MainActivity.class);
                    startActivity(intent);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                }
            }
        });

    /*    eemail.setText("xxx");
        ename.setText("xxname");
        etel.setText(tel);
        edob.setText(dob);
        epass.setText(password);*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            switch (requestCode) {
//////////////////////////////////////ดึงรูป ไปใส่ดาต้าเบส///////////////////////////////
                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {


                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        Bitmap resized = Bitmap. createScaledBitmap ( bitmap , 300 , 300 , true ) ;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.PNG, 50, baos);

                        byte[] b = baos.toByteArray();
                        String image_str = Base64.encodeToString(b, Base64.DEFAULT);
                        imgstr = image_str;
                        Toast.makeText(getApplicationContext(), "เลือกรูปภาพแล้ว", Toast.LENGTH_SHORT).show();

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        imgstr = null;
                        Toast.makeText(getApplicationContext(), "ยังไม่ได้เลือกรูปภาพ", Toast.LENGTH_SHORT).show();
                    }

            }
        } catch (Exception e) {

        }

    }
}


