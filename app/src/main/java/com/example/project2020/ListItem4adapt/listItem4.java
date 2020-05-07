package com.example.project2020.ListItem4adapt;


import android.graphics.drawable.Drawable;

public class listItem4 {
    String head;
    Drawable pass;
  //  String dest;

    public listItem4(String head, Drawable pass){
        this.head = head;
       this.pass = pass;
    }
    public String getHead(){
        return head;
    }
    public Drawable getpass()
    {
        return pass;
    }


}