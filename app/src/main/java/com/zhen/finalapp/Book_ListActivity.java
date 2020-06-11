package com.zhen.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book_ListActivity extends ListActivity {


    private String[] list_data = {"one","tow","three","four"};
    int msgWhat = 3;
    Handler handler;

    private final String TAG = "Rate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_book__list);



        ListAdapter adapter = new ArrayAdapter<String>(Book_ListActivity.this,android.R.layout.simple_list_item_1,list_data);
        setListAdapter(adapter);
    }

    public void run() {
        Log.i("thread","run.....");
        List<String> rateList = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("https://weread.qq.com/web/category/all").get();

//            Log.i(TAG, "run: " + doc.title());

//                Elements ols = doc.getElementsByTag("ol");
            Elements uls = doc.getElementsByTag("ul");

            for(Element ul :uls){
                Log.i(TAG, "run: uls" + ul);
            }

//            Element ul2=uls.get(1);
//            Elements pw = ul2.getElementsByTag("p");
//
//            for(Element p :pw){
//                Log.i(TAG, "run:p " + p);
//            }

            } catch (IOException ex) {
            ex.printStackTrace();
        }


        Message msg = handler.obtainMessage(5);

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread","sendMessage.....");
    }


}
