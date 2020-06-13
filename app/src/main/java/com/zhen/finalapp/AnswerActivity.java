package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

public class AnswerActivity extends ListActivity implements Runnable{

    EditText keyw;

    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器
    Handler handler;
    String keyword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        keyw = (EditText) findViewById(R.id.keyWord);

        keyword = keyw.getText().toString();

        Thread t = new Thread(this);
        t.start();

        initListView();
        this.setListAdapter(listItemAdapter);



        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 7){
                    List<HashMap<String, String>> retList = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(AnswerActivity.this, retList, // listItems数据源
                            R.layout.activity_answer_item, // ListItem的XML布局实现
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
            map.put("resultName", "name： " + i); // 标题文字
            map.put("Producer", "author" + i); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems数据源
                R.layout.activity_booklist_item, // ListItem的XML布局实现
                new String[] { "resultName", "Producer" },
                new int[] { R.id.resultName, R.id.producer }
        );
    }


    @Override
    public void run() {
        boolean marker = false;

        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();

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
            for(int n = 1;n < pw.size();n++){
                Element td = pw.get(n+1);
                Element td2 = pw.get(n+2);

                int x = n/5+1;

                String tdStr = td.text();
                String pStr = td2.text();
                String result = String.valueOf(x) +"."+tdStr+"（书籍）";

                if(tdStr.contains(keyword)||(pStr.contains(keyword))){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("resultName", result);
                    map.put("Producer", pStr);
                }
                else {
                    break;
                }

            }

            for (int i = 0; i < as.size(); i+=2) {
                Element td = as.get(i+1);
//                Element td2 = spans.get(i+2);

                Element pss=ps.get(i);


                int x = i/2 +1;
                String tdStr = td.text();

                String result = String.valueOf(x) +"."+tdStr+"(电影)";
//                String pStr = td2.text();

                String psss=pss.text();


                    if(tdStr.contains(keyword)||psss.contains(keyword)){
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("resultName", result);
                        map.put("Producer", psss);
                    }
                    else {
                        break;
                    }


            }
            marker = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
