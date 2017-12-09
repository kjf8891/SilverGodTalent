package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-10-30.
 */

public class RecruitActivity extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;
    TextView textView;
    TextView title;

    //ArrayList<MGroup> items;
    ArrayList<Recruit> items;
    //MGroupAdapter MGroupAdapter;
    RecruitAdapter RecruitAdapter;
    JSONArray retJson;


    //디비에서 데이터 불러오기, 구인 공고 목록
    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {
            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;
            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                String title = json.getString("RTitle");
                String num = json.getString("RNum");
                String date = json.getString("date");
                String city = json.getString("city");
                //위도 경도 여기 있음
                String latitude = json.getString("latitude");
                String longitude = json.getString("longitude");
                //String date = json.getString("date");
                //String location = json.getString("location");
                //items.add(new MGroup(title,date,location));
                items.add(new Recruit(num,title,date,city,latitude,longitude));
                //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
            }
            RecruitAdapter.notifyDataSetChanged();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);
        init();
    }

    void init(){
        textView = (TextView)findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listview);

        items = new ArrayList<Recruit>();
        //items.add(new Recruit("1","userID1","Content1","Title1","2017-03-03"));
//        items.add(new Recruit("2","userID2","content2","Special Dancing Practice","2017-12-02"));
//        items.add(new Recruit("3","한자를 공부합시다","2017-10-23","금천구","2017-12-02"));
//        items.add(new Recruit("4","노래! 어렵지 않아요~","2017-08-31","강남구","2017-12-02"));
//        items.add(new Recruit("5","다함께 배드민턴","2017-09-03","광진구","2017-12-02"));
//        items.add(new Recruit("6","빠르게 배우는 바둑교실","2017-11-22","성북구","2017-12-02"));
        RecruitAdapter = new RecruitAdapter(getApplicationContext(),R.layout.row_recruit,items);
        listView.setAdapter(RecruitAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recruit item = (Recruit) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getnoticeRecruitNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getnoticeRecruitNum().toString());

                DetailRecruitFragment fragment = new DetailRecruitFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout,fragment)
                        .commit();

            }
        });

        String url = "http://13.124.85.122:52273/getRList";
        //초기화

        JSONObject tmp =new JSONObject();
        RequestForm req = new RequestForm(url);
        dwTask.execute(req);
    }

    public void addRecruitNoticeBtn(View v){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout,new AddRecruitFragment())
                .commit();
    }

    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        //startActivity(intent);
        finish();
    }

    @Override
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
