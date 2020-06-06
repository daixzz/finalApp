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

public class MainActivity extends AppCompatActivity implements Runnable {


    private final String TAG = "Rate";
    String updateDate = "";
    EditText movie_keyw;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movie_keyw = (EditText) findViewById(R.id.keyWord);


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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {

        Log.i(TAG, "run: run()......");
        for (int i = 1; i < 3; i++) {
            Log.i(TAG, "run: i=" + i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage(5);
            msg.obj = "Hello from run()";
            handler.sendMessage(msg);


            Document doc = null;
            try {
                doc = Jsoup.connect("https://movie.douban.com/top250").get();
                Log.i(TAG, "run: " + doc.title());

                Elements ols = doc.getElementsByTag("ol");

                /*int m =1;
                for(Element ol :ols){
                    Log.i(TAG, "run: ol["+m+"] " + ols);
                }*/

                Element ol1 = ols.get(0);
                //Log.i(TAG, "run: ol1" + ol1);
                //获取span中的数据
                Elements lis = ol1.getElementsByTag("li");

                for(Element li:lis)
                {
                    Log.i(TAG, "run: spans "+ li);

                }

                Elements spans = ol1.getElementsByTag("span");
                Elements ps = ol1.getElementsByTag("p");
                Elements as = ol1.getElementsByTag("a");
                for(Element span:spans){
//                    Log.i(TAG, "run: div" + div);

                    Log.i(TAG, "run: text=" +span.text());

                }
                /*for(Element p :ps){
                    Log.i(TAG, "run: p" + ps);
                }

                for(Element a :as){
                    Log.i(TAG, "run: href" + a);
                }

                for(int q = 0;i<divs.size();q+=38){
                    Element div1 = divs.get(i);
                    Element p1 = ps.get(i);
                    Element a1 = ps.get(i);

                }*/




            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
