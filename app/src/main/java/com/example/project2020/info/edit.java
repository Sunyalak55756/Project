package com.example.project2020.info;

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

import com.example.project2020.R;

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

public class edit extends AppCompatActivity {
    private static final String MY_PREFS = "my_prefs";
    private  String imgstr;
    private byte[] decodedString;
    List<NameValuePair> nameValuePairList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpet);
        ////////////////////////////////


        final EditText name1;
        final EditText breed1;
        final EditText type1;
        final EditText color1;
        final EditText marking1;
        Button img; Button submit;
        String name;String type;String breed;String color;String marking;
        final String image;
        final String id;
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        breed= getIntent().getStringExtra("breed");
        color = getIntent().getStringExtra("color");
        marking = getIntent().getStringExtra("marking");
        image = getIntent().getStringExtra("image");
        id = getIntent().getStringExtra("id");
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        name1 = findViewById(R.id.nameuser); breed1 = findViewById(R.id.breedpet); type1 = findViewById(R.id.typepet); color1 = findViewById(R.id.colorpet);marking1 = findViewById(R.id.markingpet);
        Toast.makeText(getApplicationContext(), "ข้อมูลแสดง", Toast.LENGTH_SHORT).show();
        name1.setText(name);
        breed1.setText(breed);
        type1.setText(type);
        color1.setText(color);
        marking1.setText(marking);
        img =findViewById(R.id.uploadpet);
        submit = findViewById(R.id.submitpet);



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });




            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    nameValuePairList.add(new BasicNameValuePair("name", name1.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("breed", breed1.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("type", type1.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("color", color1.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("marking", marking1.getText().toString()));
                    nameValuePairList.add(new BasicNameValuePair("id", id));
                    nameValuePairList.add(new BasicNameValuePair("email", shared.getString("email", "null")));
                    if (imgstr!=null)
                    {

                        nameValuePairList.add(new BasicNameValuePair("image", imgstr));

                    }
                    else
                    {
                        nameValuePairList.add(new BasicNameValuePair("image", image));
                    }

                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        String url = "http://sniperkla.lnw.mn/editpet.php";
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        InputStream is = httpEntity.getContent();
                        Toast.makeText(edit.this, "บันทึกการเปลี่ยนแปลงแล้ว", Toast.LENGTH_SHORT).show();
                        is.close();
                        Intent intent = new Intent(edit.this, showinfo.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        Toast.makeText(edit.this, "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                    }

                }
            });

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


