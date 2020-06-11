package com.zhen.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Movie_ListActivity extends ListActivity {


    private String[] list_data = {"one","tow","three","four"};
    int msgWhat = 3;
    Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie__list);
        ListAdapter adapter = new ArrayAdapter<String>(Movie_ListActivity.this,android.R.layout.simple_list_item_1,list_data);
        setListAdapter(adapter);
    }

    public void run() {
        Log.i("thread","run.....");
        List<String> rateList = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("https://movie.douban.com/top250").get();

            Elements ols = doc.getElementsByTag("ol");
            Element table = ols.get(0);

            Elements tds = table.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i+=5) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+3);

                String tdStr = td.text();
                String pStr = td2.text();
                rateList.add(tdStr + "=>" + pStr);

                Log.i("td",tdStr + "=>" + pStr);
            }
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(5);

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread","sendMessage.....");
    }



}
