package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class Movie_ListActivity extends ListActivity implements Runnable{


    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器

    private int msgWhat = 7;
    private final String TAG = "Rate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__list);

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
                    SimpleAdapter adapter = new SimpleAdapter(Movie_ListActivity.this, retList, // listItems数据源
                            R.layout.activity_movielist_item, // ListItem的XML布局实现
                            new String[] { "movieName", "DirectorName" },
                            new int[] { R.id.movieName, R.id.directorName });
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
            map.put("movieName", "........?........ " ); // 标题文字
            map.put("DirectorName", "........!........"  ); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems数据源
                R.layout.activity_movielist_item, // ListItem的XML布局实现
                new String[] { "movieName", "DirectorName" },
                new int[] { R.id.movieName, R.id.directorName }
        );
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ranking,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_set){

            Intent fine = new Intent(this,MainActivity.class);
            startActivity(fine);        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void run() {



        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
        Document doc = null;
        try {
            /*doc = Jsoup.connect("https://movie.douban.com/top250").get();*/
            doc = Jsoup.connect("https://movie.douban.com/top250").get();

//                Log.i(TAG, "run: " + doc.title());

            Elements ols = doc.getElementsByTag("ol");

/*                int m = 1;
                for(Element ul :uls){
                    Log.i(TAG, "run: ul["+m+"]" + uls);
                }*/

//获取书名

            Element ol1=ols.get(0);
//                Log.i(TAG, "run: ul2" + ul2);

            Elements as = ol1.getElementsByTag("a");


            Elements ps = ol1.getElementsByTag("p");

            for(Element p :ps){
                Log.i(TAG, "run: p" + p.text());
            }




            for (int i = 0; i < as.size(); i+=2) {
                Element td = as.get(i+1);
//                Element td2 = spans.get(i+2);

                Element pss=ps.get(i);


                int x = i/2 +1;
                String tdStr = td.text();

                String result ="[电影]" +String.valueOf(x) +"."+tdStr;
//                String pStr = td2.text();

                String psss=pss.text();


                HashMap<String, String> map = new HashMap<String, String>();
                map.put("movieName", result);
                map.put("DirectorName", psss);

                rateList.add(map);
                Log.i("wwwww",tdStr);
                Log.i(TAG, "run: " +psss);
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



}
