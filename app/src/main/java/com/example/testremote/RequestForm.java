package com.example.testremote;

import org.json.JSONObject;

/**
 * Created by Donghyun on 2017-11-23.
 */

public class RequestForm {

    String url;
    JSONObject jsonParam;

    RequestForm(String url){

        this.url = url;
        jsonParam = null;

    }

    RequestForm(String url, JSONObject jsonObject){

        this.url = url;
        this.jsonParam = jsonObject;

    }
}
