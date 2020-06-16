package com.zhen.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class theBeautyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_beauty);
    }


    public  void openMusic(View btn){
        Intent okkk = new Intent(this,MainActivity.class);
        startActivity(okkk);
    }
}
