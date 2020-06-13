package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.ECKey;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Runnable {


    private final String TAG = "Rate";
    String updateDate = "";
    EditText keyw;

    Handler handler;
    String keyword ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyw = (EditText) findViewById(R.id.keyWord);

         keyword = keyw.getText().toString();



        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {


                super.handleMessage(msg);
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ranking,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_set){
            openConfig();

        }

            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        Log.i("open", "openOne: ");
        Intent hello = new Intent(this,AnswerActivity.class);
        EditText editText = (EditText) findViewById(R.id.keyWord);
        String message = editText.getText().toString();
        hello.putExtra("EXTRA_MESSAGE",message);
        startActivityForResult(hello,1);

    }

    public void openMovie(View btn){
        Intent ok = new Intent(this,Movie_ListActivity.class);
        startActivity(ok);
    }

    public void openBooks(View btn){
        Intent bookkks = new Intent(this,Book_ListActivity.class);
        startActivity(bookkks);
    }





    @Override
    public void run() {

            Message msg = handler.obtainMessage(5);
            msg.obj = "Hello from run()";
            handler.sendMessage(msg);


            Document doc1 = null;
            Document doc2 = null;

            try {
                doc1 = Jsoup.connect("https://movie.douban.com/top250").get();
                doc2 = Jsoup.connect("https://weread.qq.com/web/category/all").get();

//                Log.i(TAG, "run: " + doc.title());

                Elements ols = doc1.getElementsByTag("ol");
                Elements uls = doc2.getElementsByTag("ul");

                Element ul2=uls.get(1);
                Element ol1 = ols.get(0);

                Elements pw = ul2.getElementsByTag("p");

                Elements as = ol1.getElementsByTag("a");

                Elements ps = ol1.getElementsByTag("p");
                /*for(int n = 1;n < pw.size();n++){
                    Element td = pw.get(n+1);
                    Element td2 = pw.get(n+2);

                    int x = n/5+1;

                    String tdStr = td.text();
                    String pStr = td2.text();

                    if(tdStr.contains(keyword)||(pStr.contains(keyword))){
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("movieName", tdStr);
                        map.put("DirectorName", pStr);
                    }

                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


