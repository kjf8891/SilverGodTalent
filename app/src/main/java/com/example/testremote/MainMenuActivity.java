package com.example.testremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {


    Button gotoMyPageMenuBtn, gotoRecruitMenuBtn, gotoMentoringMenuBtn, gotoClubMenuBtn;

    //FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }
    public void init() {
        //gotoHelpBtn = (Button) findViewById(R.id.gotoHelpBtn);
        gotoMyPageMenuBtn = (Button) findViewById(R.id.gotoMyPageMenuBtn);
        gotoRecruitMenuBtn = (Button) findViewById(R.id.gotoRecruitMenuBtn);
        gotoMentoringMenuBtn = (Button) findViewById(R.id.gotoMentoringMenuBtn);
        gotoClubMenuBtn = (Button) findViewById(R.id.gotoClubMenuBtn);


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void gotoMyPageMenu(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout,new Fragment_mypage())
//                .commit();
        Intent intent = new Intent(getApplicationContext(), MyPageListActivity.class);
        startActivity(intent);

    }

    public void gotoRecruitMenu(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout,new Fragment_lecture())
//                .commit();
        Intent intent = new Intent(getApplicationContext(), RecruitActivity.class);
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
