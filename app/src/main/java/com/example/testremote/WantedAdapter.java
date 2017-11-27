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
public class WantedAdapter extends ArrayAdapter<NoticeWanted> {
    ArrayList<NoticeWanted> items;
    Context context;

    public WantedAdapter(Context context, int resource, ArrayList<NoticeWanted> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.row_wanted,parent,false);
        }
        NoticeWanted getItem = items.get(position);
        if(getItem!=null){
            TextView wanted_num = (TextView)v.findViewById(R.id.wanted_num);
            TextView wanted_title = (TextView)v.findViewById(R.id.wanted_title);
            TextView wanted_writer = (TextView)v.findViewById(R.id.wanted_writer);
            TextView watned_date = (TextView)v.findViewById(R.id.wanted_date);

            wanted_num.setText(getItem.getnoticeWantedNum());
            wanted_title.setText(getItem.getnoticeWantedTitle());
            wanted_writer.setText(getItem.getnoticeWantedWriter());
            watned_date.setText(getItem.getnoticeWantedWritingDate());

//            Typeface myTypeface= Typeface.createFromAsset(context.getAssets(),"com.ttf");
//            wanted_num.setTypeface(myTypeface);
//            wanted_title.setTypeface(myTypeface);
//            wanted_writer.setTypeface(myTypeface);
//            watned_date.setTypeface(myTypeface);
        }
        return v;
    }
}
