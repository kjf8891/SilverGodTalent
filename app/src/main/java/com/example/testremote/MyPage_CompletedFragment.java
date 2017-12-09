package com.example.testremote;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 혜진 on 2017-10-24.
 */

public class MyPage_CompletedFragment extends Fragment {

    RecyclerView frag1List;
    List<MyPage_BucketItem> bucketItems = new ArrayList<>();
    MyPage_BucketAdapter adapter = new MyPage_BucketAdapter(getActivity(),bucketItems);;
    Paint p = new Paint();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_fragment_1,container,false);
        frag1List = (RecyclerView)view.findViewById(R.id.mypage_fragment1);

        //frag1List.addItemDecoration(new com.example.sgt.DividerItemDecoration(getContext()));

        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        frag1List.setLayoutManager(lm);



        frag1List.setAdapter(new MyPage_BucketAdapter(getActivity(),bucketItems));

        MyPage_BucketItem[] bucketItem = new MyPage_BucketItem[5];

//        for(int i = 0; i<5; i++){
//            addItem("tt","tt","tt");
//            bucketItem[i] = new MyPage_BucketItem("완료", "2017-00-00","content");
//            bucketItems.add(bucketItem[i]);
//        }
        adapter.notifyDataSetChanged();
//
//        frag1List.setAdapter(new MyPage_BucketAdapter(getActivity(),bucketItems));

        return view;
    }


    public void addItem(String title, String date, String content){
        MyPage_BucketItem bucketItem = new MyPage_BucketItem(title, date, content);

        Log.d(title,date);
        bucketItems.add( new MyPage_BucketItem(title, date, content));
        adapter.notifyDataSetChanged();
    }
}
