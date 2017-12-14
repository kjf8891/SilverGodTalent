package com.example.testremote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        final View tmpView = v;

        MGroup getItem = items.get(position);
        if (getItem != null) {
            TextView lec_num = (TextView) v.findViewById(R.id.lec_num);
            lec_num.setVisibility(View.INVISIBLE);
            ZoomText lec_title = (ZoomText) v.findViewById(R.id.lec_title);
            ZoomText lec_city = (ZoomText) v.findViewById(R.id.lec_city);
            ZoomText lec_date = (ZoomText) v.findViewById(R.id.lec_date);
            ImageView lec_btn = (ImageView)v.findViewById(R.id.lec_btn);
            lec_btn.setVisibility(View.VISIBLE);
//            final View finalV = v;
//            lec_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    TextView t = (TextView) view;
//
//                    Log.e("view",t.getText().toString());
//                    finalV.performClick();
//                }
//            });
            // ImageView lec_img = (ImageView)v.findViewById(R.id.lec_img);

            lec_num.setText(getItem.getMentoringNum());
            lec_title.setText(getItem.getMentoringTitle());
            lec_city.setText(getItem.getMentoringCity());
            lec_date.setText(getItem.getMentoringDate());
        }
        return v;
    }
}
