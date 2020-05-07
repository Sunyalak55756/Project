package com.example.project2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class content_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        final Dialog MyDialog = new Dialog(content_main.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_login);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        MyDialog.show();
        ImageView button;
        button = (findViewById(R.id.login));
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(content_main.this, content_main.class);
                startActivity(intent);
            }


    });

}
}
