package com.example.testremote;

/**
 * Created by seyeon on 2017-10-19.
 */

public class User {
    private String num;
    private String Name;
    private String ID;

    public User(String num,String ID){
        this.num = num;
        this.ID = ID;
    }
    public User(String num, String ID, String Name){
        this.num = num;
        this.Name = Name;
        this.ID = ID;
    }
    public void setUserNum(String Name){this.num = num;}
    public void setUserName(String Name){this.Name = Name;}
    public void setUserID(String ID){this.ID = ID;}

    public String getUserNum(){return num;}
    public String getUserName(){return Name;}
    public String getUserID(){return ID;}
}
