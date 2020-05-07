package com.example.project2020.announce;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.example.project2020.ListItem4adapt.listItem2;
import com.example.project2020.ListItem4adapt.listItem5;
import com.example.project2020.R;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static android.Manifest.permission.CALL_PHONE;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class adaptannounce extends RecyclerView.Adapter<adaptannounce.ViewHolder> {

    private static final String MY_PREFS = "my_prefs";
    ArrayList<HashMap<String, String>> location;

    private List<listItem5> listItems;
    private Context Context;
    private LayoutInflater mInflater;
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> email1 = new ArrayList<String>();
    private ArrayList<String> tel = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> summary = new ArrayList<String>();
    private ArrayList<String> nameuser = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();

    String type1;
    String breed1;
    String color1;
    String marking1;
    String image1;


    public adaptannounce(List<listItem5> listItems, android.content.Context Context) {
        this.listItems = listItems;

        this.mInflater = LayoutInflater.from(Context);
        this.Context = Context;


    }

    @Override
    public adaptannounce.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rowlayout1, parent, false);
        adaptannounce.ViewHolder viewHolder = new adaptannounce.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(final adaptannounce.ViewHolder holder, final int position) {
        listItem5 listitem = listItems.get(position);
        holder.myTextView1.setText(listitem.getHead());
        holder.myTextView2.setText(listitem.getDesc());
        holder.myTextView3.setText(listitem.getFinale());
        holder.myTextView4.setText(listitem.getFinale1());
        holder.myTextView5.setText(listitem.getFinale2());
        holder.img.setImageBitmap(((Bitmap.createScaledBitmap(listitem.getFinale3(), 250, 250, false))));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////


        holder.view.setOnClickListener(new View.OnClickListener() {
            final SharedPreferences shared = Context.getSharedPreferences(MY_PREFS,
                    Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = shared.edit();

            @Override
            public void onClick(View view) {
                String url2 = "http://sniperkla.lnw.mn/getannounce.php"; // ชื่อ host เพื่อติดต่อกับ php เพื่อดึงข้อมูล DB
                try {
                    JSONArray data = new JSONArray(getHttpGet(url2));

                    location = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map1;

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        map1 = new HashMap<String, String>();
                        map1.clear();
                        map1.put("id", c.getString("id"));
                        map1.put("email", c.getString("email"));
                        map1.put("summary", c.getString("summary"));
                        map1.put("tel", c.getString("tel"));
                        map1.put("date", c.getString("date"));
                        map1.put("name", c.getString("name"));
                        map1.put("nameuser", c.getString("nameuser"));
                        location.add(map1);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                int x = 0;

                for (int i = 0; i < location.size(); i++) {
                    id.add(location.get(i).get("id").toString());
                    email1.add(location.get(i).get("email").toString());
                    summary.add(location.get(i).get("summary").toString());
                    tel.add(location.get(i).get("tel").toString());
                    date.add(location.get(i).get("date").toString());
                    name.add(location.get(i).get("name").toString());
                    nameuser.add(location.get(i).get("nameuser").toString());

                }
                Collections.reverse(email1);
                Collections.reverse(id);
                Collections.reverse(summary);
                Collections.reverse(tel);
                Collections.reverse(date);
                Collections.reverse(name);
                Collections.reverse(nameuser);

                if (shared.getString("email", "null").equals(email1.get(position))) {

                    final Dialog MyDialog = new Dialog(view.getContext());
                    MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    MyDialog.setContentView(R.layout.dialogtrue);
                    MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView head;
                    head = (MyDialog).findViewById(R.id.head);
                    head.setText("" + nameuser.get(position));
                    Button call;
                    Button edit;
                    Button del;
                    Button data;


                    edit = (MyDialog).findViewById(R.id.edit);
                    del = (MyDialog).findViewById(R.id.del);
                    call = (MyDialog).findViewById(R.id.tel);
                    data = (MyDialog).findViewById(R.id.data);
                    MyDialog.show();
                    ////////////////โทร//////////////////
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri number = Uri.parse("tel:"+tel.get(position));
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                            view.getContext().startActivity(callIntent);
                        }
                    });
                    //////////////ลบข้อมูล/////////////////
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog MyDialog1 = new Dialog(view.getContext());
                            MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            MyDialog1.setContentView(R.layout.dialogaccept);
                            MyDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            MyDialog1.show();
                            Button accept;
                            Button close;
                            accept = MyDialog1.findViewById(R.id.submit);
                            close = MyDialog1.findViewById(R.id.close);
                            accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("id", id.get(position)));
                                    nameValuePairList.add(new BasicNameValuePair("name", name.get(position)));
                                    nameValuePairList.add(new BasicNameValuePair("email", shared.getString("email","null")));
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        String url = "http://sniperkla.lnw.mn/deleteannounce.php";
                                        HttpPost httpPost = new HttpPost(url);
                                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                        HttpResponse httpResponse = httpClient.execute(httpPost);
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        InputStream is = httpEntity.getContent();
                                        Toast.makeText(view.getContext(), "ลบข้อมูลแล้ว", Toast.LENGTH_SHORT).show();

                                        is.close();
                                        Intent intent = new Intent(view.getContext(), showannounce.class);
                                        view.getContext().startActivity(intent);
                                    } catch (IOException e) {
                                        Toast.makeText(view.getContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MyDialog1.dismiss();
                                }
                            });

                        }


                    });

                    //////แก้ไขข้อมูล///////
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog MyDialog1 = new Dialog(view.getContext());
                            MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            MyDialog1.setContentView(R.layout.activity_editannounce);
                            MyDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            MyDialog1.show();
                            final EditText infomation;
                            infomation = MyDialog1.findViewById(R.id.infomation);
                            Button submit;
                            submit = MyDialog1.findViewById(R.id.submitannounce);
                            infomation.setText(summary.get(position));
                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("summary", infomation.getText().toString()));
                                    nameValuePairList.add(new BasicNameValuePair("id", id.get(position)));
                                    try {
                                        HttpClient httpClient = new DefaultHttpClient();
                                        String url = "http://sniperkla.lnw.mn/editannounce.php";
                                        HttpPost httpPost = new HttpPost(url);
                                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));//ส่วนที่ทำให้ป้อนภาษาไทยได้
                                        HttpResponse httpResponse = httpClient.execute(httpPost);
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        InputStream is = httpEntity.getContent();
                                        Toast.makeText(view.getContext(), "บันทึกการเปลี่ยนแปลงแล้ว", Toast.LENGTH_SHORT).show();

                                        is.close();
                                        Intent intent = new Intent(view.getContext(), showannounce.class);
                                        view.getContext().startActivity(intent);
                                    } catch (IOException e) {
                                        Toast.makeText(view.getContext(), "ล้มเหลวโปรดลองใหม่", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    });


                    //////////////แสดงข้อมูลเพิ่มเติม//////////
                    data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), announceinfo.class);
                            intent.putExtra("nameuser",nameuser.get(position));
                            intent.putExtra("summary",summary.get(position));
                            intent.putExtra("email",email1.get(position));
                            view.getContext().startActivity(intent);
                        }
                    });
                } else {
                    final Dialog MyDialog = new Dialog(view.getContext());
                    MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    MyDialog.setContentView(R.layout.dialogtrue);
                    MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView head;
                    TextView head1;
                    head1 = (MyDialog).findViewById(R.id.textdel);
                    head1.setVisibility(View.INVISIBLE);
                    head = (MyDialog).findViewById(R.id.head);
                    head.setText("" + nameuser.get(position));
                    Button call;
                    Button del;
                    Button edit;
                    Button data;
                    del = (MyDialog).findViewById(R.id.del);
                    call = (MyDialog).findViewById(R.id.tel);
                    del.setVisibility(View.INVISIBLE);
                    MyDialog.show();
                    data = (MyDialog).findViewById(R.id.data);
                    MyDialog.show();
                    //โทร//
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri number = Uri.parse("tel:"+tel.get(position));
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                            view.getContext().startActivity(callIntent);
                        }
                    });


//แก้ไข//
                    edit = MyDialog.findViewById(R.id.edit);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(view.getContext(), "คุณไม่ใช่เจ้าของโพส", Toast.LENGTH_SHORT).show();
                        }
                    });



// แสดง //
                    data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                Intent intent = new Intent(view.getContext(), announceinfo.class);
                                intent.putExtra("nameuser",nameuser.get(position));
                                intent.putExtra("summary",summary.get(position));
                                intent.putExtra("email",email1.get(position));
                                view.getContext().startActivity(intent);
                            }

                    });

                }

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
        public TextView myTextView2;
        public TextView myTextView3;
        public TextView myTextView4;
        public TextView myTextView5;
        public ImageView img;
        public Button button;
        public View view;

        /////////////////////////////////////////////////////กำหนดปุ่มม
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            myTextView1 = (TextView) itemView.findViewById(R.id.Heading);
            myTextView2 = (TextView) itemView.findViewById(R.id.dest);
            myTextView3 = (TextView) itemView.findViewById(R.id.finale);
            myTextView4 = (TextView) itemView.findViewById(R.id.finale1);
            myTextView5 = (TextView) itemView.findViewById(R.id.finale2);
            img = (ImageView) itemView.findViewById(R.id.finale3);
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






