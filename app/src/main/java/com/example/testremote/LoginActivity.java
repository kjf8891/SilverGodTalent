package com.example.testremote;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LoginActivity extends Activity {
    SharedPreferences prefs;
    EditText pw, mobno, helper_mobno;
    Button login,help_btn, signin;
    List<NameValuePair> params;
    ProgressDialog progress;


    Button authorization_btn;
    EditText authorization_edit;
    Switch authorization_switch;

    Bundle bundle;

    public static Context loginContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginContext = this;

        prefs = getApplicationContext().getSharedPreferences("Chat", 0);

        pw = (EditText)findViewById(R.id.pw);
        mobno = (EditText)findViewById(R.id.mobno);
        login = (Button)findViewById(R.id.log_btn);
        signin = (Button)findViewById(R.id.signin);

        progress = new ProgressDialog(getApplicationContext());
        progress.setMessage("Registering ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        help_btn = (Button)findViewById(R.id.help_btn);
        helper_mobno = (EditText)findViewById(R.id.help_mobno);
        authorization_btn = (Button)findViewById(R.id.authorization_ok_btn);
        authorization_edit = (EditText)findViewById(R.id.authorization_number);
        authorization_switch = (Switch)findViewById(R.id.helping_switch);


        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("screeninfo"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("activity_change"));



        authorization_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    help_btn.setVisibility(View.GONE);
                    helper_mobno.setVisibility(View.GONE);
                    authorization_btn.setVisibility(View.VISIBLE);
                    authorization_edit.setVisibility(View.VISIBLE);
                }else{
                    help_btn.setVisibility(View.VISIBLE);
                    helper_mobno.setVisibility(View.VISIBLE);
                    authorization_btn.setVisibility(View.GONE);
                    authorization_edit.setVisibility(View.GONE);
                }
            }
        });

        authorization_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = getIntent().getBundleExtra("random_number");   //인증번호 번들
                if(bundle!= null){
                    if(bundle.getString("random_number") != null){
                        if(authorization_edit.getText().toString().equals(bundle.getString("random_number"))){
                            Toast.makeText(getApplicationContext(),"인증에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putString("Helper_authorization","OK");
                            edit.putString("needer_id",bundle.getString("needer_id"));
                            edit.commit();

                            authorization_edit.setText("인증성공");
                            edit.putString("HELP_FLAG", "2");

                            edit.commit();

                            Log.d("인증 성공",prefs.getString("needer_id",""));
//                            edit.putString("REG_FROM", mobno.getText().toString()); //비밀번호
//                            edit.putString("FROM_NAME", name.getText().toString()); //핸드폰번호
//                            edit.commit();

                            //인증이 성공하면 needer에게 원격조정이 시작된다고 알려준다.
                            //그리고 needer가 자신의 화면정보를 전달한다.
                            new AuthorizationResult().execute();

                        }
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("signinBtn", "signinBtn");
                Log.d("fdfdfdfdfdf",prefs.getString("HELP_FLAG",""));
                if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
                    new ActivityChangeSend().execute("signin");
                }



            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress.show();
                Log.d("loginstart", "loginininini");
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("REG_FROM", mobno.getText().toString());
                edit.putString("FROM_NAME", pw.getText().toString());
                edit.commit();

                String smobno = mobno.getText().toString();
                String spw = pw.getText().toString();

                new Login().execute(spw,smobno);

            }
        });

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress.show();
//                SharedPreferences.Editor edit = prefs.edit();
//                edit.putString("HELP_FLAG", "1");
//                edit.commit();


                String h_smobno = helper_mobno.getText().toString();


                Log.d("fdfdfdfdfdf",prefs.getString("HELP_FLAG",""));

                Random random = new Random();
                int x[] = new int[4];
                String random_number = "";
                for(int i =0; i<4; i++) {
                    x[i] = random.nextInt(10);
                    random_number += x[i];
                }

                Log.d("nnnnnn",random_number);

                new Help().execute(h_smobno,random_number);
            }
        });

    }

    private class AuthorizationResult extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();


            //params.add(new BasicNameValuePair("type", "helper_request"));
            params.add(new BasicNameValuePair("Helper_authorization", prefs.getString("Helper_authorization","")));
            params.add((new BasicNameValuePair("needer_id",prefs.getString("needer_id",""))));
            params.add((new BasicNameValuePair("helper_id",prefs.getString("REG_ID",""))));

            Log.d("AuthorizationResult", prefs.getString("REG_ID",""));
            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/authorizationResult",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class Help extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();
            Log.d("hphp",args[0]);

            //params.add(new BasicNameValuePair("type", "helper_request"));
            params.add(new BasicNameValuePair("helper_mobno", args[0]));
            params.add(new BasicNameValuePair("random_number", args[1]));
            params.add((new BasicNameValuePair("needer_id",prefs.getString("REG_ID",""))));

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("helper_id", args[0]);
            edit.commit();

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/help",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class Login extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            Log.d("loglogloglog",args[0]);

            params.add(new BasicNameValuePair("name", args[0]));
            params.add(new BasicNameValuePair("mobno", args[1]));
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/login",params);
            return jObj;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
           // progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Sucessfully Registered")) {
//                    Fragment reg = new UserFragment();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, reg);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.addToBackStack(null);
//                    ft.commit();
                    startActivity(new Intent(getApplicationContext(),UserActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class NeederScreenInfoSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());

            //params.add(new BasicNameValuePair("type", "helper_request"));
            params.add(new BasicNameValuePair("screen_info", GetClassName(getApplicationContext())));   //어르신 화면정보
            params.add((new BasicNameValuePair("mobno_edittext", args[0])));    //화면에 있는 mobno
            params.add((new BasicNameValuePair("pw_edittext", args[1])));   //화면에 있는 pw
            params.add((new BasicNameValuePair("helper_mobno",args[2])));   //화면에 있는 helper번호
            params.add((new BasicNameValuePair("needer_id", prefs.getString("REG_ID",""))));    //어르신토큰


            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/NeederScreenInfoSend",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public String GetClassName(Context context){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        return runningTaskInfos.get(0).topActivity.getClassName();
    }

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
                params.add(new BasicNameValuePair("activity_change", "signin")); //어떤 화면으로 전환할 버튼을 눌렀는지
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
                if(res.equals("Success")) {
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void NeederScreenInfoSend(){

        Log.d("NeederScreenInfoSend","LoginActivity");
        //연결이 되어있다면 HELP_FLAG가 1이라면 어르신이 자신의 화면을 helper에게 보낸다.
        String spw = pw.getText().toString();
        String smobno = mobno.getText().toString();
        String shelpermobno = helper_mobno.getText().toString();

        new NeederScreenInfoSend().execute(smobno,spw,shelpermobno);

    }



    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            // Intent SendBroadCast로 보낸 action TAG 이름으로 필요한 방송을 찾는다.

            if(name.equals("screeninfo")){  //어르신이 보낸 정보로 화면 셋팅하기. 로그인화면
                Log.d("Login: BroadCast", "BroadcastReceiver :: com.dwfox.myapplication.SEND_BROAD_CAST :: "); // putExtra를 이용한 String전달 }

                String screeninfo = intent.getStringExtra("screeninfo");
                String mobno_edittext = intent.getStringExtra("mobno_edittext");
                String pw_edittext = intent.getStringExtra("pw_edittext");

                mobno.setText(mobno_edittext);
                pw.setText(pw_edittext);
            }else if(name.equals("activity_change")){  //어르신이 보낸 정보로 화면 셋팅하기. 로그인화면
                Log.d("Login: BroadCast", "activity_change"); // putExtra를 이용한 String전달 }

                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                //String pw_edittext = intent.getStringExtra("pw_edittext");

                //mobno.setText(mobno_edittext);
                //pw.setText(pw_edittext);
                if(activity_change.equals("signin")){
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                }

            }
        }
    };
}
