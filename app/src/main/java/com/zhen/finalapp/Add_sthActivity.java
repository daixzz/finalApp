package com.zhen.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class Add_sthActivity extends AppCompatActivity {


    ItemManager manager;

    EditText date;
    String dateto;

    private Spinner spinner;

    String total;

    private ArrayAdapter<String> adapter;
    String text;
    String title,notes;
    EditText name , notees;
    String TAG = "ok";

    String[] data1 = {"电视剧", "电影", "综艺", "书籍"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sth);

        date = (EditText)findViewById(R.id.dateTo);
        name = (EditText)findViewById(R.id.itsok);
        notees = (EditText) findViewById(R.id.thatsalright);


        spinner = (Spinner)findViewById(R.id.hisp);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data1);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
             text = data1[arg2];
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    public void onclick(View v){
        Calendar calendar=Calendar.getInstance();
        new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = "你选择了：" + year + "年" + (month + 1) + "月" + dayOfMonth + "日";

                dateto=year+"-" + (month+1) +"-"+dayOfMonth;

                date.setText(dateto);
                Toast.makeText( Add_sthActivity.this, dateto, Toast.LENGTH_SHORT ).show();
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onOpen(View v){

        title = name.getText().toString();
        notes = notees.getText().toString();

        total = "[" + text + "]" +"——" + dateto +":" + title;
        Intent ok = new Intent(this,WishListActivity.class);


        thingItem wiItem = new thingItem(title, notes);
        manager = new ItemManager(this);
        manager.add(wiItem);
        startActivity(ok);



    }






}
