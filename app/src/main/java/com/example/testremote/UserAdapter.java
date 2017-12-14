package com.example.testremote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-10-19.
 */

public class UserAdapter extends ArrayAdapter<User>{
    ArrayList<User> items;
    Context context;

    JSONObject jsonObject;

    public UserAdapter(Context context, int resource, ArrayList<User> objects) {
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
            v=layoutInflater.inflate(R.layout.row_user,parent,false);
        }
        User getItem = items.get(position);
        if(getItem!=null){
            Button apprvBtn = (Button)v.findViewById(R.id.apprvBtn);
            TextView num = (TextView)v.findViewById(R.id.user_num);
            TextView name = (TextView)v.findViewById(R.id.user_name);
            TextView id = (TextView)v.findViewById(R.id.user_id);

            name.setText(getItem.getUserName());
            id.setText(getItem.getUserID());
            num.setText(getItem.getUserNum());

            //approval누른 지원자의 이름 받아온다.
            final String selectedId = getItem.getUserID();
            final String selectedName = getItem.getUserName();
            final String selectedNum = getItem.getUserNum();
            apprvBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), selectedId+":ID,"+selectedNum+":ID,"+selectedName+":", Toast.LENGTH_SHORT).show();
                    updateApprv(selectedNum,selectedId);
                }
            });
        }
        return v;
    }

    //add함수
    public void updateApprv(String num,String id){
        //실행하기 위한 객체
        InsertDataTask isTask;
        //int apprv=1;
        String apprv = "1";
        try {
            jsonObject = new JSONObject();
            jsonObject.put("apprv",apprv);
            jsonObject.put("num",num);
            jsonObject.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://13.124.85.122:52273/updateApprv";
        isTask = new InsertDataTask(jsonObject);
        RequestForm req = new RequestForm(url);
        isTask.execute(req);
    }
}
