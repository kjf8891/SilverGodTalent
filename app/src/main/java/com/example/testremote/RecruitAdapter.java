package com.example.testremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016-06-08.
 */
public class RecruitAdapter extends ArrayAdapter<Recruit> {
    ArrayList<Recruit> items;
    Context context;

    public RecruitAdapter(Context context, int resource, ArrayList<Recruit> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.row_recruit,parent,false);
        }
        Recruit getItem = items.get(position);
        if(getItem!=null){
            TextView recruit_num = (TextView)v.findViewById(R.id.recruit_num);
            TextView recruit_title = (TextView)v.findViewById(R.id.recruit_title);
            //TextView recruit_writer = (TextView)v.findViewById(R.id.recruit_writer);
            TextView recruit_date = (TextView)v.findViewById(R.id.recruit_date);
            TextView recruit_city = (TextView)v.findViewById(R.id.recruit_city);

            recruit_num.setText(getItem.getnoticeRecruitNum());
            recruit_title.setText(getItem.getnoticeRecruitTitle());
            //recruit_writer.setText(getItem.getnoticeRecruitWriter());
            recruit_date.setText(getItem.getnoticeRecruitWorkingDate());
            recruit_city.setText(getItem.getnoticeRecruitCity());

//            Typeface myTypeface= Typeface.createFromAsset(context.getAssets(),"com.ttf");
//            recruit_num.setTypeface(myTypeface);
//            recruit_title.setTypeface(myTypeface);
//            recruit_writer.setTypeface(myTypeface);
//            watned_date.setTypeface(myTypeface);
        }
        return v;
    }

    public void updateData(ArrayList<Recruit> newList){

        this.items = newList;

    }
}
