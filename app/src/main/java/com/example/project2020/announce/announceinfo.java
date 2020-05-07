package com.example.project2020.announce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2020.R;

public class announceinfo extends AppCompatActivity {
    private byte[] decodedString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announceinfo);
        Intent intent = getIntent();
        String email;
        String nameuser;
        String summary;
        nameuser =  intent.getStringExtra("nameuser");
        email = intent.getStringExtra("email");
        summary = intent.getStringExtra("summary");
        TextView name; TextView summary1; TextView email1;

        email1 = findViewById(R.id.email);
        name = findViewById(R.id.nameuser);
        summary1 = findViewById(R.id.sum);



        name.setText("Username : ​"+nameuser);
        email1.setText("Email  : "+email);
        summary1.setText("        "+summary);
    }
}
