package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by seyeon on 2017-10-30.
 */

public class MentoringActivity extends AppCompatActivity {

    ArrayList<MGroup> items;
    ListView listView;
    MGroupAdapter MGroupAdapter;
    Toolbar toolbar;
    TextView textView;
    TextView title;
    JSONArray retJson;


    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;

            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                String title = json.getString("MTitle");
                String num = json.getString("MNum");
                String date = json.getString("date");
                String city = json.getString("city");
                //String date = json.getString("date");
                //String location = json.getString("location");
                //items.add(new MGroup(title,date,location));
                items.add(new MGroup(num,title,date,city));
                //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
            }
            MGroupAdapter.notifyDataSetChanged();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring);
        init();
    }

    void init(){
        textView = (TextView)findViewById(R.id.textView);
        //title = (TextView)findViewById(R.id.title);
        //title.setText("[ LECTURE LIST ]");
        //title.setTextSize(30);
        //title.setBackgroundColor(getResources().getColor(R.color.pink));
        //title.setGravity(Gravity.CENTER);
        //toolbar = (Toolbar)findViewById(R.id.app_bar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.pink));
        //setSupportActionBar(toolbar);
        //toolbar.setTitle(textView.getImeActionId());
        //getSupportActionBar().setTitle("test5");
        listView = (ListView)findViewById(R.id.listview);
        items = new ArrayList<MGroup>();
        MGroupAdapter = new MGroupAdapter(getApplicationContext(),R.layout.row_mentoring,items);
        listView.setAdapter(MGroupAdapter);




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                isLongclicked = true;

                if(view.getClass() == ZoomText.class) {


                }

                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MGroup item = (MGroup) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getMentoringNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getMentoringNum().toString());

                DetailMentoringFragment fragment = new DetailMentoringFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout,fragment)
                        .commit();

                }

        });
        String url = "http://13.124.85.122:52273/getMList";
        //초기화

        JSONObject tmp =new JSONObject();
        RequestForm req = new RequestForm(url);
        dwTask.execute(req);
    }

    public void addMGroupBtn(View v){
        //Intent intent = new Intent(getApplicationContext(), ClubAddActivity.class);
        //startActivity(intent);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout,new AddMentoringFragment())
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
