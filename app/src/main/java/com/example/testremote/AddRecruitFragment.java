package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by seyeon on 2017-11-17.
 */

public class AddRecruitFragment extends Fragment {
    View syView;
    Button completeBtn;
    EditText title;
    //EditText content;
    EditText date;
    //TextView date;
    TextView uId;
    TextView textView;
    JSONObject jsonObject;

    //사용자 아이디 불러오기
    SharedPreferences prefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_add_recruit,container,false);
        init();
        return syView;
    }
    public void init(){
        //사용자 아이디 불러오기

        textView = (TextView)syView.findViewById(R.id.textView);
        prefs = getActivity().getSharedPreferences("Chat", 0);
        completeBtn = (Button)syView.findViewById(R.id.btn_move_W);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                Intent intent = new Intent(getActivity(), RecruitActivity.class);
                startActivity(intent);
            }
        });
        title = (EditText)syView.findViewById(R.id.edit_title);
        //content = (EditText)syView.findViewById(R.id.edit_content);
        date = (EditText)syView.findViewById(R.id.edit_date);
        //textView.setText(date.getText().toString());

        //TextView date = (TextView)syView.findViewById(R.id.date);//날짜는 자동으로 받아오기.... 어떻게?
    }
    //add함수
    public void add(){
        //실행하기 위한 객체
        InsertDataTask isTask;
        try {
            String tmp_id = prefs.getString("REG_FROM","");//ID(mobno), 아직안됨 왜안들어가지
            //String tmp_pw = prefs.getString("REG_NAME","");//PW(name)
            jsonObject = new JSONObject();
            jsonObject.put("title",title.getText().toString());
            //jsonObject.put("content",content.getText().toString());
            jsonObject.put("date",date.getText().toString());
            //Toast.makeText(getActivity(), "date" + date, Toast.LENGTH_SHORT).show();
            //jsonObject.put("id",tmp_id);
            //도시(무슨구,무슨주인지), 위도, 경도, 건물이름(자치센터,주민회관)는 어떻게 넣지?

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://13.124.85.122:52273/addRInfo";
        //초기화
        isTask = new InsertDataTask(jsonObject);// 이렇게 json넣어줘야함
        RequestForm req = new RequestForm(url); // 여기선url만 넣기
        isTask.execute(req); // 이렇게실행하면 웹서버에 보내져

        //
    }

//    public void move_Back(View v){
//        Intent intent = new Intent(getActivity(), MentoringActivity.class);
//        startActivity(intent);
//    }
}