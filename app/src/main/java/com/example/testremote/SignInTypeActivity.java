package com.example.testremote;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignInTypeActivity extends AppCompatActivity {

    ImageButton btn_general;
    ImageButton btn_company;
    List<NameValuePair> params;

    SharedPreferences prefs;

    @Override
    public void onBackPressed() {
        Log.d("SignInActivity", "Backkkkkk");

        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new SignInTypeActivity.ActivityChangeSend().execute("back");
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("activity_change"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("SignInEditSend"));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("remote_noti");
        registerReceiver(onNotice, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("remote_noti");
        unregisterReceiver(onNotice);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);



        btn_general = (ImageButton)findViewById(R.id.btn_general);

        btn_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
                    new SignInTypeActivity.ActivityChangeSend().execute("signin");
                    //new LoginActivity2.ActivityChangeSend().execute("signin");
                }else{

                    Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivityForResult(mIntent,4);
                }


            }
        });

        btn_company = (ImageButton)findViewById(R.id.btn_company);

        btn_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent mIntent = new Intent(getApplicationContext(), CompanySignInActivity.class);
                    startActivityForResult(mIntent,4);
            }
        });

//
//        btn_STT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),STTActivity.class);
//                startActivity(i);
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case 0:
                if(resultCode == RESULT_OK) {
             //       nickname.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
               //     pw.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                 //   name.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if(resultCode == RESULT_OK) {
                    finish();
                }
            case 8:     //회원가입 완료 응답
                if(resultCode == RESULT_OK){
                    Log.d("회원가입 원격 완료", "가입시키기성공");

                    //로그인 안되어있으면 로그인 시켜야 함.
                    Intent returnIntent = new Intent();
                    // returnIntent.putExtra("result",result);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;

        }

    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            // Intent SendBroadCast로 보낸 action TAG 이름으로 필요한 방송을 찾는다.

        if(name.equals("activity_change")){  //화면 바뀔때
                Log.d("Login: BroadCast", "activity_change"); // putExtra를 이용한 String전달 }


                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");
                //String pw_edittext = intent.getStringExtra("pw_edittext");

                //mobno.setText(mobno_edittext);
                //pw.setText(pw_edittext);
                if(now_activity.equals("SignInType")){
                    if(activity_change.equals("signin")){
                        Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);

                        // Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        // startActivity(mIntent);

                    }else if(activity_change.equals("SignInType")){
                        Intent mIntent = new Intent(getApplicationContext(), SignInTypeActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                    }else if(activity_change.equals("back")){
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
//                                new Instrumentation().sendKeySync(event);
//                            }
//                        });
                        Log.d("login","back");
                        finish();
                    }

                }

            }else if(name.equals("remote_noti")){
                Toast.makeText(getApplicationContext(),"The connection is aborted.",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove("HELP_FLAG");
                edit.remove("Helper_authorization");
                edit.remove("needer_id");
                edit.commit();

            NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(1);


        }
        }
    };

    private class ActivityChangeSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());

            //params.add(new BasicNameValuePair("type", "helper_request"));
            if(args[0].equals("login")){
                //이건 로그인 성공하고 수행해야하는 것?
            }else if(args[0].equals("signin")){
                Log.d("SignInType","classSignin");

                params.add(new BasicNameValuePair("now_activity", "LoginActivity2"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "signin")); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

            }else if(args[0].equals("SignInType")){
                Log.d("SignInType","SignInType");

                params.add(new BasicNameValuePair("now_activity", "LoginActivity2"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "SignInType")); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

            }else if(args[0].equals("back")){

                Log.d("SignInType","classBack");
                params.add(new BasicNameValuePair("now_activity", "SignInType"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "back")); //어떤 화면으로 전환할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰


                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

            }

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/ActivityChangeSend",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                String activity = json.getString("activity_change");

                if(res.equals("Success")) {

                    if(activity.equals("signin")){
                        Log.d("login","start_signin");

                        Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        //
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(mIntent);
                    }
                    else if(activity.equals("cancel") || activity.equals("back")){
                        finish();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
