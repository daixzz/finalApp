package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WishListActivity extends ListActivity implements Runnable,AdapterView.OnItemClickListener{


    int index;
    Handler handler;
    private List<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器
    ItemManager manager;

    String notes,title;
    String data[] ={"wait..."};
    private int msgWhat = 7;
    private final String TAG = "Rate";

    ListView listView;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_wish_list);

        title = getIntent().getStringExtra("total");
        notes = getIntent().getStringExtra("notes");




        Thread t = new Thread(this);
        t.start();

        initListView();
        this.setListAdapter(listItemAdapter);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 7){
//                    List<HashMap<String, String>> list2 = (List<HashMap<String, String>>) msg.obj;
                    listItems = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(WishListActivity.this, listItems, // listItems数据源
                            R.layout.activity_wish_item, // ListItem的XML布局实现
                            new String[] { "titles", "notes" },
                            new int[] { R.id.wishName, R.id.wishNote });
                    setListAdapter(adapter);
                    Log.i("handler","reset list...");;
                }

                super.handleMessage(msg);
            }

        };

        getListView().setOnItemClickListener(this);
        getListView().setBackgroundColor(Color.parseColor("#bebebe"));

    }


    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("titles", "........?........ " ); // 标题文字
            map.put("notes", "........!........：" ); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems数据源
                R.layout.activity_wish_item, // ListItem的XML布局实现
                new String[] { "titles", "notes" },
                new int[] { R.id.wishName, R.id.wishNote }
        );
    }

    @Override
    public void run() {

        thingItem wiItem = new thingItem(title, notes);

        manager = new ItemManager(this);

        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();


        Log.i(TAG, "onCreate: 写入数据");

        List<thingItem> testList = manager.listAll();

        for (thingItem item : testList) {

            String str1 = item.getTitlethings();
            String str2 = item.getNotesthings() ;

            Log.i(TAG, "run: " +str2 +str1);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("titles", str2);
            map.put("notes", str1);

            rateList.add(map);
        }

        marker = true;
        Message msg = handler.obtainMessage();
        msg.what = 7;
        if(marker){
            msg.arg1 = 1;
        }else{
            msg.arg1 = 0;
        }

        msg.obj = rateList;
        handler.sendMessage(msg);


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
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

        Log.i(TAG, "onItemClick: " + position);
        Log.i(TAG, "onItemClick: " + listItems.size());


        manager.delete(position);

        listItems.remove(id);
    }
}
