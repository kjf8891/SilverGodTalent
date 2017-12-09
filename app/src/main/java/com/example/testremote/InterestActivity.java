package com.example.testremote;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InterestActivity extends AppCompatActivity {

    Button complete;
    String tmp;
    JSONArray retJson;
    //ArrayList<InterestData> interests;
    String url;
    RequestForm req;
    ArrayList<String> checkedInterest;

    ArrayList<Boolean> isChecked;

    JSONObject userinfo;
    String userinf_id, userinfo_pw, userinfo_Nickname;
    SharedPreferences prefs;
    List<NameValuePair> params;

    private RecyclerView mRecyclerView;
    private InterestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InterestData> myDataset;
    boolean flag = true;


    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;

            for(int i = 0 ; i< retJson.length(); i++){

                JSONObject json = retJson.getJSONObject(i);
                String area = json.getString("area");

                isChecked.add(false);
                myDataset.add(new InterestData(area));
                //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);


        try {
            userinfo = new JSONObject(getIntent().getStringExtra("userinfo")); //signin 액티비티에서 데이터 받아옴
            Log.e("UserInfo", "Passed UserInfo: " + userinfo.getString("Nickname"));
            userinf_id = userinfo.getString("ID");
            userinfo_pw = userinfo.getString("PW");
            userinfo_Nickname = userinfo.getString("Nickname");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        init();

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new InterestAdapter(myDataset, new InterestAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {


                Toast.makeText(getApplicationContext(),String.valueOf(isChecked.get(position)),Toast.LENGTH_SHORT);

                if(!isChecked.get(position)) {
                    checkedInterest.add(myDataset.get(position).area.toString());
                    isChecked.set(position,true);
                    Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"add",Toast.LENGTH_SHORT).show();

                }else{
                    checkedInterest.remove(myDataset.get(position).area.toString());
                    isChecked.set(position,false);
                    Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"delete",Toast.LENGTH_SHORT).show();

                }

                Log.d("interest_onclick" , String.valueOf(isChecked.get(position)));
                new InterestActivity.InterestCheckSend().execute(String.valueOf(position),String.valueOf(isChecked.get(position)));
                Log.e("interests_cl", checkedInterest.toString());

            }
        });


        mRecyclerView.setAdapter(mAdapter);
    }

    private class Login extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
           // Log.d("loglogloglog",args[0]);

//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putString("REG_FROM", id.getText().toString());
//            edit.putString("FROM_NAME", pw.getText().toString());
//            edit.commit();
            //params.add(new BasicNameValuePair("name", prefs.getString("FROM_NAME","")));
            //params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));

            params.add(new BasicNameValuePair("name", userinfo_pw));
            params.add(new BasicNameValuePair("mobno",userinf_id));
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

                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_FROM", userinfo.getString("ID"));
                    edit.putString("FROM_NAME", userinfo.getString("PW"));
                    edit.putString("Nickname", userinfo_Nickname);
                    Log.d("NickName", userinfo_Nickname);
                    edit.commit();

                    startActivity(new Intent(getApplicationContext(),UserActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onBackPressed() {
        Log.d("Interest", "BackPress");

        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new InterestActivity.ActivityChangeSend().execute("back");
        }else{
            super.onBackPressed();
        }
    }

    void init(){
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("activity_change"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("InterestCheckSend"));

        myDataset = new ArrayList<>();
        checkedInterest = new ArrayList<>();
        isChecked = new ArrayList<>();

        url = "http://13.124.85.122:52273/getInterest";
        req = new RequestForm(url);


        dwTask.execute(req);
        complete = (Button)findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신이면
                    new InterestActivity.ActivityChangeSend().execute("complete");
                }else if( prefs.getString("HELP_FLAG","").equals("2")){ //도와주는 사람이면
                    new InterestActivity.ActivityChangeSend().execute("complete");
                }else{
                    btn_complete_Click();
                }

                //어르신이 누르면 btn_complete_Click()을 실행하면 되지만
                //도와주는 사람이 누르면 실제로 회원가입을 그사람이 하는 것은 아니기 때문에 도와주는사람은 완료되면 메인화면으로 넘어가면 된다.


            }
        });
    }

    public void btn_complete_Click(){

        JSONArray interests = new JSONArray();

        for(int i = 0 ; i < checkedInterest.size();i++){
            JSONObject iObject = new JSONObject();

            try {
                iObject.put("checkedArea",checkedInterest.get(i));
                interests.put(iObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            userinfo.put("interests",interests);
            Log.e("jsoninput",userinfo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InsertDataTask insertTask = new InsertDataTask(userinfo);

        url = "http://13.124.85.122:52273/pushData";
        req = new RequestForm(url);

//                for ( int i = 0; i <interests.length(); i++){
//                    JSONObject json = interests.getJSONObject(i);
//
//                    if(json.has("ID"))
//                        id = json.getString("ID");
//                    if(json.has("PW"))
//                        name = json.getString("PW");
//                    if(json.has("Name"))
//                        quantity = json.getString("Name");
//                    if(json.has("no"))
//                        no = json.getInt("no");
//                    if(json.has("area"))
//                        area = json.getString("area");


        insertTask.execute(req);

        new InterestActivity.Login().execute();
        Log.e("userinfo 전송","userinfo 전송");

        Intent returnIntent = new Intent();
        // returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

    public void selInterest(View v){

        if(((ToggleButton)v).isChecked())
            Toast.makeText(this,v.getId()+ "checked",Toast.LENGTH_SHORT ).show();
        else
            Toast.makeText(this,v.getId()+ "not checked",Toast.LENGTH_SHORT ).show();

        //배열같은곳에 누를때마다 getText해서 뭘 골랐는지 넣어두고
        //선택완료 버튼을 누를때 디비에 넣으면 될것.
    }



    private class ActivityChangeSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());

            //params.add(new BasicNameValuePair("type", "helper_request"));
            if(args[0].equals("complete")){
                params.add(new BasicNameValuePair("now_activity", "InterestActivity"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", "complete")); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }

            }else if(args[0].equals("back") || args[0].equals("cancel")){
                params.add(new BasicNameValuePair("now_activity", "InterestActivity"));    //현재 액티비티
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
                    Log.d("interest","Success");
                    if(activity.equals("complete")){
                        // btn_next.performClick();

                        //btn_complete_Click();
                        if(prefs.getString("HELP_FLAG","").equals("1")){
                            btn_complete_Click();
                        }else if(prefs.getString("HELP_FLAG","").equals("2")){
                            Log.d("interest","complete");
                            Intent returnIntent = new Intent();
                            // returnIntent.putExtra("result",result);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }


                    }else if(activity.equals("cancel") || activity.equals("back")){
                        Log.d("InterestActivity","Success,back");
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

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            // Intent SendBroadCast로 보낸 action TAG 이름으로 필요한 방송을 찾는다.

            if(name.equals("screeninfo")){  //어르신이 보낸 정보로 화면 셋팅하기. 로그인화면
                Log.d("Signin: BroadCast", "BroadcastReceiver :: com.dwfox.myapplication.SEND_BROAD_CAST :: "); // putExtra를 이용한 String전달 }
//                String screeninfo = intent.getStringExtra("screeninfo");
//                String mobno_edittext = intent.getStringExtra("mobno_edittext");
//                String pw_edittext = intent.getStringExtra("pw_edittext");
//
//                mobno.setText(mobno_edittext);
//                pw.setText(pw_edittext);

            }else if(name.equals("activity_change")){  //화면 바뀔때
                Log.d("Interest:BroadCas", "activity_change"); // putExtra를 이용한 String전달 }


                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");
                //String pw_edittext = intent.getStringExtra("pw_edittext");

                //mobno.setText(mobno_edittext);
                //pw.setText(pw_edittext);

                if(now_activity.equals("InterestActivity")){
                    if(activity_change.equals("complete")){
                        //btn_complete_Click();

                        if(prefs.getString("HELP_FLAG","").equals("1")){
                            btn_complete_Click();
                        }else if(prefs.getString("HELP_FLAG","").equals("2")){
                            Intent returnIntent = new Intent();
                            // returnIntent.putExtra("result",result);
                            InterestActivity.this.setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }

                        Log.d("interset","complete");

                    }else if(activity_change.equals("back") || activity_change.equals("cancel")){
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
//                                new Instrumentation().sendKeySync(event);
//                            }
//                        });
                        Log.d("InterestActivity","BroadcastBack");
                        finish();
                    }
                }
            }else if(name.equals("InterestCheckSend")){

                String sender_id = intent.getStringExtra("interset_check");
                String now_activity = intent.getStringExtra("now_activity");
                int position = Integer.parseInt(intent.getStringExtra("interest_position"));
                String interest_clicked = intent.getStringExtra("interest_clicked");

                Toast.makeText(getApplicationContext(),String.valueOf(isChecked.get(position)),Toast.LENGTH_SHORT);

                final InterestAdapter.ViewHolder holder = (InterestAdapter.ViewHolder) mRecyclerView.findViewHolderForPosition(position);
                Log.d("Interest", interest_clicked);
                Log.d("Interest", String.valueOf(isChecked.get(position)));
                if(!String.valueOf(isChecked.get(position)).equals(interest_clicked) ){
                    //holder.mBtn1.performClick();

                    if(!isChecked.get(position)) {
                        checkedInterest.add(myDataset.get(position).area.toString());
                        isChecked.set(position,true);
                        holder.mBtn1.setChecked(true);
                        Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"add",Toast.LENGTH_SHORT).show();
                    }else{
                        checkedInterest.remove(myDataset.get(position).area.toString());
                        isChecked.set(position,false);
                        holder.mBtn1.setChecked(false);
                        Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"delete",Toast.LENGTH_SHORT).show();
                    }
                    Log.d("broadcast","brodcast");
                }



//                if(!isChecked.get(position)) {
//                    checkedInterest.add(myDataset.get(position).area.toString());
//                    isChecked.set(position,true);
//                    Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"add",Toast.LENGTH_SHORT).show();
//
//                }else{
//                    checkedInterest.remove(myDataset.get(position).area.toString());
//                    isChecked.set(position,false);
//                    Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"delete",Toast.LENGTH_SHORT).show();
//
//                }


                mAdapter.notifyDataSetChanged();

            }
        }
    };

    private class InterestCheckSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());
            //params.add(new BasicNameValuePair("type", "helper_request"));

            params.add(new BasicNameValuePair("now_activity", "SignInActivity"));    //현재 액티비티
            params.add(new BasicNameValuePair("interest_position",args[0])); //어떤 포지션을 선택했는지
            params.add(new BasicNameValuePair("interest_clicked",args[1])); //어떤 포지션을 선택했는지
            params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));

            Log.d("interest_class" , args[1]);
            if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
            }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
            }

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/InterestCheckSend",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {
                    Log.d("Interest", "Success");

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}


