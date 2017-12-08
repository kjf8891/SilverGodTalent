package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by seyeon on 2017-10-30.
 */

public class ClubActivity extends AppCompatActivity {

    ArrayList<Club> items;
    ListView listView;
    ClubAdapter ClubAdapter;
    Button createbtn;
    JSONArray retJson;


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
                //String date = json.getString("date");
                //될까?
                //Date date = (Date) json.get("date");
                String city = json.getString("city");
                //String date = json.getString("date");
                //String location = json.getString("location");
                //items.add(new MGroup(title,date,location));
                //items.add(new Club(num,title,date.toString(),city));
                items.add(new Club(num,title,city));
                //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
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
        items = new ArrayList<Club>();
        listView = (ListView)findViewById(R.id.listview_club);
        //createbtn = (Button)findViewById(R.id.createbtn);
        //items.add(new MGroup("1","test study","2017-11-20","test"));
//        items2.add(new MGroup("2","Funny tennis Practicing","2017-02-02","Amsterdam"));
//        items2.add(new MGroup("3","Let's study mathmatics","2018-11-23","Rotterdam"));
//        items2.add(new MGroup("4","노래! 어렵지 않아요~","2017-08-31","강남구"));
//        items2.add(new MGroup("5","다함께 배드민턴","2017-09-03","광진구"));
//        items2.add(new MGroup("6","빠르게 배우는 바둑교실","2017-11-22","성북구"));
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
