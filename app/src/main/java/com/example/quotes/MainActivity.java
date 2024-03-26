package com.example.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String ,String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);




        String url = "https://dummyjson.com/quotes";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray jsonArray= response.getJSONArray("quotes");

                    for (int x =0; x<jsonArray.length() ;x++){

                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                        String quote = jsonObject.getString("quote");
                        String author = jsonObject.getString("author");



                        hashMap = new HashMap<>();
                        hashMap.put("quote",quote);
                        hashMap.put("author",author);
                        arrayList.add(hashMap);

                    }
                    Myadapter myAdapter = new Myadapter();
                    gridView.setAdapter(myAdapter);



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);

    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.quotes,parent,false);



            TextView tvtext= myView.findViewById(R.id.tvtext);
            TextView tvwriter = myView.findViewById(R.id.tvwriter);
            ImageView tvcopy = myView.findViewById(R.id.tvcopy);
            ImageView tvshare = myView.findViewById(R.id.tvshare);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            tvtext.setTextColor(color);



            HashMap<String,String> hashMap= arrayList.get(position);
            String quote = hashMap.get("quote");
            String author = hashMap.get("author");



            tvtext.setText(quote);
            tvwriter.setText(author);

            tvcopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /////for copied///
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newHtmlText("textview",tvtext.getText().toString().trim(),null);
                    clipboardManager.setPrimaryClip(data);
                    Toast.makeText(getApplicationContext(),"copied",Toast.LENGTH_SHORT).show();
                }
            });

            tvshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String text = quote;


                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,text);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);

                }
            });


            return myView;
        }
    }
}