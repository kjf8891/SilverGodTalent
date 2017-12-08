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

public class DetailClubFragment extends Fragment {
    View syView;
    Button btn_move_W;
    Button applyBtn;
    //TextView textView;
    //String num;
    TextView Cnum;
    TextView Ctitle;
    TextView Ccontent;
    TextView Ccity;
    TextView Clocation;
    TextView textView;
    String Bundle_num;
    JSONArray retJson;

    String title;
    //int num;
    String date;
    String city;
    String location;
    String content;
    String leader;



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
                title = json.getString("CTitle");
                //num = json.get("CNum");
                //date = json.getString("date");
                city = json.getString("city");
                location = json.getString("location");
                content = json.getString("content");
                leader = json.getString("leader");
                Log.d("gffgggggg",title);
                Toast.makeText(getActivity(),"되나:"+title,Toast.LENGTH_SHORT).show();

                Cnum.setText(Bundle_num);
                Ctitle.setText(title);
                Ccontent.setText(content);
            }
            //MGroupAdapter.notifyDataSetChanged();
        }
    });

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_detail_club,container,false);
        init();
        return syView;
    }
    public void init(){

        Bundle arguments = this.getArguments();
        Bundle_num = arguments.getString("num");//글번호!!!!

        String url = "http://13.124.85.122:52273/findClubInfo";
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
        Cnum = (TextView) syView.findViewById(R.id.Cnum);;
        Ctitle = (TextView) syView.findViewById(R.id.Ctitle);
        Ccontent = (TextView) syView.findViewById(R.id.Ccontent);

        textView = (TextView) syView.findViewById(R.id.textView);
        //textView.setText(num);
        btn_move_W = (Button)syView.findViewById(R.id.btn_move_W);
        applyBtn = (Button)syView.findViewById(R.id.applyBtn);
        btn_move_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClubActivity.class);
                startActivity(intent);
            }
        });

    }
    public void applyBtn(View v){
//        Intent intent = new Intent(getActivity(), MentoringActivity.class);
//        startActivity(intent);
        Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();
        applyC();

    }

    public void applyC(){
        InsertDataTask isTask;
        try {
            String tmp_id = prefs.getString("REG_FROM","");//ID(mobno), 아직안됨 왜안들어가지
            //String tmp_pw = prefs.getString("REG_NAME","");//PW(name)
            jsonObject = new JSONObject();
            jsonObject.put("id",tmp_id);
            jsonObject.put("CNum",Bundle_num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://13.124.85.122:52273/applyC";
        isTask = new InsertDataTask(jsonObject);
        RequestForm req = new RequestForm(url);
        isTask.execute(req);
    }


}