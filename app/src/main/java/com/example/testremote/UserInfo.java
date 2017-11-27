package com.example.testremote;

/**
 * Created by Donghyun on 2017-11-23.
 */

public class UserInfo {
    String id;
    String pw;
    String nickname;
    String name;

    UserInfo(){

        id = "";
        pw = "";
        nickname = "";
        name = "";

    }

    UserInfo(String id, String pw, String nickname, String name){

        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.name = name;
    }
}
