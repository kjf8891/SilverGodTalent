package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.text.TextUtils.isEmpty;

/**
 * Created by seyeon on 2017-10-30.
 */

public class ClubActivity extends AppCompatActivity {

    ArrayList<Club> items;
    ListView listView;
    ClubAdapter ClubAdapter;
    Button createbtn;
    JSONArray retJson;

    SharedPreferences club_pref;

    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;

            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                String title = json.getString("CTitle");
                String num = json.getString("CNum");
                String dateString = json.getString("date");
                //club 테이블에 날짜가 date type이라서 필요한 코드
                String[] separated = dateString.split("T");
                String city = json.getString("city");
                //String date = json.getString("date");
                //String location = json.getString("location");
                //items.add(new MGroup(title,date,location));
                items.add(new Club(num,title,separated[0],city));
                //items.add(new Club(num,title,city));
                //Toast.makeText(getApplicationContext(),"되나:"+dateString+separated[0],Toast.LENGTH_SHORT).show();
            }
            ClubAdapter.notifyDataSetChanged();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        init();
    }

    void init(){
        club_pref = getSharedPreferences("Chat2",0);

        if(isEmpty(club_pref.getString("club_first",""))){
            Toast.makeText(this, "처음이야", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),ClubTutoActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = club_pref.edit();
            editor.putString("club_first","1");
            editor.commit();
        }else{
            Toast.makeText(getApplicationContext(), "처음아니야", Toast.LENGTH_SHORT).show();
        }


        items = new ArrayList<Club>();
        listView = (ListView)findViewById(R.id.listview_club);
        //createbtn = (Button)findViewById(R.id.createbtn);
        //items.add(new MGroup("1","test study","2017-11-20","test"));
//        items2.add(new MGroup("2","Funny tennis Practicing","2017-02-02","Amsterdam"));
//        items2.add(new MGroup("3","Let's study mathmatics","2018-11-23","Rotterdam"));
        ClubAdapter = new ClubAdapter(getApplicationContext(),R.layout.row_club,items);
        listView.setAdapter(ClubAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Club item = (Club) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getClubNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getClubNum().toString());

                DetailClubFragment fragment = new DetailClubFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout,fragment)
                        .commit();

            }
        });
        String url = "http://13.124.85.122:52273/getCList";
        //초기화

        JSONObject tmp =new JSONObject();
        RequestForm req = new RequestForm(url);
        dwTask.execute(req);
    }

    public void addClubBtn(View v){
        //Intent intent = new Intent(getApplicationContext(), ClubAddActivity.class);
        //startActivity(intent);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout,new AddClubFragment())
                .commit();
    }
    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        //startActivity(intent);
        finish();
    }
    public void onBackPressed() {
        //super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
