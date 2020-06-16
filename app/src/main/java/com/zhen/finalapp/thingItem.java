package com.zhen.finalapp;

public class thingItem  {


    private int id;
    private String titlethings;
    private String notesthings;


    public thingItem() {
        titlethings = "";
        notesthings = "";
    }
    public thingItem(String titlethings, String notesthings) {
        this.titlethings = titlethings;
        this.notesthings = notesthings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitlethings() {
        return titlethings;
    }

    public void setTitlethings(String titlethings) {
        this.titlethings = titlethings;
    }

    public String getNotesthings() {
        return notesthings;
    }

    public void setNotesthings(String notesthings) {
        this.notesthings = notesthings;
    }
}
