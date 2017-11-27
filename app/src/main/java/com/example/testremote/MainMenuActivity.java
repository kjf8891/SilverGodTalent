package com.example.testremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {


    Button gotoMyPageMenuBtn,gotoWantedMenuBtn, gotoMentoringMenuBtn, gotoClubMenuBtn;

    //FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }
    public void init(){

        gotoMyPageMenuBtn = (Button) findViewById(R.id.gotoMyPageMenuBtn);
        gotoWantedMenuBtn = (Button)findViewById(R.id.gotoWantedMenuBtn);
        gotoMentoringMenuBtn = (Button)findViewById(R.id.gotoMentoringMenuBtn);
        gotoClubMenuBtn = (Button)findViewById(R.id.gotoClubMenuBtn);



    }



    public void gotoMyPageMenu(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout,new Fragment_mypage())
//                .commit();
        Intent intent = new Intent(getApplicationContext(), MyPageListActivity.class);
        startActivity(intent);

    }

    public void gotoWantedMenu(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout,new Fragment_lecture())
//                .commit();
        Intent intent = new Intent(getApplicationContext(), WantedActivity.class);
        startActivity(intent);
    }
    public void gotoMentoringMenu(View v){
        Intent intent = new Intent(getApplicationContext(), MentoringActivity.class);
        startActivity(intent);
    }
    public void gotoClubMenu(View v){
        Intent intent = new Intent(getApplicationContext(), ClubActivity.class);
        startActivity(intent);
    }
}
