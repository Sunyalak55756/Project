package com.example.project2020.ListItem4adapt;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class listItem5 {
    String head;
    String dest;
    String finale;
    String finale1;
    String finale2;
    Bitmap finale3;
    public listItem5(String head, String dest, String finale,String finale1,String finale2,Bitmap finale3){
        this.head = head;
        this.dest = dest;
        this.finale  = finale;
        this.finale1  = finale1;
        this.finale2  = finale2;
        this.finale3 = finale3;
    }
    public String getHead(){
        return head;
    }
    public String getDesc(){
        return dest;
    }
    public String getFinale(){
        return finale;
    }
    public String getFinale1() {return finale1;}
    public String getFinale2() {return finale2;}
    public Bitmap getFinale3() {return finale3;}

}

