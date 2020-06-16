package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.app.Activity;
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

public class Book_ListActivity extends ListActivity implements Runnable , AdapterView.OnItemClickListener{



    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器

    private int msgWhat = 7;
    private final String TAG = "Rate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__list);

//        List<String> list1 = new ArrayList<String>();
//
//        for(int i =1;i<100;i++){
//            list1.add("item" + i);
//        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Book_ListActivity.this,android.R.layout.simple_list_item_1,list_data);//新建并配置ArrayAapeter
//        listt.setAdapter(adapter);



        Thread t = new Thread(this);
        t.start();

        initListView();
        this.setListAdapter(listItemAdapter);



        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
            if(msg.what == 7){
                List<HashMap<String, String>> retList = (List<HashMap<String, String>>) msg.obj;
                SimpleAdapter adapter = new SimpleAdapter(Book_ListActivity.this, retList, // listItems数据源
                        R.layout.activity_booklist_item, // ListItem的XML布局实现
                        new String[] { "bookname", "authorname" },
                        new int[] { R.id.bookname, R.id.theAuthor });
                setListAdapter(adapter);
                Log.i("handler","reset list...");;
            }

        super.handleMessage(msg);
        }

        };
    }


    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("bookname", "name： " + i); // 标题文字
            map.put("authorname", "author" + i); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems数据源
                R.layout.activity_booklist_item, // ListItem的XML布局实现
                new String[] { "bookname", "authorname" },
                new int[] { R.id.bookname, R.id.theAuthor }
        );
    }
    public void run() {
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
            Document doc = null;
            try {
                /*doc = Jsoup.connect("https://movie.douban.com/top250").get();*/
                doc = Jsoup.connect("https://weread.qq.com/web/category/all").get();

//                Log.i(TAG, "run: " + doc.title());

                Elements uls = doc.getElementsByTag("ul");

/*                int m = 1;
                for(Element ul :uls){
                    Log.i(TAG, "run: ul["+m+"]" + uls);
                }*/

//获取书名

                Element ul2=uls.get(1);
//                Log.i(TAG, "run: ul2" + ul2);

                Elements pw = ul2.getElementsByTag("p");

                for(Element p :pw){
                    Log.i(TAG, "run:p " + p.text());
                }


                for (int i = 0; i < pw.size(); i+=5) {
                    Element td = pw.get(i+1);
                    Element td2 = pw.get(i+2);

                    int x = i/5+1;

                    String tdStr = td.text();

                    String result = "[书籍]"+String.valueOf(x) +"."+tdStr;
                    String pStr = td2.text();

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("bookname", result);
                    map.put("authorname", pStr);

                    rateList.add(map);
                    Log.i("td",tdStr + "=>" + pStr);
                }

                marker = true;

            } catch (IOException e) {
                e.printStackTrace();
            }


        Message msg = handler.obtainMessage();
        msg.what = 7;
        if(marker){
            msg.arg1 = 1;
        }else{
            msg.arg1 = 0;
        }

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread", "sendMessage.....");
        }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        getListView().setOnItemClickListener(this);
        Log.i(TAG, "onItemClick: parent=" + parent);
        Log.i(TAG, "onItemClick: view=" + view);
        Log.i(TAG, "onItemClick: position=" + position);
        Log.i(TAG, "onItemClick: id=" + id);
    }
}

