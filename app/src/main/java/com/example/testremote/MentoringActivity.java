package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

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

    static PopupWindow popupWindow;
    TextView tvPop;
    TextView tvOrigin;
    boolean isLongclicked = false;

    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

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
        items.add(new MGroup("1","Let's study KOREAN","2017-11-30","Rotterdaam"));
        items.add(new MGroup("2","Special Dancing Practice","2017-12-02","Amsterdam"));
        items.add(new MGroup("3","한자를 공부합시다","2017-10-23","금천구"));
        items.add(new MGroup("4","노래! 어렵지 않아요~","2017-08-31","강남구"));
        items.add(new MGroup("5","다함께 배드민턴","2017-09-03","광진구"));
        items.add(new MGroup("6","빠르게 배우는 바둑교실","2017-11-22","성북구"));
        MGroupAdapter = new MGroupAdapter(getApplicationContext(),R.layout.row_lec,items);
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

                    Log.e("onclick", "click");


                    Toast.makeText(getApplicationContext(), item.getMentoringNum().toString(), Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    Bundle bundle = new Bundle(1);
                    bundle.putString("num", item.getMentoringNum().toString());

                    DetailMentoringFragment fragment = new DetailMentoringFragment();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .commit();

                }

        });
    }

    public void addMGroupBtn(View v){
        //Intent intent = new Intent(getApplicationContext(), ClubAddActivity.class);
        //startActivity(intent);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout,new AddMentoringFragment())
                .commit();
    }

    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    public void activateMagGlass(View view) {


    }
}
