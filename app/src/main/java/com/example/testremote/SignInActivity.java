package com.example.testremote;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    Button btn_STT;
    Button btn_next, btn_cancel;

    EditText nickname;
    EditText name;
    EditText pw;
    EditText id;
    SharedPreferences prefs;
    List<NameValuePair> params;
    boolean flag = true;
    boolean flag2 = true;

    @Override
    public void onBackPressed() {
        Log.d("SignInActivity", "Backkkkkk");

        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new SignInActivity.ActivityChangeSend().execute("back");
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Log.d("Signin","start");
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);

        nickname = (EditText)findViewById(R.id.signin_nickname);
        name = (EditText)findViewById(R.id.signin_name);
        pw = (EditText)findViewById(R.id.signin_pw);
        id = (EditText)findViewById(R.id.signin_id);

        btn_next = (Button)findViewById(R.id.btn_next);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);


        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("activity_change"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("SignInEditSend"));


        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", s.toString());
                if((prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")) && flag) {
                    flag = false;
                    flag2 = false;
                    new SignInActivity.SignInEditSend().execute(s.toString(), pw.getText().toString(), name.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", s.toString());
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforename", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onname", s.toString());
                if((prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2"))&& flag) {
                    new SignInActivity.SignInEditSend().execute(nickname.getText().toString(), pw.getText().toString(), s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterename", s.toString());
            }
        });

        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforepw", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onpw", s.toString());
                if((prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")) && flag) {
                    new SignInActivity.SignInEditSend().execute(nickname.getText().toString(), s.toString(), name.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterpw", s.toString());
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
                    new SignInActivity.ActivityChangeSend().execute("InterestActivity");
                }else {
                    btn_nextClick();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
                    new SignInActivity.ActivityChangeSend().execute("cancel");
                }else{
                    finish();
                }


            }
        });

        setUserNumber();
//        btn_STT = (Button)findViewById(R.id);
//
//        btn_STT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),STTActivity.class);
//                startActivity(i);
//            }
//        });
    }

    public void btn_nextClick(){

        Log.d("Signin","btn_nextClick");
        JSONObject tmp = new JSONObject();
        try {

//                    SharedPreferences.Editor edit = prefs.edit();
//                    edit.putString("REG_FROM", id.getText().toString()); //ID(mobno)
//                    edit.putString("FROM_NAME", pw.getText().toString());//PW(name)
//                    edit.commit();

            String smobno = id.getText().toString();
            String spw = pw.getText().toString();

            // new LoginActivity2.Login().execute(spw,smobno);

            tmp.put("Nickname", nickname.getText());
            tmp.put("Name", name.getText());
            tmp.put("PW", pw.getText());
            tmp.put("ID", id.getText());
            //tmp.put("reg_id",prefs.getString("REG_ID",""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent mIntent = new Intent(getApplicationContext(), InterestActivity.class);
        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

        mIntent.putExtra("userinfo", tmp.toString());
        //8번 받으면 로그인 완료. 종료시키면 된다.
        startActivityForResult(mIntent, 8);
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String label = intent.getAction();
            // Intent SendBroadCast로 보낸 action TAG 이름으로 필요한 방송을 찾는다.

            if(label.equals("SignInEditSend")){  //어르신이 보낸 정보로 화면 셋팅하기. 로그인화면
                Log.d("Signin: BroadCast", "SignInEditSend "); // putExtra를 이용한 String전달 }

                String snickname = intent.getStringExtra("nickname");
                String spw = intent.getStringExtra("pw");
                String sname = intent.getStringExtra("name");


                if(snickname.equals(nickname.getText().toString()) && spw.equals(pw.getText().toString()) && sname.equals(name.getText().toString())){
                    //flag = false;
                }else{
                    flag = false;
                    nickname.setText(snickname);
                    pw.setText(spw);
                    name.setText(sname);
                    //flag2 = false;
                }


            }else if(label.equals("activity_change")){  //화면 바뀔때
                Log.d("Signin: BroadCast", "activity_change"); // putExtra를 이용한 String전달 }


                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");
                //String pw_edittext = intent.getStringExtra("pw_edittext");

                //mobno.setText(mobno_edittext);
                //pw.setText(pw_edittext);

                if(now_activity.equals("SignInActivity")){
                    if(activity_change.equals("InterestActivity")){
                        Log.d("signin","interestActivity");
                        btn_nextClick();

                    }else if(activity_change.equals("back") || activity_change.equals("cancel")){
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
//                                new Instrumentation().sendKeySync(event);
//                            }
//                        });
                        Log.d("signin","back");
                        finish();
                    }

                }


            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case 0:
                if(resultCode == RESULT_OK) {
                    nickname.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
                    pw.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    name.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
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
            default:
                Log.d("Signin","default");
                finish();
                break;

        }
    }

    public void callSTT(View view) {

        Intent i = new Intent(getApplicationContext(),STTActivity.class);

        switch (view.getId()) {
            case R.id.btn_nickname:

                startActivityForResult(i, 0);
                break;
            case R.id.btn_pw:

                startActivityForResult(i, 1);
                break;
            case R.id.btn_name:

                startActivityForResult(i, 2);
                break;

        }

    }


    void setUserNumber(){  // 번호 읽어서 국제번호로 변환 후 id란에 Setting


        TelephonyManager t = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = "";

        String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"}; //번호 가져오기
        Object object = getContentResolver().query(Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, "data"),
                main_data, "mimetype=?",
                new String[]{"vnd.android.cursor.item/phone_v2"},
                "is_primary DESC");
        if (object != null) {
            do {
                if (!((Cursor) (object)).moveToNext())
                    break;
                phoneNumber = ((Cursor) (object)).getString(4);
                Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_SHORT).show();

            } while (true);
            ((Cursor) (object)).close();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
            phoneNumber= PhoneNumberUtils.formatNumberToE164(phoneNumber,t.getNetworkCountryIso().toUpperCase());
            Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_SHORT).show();
            id.setText(phoneNumber);
            id.setEnabled(false);
        }
    }

    private class ActivityChangeSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());

            //params.add(new BasicNameValuePair("type", "helper_request"));
           if(args[0].equals("InterestActivity")){
                params.add(new BasicNameValuePair("now_activity", "SignInActivity"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "InterestActivity")); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

            }else if(args[0].equals("back") || args[0].equals("cancel")){

                params.add(new BasicNameValuePair("now_activity", "SignInActivity"));    //현재 액티비티
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
                    if(activity.equals("InterestActivity")){
                        btn_nextClick();
                    }else if(activity.equals("cancel") || activity.equals("back")){
                        finish();
                    }
                    //startActivity(new Intent(getApplicationContext(),SignInActivity.class));

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class SignInEditSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());
            //params.add(new BasicNameValuePair("type", "helper_request"));

            params.add(new BasicNameValuePair("now_activity", "SignInActivity"));    //현재 액티비티
            params.add(new BasicNameValuePair("nickname",args[0])); //어떤 화면으로 전환할 버튼을 눌렀는지
            params.add(new BasicNameValuePair("pw",args[1]));
            params.add(new BasicNameValuePair("name",args[2]));
            params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));


            if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
            }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
            }



            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/SignInEditSend",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {
                    Log.d("Signin", "flag");
                    flag = true;
                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
