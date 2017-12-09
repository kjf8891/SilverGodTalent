package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by seyeon on 2017-11-17.
 */

public class DetailMentoringFragment extends Fragment {
    View syView;
    //Button btn_move_W;
    Button applyBtn;
    TextView Mnum;
    TextView Mtitle;
    TextView Mcontent;
    TextView Mleader;
    TextView Marea;
    TextView Mcity;
    TextView MttNum;
    TextView Mdate;
    //TextView Mlocation;
    TextView textView;
    String Bundle_num;
    JSONArray retJson;

    String num;
    String area;
    String title;
    String city;
    String date;
    String ttNum;
    //String id;
    String content;
    //String location;
    String leader;
    //String institution;



    JSONObject jsonObject;

    //사용자 아이디 불러오기
    SharedPreferences prefs;

    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            retJson = ret;

            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                num = json.getString("MNum");
                area = json.getString("interest");
                title = json.getString("MTitle");
                date = json.getString("date");
                city = json.getString("city");
                ttNum = json.getString("total_num");
                //location = json.getString("location");
                content = json.getString("content");
                leader = json.getString("leader");
                //Log.d("gffgggggg",title);
                //Toast.makeText(getActivity(),"되나:"+title,Toast.LENGTH_SHORT).show();

                Mnum.setText(Bundle_num);
                Marea.setText(area);
                Mtitle.setText(title);
                Mcity.setText(city);
                Mdate.setText(date);
                MttNum.setText(ttNum);
                Mleader.setText(leader);
                Mcontent.setText(content);
            }
            //MGroupAdapter.notifyDataSetChanged();
        }
    });

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_detail_mentoring,container,false);
        init();
        return syView;
    }
    public void init(){

        //사용자 아이디 불러오기
        prefs = getActivity().getSharedPreferences("Chat", 0);

        Bundle arguments = this.getArguments();
        Bundle_num = arguments.getString("num");//글번호!!!!

        String url = "http://13.124.85.122:52273/findMentoringInfo";
        //초기화

        int tmptmp = Integer.parseInt(Bundle_num);
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("num",tmptmp);
            RequestForm req = new RequestForm(url,jsonObject);
            dwTask.execute(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textView = (TextView) syView.findViewById(R.id.textView);
        Mnum = (TextView) syView.findViewById(R.id.Mnum);;
        Marea = (TextView) syView.findViewById(R.id.MArea);
        Mtitle = (TextView) syView.findViewById(R.id.Mtitle);
        Mcity = (TextView) syView.findViewById(R.id.Mcity);
        Mdate = (TextView) syView.findViewById(R.id.Mdate);
        MttNum = (TextView) syView.findViewById(R.id.MTotalNum);
        Mleader = (TextView) syView.findViewById(R.id.Mid);
        Mcontent = (TextView) syView.findViewById(R.id.Mcontent);

        //textView.setText(num);
//        Mnum.setText(Bundle_num);
//        Mtitle.setText(title);
//        Mcontent.setText(content);

        //btn_move_W = (Button)syView.findViewById(R.id.btn_move_W);
        applyBtn = (Button)syView.findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyM();
                Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MentoringActivity.class);
                startActivity(intent);
                //finish();
                //뒤로가기 어케해
            }
        });

//        btn_move_W.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MentoringActivity.class);
//                startActivity(intent);
//            }
//        });
    }
    //왜안돼
//    public void applyBtn(View v){
////        Intent intent = new Intent(getActivity(), MentoringActivity.class);
////        startActivity(intent);
//        applyM();
//        Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();
//
//    }

    public void applyM(){
        InsertDataTask isTask;
        try {
            String tmp_id = prefs.getString("REG_FROM","");//ID(mobno), 아직안됨 왜안들어가지
            //String tmp_pw = prefs.getString("REG_NAME","");//PW(name)
            jsonObject = new JSONObject();
            jsonObject.put("id",tmp_id);
            jsonObject.put("MNum",Bundle_num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://13.124.85.122:52273/applyM";
        isTask = new InsertDataTask(jsonObject);
        RequestForm req = new RequestForm(url);
        isTask.execute(req);
    }
}