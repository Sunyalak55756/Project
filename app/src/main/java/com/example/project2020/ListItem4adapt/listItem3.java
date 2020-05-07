package com.example.project2020.ListItem4adapt;


import android.graphics.Bitmap;
import android.widget.ImageView;

public class listItem3 {
    String head;
    Bitmap dest;
    public listItem3(String head, Bitmap  dest){
        this.head = head;
        this.dest = dest;
    }
    public String getHead(){
        return head;
    }
    public Bitmap  getDesc(){
        return dest;
    }

    }

