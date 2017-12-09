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

public class ClubAdapter extends ArrayAdapter<Club>{
    ArrayList<Club> items;
    Context context;

    public ClubAdapter(Context context, int resource, ArrayList<Club> objects) {
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
            v=layoutInflater.inflate(R.layout.row_club,parent,false);
        }
        Club getItem = items.get(position);
        if(getItem!=null){
            TextView club_num = (TextView)v.findViewById(R.id.club_num);
            TextView club_title = (TextView)v.findViewById(R.id.club_title);
            TextView club_city = (TextView)v.findViewById(R.id.club_city);
            TextView club_date = (TextView)v.findViewById(R.id.club_date);

            club_num.setText(getItem.getClubNum());
            club_title.setText(getItem.getClubTitle());
            club_city.setText(getItem.getClubCity());
            club_date.setText(getItem.getClubDate());
        }
        return v;
    }
}
