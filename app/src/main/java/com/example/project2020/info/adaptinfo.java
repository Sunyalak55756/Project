package com.example.project2020.info;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2020.ListItem4adapt.listItem3;
import com.example.project2020.announce.showannounce;
import com.example.project2020.R;
import com.example.project2020.qrcodegen;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class adaptinfo extends RecyclerView.Adapter<adaptinfo.ViewHolder> {

    private static final String MY_PREFS = "my_prefs";
    ArrayList<HashMap<String, String>> location ;

    private List<listItem3> listItems;
    private Context Context;
    private LayoutInflater mInflater;
     List<String> name1 = new ArrayList<String>();
    List<String> type1 = new ArrayList<String>();
    List<String> breed1 = new ArrayList<String>();
    List<String> color1 = new ArrayList<String>();
    List<String> marking1 = new ArrayList<String>();
    List<String> id1 = new ArrayList<String>();
    List<String> image = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ImageView pic ;
    String url;
    public adaptinfo(List<listItem3> listItems, android.content.Context Context) {

        this.listItems = listItems;

        this.mInflater = LayoutInflater.from(Context);
        this.Context = Context;




    }

    @Override
    public adaptinfo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rowlayout, parent, false);
        adaptinfo.ViewHolder viewHolder = new adaptinfo.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(final adaptinfo.ViewHolder holder, final int position) {
        listItem3 listitem = listItems.get(position);
        holder.myTextView1.setText(listitem.getHead());
        holder.img.setImageBitmap(((Bitmap.createScaledBitmap(listitem.getDesc(), 250, 250, false))));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////


        holder.view.setOnClickListener(new View.OnClickListener() {
            final SharedPreferences shared = Context.getSharedPreferences(MY_PREFS,
                    Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = shared.edit();
            @Override
            public void onClick(View view) {

                final Dialog MyDialog = new Dialog(view.getContext());
              //  MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.customdialog);
                MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final Button qrcode = (Button) MyDialog.findViewById(R.id.hello);
                final Button announce = (Button) MyDialog.findViewById(R.id.announce);
                final Button edit = (Button) MyDialog.findViewById(R.id.edit);
                final Button del = (Button) MyDialog.findViewById(R.id.delete);
                final ImageView pic= (ImageView) MyDialog.findViewById(R.id.pic);
                qrcode.setEnabled(true);
                announce.setEnabled(true);
                del.setEnabled(true);
                edit.setEnabled(true);
                MyDialog.show();
                ////////
                ArrayList<HashMap<String, String>> location = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    String url = "http://sniperkla.lnw.mn/getpet.php";
                    HttpPost httppost = new HttpPost(url);
                    try {
                        location = new ArrayList<HashMap<String, String>>();
                        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                        nameValuePairList.add(new BasicNameValuePair("email",shared.getString("email","null")));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                        HttpClient httpClient = new DefaultHttpClient();
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
                        HttpResponse httpResponse = httpClient.execute(httppost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        InputStream is = httpEntity.getContent();
                        is.close();
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        String responseBody = httpclient.execute(httppost, responseHandler);
                        JSONArray data = new JSONArray(responseBody);
                        location = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> map1;
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            map1 = new HashMap<String, String>();
                            map1.clear();
                            map1.put("id", c.getString("id"));
                            map1.put("name", c.getString("name"));
                            map1.put("type", c.getString("type"));
                            map1.put("breed", c.getString("breed"));
                            map1.put("color", c.getString("color"));
                            map1.put("marking", c.getString("marking"));
                            map1.put("image", c.getString("image"));


                            location.add(map1);
                            id1.add(location.get(i).get("id").toString());
                            name1.add(location.get(i).get("name").toString());
                            type1.add(location.get(i).get("type").toString());
                            breed1.add(location.get(i).get("breed").toString());
                            color1.add(location.get(i).get("color").toString());
                            marking1.add(location.get(i).get("marking").toString());
                            image .add(location.get(i).get("image").toString());



                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                byte[] decodedString = Base64.decode((image.get(position)), Base64.DEFAULT);
                Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                pic.setImageBitmap(imgBitMap);



                /////////////////////////////////////////////////////////




                //////////// generate html file on ftp server /////////////
                ///////////  สร้าง qr code /////////////
                    qrcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                               // Toast.makeText(view.getContext(), ""+shared.getString("email","null") , Toast.LENGTH_SHORT).show();
                                String newpath = shared.getString("email","null");
                                ArrayList<HashMap<String, String>> location = null;
                                try {
                                    HttpClient httpclient = new DefaultHttpClient();
                                    String url = "http://sniperkla.lnw.mn/getpetiflost.php";
                                    HttpPost httppost = new HttpPost(url);
                                    try {
                                        location = new ArrayList<HashMap<String, String>>();
                                        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                                        nameValuePairList.add(new BasicNameValuePair("email",shared.getString("email","null")));
                                        nameValuePairList.add(new BasicNameValuePair("name",name1.get(position)));
                                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                                        HttpClient httpClient = new DefaultHttpClient();
                                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
                                        HttpResponse httpResponse = httpClient.execute(httppost);
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        InputStream is = httpEntity.getContent();
                                        is.close();
                                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                                        String responseBody = httpclient.execute(httppost, responseHandler);
                                        JSONArray data = new JSONArray(responseBody);
                                        for (int i = 0; i < data.length(); i++) {
                                            JSONObject c = data.getJSONObject(i);
                                             status.add(c.getString("statuspet"));
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                } catch (ClientProtocolException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                Toast.makeText(view.getContext(), ""+status.get(0), Toast.LENGTH_SHORT).show();
                                if (status.get(0).equals("lost")) {
                                     url = "http://sniperkla.lnw.mn/genhtmliflost.php";
                                }
                                else {
                                    url = "http://sniperkla.lnw.mn/genhtml.php";
                                }
                                String newpaths = newpath+(id1.get(position));
                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("email", shared.getString("email","null")));
                                nameValuePairList.add(new BasicNameValuePair("number", shared.getString("edittel","null")));
                                nameValuePairList.add(new BasicNameValuePair("name", name1.get(position)));
                                nameValuePairList.add(new BasicNameValuePair("color", color1.get(position)));
                                nameValuePairList.add(new BasicNameValuePair("marking", marking1.get(position)));
                                nameValuePairList.add(new BasicNameValuePair("breed", breed1.get(position)));
                                nameValuePairList.add(new BasicNameValuePair("type", type1.get(position)));
                                nameValuePairList.add(new BasicNameValuePair("path",  newpaths));

                                editor.putString("path",newpaths);
                                editor.putString("pathpet",name1.get(position));
                                editor.commit();


                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost(url);
                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                HttpResponse httpResponse = httpClient.execute(httpPost);
                                HttpEntity httpEntity = httpResponse.getEntity();
                                InputStream is = httpEntity.getContent();

                                is.close();

                                Intent intent = new Intent(view.getContext(), qrcodegen.class);
                                intent.putExtra("name",name1.get(position));
                                view.getContext().startActivity(intent);


                            } catch (IOException e) {

                            }
                        }
                    });




                ///////////////สร้าง announce ////////////
                announce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date date = new Date();
                        final Dialog MyDialog = new Dialog(view.getContext());
                        //  MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        MyDialog.setContentView(R.layout.announce);
                      //  MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button submit ;
                        submit=(Button) MyDialog.findViewById(R.id.submit);
                        final  EditText summary;
                        final EditText time;
                        final EditText name;
                        final EditText tel;
                        name = (MyDialog).findViewById(R.id.namep);
                        summary = (MyDialog).findViewById(R.id.sum);
                        tel = (MyDialog).findViewById(R.id.tel);
                        time = (MyDialog).findViewById(R.id.date);
                        tel.setText(shared.getString("edittel","null").toString());
                        name.setText(name1.get(position));
                        time.setText(formatter.format(date));
                        MyDialog.show();

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    List<NameValuePair> nameValuePairList1 = new ArrayList<>();
                                    nameValuePairList1.add(new BasicNameValuePair("name",name.getText().toString()));
                                    nameValuePairList1.add(new BasicNameValuePair("email",shared.getString("email","null")));
                                    nameValuePairList1.add(new BasicNameValuePair("tel", tel.getText().toString()));
                                    nameValuePairList1.add(new BasicNameValuePair("time", time.getText().toString()));
                                    nameValuePairList1.add(new BasicNameValuePair("summary", summary.getText().toString()));
                                    nameValuePairList1.add(new BasicNameValuePair("user", shared.getString("editname","null")));
                                        try {
                                            HttpClient httpClient = new DefaultHttpClient();
                                            String url = "http://sniperkla.lnw.mn/announce.php";
                                            HttpPost httpPost = new HttpPost(url);
                                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList1, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                            HttpResponse httpResponse = httpClient.execute(httpPost);
                                            HttpEntity httpEntity = httpResponse.getEntity();
                                            InputStream is = httpEntity.getContent();
                                            Toast.makeText(view.getContext(), "ส่งค่าแล้ว", Toast.LENGTH_SHORT).show();
                                            is.close();
                                            Intent intent = new Intent (view.getContext(), showannounce.class);
                                            view.getContext().startActivity(intent);
                                            MyDialog.cancel();

                                        } catch (IOException e) {
                                            Toast.makeText(view.getContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                                        }
                                    List<NameValuePair> nameValuePairList2 = new ArrayList<>();
                                    nameValuePairList2.add(new BasicNameValuePair("statuspet","lost"));
                                    nameValuePairList2.add(new BasicNameValuePair("name",name1.get(position)));
                                    nameValuePairList2.add(new BasicNameValuePair("email",shared.getString("email","null")));
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        String url = "http://sniperkla.lnw.mn/editpetiflost.php";
                                        HttpPost httpPost = new HttpPost(url);
                                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList2, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                        HttpResponse httpResponse = httpClient.execute(httpPost);
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        InputStream is = httpEntity.getContent();
                                        is.close();

                                    } catch (IOException e) {
                                        Toast.makeText(view.getContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                });
                    ////////////////// edit function ////////////
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent (view.getContext(),edit.class);
                        intent.putExtra("name",name1.get(position));
                        intent.putExtra("breed",breed1.get(position));
                        intent.putExtra("type",type1.get(position));
                        intent.putExtra("color",color1.get(position));
                        intent.putExtra("marking",marking1.get(position));
                        intent.putExtra("id",id1.get(position));
                        intent.putExtra("image",image.get(position));
                        view.getContext().startActivity(intent);

                    }
                });
                    ///////////

               pic.findViewById(R.id.pic);
                //////////////////////// ลบข้อมูล///////////////////
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog MyDialog1 = new Dialog(view.getContext());
                        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        MyDialog1.setContentView(R.layout.dialogaccept);
                        MyDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        final Button close = (Button) MyDialog1.findViewById(R.id.close);
                        final Button submit = (Button) MyDialog1.findViewById(R.id.submit);
                        MyDialog1.show();
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MyDialog1.cancel();
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<NameValuePair> nameValuePairList1 = new ArrayList<>();
                                nameValuePairList1.add(new BasicNameValuePair("id",id1.get(position)));
                                nameValuePairList1.add(new BasicNameValuePair("email",shared.getString("email","null")));
                                nameValuePairList1.add(new BasicNameValuePair("name",name1.get(position)));
                                try {
                                    HttpClient httpClient = new DefaultHttpClient();
                                    String url = "http://sniperkla.lnw.mn/deletepet.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList1, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                    HttpResponse httpResponse = httpClient.execute(httpPost);
                                    HttpEntity httpEntity = httpResponse.getEntity();
                                    InputStream is = httpEntity.getContent();
                                    Toast.makeText(view.getContext(), "ลบข้อมูลแล้ว", Toast.LENGTH_SHORT).show();
                                    is.close();
                                } catch (IOException e) {
                                    Toast.makeText(view.getContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                                }

                                String newpath = shared.getString("email","null");
                                String newpaths = newpath+(id1.get(position));
                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("path",newpaths));
                                try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        String url = "http://sniperkla.lnw.mn/delqrcode.php";
                                        HttpPost httpPost = new HttpPost(url);
                                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                        HttpResponse httpResponse = httpClient.execute(httpPost);
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        InputStream is = httpEntity.getContent();
                                        is.close();
                                        Intent intent = new Intent(view.getContext(), showinfo.class);
                                        view.getContext().startActivity(intent);
                                    } catch (IOException e) {
                                    }

                                }
                        });

                    }
                });




            }
        });
    }





    // total number of rows
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // private final Button btnButton1;
        public TextView myTextView1;
        public ImageView img;
        public Button button;
        public View view;

        /////////////////////////////////////////////////////กำหนดปุ่มม
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            myTextView1 = (TextView) itemView.findViewById(R.id.Heading);
            img = (ImageView) itemView.findViewById(R.id.img);
        }

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


// stores and recycles views as they are scrolled off screen






