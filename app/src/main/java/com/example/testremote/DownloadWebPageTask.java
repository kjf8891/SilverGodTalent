package com.example.testremote;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testremote.RequestForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by 혜진 on 2017-10-23.
 */
public class DownloadWebPageTask extends AsyncTask<RequestForm, Integer, String> {


    JSONArray retObj;

    public AsyncResponse delegate = null;

    DownloadWebPageTask(AsyncResponse delegate){

        this.delegate = delegate;

    }

    //웹에서 데이터를 받아오는 부분
    @Override
    protected String doInBackground(RequestForm... urls) {
        try{
            return downdloadUrl(urls[0]);//내가 넣어준 url
        }catch(IOException e){
            e.printStackTrace();
            return "다운로드 실패";
        }
    }

    //doingbackground가 끝난 결과가 여기로 들어옴
    //웹에서 읽어온 데이터를 여기서 처리
    @Override
    protected void onPostExecute(String s) {

        Log.i("onPostExecute",s);
        jsonParsing(s);//제이슨 파싱함수는 밑에있음
        try {
            delegate.processFinish(retObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String downdloadUrl(RequestForm req) throws IOException{
        HttpURLConnection conn = null;
        try{
            URL url = new URL(req.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoOutput(true);

            if(req.jsonParam != null) {

                OutputStream os = conn.getOutputStream();
                os.write(req.jsonParam.toString().getBytes());
                Log.d("send params", "파라미터 전송");
                os.flush();
            }

            //DB읽어오는 부분
            BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));
            //결과를 string으로 받음
            String line = null;
            String page = "";
            while ((line = bufreader.readLine()) != null) {//row결과를 읽음 readline
                page += line;
            }
            Log.i("page : " , page);
            return page;//결과를 string으로 받아온 결과
        }finally {
            conn.disconnect();
        }
    }

    void jsonParsing (String file) {

        String id = "";
        String name = "";
        String quantity = "";
        int  no = 0;
        String area = "";

        //171204 sy 변수선언
        String title = "";
        String content = "";
        //String date = "";


        try {
            retObj = new JSONArray(file);
            for ( int i = 0; i <retObj.length(); i++) {

                JSONArray jsonarray = retObj.getJSONArray(i);

                for (int j = 0; j < jsonarray.length(); j++) {


                    JSONObject json = jsonarray.getJSONObject(j);


                    if (json.has("ID"))
                        id = json.getString("ID");
                    if (json.has("PW"))
                        name = json.getString("PW");
                    if (json.has("Name"))
                        quantity = json.getString("Name");
                    if (json.has("no"))
                        no = json.getInt("no");
                    if (json.has("area"))
                        area = json.getString("area");

                    //171204 sy dwtask
                    if (json.has("title"))
                        title = json.getString("title");
//                if(json.has("date"))
//                    date = json.get("date");
                    if (json.has("content"))
                        content = json.getString("content");
//                if(json.has("writer"))
//                    name = json.getString("writer");


                    //171206 sy findMentoringInfo
                    if (json.has("MTitle"))
                        title = json.getString("MTitle");
//                if(json.has("date"))
//                    date = json.get("date");
                    if (json.has("content"))
                        content = json.getString("content");
                    if (json.has("MNum"))
                        content = json.getString("MNum");
                    if (json.has("content"))
                        content = json.getString("content");
//                if(json.has("writer"))
//                    name = json.getString("writer");


                    if (json.has("CNum"))
                        content = json.getString("CNum");
                    ////////////////////////////////////////////////////////////
                    Log.i("jsonParsing:", no + area);
                }
            }
        }catch (Exception e) {
            Log.i("err :", e.getMessage());
        }
    }

    public interface AsyncResponse{

        void processFinish(JSONArray ret) throws JSONException;
    }




}

