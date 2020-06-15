package com.zhen.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class add_pageActivity extends AppCompatActivity {



    String text;
    int Year,Month,Day;
    DatePicker datePicker;

    private EditText dateEdit;

    private Calendar calendar;
    Button aaa;
    TextView newText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);


        dateEdit =(EditText) findViewById(R.id.dateTo);



        aaa = (Button) findViewById(R.id.chooseDate);
        calendar = Calendar.getInstance();
        // 点击"日期"按钮布局 设置日期
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(add_pageActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                // TODO Auto-generated method stub
                                Year = year;
                                Month = month;
                                Day = day;
                                // 更新EditText控件日期 小于10加0
                                dateEdit.setText(new StringBuilder()
                                        .append(Year)
                                        .append("-")
                                        .append((Month + 1) < 10 ? "0"
                                                + (Month + 1) : (Month + 1))
                                        .append("-")
                                        .append((Day < 10) ? "0" + Day : Day));
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }



}
