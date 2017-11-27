package com.example.testremote;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 혜진 on 2017-10-23.
 */

public class InsertDataTask extends AsyncTask<RequestForm,Integer,String> {

    JSONObject job;


    InsertDataTask(){



    }

    InsertDataTask(JSONObject reqJSONObject){

        this.job = reqJSONObject;

    }

    @Override
    protected String doInBackground(RequestForm... urls) {
        try{
            return downloadUrl(urls[0]);
        }catch (IOException e){
            e.printStackTrace();
            return "다운로드 실패";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("InsertOK")){
            Log.d("ddd","ddddddddddddddddddddddddddddddd");
            // new DownloadWebPageTask().execute("http://13.124.85.122:52273/search");
            //  Toast.makeText(,"select",Toast.LENGTH_SHORT).show();
        }
    }

    public String downloadUrl(RequestForm myurl) throws IOException{
        HttpURLConnection conn = null;
        String response = "InsertFailed";

        // job = new JSONObject();

        try{
            URL url = new URL(myurl.url);
            conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoOutput(true);

            //insert하려는 데이터 셋팅 필요. 변수 넘겨야 할듯..
//            job.put("type","login");
//            job.put("ID", "jackie0304");
//            job.put("PW", "1127");
//            job.put("Name", "안동현");

            Log.e("job 내용",job.toString());

            OutputStream os  = conn.getOutputStream();
            os.write(job.toString().getBytes());
            Log.e("ddd","ReqJSONObject 전송");
            os.flush();


//            BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
//            BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));
//            String line = null;
//            String page = "";
//            while ((line = bufreader.readLine()) != null) {
//                page += line;
//            }
//            Log.i("page : " , page);
//             query 중복 에러 처리 해야됨.

            int responseCode = conn.getResponseCode();
            Log.e("ddd","ReqJSONObject 성공");
            if(responseCode == HttpURLConnection.HTTP_OK){
                response = "InsertOK";
            }
            else{
                Log.e("insert error",String.valueOf(responseCode));
            }

        } finally {
            conn.disconnect();
        }

        return  response;
    }

}
