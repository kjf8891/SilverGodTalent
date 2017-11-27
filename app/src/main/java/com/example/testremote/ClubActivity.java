package com.example.testremote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-10-30.
 */

public class ClubActivity extends AppCompatActivity {

    ArrayList<MGroup> items;
    ListView listView;
    MGroupAdapter MGroupAdapter;
    Button createbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        init();
    }

    void init(){
        items = new ArrayList<MGroup>();
        listView = (ListView)findViewById(R.id.listview);
        //createbtn = (Button)findViewById(R.id.createbtn);
        items.add(new MGroup("1","English Chocolate study","2017-11-20","Rotterdam"));
        items.add(new MGroup("2","Funny tennis Practicing","2017-02-02","Amsterdam"));
        items.add(new MGroup("3","Let's study mathmatics","2018-11-23","Rotterdam"));
        items.add(new MGroup("4","노래! 어렵지 않아요~","2017-08-31","강남구"));
        items.add(new MGroup("5","다함께 배드민턴","2017-09-03","광진구"));
        items.add(new MGroup("6","빠르게 배우는 바둑교실","2017-11-22","성북구"));
        MGroupAdapter = new MGroupAdapter(getApplicationContext(),R.layout.row_lec,items);
        listView.setAdapter(MGroupAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MGroup item = (MGroup) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getMentoringNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getMentoringNum().toString());

                DetailClubFragment fragment = new DetailClubFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,fragment)
                        .commit();

            }
        });
    }

    public void addClubBtn(View v){
        //Intent intent = new Intent(getApplicationContext(), ClubAddActivity.class);
        //startActivity(intent);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout,new AddClubFragment())
                .commit();
    }
    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }
}
