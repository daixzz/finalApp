package com.zhen.finalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {


    private DBHelper dbHelper;
    private String TBNAME;

    public ItemManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(thingItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("curname", item.getTitlethings());
        values.put("currate", item.getNotesthings());
        db.insert(TBNAME, null, values);
        db.close();
    }


    public List<thingItem> listAll(){
        List<thingItem> rateList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            rateList = new ArrayList<thingItem>();
            while(cursor.moveToNext()){
                thingItem item = new thingItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setNotesthings(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item.setTitlethings(cursor.getString(cursor.getColumnIndex("CURRATE")));

                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;

    }


    public void addAll(List<thingItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (thingItem item : list) {
            ContentValues values = new ContentValues();
            values.put("curname", item.getTitlethings());
            values.put("currate", item.getNotesthings());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }
    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }
}
