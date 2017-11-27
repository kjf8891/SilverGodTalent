package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-10-30.
 */

public class WantedActivity extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;
    TextView textView;
    TextView title;

    //ArrayList<MGroup> items;
    ArrayList<NoticeWanted> items;
    //MGroupAdapter MGroupAdapter;
    WantedAdapter WantedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted);
        init();
    }

    void init(){
        textView = (TextView)findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listview);

        items = new ArrayList<NoticeWanted>();
        items.add(new NoticeWanted("1","userID1","Content1","Title1","2017-03-03"));
        items.add(new NoticeWanted("2","userID2","content2","Special Dancing Practice","2017-12-02"));
        items.add(new NoticeWanted("3","한자를 공부합시다","2017-10-23","금천구","2017-12-02"));
        items.add(new NoticeWanted("4","노래! 어렵지 않아요~","2017-08-31","강남구","2017-12-02"));
        items.add(new NoticeWanted("5","다함께 배드민턴","2017-09-03","광진구","2017-12-02"));
        items.add(new NoticeWanted("6","빠르게 배우는 바둑교실","2017-11-22","성북구","2017-12-02"));
        WantedAdapter = new WantedAdapter(getApplicationContext(),R.layout.row_wanted,items);
        listView.setAdapter(WantedAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeWanted item = (NoticeWanted) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getnoticeWantedNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getnoticeWantedNum().toString());

                DetailWantedFragment fragment = new DetailWantedFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,fragment)
                        .commit();

            }
        });
    }

    public void addWantedNoticeBtn(View v){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout,new AddWantedFragment())
                .commit();
    }

    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

}
