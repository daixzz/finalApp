package com.zhen.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

public class WishListActivity extends ListActivity implements Runnable,AdapterView.OnItemLongClickListener{


    int index;
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
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
                    List<HashMap<String, String>> list2 = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(WishListActivity.this, list2, // listItems数据源
                            R.layout.activity_wish_item, // ListItem的XML布局实现
                            new String[] { "titles", "notes" },
                            new int[] { R.id.wishName, R.id.wishNote });
                    setListAdapter(adapter);
                    Log.i("handler","reset list...");;
                }

                super.handleMessage(msg);
            }

        };


        getListView().setOnItemLongClickListener(this);

    }


    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("titles", "title： " + i); // 标题文字
            map.put("notes", "notes" + i); // 详情描述
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


        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();

        thingItem wiItem = new thingItem(title, notes);
        manager = new ItemManager(this);
        manager.add(wiItem);
        Log.i(TAG, "onCreate: 写入数据");

        List<thingItem> testList = manager.listAll();

        for (thingItem item : testList) {

            String str1 = item.getTitlethings();
            String str2 = item.getNotesthings() ;

            Log.i(TAG, "run: " +str2 +str1);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("titles", str1);
            map.put("notes", str2);

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

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {


        Log.i(TAG,"onItemLongClick:长按列表项position"+position);
        Log.i(TAG,"onItemLongClick:长按列表项id"+id);
        //删除操作
        //stItems.remove((position));
        //stItemAdapter.notifyDataSetChanged();
        //构造对话框进行确认操作
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick:对话框事件处理");

                String deleteText = listItems.get(position).get("titles").toString();
                Log.i("TAG","Text的值"+deleteText);
                String det = listItems.get(position).get("notes").toString();
                Log.i("TAG","Text的值"+det);
                List<thingItem>testList=manager.listAll();
                for(thingItem i:testList){
                    if(deleteText.equals(i.getTitlethings())&&det.equals(i.getNotesthings())){
                        index=i.getId();
                        Log.i("TAG","index"+index);
                        manager.delete(index);
                        listItems.remove(position);
                        listItemAdapter.notifyDataSetChanged();

                    }
                }


                Log.i(TAG,"onItemLongClick:size="+listItems.size());


            }})
                .setNegativeButton("否",null);
        builder.create().show();


        return true;

    }


}
