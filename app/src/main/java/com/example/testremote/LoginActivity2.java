package com.example.testremote;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class LoginActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    Button btn_login;
    Button btn_signin;

    private GoogleMap mMap;
    String tmp;
    String tpw;
    JSONArray retJson;

    EditText eid;
    EditText epw;

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

    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;


    @Override
    public void onBackPressed() {

        Log.d("LoginActivity2", "Backkkkkk");

        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new LoginActivity2.ActivityChangeSend().execute("back");
        }else{
            super.onBackPressed();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case 8:     //회원가입 완료 응답
                if(resultCode == RESULT_OK){
                    finish();
                    startActivity(new Intent(getApplicationContext(),StartActivity.class));
                }
                break;
            default:
                Log.d("Signin","default");
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        loginContext = this;

        prefs = getApplicationContext().getSharedPreferences("Chat", 0);

        pw = (EditText)findViewById(R.id.epw);
        mobno = (EditText)findViewById(R.id.eid);
        login = (Button)findViewById(R.id.login);
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
                            new LoginActivity2.AuthorizationResult().execute();

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

                    new LoginActivity2.ActivityChangeSend().execute("signin");
                }else{
                    startActivity(new Intent(getApplicationContext(),SignInTypeActivity.class));
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress.show();
                Log.d("loginstart", "loginininini");
//                SharedPreferences.Editor edit = prefs.edit();
//                edit.putString("REG_FROM", mobno.getText().toString());
//                edit.putString("FROM_NAME", pw.getText().toString());
//                edit.commit();
//
//                String smobno = mobno.getText().toString();
//                String spw = pw.getText().toString();
//
//                new LoginActivity2.Login().execute(spw,smobno);

                try {

                    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {   //로그인 작업
                        @Override
                        public void processFinish(JSONArray ret) throws JSONException {

                            Log.e("err","processFinish");
                            Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
                            retJson = ret;

                            //    while(retJson.get(0).equals(null)); //jsonArray가 NULL이면 대기

//                            if(retJson == null)
//                            {
//                                Toast.makeText(getApplicationContext(),"no Information",Toast.LENGTH_SHORT).show();
//                                return;
//                            }

                            for(int i = 0 ; i< retJson.length(); i++){

                                JSONObject json = retJson.getJSONObject(i);

                                String id = json.getString("ID");
                                String pw = json.getString("PW");
                                tpw = pw;
                                //  String quantity = json.getString("Name");



                                Toast.makeText(getApplicationContext(),"되나:"+id+pw,Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(getApplicationContext(),"tpw:"+tpw+"epw:"+epw.getText().toString(),Toast.LENGTH_SHORT).show();

                            if(tpw.equals(epw.getText().toString())) {
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.putString("REG_FROM", mobno.getText().toString());
                                edit.putString("FROM_NAME", pw.getText().toString());
                                edit.commit();

                                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getApplicationContext(),"비밀번호 오류",Toast.LENGTH_SHORT).show();

                        }
                    });

                    String url = "http://13.124.85.122:52273/search";
                    JSONObject loginInfo = new JSONObject();
                    loginInfo.put("ID",eid.getText().toString());

                    RequestForm req = new RequestForm(url,loginInfo);

                    tmp = dwTask.execute(req).get();
                    Log.i("tmp",tmp);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(),"되나1:",Toast.LENGTH_SHORT).show();
                Log.e("err","retJson NULL?");

                // while(!tpw.equals(""));


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

                new LoginActivity2.Help().execute(h_smobno,random_number);
            }
        });



        eid = (EditText)findViewById(R.id.eid);
        epw = (EditText)findViewById(R.id.epw);



        tpw = "";


        final Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

//        final CustomMapFragment map = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        map.getMapAsync(this);  //구글 맵

        //메인메뉴 가는 코드
//        Toast.makeText(getApplicationContext(), "dd", Toast.LENGTH_LONG);
//        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);



        //startActivity(intent);

//        btn_login = (Button) findViewById(R.id.login);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                //hi branch되나볼까나 흠 합쳐지나
//
//
//                try {
//
//                    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {   //로그인 작업
//                        @Override
//                        public void processFinish(JSONArray ret) throws JSONException {
//
//                            Log.e("err","processFinish");
//                            Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
//                            retJson = ret;
//
//                        //    while(retJson.get(0).equals(null)); //jsonArray가 NULL이면 대기
//
////                            if(retJson == null)
////                            {
////                                Toast.makeText(getApplicationContext(),"no Information",Toast.LENGTH_SHORT).show();
////                                return;
////                            }
//
//                            for(int i = 0 ; i< retJson.length(); i++){
//
//                                JSONObject json = retJson.getJSONObject(i);
//
//                                String id = json.getString("ID");
//                                String pw = json.getString("PW");
//                                tpw = pw;
//                                //  String quantity = json.getString("Name");
//
//
//
//                                Toast.makeText(getApplicationContext(),"되나:"+id+pw,Toast.LENGTH_SHORT).show();
//                            }
//
//                            Toast.makeText(getApplicationContext(),"tpw:"+tpw+"epw:"+epw.getText().toString(),Toast.LENGTH_SHORT).show();
//
//                            if(tpw.equals(epw.getText().toString()))
//                                startActivity(i);
//                            else
//                                Toast.makeText(getApplicationContext(),"비밀번호 오류",Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//                    String url = "http://13.124.85.122:52273/search";
//                    JSONObject loginInfo = new JSONObject();
//                    loginInfo.put("ID",eid.getText().toString());
//
//                    RequestForm req = new RequestForm(url,loginInfo);
//
//                    tmp = dwTask.execute(req).get();
//                    Log.i("tmp",tmp);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(getApplicationContext(),"되나1:",Toast.LENGTH_SHORT).show();
//                Log.e("err","retJson NULL?");
//
//               // while(!tpw.equals(""));
//
//
//
//            }
//        });
//
//        btn_signin = (Button)findViewById(R.id.signin);
//        btn_signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//
//                startActivity(intent);
//            }
//        });

        getPermissionToReadUserContacts();

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
                Log.d("LoginActivity2","classSignin");

                params.add(new BasicNameValuePair("now_activity", "LoginActivity2"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "signin")); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

            }else if(args[0].equals("back")){

                Log.d("LoginActivity2","classBack");
                params.add(new BasicNameValuePair("now_activity", "LoginActivity2"));    //현재 액티비티
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

    public void NeederScreenInfoSend(){

        Log.d("NeederScreenInfoSend","LoginActivity");
        //연결이 되어있다면 HELP_FLAG가 1이라면 어르신이 자신의 화면을 helper에게 보낸다.
        String spw = pw.getText().toString();
        String smobno = mobno.getText().toString();
        String shelpermobno = helper_mobno.getText().toString();

        new LoginActivity2.NeederScreenInfoSend().execute(smobno,spw,shelpermobno);

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
            }else if(name.equals("activity_change")){  //화면 바뀔때
                Log.d("Login: BroadCast", "activity_change"); // putExtra를 이용한 String전달 }


                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");
                //String pw_edittext = intent.getStringExtra("pw_edittext");

                //mobno.setText(mobno_edittext);
                //pw.setText(pw_edittext);
                if(now_activity.equals("LoginActivity2")){
                    if(activity_change.equals("signin")){
                        Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);

                       // Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                       // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                       // startActivity(mIntent);

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

            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();

                   setUserNumber();

            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();

                Toast.makeText(this, "Read Contacts permission is required.", Toast.LENGTH_SHORT).show();

                closeNow();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


}

    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {



            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_CONTACTS)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_PERMISSIONS_REQUEST);
            }
        }
        else
        {
            setUserNumber();
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

       // setUserNumber();

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
            eid.setText(phoneNumber);
            eid.setEnabled(true);
        }




    }

}
