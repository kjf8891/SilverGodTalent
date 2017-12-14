package com.example.testremote;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPageActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    Bundle bundle;
    TextView class_info_txt, class_num_txt;
    String mypage_type;
    String number, title;
    String leader;
    Button applicantBtn;


    MyPage_TabPagerAdapter fragmentPagerAdapter;
    List<MyPage_CompletedFragment> listFragments;

    List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        applicantBtn = (Button)findViewById(R.id.applicantBtn);
        class_info_txt = (TextView)findViewById(R.id.class_info_txt);
        class_num_txt = (TextView)findViewById(R.id.class_num_txt);

//        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.my_page, null, false);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        ZoomView zoomView = new ZoomView(this);
//        zoomView.addView(v);
//        zoomView.setLayoutParams(layoutParams);
//        //zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
//        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
//        //zoomView.setMiniMapCaption("Mini Map Test"); //미니 맵 내용
//        //zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정
//
//        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.activity_mypage);
//        container.addView(zoomView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("빠르게 배우는 바둑 교실");

        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nestedview);
        scrollView.setFillViewport (true);


        // bundle = getIntent().getBundleExtra("random_number");   //인증번호 번들

        if(getIntent().getBundleExtra("Club") != null) {
            bundle = getIntent().getBundleExtra("Club");
            mypage_type = "Club";
            number = bundle.getString("CNum");
            title = bundle.getString("CTitle");
            leader = bundle.getString("leader");        //id

            setTitle(bundle.getString("CTitle"));
            class_num_txt.setText("Club Member :  " + bundle.getString("applicationNum"));


        }else if(getIntent().getBundleExtra("Mentoring") != null){
            bundle = getIntent().getBundleExtra("Mentoring");

        }else if(getIntent().getBundleExtra("Recruit")!=null){
            bundle = getIntent().getBundleExtra("Recruit");

        }

        //게시판이랑 공지글 업데이트하기
        loadPage();

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();

        Toast.makeText(this,"oncreate",Toast.LENGTH_SHORT).show();
    }

    public void applicantList(View v){
        Intent intent = new Intent(getApplicationContext(), ApplicantStateActivity.class);
        startActivity(intent);
    }

    public void loadPage(){


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new MyPageActivity.Load().execute();
        fragmentPagerAdapter.notifyDataSetChanged();
        viewPager.getAdapter().notifyDataSetChanged();
        //BulletinBoardUpdate();
        Toast.makeText(this,"액티비티 리스타트전환",Toast.LENGTH_SHORT).show();
    }

    public void init(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

//        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
//        adapter.addFragment(new MyFragment(), "게시판");
//        adapter.addFragment(new MyFragment(), "공지");
//        viewPager.setAdapter(adapter);
//         tabLayout.setupWithViewPager(viewPager);


        listFragments = new ArrayList<>();
        final MyPage_CompletedFragment myPage_completedFragment1 = new MyPage_CompletedFragment();
        MyPage_CompletedFragment myPage_completedFragment2 = new MyPage_CompletedFragment();

        listFragments.add(myPage_completedFragment1);
        listFragments.add(myPage_completedFragment2);

//        if(myPage_completedFragment1.bucketItems == null){
//            Log.d("completed","null");
//        }else{
//            listFragments.get(0).bucketItems.add(new MyPage_BucketItem("jjong","2017-12-09","Hello"));
//            listFragments.get(0).adapter.notifyDataSetChanged();
//        }


        fragmentPagerAdapter = new MyPage_TabPagerAdapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("게시판"));
        tabLayout.addTab(tabLayout.newTab().setText("공지방"));
        tabLayout.setTabTextColors(Color.LTGRAY,Color.GREEN);


        //viewPager.getAdapter().getItemPosition(0)

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        viewPager.setCurrentItem(tab.getPosition());
                        fragmentPagerAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                if(mypage_type.equals("Club")){
                    Intent mIntent = new Intent(getApplicationContext(), BulletinWriteActivity.class);
                    //번들로 넘겨줘야한다.
                    Bundle args = new Bundle();
                    args.putString("type", mypage_type);
                    args.putString("Num", number);
                    args.putString("Title",title);

                    mIntent.putExtra("userinfo",args);
                    startActivityForResult(mIntent, 8);
                }
            }
        });



        new MyPageActivity.Load().execute();
        fragmentPagerAdapter.notifyDataSetChanged();
        viewPager.getAdapter().notifyDataSetChanged();
        //BulletinBoardUpdate();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        new MyPageActivity.Load().execute();
//        viewPager.getAdapter().notifyDataSetChanged();
//        fragmentPagerAdapter.notifyDataSetChanged();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
    }

    public void BulletinBoardUpdate(){

        if(listFragments.get(0).bucketItems == null){
            Log.d("completed","null");
        }else{
            //디비에서 가져오쟈.
            //ClubBulletinBoard
            //CNum으로 찾자. 타입은 mypage_type으로 구분

           // listFragments.get(0).bucketItems.add(new MyPage_BucketItem("jjong","2017-12-09","Hello"));
            //listFragments.get(0).adapter.notifyDataSetChanged();
            new MyPageActivity.Load().execute();
        }



    }
    public void classInfo(View view) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void classNum(View view) {

    }

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mypage_type", mypage_type));
            params.add(new BasicNameValuePair("Num", number));
            JSONArray jAry = json.getJSONArray("http://13.124.85.122:8080/getBulletinBoard",params);

            return jAry;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            Log.d("MyPageLoad","Post");


            listFragments.get(0).bucketItems.clear();
            listFragments.get(1).bucketItems.clear();
            listFragments.get(0).adapter.notifyDataSetChanged();
            listFragments.get(1).adapter.notifyDataSetChanged();
            fragmentPagerAdapter.notifyDataSetChanged();

            for(int i = 0; i < json.length(); i++){
                JSONObject c = null;
                Log.d("MyPageLoad",String.valueOf(json.length()));
                try {
                    c = json.getJSONObject(i);
                    String sid = c.getString("id");
                    String sNum = "";
                    String sBNum = "";
                    if(mypage_type.equals("Club")){
                        sNum = c.getString("CNum");
                        sBNum = c.getString("CBNum");
                    }
                    else if(mypage_type.equals("Mentroing")) {
                        sNum = c.getString("MNum");
                        sBNum = c.getString("MBNum");
                    }
                    else if(mypage_type.equals("Recruit")){
                        sNum = c.getString("RNum");
                        sBNum = c.getString("RBNum");
                    }

                    String sNickname = c.getString("Nickname");
                    String sdate = c.getString("date");
                    String scontent = c.getString("content");

                    Log.d("leader",leader);
                    Log.d("ggg",sNickname+sdate+scontent);

                    if(sid.equals(leader)){
                        Log.d("tttt","tttt");
                        listFragments.get(0).bucketItems.add(new MyPage_BucketItem(sNickname, sdate, scontent));
                    }else {
                        Log.d("gdgdg","gdgdg");
                        listFragments.get(1).bucketItems.add(new MyPage_BucketItem(sNickname, sdate, scontent));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            listFragments.get(0).adapter.notifyDataSetChanged();
            listFragments.get(1).adapter.notifyDataSetChanged();
            fragmentPagerAdapter.notifyDataSetChanged();
        }
    }
}




//<android.support.design.widget.AppBarLayout
//        android:layout_marginTop="20dp"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">
//
//<android.support.design.widget.TabLayout
//        android:id="@+id/tabs"
//        android:layout_width="match_parent"
//        android:layout_height="60dp"
//        android:layout_gravity="bottom"
//        app:tabGravity="fill"
//        app:tabMode="fixed" />
//</android.support.design.widget.AppBarLayout>
//
//<android.support.v4.view.ViewPager
//        android:id="@+id/viewpager"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:layout_marginBottom="60dp"
//        app:layout_behavior="@string/appbar_scrolling_view_behavior" />


