package com.example.testremote;

/**
 * Created by 혜진 on 2017-10-24.
 */

public class MyPage_BucketItem {
    String title;
    String date;
    String content;

    public MyPage_BucketItem(String title, String date, String content){
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}
