package com.example.project2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class signup extends AppCompatActivity {
    private String imgstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText tel = (EditText) findViewById(R.id.tel);
        final EditText dob = (EditText) findViewById(R.id.dob);
        final EditText email = (EditText) findViewById(R.id.email);

        Button button = (Button) findViewById(R.id.submit);
        Button upload = (Button) findViewById(R.id.upload);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(password.getText()) || TextUtils.isEmpty(tel.getText()) || TextUtils.isEmpty(dob.getText())
                        || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(getApplicationContext(),
                            "มีฟิลที่ยังไม่ได้กรอก", Toast.LENGTH_SHORT).show();

                }

                else if(email.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d" +
                            "-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[" +
                            "a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][" +
                            "0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")&&password.getText().toString().length()>6
                            &&tel.getText().toString().length()>9&&email.getText().toString().length()>15)
                    {
                        String name1 = name.getText().toString();
                        String password1 = password.getText().toString();
                        String tel1 = tel.getText().toString();
                        String dob1 = dob.getText().toString();
                        String email1 = email.getText().toString();


                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("email1", email1));
                        nameValuePairList.add(new BasicNameValuePair("password1", password1));
                        nameValuePairList.add(new BasicNameValuePair("name1", name1));
                        nameValuePairList.add(new BasicNameValuePair("tel1", tel1));
                        nameValuePairList.add(new BasicNameValuePair("dob1", dob1));
                        nameValuePairList.add(new BasicNameValuePair("statuspet1", "false"));
                        nameValuePairList.add(new BasicNameValuePair("image1", imgstr));


                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            String url = "http://sniperkla.lnw.mn/testregisterpet.php";
                            HttpPost httpPost = new HttpPost(url);
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                            HttpResponse httpResponse = httpClient.execute(httpPost);
                            HttpEntity httpEntity = httpResponse.getEntity();
                            InputStream is = httpEntity.getContent();
                            Toast.makeText(getApplicationContext(), "ส่งค่าแล้ว", Toast.LENGTH_SHORT).show();
                            is.close();
                            Intent intent = new Intent(signup.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(),
                                "รูปแบบผิดพลาด", Toast.LENGTH_SHORT).show();

            }


        });
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }
});

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(signup.this,MainActivity.class);
        startActivity(intent);
       // super.onBackPressed();
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            switch (requestCode) {
//////////////////////////////////////ดึงรูป ไปใส่ดาต้าเบส///////////////////////////////
                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {


                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        Bitmap resized = Bitmap. createScaledBitmap ( bitmap , 250 , 250 , true ) ;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.PNG, 50, baos);

                        byte[] b = baos.toByteArray();
                        String image_str = Base64.encodeToString(b, Base64.DEFAULT);
                        imgstr = image_str;
                        Toast.makeText(getApplicationContext(), "เลือกรูปภาพแล้ว", Toast.LENGTH_SHORT).show();

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(getApplicationContext(), "ยังไม่ได้เลือกรูปภาพ", Toast.LENGTH_SHORT).show();
                    }

            }
        } catch (Exception e) {

        }


    }
}
