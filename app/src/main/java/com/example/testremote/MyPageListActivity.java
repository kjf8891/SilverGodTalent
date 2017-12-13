package com.example.testremote;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.testremote.BuildConfig.DEBUG;
import static com.example.testremote.JSONParser.jObj;

public class MyPageListActivity extends AppCompatActivity {


    private int mBindFlag;
    private Messenger mServiceMessenger;
    private MSGReceiver msgReceiver;


    ListView listview_club, listview_mentoring, listview_lec, listview_wait ;
    ListViewMyClassAdapter adapter_club, adapter_mentoring, adapter_lec, adapter_wait;
    TextView club_txt, mentoring_txt, lec_txt, wait_txt;

    ToggleButton recogStart;
    boolean isRecogChecked = false;
    boolean isRegistered = false;
    SharedPreferences settings;

    SharedPreferences prefs;
    List<NameValuePair> params;
    ArrayList<HashMap<String, String>> my_activity_club = new ArrayList<HashMap<String, String>>();  //club, mentoring, 강의 전부
    ArrayList<HashMap<String, String>> my_activity_mentoring = new ArrayList<HashMap<String, String>>();  //club, mentoring, 강의 전부
    ArrayList<HashMap<String, String>> my_activity_lec = new ArrayList<HashMap<String, String>>();  //club, mentoring, 강의 전부
    ArrayList<HashMap<String, String>> my_activity_wait = new ArrayList<HashMap<String, String>>();  //club, mentoring, 강의 전부

    int select_position = 0;
    Bundle bundle;



    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            if (DEBUG) {Log.e("TAG", "onServiceConnected");} //$NON-NLS-1$

            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
            msg.what = MyService.MSG_RECOGNIZER_START_LISTENING;

            try
            {
                mServiceMessenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            if (DEBUG) {Log.e("TAG", "onServiceDisconnected");} //$NON-NLS-1$
            mServiceMessenger = null;
        }

    }; // mServiceConnection

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("isRecogChecked",isRecogChecked);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        settings = getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isRecogChecked", isRecogChecked);
        editor.putBoolean("isRegistered",isRegistered);
        editor.commit();


        unbindService(mServiceConnection);

        if(msgReceiver != null)
        unregisterReceiver(msgReceiver);



    }

    @Override
    protected void onStart() {
        super.onStart();

//        bindService(new Intent(this, MyService.class), mServiceConnection, mBindFlag);
        //startService(new Intent(this,MyService.class));

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_list);

        if(savedInstanceState != null){

            isRecogChecked = savedInstanceState.getBoolean("isRecogChecked");

        }




        init();
    }
    public void init(){
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);
        settings = getApplicationContext().getSharedPreferences("settings",0);
        isRecogChecked = settings.getBoolean("isRecogChecked",false);
        isRegistered = settings.getBoolean("isRegistered",false);

        if(isRegistered) {
            msgReceiver = new MSGReceiver();
            Intent service = new Intent(getApplicationContext(), MyService.class);

            IntentFilter intentFilter = new IntentFilter("com.example.testremote.MyService");
            registerReceiver(msgReceiver,intentFilter);
            //startService(service);
            //mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;

            bindService(new Intent(getApplicationContext(), MyService.class), mServiceConnection, mBindFlag);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("activity_change"));

        recogStart = (ToggleButton)findViewById(R.id.recog_start);
        recogStart.setChecked(isRecogChecked);

        //Toast.makeText(getApplicationContext(),String.valueOf(settings.getBoolean("isRecogChecked",false)),Toast.LENGTH_SHORT).show();

        recogStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false){
                    if(mServiceConnection!= null && isRegistered) {

                        isRecogChecked = false;
                        isRegistered = false;




                            Intent service = new Intent(getApplicationContext(), MyService.class);
                            stopService(service);

                    }
                }
                else{
                    isRecogChecked = true;
                    isRegistered = true;

                    msgReceiver = new MSGReceiver();
                    Intent service = new Intent(getApplicationContext(), MyService.class);

                    IntentFilter intentFilter = new IntentFilter("com.example.testremote.MyService");
                    registerReceiver(msgReceiver,intentFilter);
                    startService(service);
                    mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;

                    bindService(new Intent(getApplicationContext(), MyService.class), mServiceConnection, mBindFlag);
                }
            }
        });

        club_txt = (TextView) findViewById(R.id.mypagelist_club_txt);
        mentoring_txt = (TextView) findViewById(R.id.mypagelist_mentoring_txt);
        lec_txt = (TextView) findViewById(R.id.mypagelist_lec_txt);
        wait_txt = (TextView) findViewById(R.id.mypagelist_wait_txt);

        // Adapter 생성
        adapter_club = new ListViewMyClassAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        listview_club = (ListView) findViewById(R.id.mypagelistclass_club);
        listview_club.setAdapter(adapter_club);

        // Adapter 생성
        adapter_mentoring = new ListViewMyClassAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        listview_mentoring = (ListView) findViewById(R.id.mypagelistclass_mentoring);
        listview_mentoring.setAdapter(adapter_mentoring);

        // Adapter 생성
        adapter_lec = new ListViewMyClassAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        listview_lec = (ListView) findViewById(R.id.mypagelistclass_lec);
        listview_lec.setAdapter(adapter_lec);

        // Adapter 생성
        adapter_wait = new ListViewMyClassAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        listview_wait = (ListView) findViewById(R.id.mypagelistclass_wait);
        listview_wait.setAdapter(adapter_wait);

//        bundle = getIntent().getBundleExtra("remote_mypagelist");
//        if(bundle != null){     //원격으로 젊은이가 어르신한테 needer_id를 넘겨받는것.
//
//        }
//        setTitle(bundle.getString("CTitle"));

        showClassList();

        //리스트뷰 이벤트 처리. 눌르면 해당 정보를 데이터베이스에서 로드하기.
        listview_club.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewMyClass item = (ListViewMyClass) parent.getItemAtPosition(position) ;
                select_position = position;

                String titleStr = item.getTitle() ;
                String descStr = item.getDesc() ;
                Drawable iconDrawable = item.getIcon();


                if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
                    new MyPageListActivity.ActivityChangeSend().execute("MyPageActivity",String.valueOf(position), "Club");

                }else {
                    //마이페이지 로딩 액티비티 전환
                    Toast.makeText(getApplicationContext(), titleStr, Toast.LENGTH_LONG).show();

                    BundleSend("Club");
//                    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
//                    startActivity(intent);

                }
            }
        }) ;



        //
        //new InsertDataTask().execute("http://sanhyup.c4hbycfv6pky.ap-northeast-2.rds.amazonaws.com:5306/pushData");
        //
        // new InsertDataTask().execute("http://13.124.85.122:52273/pushData");

    }


    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            // Intent SendBroadCast로 보낸 action TAG 이름으로 필요한 방송을 찾는다.

            if(name.equals("activity_change")){  //화면 바뀔때
                Log.d("MyPageList:BroadCast", "activity_change"); // putExtra를 이용한 String전달 }

                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");

                if(activity_change.equals("back")) {
                    Log.d("MyPageList", "back");
                    finish();
                }
            }else if(name.equals("mypagelistclick")){
                Log.d("MyPageList:BroadCast", "mypagelistclick");

                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");

                //String pw_edittext = intent.getStringExtra("pw_edittext");
                //mobno.setText(mobno_edittext);
                //pw.setText(pw_edittext);

                if(activity_change.equals("MyPageActivity")){

                    //myPageActivity의 경우
                    //몇번째 리스트를 선택했는지 position 정보가 필요하다.
                    int position = Integer.parseInt(intent.getStringExtra("position"));
                    String mypage_type = intent.getStringExtra("mypage_type");

                    if(mypage_type.equals("Club")){
                        //bundle = getIntent().getBundleExtra("Club");

                        Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                        //번들로 넘겨줘야한다.
                        Bundle args = new Bundle();
                        args.putString("CNum", my_activity_club.get(position).get("CNum"));
                        args.putString("CTitle", my_activity_club.get(position).get("CTitle"));
                        args.putString("city", my_activity_club.get(position).get("city"));
                        args.putString("location", my_activity_club.get(position).get("location"));
                        args.putString("leader", my_activity_club.get(position).get("leader"));
                        args.putString("date", my_activity_club.get(position).get("date"));
                        args.putString("content", my_activity_club.get(position).get("content"));
                        args.putString("applicationNum", my_activity_club.get(position).get("applicationNum"));
                        args.putString("total_num", my_activity_club.get(position).get("total_num"));


                        mIntent.putExtra("Club",args);

                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        //
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(mIntent);
                    }else if(mypage_type.equals("Mentoring")){
                        Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                        //번들로 넘겨줘야한다.
                        Bundle args = new Bundle();
                        args.putString("MNum", my_activity_mentoring.get(position).get("MNum"));
                        args.putString("MTitle", my_activity_mentoring.get(position).get("MTitle"));
                        args.putString("city", my_activity_mentoring.get(position).get("city"));
                        args.putString("location", my_activity_mentoring.get(position).get("location"));
                        args.putString("leader", my_activity_mentoring.get(position).get("leader"));
                        args.putString("date", my_activity_mentoring.get(position).get("date"));
                        args.putString("latitude", my_activity_mentoring.get(position).get("latitude"));
                        args.putString("longitude", my_activity_mentoring.get(position).get("longitude"));
                        args.putString("total_num", my_activity_mentoring.get(position).get("total_num"));
                        args.putString("applicationNum", my_activity_mentoring.get(position).get("applicationNum"));


                        //bundle = getIntent().getBundleExtra("Club");
                        mIntent.putExtra("Mentoring",args);

                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        //
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(mIntent);
                    }else if(mypage_type.equals("Recruit")){
                        Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                        //번들로 넘겨줘야한다.
                        Bundle args = new Bundle();
                        args.putString("RNum", my_activity_lec.get(position).get("RNum"));
                        args.putString("RTitle", my_activity_lec.get(position).get("RTitle"));
                        args.putString("applicationNum", my_activity_lec.get(position).get("applicationNum"));
                        args.putString("total_num", my_activity_lec.get(position).get("total_num"));
                        args.putString("institution", my_activity_lec.get(position).get("institution"));
                        args.putString("interest", my_activity_lec.get(position).get("interest"));
                        args.putString("city", my_activity_lec.get(position).get("city"));
                        args.putString("content", my_activity_lec.get(position).get("content"));


                        mIntent.putExtra("Recruit",args);

                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        //
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(mIntent);
                    }



                    Intent mIntent = new Intent(getApplicationContext(), MyPageListActivity.class);
                    startActivityForResult(mIntent, 9);

                    // Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                    // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                    // startActivity(mIntent);
                }
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
            }else if(args[0].equals("MyPageActivity") ){
                Log.d("MyPageListAc",args[0]);

                params.add(new BasicNameValuePair("now_activity", "MyPageListActivity"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", args[0])); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

                //어떤 항목을 선택했는지. 클럽인지 멘토링인지 강의인지. 그 중에서 어떤 클럽인지
               // params.add((new BasicNameValuePair("select_type", )));

                int position = Integer.valueOf(args[1]);
                if(args[2].equals("Club")){
                    //CNum만 넘겨서 받은사람이 브로드캐스트 리시버에서 받은게 Club에 CNum이면 쿼리를 수행해서 자기꺼 액티비티 넘길때 번들로 설정해서
                    //보내도록 하자.
                    params.add((new BasicNameValuePair("mypage_type","Club")));
                    params.add((new BasicNameValuePair("position", my_activity_club.get(position).get("CNum"))));

                }else if(args[2].equals("Mentoring")){
                    params.add((new BasicNameValuePair("mypage_type","Mentoring")));
                    params.add((new BasicNameValuePair("position", my_activity_mentoring.get(position).get("MNum"))));

                }else if(args[2].equals("Recruit")){
                    params.add((new BasicNameValuePair("mypage_type","Recruit")));
                    params.add((new BasicNameValuePair("position", my_activity_lec.get(position).get("RNum"))));
                }


                JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/MyPageListActivityChange",params);
            }else if(args[0].equals("back")){

                Log.d("MainMenuActivity","classBack");
                params.add(new BasicNameValuePair("now_activity", "MyPageListActivity"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "back")); //어떤 화면으로 전환할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰


                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }
                JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/ActivityChangeSend",params);
            }


            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                //String activity = json.getString("activity_change");
                //여기에 Club인지 Mentoring인지 Recruit인지 받자. 서버에서 mypage_type으로넘기자
                //String mypage_type = json.getString("mypage_type");

                if(res.equals("Success")) {
                    String activity = json.getString("activity_change");
                    if(activity.equals("MyPageActivity")){

                        Log.d("MainMenuPost",activity);
                        String mypage_type = json.getString("mypage_type");
                        if(mypage_type.equals("Club")){
                            //bundle = getIntent().getBundleExtra("Club");

                            Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                            //번들로 넘겨줘야한다.
                            Bundle args = new Bundle();
                            args.putString("CNum", my_activity_club.get(select_position).get("CNum"));
                            args.putString("CTitle", my_activity_club.get(select_position).get("CTitle"));
                            args.putString("city", my_activity_club.get(select_position).get("city"));
                            args.putString("location", my_activity_club.get(select_position).get("location"));
                            args.putString("leader", my_activity_club.get(select_position).get("leader"));
                            args.putString("date", my_activity_club.get(select_position).get("date"));
                            args.putString("content", my_activity_club.get(select_position).get("content"));
                            args.putString("total_num", my_activity_club.get(select_position).get("total_num"));
                            args.putString("applicationNum", my_activity_club.get(select_position).get("applicationNum"));


                            mIntent.putExtra("Club",args);

                            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //8번 받으면 로그인 완료. 종료시키면 된다.
                            startActivityForResult(mIntent, 8);
                            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                            //
                            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //startActivity(mIntent);
                        }else if(mypage_type.equals("Mentoring")){
                            Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                            //번들로 넘겨줘야한다.
                            Bundle args = new Bundle();
                            args.putString("MNum", my_activity_mentoring.get(select_position).get("MNum"));
                            args.putString("MTitle", my_activity_mentoring.get(select_position).get("MTitle"));
                            args.putString("city", my_activity_mentoring.get(select_position).get("city"));
                            args.putString("location", my_activity_mentoring.get(select_position).get("location"));
                            args.putString("leader", my_activity_mentoring.get(select_position).get("leader"));
                            args.putString("date", my_activity_mentoring.get(select_position).get("date"));
                            args.putString("latitude", my_activity_mentoring.get(select_position).get("latitude"));
                            args.putString("longitude", my_activity_mentoring.get(select_position).get("longitude"));
                            args.putString("total_num", my_activity_mentoring.get(select_position).get("total_num"));
                            args.putString("applicationNum", my_activity_mentoring.get(select_position).get("applicationNum"));


                            //bundle = getIntent().getBundleExtra("Club");
                            mIntent.putExtra("Mentoring",args);

                            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //8번 받으면 로그인 완료. 종료시키면 된다.
                            startActivityForResult(mIntent, 8);
                            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                            //
                            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //startActivity(mIntent);
                        }else if(mypage_type.equals("Recruit")){
                            Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                            //번들로 넘겨줘야한다.
                            Bundle args = new Bundle();
                            args.putString("RNum", my_activity_lec.get(select_position).get("RNum"));
                            args.putString("RTitle", my_activity_lec.get(select_position).get("RTitle"));
                            args.putString("applicationNum", my_activity_lec.get(select_position).get("applicationNum"));
                            args.putString("total_num", my_activity_lec.get(select_position).get("total_num"));
                            args.putString("institution", my_activity_lec.get(select_position).get("institution"));
                            args.putString("interest", my_activity_lec.get(select_position).get("interest"));
                            args.putString("city", my_activity_lec.get(select_position).get("city"));
                            args.putString("content", my_activity_lec.get(select_position).get("content"));


                            mIntent.putExtra("Recruit",args);

                            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //8번 받으면 로그인 완료. 종료시키면 된다.
                            startActivityForResult(mIntent, 8);
                            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                            //
                            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //startActivity(mIntent);
                        }


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

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));
            params.add(new BasicNameValuePair("type", args[0]));
            JSONArray jAry = json.getJSONArray("http://13.124.85.122:8080/getActivity",params);

            return jAry;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            Log.d("MyPageLoad","Post");

            for(int i = 0; i < json.length(); i++){
                JSONObject c = null;
                Log.d("MyPageLoad",String.valueOf(json.length()));
                try {
                    c = json.getJSONObject(i);
                    String CNum = c.getString("CNum");
                    String CTitle = c.getString("CTitle");
                    String city = c.getString("city");
                    String location = c.getString("location");
                    String leader = c.getString("leader");
                    String date = c.getString("date");
                    String content = c.getString("content");

                    adapter_club.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.presentation),
                            CTitle,content) ;

                    Log.d("Chat-list", CNum + CTitle);

                    HashMap<String, String> mclub = new HashMap<String, String>();
                    mclub.put("CNum", CNum);
                    mclub.put("CTitle", CTitle);
                    mclub.put("city", CTitle);
                    mclub.put("location", CTitle);
                    mclub.put("leader", CTitle);
                    mclub.put("date", CTitle);
                    mclub.put("content", CTitle);

                    my_activity_club.add(mclub);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            adapter_club.notifyDataSetChanged();
//            ListAdapter adapter = new SimpleAdapter(getApplicationContext(), users,
//                    R.layout.user_list_single,
//                    new String[] { "name","mobno" }, new int[] {
//                    R.id.name, R.id.mobno});
//            list.setAdapter(adapter);
//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    Bundle args = new Bundle();
//                    args.putString("mobno", users.get(position).get("mobno"));
//                    Intent chat = new Intent(getApplicationContext(), ChatActivity.class);
//                    chat.putExtra("INFO", args);
//                    startActivity(chat);
//                }
//            });
        }
    }


    public void BundleSend(String mypage_type){
        if(mypage_type.equals("Club")){
            //bundle = getIntent().getBundleExtra("Club");

            Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
            //번들로 넘겨줘야한다.
            Bundle args = new Bundle();
            args.putString("CNum", my_activity_club.get(select_position).get("CNum"));
            args.putString("CTitle", my_activity_club.get(select_position).get("CTitle"));
            args.putString("city", my_activity_club.get(select_position).get("city"));
            args.putString("location", my_activity_club.get(select_position).get("location"));
            args.putString("leader", my_activity_club.get(select_position).get("leader"));
            args.putString("date", my_activity_club.get(select_position).get("date"));
            args.putString("content", my_activity_club.get(select_position).get("content"));
            args.putString("total_num", my_activity_club.get(select_position).get("total_num"));
            args.putString("applicationNum", my_activity_club.get(select_position).get("applicationNum"));

            mIntent.putExtra("Club",args);

            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            //8번 받으면 로그인 완료. 종료시키면 된다.
            startActivityForResult(mIntent, 8);
            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
            //
            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            //startActivity(mIntent);
        }else if(mypage_type.equals("Mentoring")){
            Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
            //번들로 넘겨줘야한다.
            Bundle args = new Bundle();
            args.putString("MNum", my_activity_mentoring.get(select_position).get("MNum"));
            args.putString("MTitle", my_activity_mentoring.get(select_position).get("MTitle"));
            args.putString("city", my_activity_mentoring.get(select_position).get("city"));
            args.putString("location", my_activity_mentoring.get(select_position).get("location"));
            args.putString("leader", my_activity_mentoring.get(select_position).get("leader"));
            args.putString("date", my_activity_mentoring.get(select_position).get("date"));
            args.putString("latitude", my_activity_mentoring.get(select_position).get("latitude"));
            args.putString("longitude", my_activity_mentoring.get(select_position).get("longitude"));
            args.putString("total_num", my_activity_mentoring.get(select_position).get("total_num"));
            args.putString("applicationNum", my_activity_mentoring.get(select_position).get("applicationNum"));


            //bundle = getIntent().getBundleExtra("Club");
            mIntent.putExtra("Mentoring",args);

            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            //8번 받으면 로그인 완료. 종료시키면 된다.
            startActivityForResult(mIntent, 8);
            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
            //
            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            //startActivity(mIntent);
        }else if(mypage_type.equals("Recruit")){
            Intent mIntent = new Intent(getApplicationContext(), MyPageActivity.class);
            //번들로 넘겨줘야한다.
            Bundle args = new Bundle();
            args.putString("RNum", my_activity_lec.get(select_position).get("RNum"));
            args.putString("RTitle", my_activity_lec.get(select_position).get("RTitle"));
            args.putString("applicationNum", my_activity_lec.get(select_position).get("applicationNum"));
            args.putString("total_num", my_activity_lec.get(select_position).get("total_num"));
            args.putString("institution", my_activity_lec.get(select_position).get("institution"));
            args.putString("interest", my_activity_lec.get(select_position).get("interest"));
            args.putString("city", my_activity_lec.get(select_position).get("city"));
            args.putString("content", my_activity_lec.get(select_position).get("content"));


            mIntent.putExtra("Recruit",args);

            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            //8번 받으면 로그인 완료. 종료시키면 된다.
            startActivityForResult(mIntent, 8);
            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
            //
            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            //startActivity(mIntent);
        }
    }
    private class MemtoringLoad extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobno", args[1]));
            params.add(new BasicNameValuePair("type", args[0]));
            JSONObject jAry = json.getJSONFromUrl("http://13.124.85.122:8080/getActivity",params);



            return jAry;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.d("MyPageLoad","Post");

            try {
                if(!(json.getString("response").equals("Fail"))) {

                    JSONArray jsonArray = json.getJSONArray("response");
                    String res = json.getString("type");

                    Log.d("resresres", res);

                    if (res.equals("Club")) {
                        if (jsonArray.length() <= 0) {
                            club_txt.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = null;
                            Log.d("MyPageLoad", String.valueOf(json.length()));
                            try {
                                c = jsonArray.getJSONObject(i);
                                String CNum = c.getString("CNum");
                                String CTitle = c.getString("CTitle");
                                String city = c.getString("city");
                                String location = c.getString("location");
                                String leader = c.getString("leader");
                                String date = c.getString("date");
                                String content = c.getString("content");
                                String total_num = c.getString("total_num");
                                String applicationNum = c.getString("applicationNum");

                                adapter_club.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.presentation),
                                        CTitle, content);

                                Log.d("Chat-list2", CNum + CTitle);

                                HashMap<String, String> mclub = new HashMap<String, String>();
                                mclub.put("CNum", CNum);
                                mclub.put("CTitle", CTitle);
                                mclub.put("city", city);
                                mclub.put("location", location);
                                mclub.put("leader", leader);
                                mclub.put("date", date);
                                mclub.put("content", content);
                                mclub.put("total_num", total_num);
                                mclub.put("applicationNum",applicationNum);



                                my_activity_club.add(mclub);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    } else if (res.equals("Mentoring")) {
                        if (jsonArray.length() <= 0) {
                            mentoring_txt.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = null;
                            Log.d("MyPageLoad2222", String.valueOf(json.length()));
                            try {
                                c = jsonArray.getJSONObject(i);
                                String MNum = c.getString("MNum");
                                String MTitle = c.getString("MTitle");
                                String city = c.getString("city");
                                String location = c.getString("location");
                                String leader = c.getString("leader");
                                String date = c.getString("date");
                                String content = c.getString("content");
                                String latitude = c.getString("latitude");
                                String longitude = c.getString("longitude");
                                String total_num = c.getString("total_num");
                                String applicationNum = c.getString("applicationNum");

                                adapter_mentoring.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.presentation),
                                        MTitle, content);

                                Log.d("Chat-list2", MNum + MTitle);

                                HashMap<String, String> mclub = new HashMap<String, String>();
                                mclub.put("MNum", MNum);
                                mclub.put("CTitle", MTitle);
                                mclub.put("city", city);
                                mclub.put("location", location);
                                mclub.put("leader", leader);
                                mclub.put("date", date);
                                mclub.put("content", content);
                                mclub.put("latitude", latitude);
                                mclub.put("longitude", longitude);
                                mclub.put("total_num", total_num);
                                mclub.put("applicationNum", applicationNum);

                                my_activity_mentoring.add(mclub);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (res.equals("Recruit")) {         //내가 기관에 채용되어 있는 정보.
                        if (jsonArray.length() <= 0) {
                            lec_txt.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = null;
                            Log.d("MyPageLoad2222", String.valueOf(json.length()));
                            try {
                                c = jsonArray.getJSONObject(i);
                                String RNum = c.getString("RNum");
                                String RTitle = c.getString("RTitle");
                                String applicationNum = c.getString("applicationNum");
                                String total_num = c.getString("total_num");
                                String institution = c.getString("institution");
                                String interest = c.getString("interest");
                                String city = c.getString("city");
                                String date = c.getString("date");
                                String content = c.getString("content");


                                adapter_lec.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.presentation),
                                        RTitle, content);

                                Log.d("Chat-list2", RNum + RTitle);

                                HashMap<String, String> mclub = new HashMap<String, String>();
                                mclub.put("RNum", RNum);
                                mclub.put("RTitle", RTitle);
                                mclub.put("city", city);
                                mclub.put("interest", interest);
                                mclub.put("institution", institution);
                                mclub.put("date", date);
                                mclub.put("content", content);
                                mclub.put("total_num", total_num);
                                mclub.put("applicationNum", applicationNum);

                                my_activity_lec.add(mclub);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (res.equals("Wait")) {   //기관에서 채용이 아직 안되어있고 신청만 한 상태
                        Log.d("Wait","Wait");
                        if (jsonArray.length() <= 0) {
                            wait_txt.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = null;
                            Log.d("MyPageLoad2222", String.valueOf(json.length()));
                            try {
                                c = jsonArray.getJSONObject(i);
                                String RNum = c.getString("RNum");
                                String RTitle = c.getString("RTitle");
                                String applicationNum = c.getString("applicationNum");
                                String total_num = c.getString("total_num");
                                String institution = c.getString("institution");
                                String interest = c.getString("interest");
                                String city = c.getString("city");
                                String date = c.getString("date");
                                String content = c.getString("content");


                                adapter_wait.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.presentation),
                                        RTitle, content);

                                Log.d("Chat-list2", RNum + RTitle);

                                HashMap<String, String> mclub = new HashMap<String, String>();
                                mclub.put("RNum", RNum);
                                mclub.put("RTitle", RTitle);
                                mclub.put("city", city);
                                mclub.put("interest", interest);
                                mclub.put("institution", institution);
                                mclub.put("date", date);
                                mclub.put("content", content);
                                mclub.put("total_num", total_num);
                                mclub.put("applicationNum", applicationNum);

                                my_activity_wait.add(mclub);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                setListViewHeightBasedOnChildren(listview_club);
                setListViewHeightBasedOnChildren(listview_mentoring);
                setListViewHeightBasedOnChildren(listview_lec);
                setListViewHeightBasedOnChildren(listview_wait);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter_club.notifyDataSetChanged();
            adapter_mentoring.notifyDataSetChanged();
            adapter_wait.notifyDataSetChanged();
            adapter_lec.notifyDataSetChanged();

            updateTextView();

        }
    }
    public void showClassList(){

        //내 디비 정보 받아와서
        // 첫 번째 아이템 추가.

        //이것도 원격제어해야한다.

        if(prefs.getString("HELP_FLAG","").equals("1") ){   //어르신이 원격제어중이면
            new MyPageListActivity.MemtoringLoad().execute("Club",prefs.getString("REG_FROM",""));
            new MyPageListActivity.MemtoringLoad().execute("Mentoring",prefs.getString("REG_FROM",""));
            // new MyPageListActivity.MemtoringLoad().execute("Wait");
            new MyPageListActivity.MemtoringLoad().execute("Recruit",prefs.getString("REG_FROM",""));
            new MyPageListActivity.MemtoringLoad().execute("Wait",prefs.getString("REG_FROM",""));

        }else if( prefs.getString("HELP_FLAG","").equals("2")) {    //젊은이가 원격제어중이면


            new MyPageListActivity.MemtoringLoad().execute("Club", prefs.getString("needer_id", ""));
            new MyPageListActivity.MemtoringLoad().execute("Mentoring", prefs.getString("needer_id", ""));
            // new MyPageListActivity.MemtoringLoad().execute("Wait");
            new MyPageListActivity.MemtoringLoad().execute("Recruit", prefs.getString("needer_id", ""));
            new MyPageListActivity.MemtoringLoad().execute("Wait", prefs.getString("needer_id", ""));

        }else{
            new MyPageListActivity.MemtoringLoad().execute("Club",prefs.getString("REG_FROM",""));
            new MyPageListActivity.MemtoringLoad().execute("Mentoring",prefs.getString("REG_FROM",""));
            // new MyPageListActivity.MemtoringLoad().execute("Wait");
            new MyPageListActivity.MemtoringLoad().execute("Recruit",prefs.getString("REG_FROM",""));
            new MyPageListActivity.MemtoringLoad().execute("Wait",prefs.getString("REG_FROM",""));
        }


        setListViewHeightBasedOnChildren(listview_club);
        setListViewHeightBasedOnChildren(listview_mentoring);
        setListViewHeightBasedOnChildren(listview_lec);
        setListViewHeightBasedOnChildren(listview_wait);




//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.presentation),
//                "다 함께 배우는 배드민턴", "기초 클래스로 배트민턴 수업입니다.") ;
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.presentation),
//                "빠르게 배우는 바둑 교실", "기초 클래스로 바둑 수업입니다.") ;
        // 두 번째 아이템 추가.
        //adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_account_circle_black_36dp),
        //        "Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        //adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_assignment_ind_black_36dp),
        //        "Ind", "Assignment Ind Black 36dp") ;
        adapter_club.notifyDataSetChanged();
        adapter_mentoring.notifyDataSetChanged();

        if(my_activity_club.size() <= 0){
            club_txt.setVisibility(View.GONE);
        }else{
            club_txt.setVisibility(View.VISIBLE);
        }

        if(my_activity_mentoring.size() <= 0){
            mentoring_txt.setVisibility(View.GONE);
        }else{
            mentoring_txt.setVisibility(View.VISIBLE);
        }
        if(my_activity_lec.size() <= 0){
            lec_txt.setVisibility(View.GONE);
        }else{
            lec_txt.setVisibility(View.VISIBLE);
        }
        if(my_activity_wait.size() <= 0){
            wait_txt.setVisibility(View.GONE);
        }else{
            wait_txt.setVisibility(View.VISIBLE);
        }

    }

    public void updateTextView(){
        adapter_club.notifyDataSetChanged();
        adapter_mentoring.notifyDataSetChanged();

        if(my_activity_club.size() <= 0){
            club_txt.setVisibility(View.GONE);
        }else{
            club_txt.setVisibility(View.VISIBLE);
        }

        if(my_activity_mentoring.size() <= 0){
            mentoring_txt.setVisibility(View.GONE);
        }else{
            mentoring_txt.setVisibility(View.VISIBLE);
        }
        if(my_activity_lec.size() <= 0){
            lec_txt.setVisibility(View.GONE);
        }else{
            lec_txt.setVisibility(View.VISIBLE);
        }
        if(my_activity_wait.size() <= 0){
            wait_txt.setVisibility(View.GONE);
        }else{
            wait_txt.setVisibility(View.VISIBLE);
        }
    }


    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        Log.d("높이",String.valueOf(totalHeight));

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int)(1.5*totalHeight) + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



//    public class InsertDataTask extends AsyncTask<String,Integer,String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try{
//                return downloadUrl((String) urls[0]);
//            }catch (IOException e){
//                e.printStackTrace();
//                return "다운로드 실패";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if(s.equals("InsertOK")){
//                Log.d("ddd","ddddddddddddddddddddddddddddddd");
//                new DownloadWebPageTask().execute("http://13.124.85.122:52273/search");
//            }
//        }
//
//        public String downloadUrl(String myurl) throws IOException{
//            HttpURLConnection conn = null;
//            String response = "InsertFailed";
//            try{
//                URL url = new URL(myurl);
//                conn = (HttpURLConnection)url.openConnection();
//                Log.d("ddd","ddddddddddddddddddddddddddddddd");
//                conn.setRequestMethod("POST");
//                Log.d("ddd","ddddddddddddddddddddddddddddddd2");
//                conn.setRequestProperty("Content-Type","application/json");
//                conn.setDoOutput(true);
//                JSONObject job = new JSONObject();
//                job.put("ID", "jackie0304");
//                job.put("PW", "1127");
//                job.put("Name", "안동현");
//                OutputStream os  = conn.getOutputStream();
//                os.write(job.toString().getBytes());
//                Log.d("ddd","ddddddddddddddddddddddddddddddd3");
//                os.flush();
//
//                int responseCode = conn.getResponseCode();
//                Log.d("ddd","ddddddddddddddddddddddddddddddd4");
//                if(responseCode == HttpURLConnection.HTTP_OK){
//                    response = "InsertOK";
//                }
//
//            }catch (JSONException e){
//                Log.d("ddd","ddddddddddddddddddddddddddddddd3");
//                e.printStackTrace();
//
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
//            }finally {
//                conn.disconnect();
//            }
//
//            return  response;
//        }
//
//    }
//
//    public class DownloadWebPageTask extends AsyncTask < String, Integer, String>{
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try{
//                return (String)downdloadUrl((String) urls[0]);
//            }catch(IOException e){
//                e.printStackTrace();
//                return "다운로드 실패";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            jsonParsing(s);
//        }
//
//
//        public String downdloadUrl(String myurl) throws IOException{
//            HttpURLConnection conn = null;
//            try{
//                URL url = new URL(myurl);
//                conn = (HttpURLConnection) url.openConnection();
//                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
//                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));
//                String line = null;
//                String page = "";
//                while ((line = bufreader.readLine()) != null) {
//                    page += line;
//                }
//                Log.i("page : " , page);
//                return page;
//            }finally {
//                conn.disconnect();
//            }
//        }
//
//        void jsonParsing (String file) {
//
//            try {
//               JSONArray jsonArray = new JSONArray(file);
//                for ( int i = 0; i <jsonArray.length(); i++){
//                    JSONObject json = jsonArray.getJSONObject(i);
//                    String id = json.getString("ID");
//                    String name = json.getString("PW");
//                    String quantity = json.getString("Name");
//                }
//            }catch (Exception e) {
//                Log.i("err :", e.getMessage());
//            }
//        }
//
//
//    }
}
