package com.example.testremote;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BulletinWriteActivity extends Activity {

    TextView date, writer, title;
    EditText content;
    SharedPreferences prefs;
    Bundle bundle;
    String userinfo_type, userinfo_title, userinfo_num;
    List<NameValuePair> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_board_write);



        writer = (TextView)findViewById(R.id.bulletin_nickname);    //닉네임
        date = (TextView)findViewById(R.id.bullentin_date); //날짜
        title = (TextView)findViewById(R.id.bulletin_title); //클럽이름
        content = (EditText)findViewById(R.id.bulletin_content);

        prefs = getApplicationContext().getSharedPreferences("Chat", 0);
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putString("Nickname", "jjong");
//        //edit.putString("FROM_NAME", pw.getText().toString());
//        edit.commit();

        bundle = getIntent().getBundleExtra("userinfo");
        userinfo_type = bundle.getString("type");
        userinfo_num = bundle.getString("Num");
        userinfo_title = bundle.getString("Title");



        //닉네임, 아이디는 prefs에서 불러오기
        writer.setText(prefs.getString("Nickname",""));
        //date에는 현재 날짜 불러오기

        long now = System.currentTimeMillis();
        Date sdate = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(sdate);

        date.setText(getTime);
        title.setText(userinfo_title);



        //type을 구분 CNum, CTitle은 번들로 넘어온거 받기
        //컨텐트는 에디트 텍스트에서 받기

    }

    public void bulletin_complete_btn(View view) {
        //5가지 정보를 디비로 전송하기
        //id, CNum, CBNum, date, content, Nickname
        //CBNum은 키, 자동으로 증가

        //닉네임, 아이디는 prefs에서 불러오기
        //writer.setText(prefs.getString("Nickname",""));
        //date에는 현재 날짜 불러오기
        //type을 구분 CNum, CTitle은 번들로 넘어온거 받기
        //컨텐트는 에디트 텍스트에서 받기

        //여기서 기업은 다른 클래스 호출해서 작성하자.

        if(userinfo_type.equals("Club")){
            new BulletinWriteActivity.InsertBulletin().execute(prefs.getString("REG_FROM",""),userinfo_num,date.getText().toString(), content.getText().toString(),prefs.getString("Nickname",""));
        }else  if(userinfo_type.equals("Recruit")){
            new BulletinWriteActivity.InsertBulletin().execute(prefs.getString("REG_FROM",""),userinfo_num,date.getText().toString(), content.getText().toString(),prefs.getString("Nickname",""));
        }
    }

    private class InsertBulletin extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            Log.d("loglogloglog",args[0]);

            if(userinfo_type.equals("Club")){
                params.add(new BasicNameValuePair("id", args[0]));
                params.add(new BasicNameValuePair("Num", args[1]));
                params.add((new BasicNameValuePair("date",args[2])));
                params.add(new BasicNameValuePair("content", args[3]));
                params.add(new BasicNameValuePair("Nickname", args[4]));
                params.add(new BasicNameValuePair("mypage_type", userinfo_type));

            }else if(userinfo_type.equals("Recruit")){

                params.add(new BasicNameValuePair("id", args[0]));
                params.add(new BasicNameValuePair("Num", args[1]));
                params.add((new BasicNameValuePair("date",args[2])));
                params.add(new BasicNameValuePair("content", args[3]));
                params.add(new BasicNameValuePair("Nickname", args[4]));
                params.add(new BasicNameValuePair("mypage_type", userinfo_type));
            }


            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/InsertBulletin",params);
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {
                    finish();
                   // startActivity(new Intent(getApplicationContext(),UserActivity.class));
                }else{
                    //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }




}
