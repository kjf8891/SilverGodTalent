package com.example.testremote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-10-19.
 */

public class MGroupAdapter extends ArrayAdapter<MGroup>{
    ArrayList<MGroup> items;
    Context context;

    public MGroupAdapter(Context context, int resource, ArrayList<MGroup> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=layoutInflater.inflate(R.layout.row_lec,parent,false);
        }
        MGroup getItem = items.get(position);
        if(getItem!=null){
            TextView lec_num = (TextView)v.findViewById(R.id.lec_num);
            TextView lec_title = (TextView)v.findViewById(R.id.lec_title);
            TextView lec_city = (TextView)v.findViewById(R.id.lec_city);
            TextView lec_date = (TextView)v.findViewById(R.id.lec_date);

            lec_num.setText(getItem.getMentoringNum());
            lec_title.setText(getItem.getMentoringTitle());
            lec_city.setText(getItem.getMentoringCity());
            lec_date.setText(getItem.getMentoringDate());
        }
        return v;
    }
}
