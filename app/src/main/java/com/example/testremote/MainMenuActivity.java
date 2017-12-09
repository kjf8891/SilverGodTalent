package com.example.testremote;

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
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {


    //Button gotoMyPageMenuBtn, gotoRecruitMenuBtn, gotoMentoringMenuBtn, gotoClubMenuBtn;
    SharedPreferences prefs;
    List<NameValuePair> params;

    Button gotoMyPageMenuBtn,gotoRecruitMenuBtn, gotoMentoringMenuBtn, gotoClubMenuBtn;

    //FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }

    @Override
    public void onBackPressed() {
        Log.d("MainMenuActivity", "Backkkkkk");

        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new MainMenuActivity.ActivityChangeSend().execute("back");
        }else{
            super.onBackPressed();
        }

    }

    public void init(){

        gotoMyPageMenuBtn = (Button) findViewById(R.id.gotoMyPageMenuBtn);
        gotoRecruitMenuBtn = (Button) findViewById(R.id.gotoRecruitMenuBtn);
        gotoMentoringMenuBtn = (Button) findViewById(R.id.gotoMentoringMenuBtn);
        gotoClubMenuBtn = (Button) findViewById(R.id.gotoClubMenuBtn);


        prefs = getApplicationContext().getSharedPreferences("Chat", 0);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("activity_change"));

    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            // Intent SendBroadCast로 보낸 action TAG 이름으로 필요한 방송을 찾는다.

          if(name.equals("activity_change")){  //화면 바뀔때
                Log.d("Menu: BroadCast", "activity_change"); // putExtra를 이용한 String전달 }

                String activity_change = intent.getStringExtra("activity_change");
                String sender_id = intent.getStringExtra("sender_id");
                String now_activity = intent.getStringExtra("now_activity");

              //String pw_edittext = intent.getStringExtra("pw_edittext");
              //mobno.setText(mobno_edittext);
              //pw.setText(pw_edittext);

                if(now_activity.equals("MainMenuActivity")){
                    if(activity_change.equals("MyPageListActivity")){




                      //  if(prefs.getString("HELP_FLAG","").equals("1") ){
                            Intent mIntent = new Intent(getApplicationContext(), MyPageListActivity.class);
                            startActivityForResult(mIntent, 8);

//                        }else if( prefs.getString("HELP_FLAG","").equals("2")){
//                            Intent mIntent = new Intent(getApplicationContext(), MyPageListActivity.class);
//
//                            //번들로 넘겨야댄다. 헬퍼의 id를
//                            //needer_id
//                            Bundle args = new Bundle();
//                            args.putString("needer_id", prefs.getString("needer_id",""));
//                            mIntent.putExtra("remote_mypagelist",args);
//                            startActivityForResult(mIntent, 8);
//                        }

                    }else if(activity_change.equals("WantedActivity")){
                        Intent mIntent = new Intent(getApplicationContext(), RecruitActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        Log.d("MainMenuActivity","WantedActivity");
                        finish();
                    }else if(activity_change.equals("MentoringActivity")){
                        Intent mIntent = new Intent(getApplicationContext(), MentoringActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        Log.d("MainMenuActivity","MentoringActivity");
                        finish();
                    }else if(activity_change.equals("ClubActivity")){
                        Intent mIntent = new Intent(getApplicationContext(), ClubActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        Log.d("MainMenuActivity","ClubActivity");
                        finish();
                    }else if(activity_change.equals("back")){
                        Log.d("MainMenuActivity","back");
                        finish();
                    }
                }
            }
        }
    };


    public void gotoMyPageMenuBtn(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout,new Fragment_mypage())
//                .commit();

        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new MainMenuActivity.ActivityChangeSend().execute("MyPageListActivity");
        }else {
            Intent intent = new Intent(getApplicationContext(), MyPageListActivity.class);
            startActivity(intent);
        }
    }

    public void gotoRecruitMenu(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout,new Fragment_lecture())
//                .commit();
        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new MainMenuActivity.ActivityChangeSend().execute("WantedActivity");
        }else {
            Intent intent = new Intent(getApplicationContext(), RecruitActivity.class);
            startActivity(intent);
        }
    }

    public void gotoMentoringMenu(View v){
        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new MainMenuActivity.ActivityChangeSend().execute("MentoringActivity");
        }else {
            Intent intent = new Intent(getApplicationContext(), MentoringActivity.class);
            startActivity(intent);
        }
    }
    public void gotoClubMenu(View v){
        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){
            new MainMenuActivity.ActivityChangeSend().execute("ClubActivity");
        }else{
            Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
            startActivity(intent);
        }

    }

    //help
    public void nothing(View view) {
        if(prefs.getString("HELP_FLAG","").equals("1") || prefs.getString("HELP_FLAG","").equals("2")){    //어르신, 헬버의 폰번호 입력
            //params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));

        }
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
            }else if(args[0].equals("MyPageListActivity") || args[0].equals("WantedActivity") || args[0].equals("ClubActivity") || args[0].equals("MentoringActivity")){
                Log.d("MainMenuActivity",args[0]);

                params.add(new BasicNameValuePair("now_activity", "MainMenuActivity"));    //현재 액티비티
                params.add(new BasicNameValuePair("activity_change", args[0])); //어떤 화면으로 전활할 버튼을 눌렀는지
                params.add((new BasicNameValuePair("sender_id", prefs.getString("REG_ID",""))));    //보내는 사람의 토큰

                if(prefs.getString("HELP_FLAG","").equals("1")){    //어르신, 헬버의 폰번호 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("helper_id",""))));
                }else if(prefs.getString("HELP_FLAG","").equals("2")){      //젊은이, needer의 토큰을 입력
                    params.add((new BasicNameValuePair("to_id", prefs.getString("needer_id",""))));
                }



                //마이페이지로 갈 경우에는 따로 해줘야 마이페이지 리스트에 갔을 때
                //어르신은 그냥 넘기면 되지만 도와주는 사람은 어르신 정보를 불러와야되기 때문에 정보를 같이 전달해줘야 한다.
                //어르신이 누르면 클래스 실행 -> post에서 그냥 액티비티 전환하면 된다. 대신 클래스에서 자신의 정보를 전달해야한다.
                //도와주는 사람이 누르면 어르신에게 정보를 요청해야한다.




            }else if(args[0].equals("back")){

                Log.d("MainMenuActivity","classBack");
                params.add(new BasicNameValuePair("now_activity", "MainMenuActivity"));    //현재 액티비티
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

                    if(activity.equals("MyPageListActivity")){

                        Log.d("MainMenuPost",activity);

                        if(prefs.getString("HELP_FLAG","").equals("1") ){
                            Intent mIntent = new Intent(getApplicationContext(), MyPageListActivity.class);
                            //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //8번 받으면 로그인 완료. 종료시키면 된다.
                            startActivityForResult(mIntent, 8);
                            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                            //
                            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //startActivity(mIntent);
                        }else if( prefs.getString("HELP_FLAG","").equals("2")){
                            Intent mIntent = new Intent(getApplicationContext(), MyPageListActivity.class);

                            //번들로 넘겨야댄다. 헬퍼의 id를
                            //needer_id

                            //Bundle args = new Bundle();
                            //args.putString("needer_id", prefs.getString("needer_id",""));
                            //mIntent.putExtra("remote_mypagelist",args);


                            startActivityForResult(mIntent, 8);
                            //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                            //
                            // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //startActivity(mIntent);
                        }


                    }else if(activity.equals("WantedActivity")){

                        Log.d("MainMenuPost",activity);


                        Intent mIntent = new Intent(getApplicationContext(), RecruitActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        //
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(mIntent);
                    }else if(activity.equals("ClubActivity")){

                        Log.d("MainMenuPost",activity);


                        Intent mIntent = new Intent(getApplicationContext(), ClubActivity.class);
                        //mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //8번 받으면 로그인 완료. 종료시키면 된다.
                        startActivityForResult(mIntent, 8);
                        //Intent mIntent = new Intent(getApplicationContext(),SignInActivity.class);
                        //
                        // mIntent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivity(mIntent);
                    }else if(activity.equals("MentoringActivity")){

                        Log.d("MainMenuPost",activity);


                        Intent mIntent = new Intent(getApplicationContext(), MentoringActivity.class);
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
