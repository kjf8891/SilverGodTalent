package com.example.testremote;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 혜진 on 2017-10-24.
 */

public class MyPage_TabPagerAdapter extends FragmentStatePagerAdapter {

    List<MyPage_CompletedFragment> listFragments;
    public MyPage_TabPagerAdapter(FragmentManager fm, List<MyPage_CompletedFragment> listFragments) {
        super(fm);
        this.listFragments = listFragments;
    }

    @Override
    public MyPage_CompletedFragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        //return super.getItemPosition(object);
        return POSITION_NONE;
    }
}
