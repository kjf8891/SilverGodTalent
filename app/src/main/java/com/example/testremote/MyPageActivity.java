package com.example.testremote;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyPageActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

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


        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
    }

    public void init(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

//        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
//        adapter.addFragment(new MyFragment(), "게시판");
//        adapter.addFragment(new MyFragment(), "공지");
//        viewPager.setAdapter(adapter);
//         tabLayout.setupWithViewPager(viewPager);


        List<android.support.v4.app.Fragment> listFragments = new ArrayList<>();
        listFragments.add(new MyPage_CompletedFragment());
        listFragments.add(new MyPage_CompletedFragment());

        MyPage_TabPagerAdapter fragmentPagerAdapter = new MyPage_TabPagerAdapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("게시판"));
        tabLayout.addTab(tabLayout.newTab().setText("공지방"));
        tabLayout.setTabTextColors(Color.LTGRAY,Color.GREEN);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        viewPager.setCurrentItem(tab.getPosition());
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




            }
        });

    }


    public void classInfo(View view) {

    }


    public void classMember(View view) {

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


