package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.reflect.Array.getInt;

public class AnswerActivity extends ListActivity implements Runnable{

    EditText keyw;

    private ArrayList<HashMap<String, String>> listItems; // ?????????
    private SimpleAdapter listItemAdapter; // ???
    Handler handler;
    String TAG = "haha";

    String keyword ;
    ItemManager manager;

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        Intent hello = getIntent();
        keyword = hello.getStringExtra("EXTRA_MESSAGE");


        Thread t = new Thread(this);
        t.start();

        initListView();
        this.setListAdapter(listItemAdapter);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 7){
                    List<HashMap<String, String>> retList = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(AnswerActivity.this, retList, // listItems???
                            R.layout.activity_answer_item, // ListItem?XML????
                            new String[] { "resultName", "Producer" },
                            new int[] { R.id.resultName, R.id.producer });
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
            map.put("resultName", "........?........：? " ); // ????
            map.put("Producer", "........!........：" ); // ????
            listItems.add(map);
        }
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems???
                R.layout.activity_answer_item, // ListItem?XML????
                new String[] { "resultName", "Producer" },
                new int[] { R.id.resultName, R.id.producer }
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


    @Override
    public void run() {

        boolean marker = false;


        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();

        Document doc1 = null;
        Document doc2 = null;


        try {
            Log.i(TAG, "okokok:" + keyword);

            doc1 = Jsoup.connect("https://movie.douban.com/top250").get();
            doc2 = Jsoup.connect("https://weread.qq.com/web/category/all").get();

//                Log.i(TAG, "run: " + doc.title());


/*                int m = 1;
                for(Element ul :uls){
                    Log.i(TAG, "run: ul["+m+"]" + uls);
                }*/

//????

//                Log.i(TAG, "run: ul2" + ul2);


            Elements ols = doc1.getElementsByTag("ol");
            Elements uls = doc2.getElementsByTag("ul");

            Element ul2=uls.get(1);
            Element ol1 = ols.get(0);

            Elements pw = ul2.getElementsByTag("p");

            Elements as = ol1.getElementsByTag("a");

            Elements ps = ol1.getElementsByTag("p");


            for(int i = 0;i < pw.size();i+=5){
                Element td = pw.get(i+1);
                Element td2 = pw.get(i+2);

                int x = i/5+1;

                String tdStr = td.text();

                String result = "[书籍]第"+String.valueOf(x) +"："+tdStr;
                String psss = td2.text();

                if(result.contains(keyword)||psss.contains(keyword)) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("resultName", result);
                    map.put("Producer", psss);
                    rateList.add(map);

                }

            }

            for (int i = 0; i < as.size(); i+=2) {
                Element td = as.get(i+1);
//                Element td2 = spans.get(i+2);

                Element pss=ps.get(i);


                int x = i/2 +1;
                String tdStr = td.text();

                String result = "[电影]第"+String.valueOf(x)+"：" +tdStr;
//                String pStr = td2.text();

                String psss=pss.text();

                if(result.contains(keyword)||psss.contains(keyword)){
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("resultName", result);
                        map.put("Producer", psss);
                    rateList.add(map);

                }

            }

            manager = new ItemManager(this);
            List<thingItem> testList = manager.listAll();

            for (thingItem item : testList) {

                String str1 = item.getTitlethings();
                String str2 = item.getNotesthings() ;

                if(str1.contains(keyword)||str2.contains(keyword)){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("resultName", str2);
                    map.put("Producer", str1);
                    rateList.add(map);

                }

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
