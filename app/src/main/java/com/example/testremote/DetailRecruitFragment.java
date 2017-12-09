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

public class DetailRecruitFragment extends Fragment {
    View syView;
    Button applyBtn;

    //TextView textView;
    TextView Rnum;
    TextView Rtitle;
    TextView Rcontent;
    TextView Rcity;
    TextView Rlocation;
    TextView textView;
    TextView RArea;
    TextView RWorkingCity;
    TextView RWorkingDate;
    TextView RTotalNum;
    TextView RWriter;

    String Bundle_num;
    JSONArray retJson;
    //String num;

    String num;
    String area;
    String title;
    String city;
    String date;
    String ttNum;
    //String id;
    String content;
    //String location;
    //String leader;
    String institution;



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
                num = json.getString("RNum");
                area = json.getString("interest");
                title = json.getString("RTitle");
                city = json.getString("city");
                date = json.getString("date");
                ttNum = json.getString("total_num");
                content = json.getString("content");
                //location = json.getString("location");
                institution = json.getString("institution");
                //leader = json.getString("leader");
                //Log.d("gffgggggg",title);
                Toast.makeText(getActivity(),"되나:"+title,Toast.LENGTH_SHORT).show();

                Rnum.setText(num);
                RArea.setText(area);
                Rtitle.setText(title);
                RWorkingCity.setText(city);
                RWorkingDate.setText(date);
                RTotalNum.setText(ttNum);
                RWriter.setText(institution);
                Rcontent.setText(content);
            }
            //MGroupAdapter.notifyDataSetChanged();
        }
    });

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_detail_recruit,container,false);
        init();
        return syView;
    }
    public void init(){
        //사용자 아이디 불러오기
        prefs = getActivity().getSharedPreferences("Chat", 0);

        Bundle arguments = this.getArguments();
        Bundle_num = arguments.getString("num");//글번호!!!!

        textView = (TextView) syView.findViewById(R.id.textView);


        String url = "http://13.124.85.122:52273/findRInfo";
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

        Rnum = (TextView) syView.findViewById(R.id.Rnum);
        RArea = (TextView) syView.findViewById(R.id.RArea);
        Rtitle = (TextView) syView.findViewById(R.id.Rtitle);
        RWorkingCity = (TextView) syView.findViewById(R.id.RWorkingCity);
        RWorkingDate = (TextView) syView.findViewById(R.id.RWorkingDate);
        RTotalNum = (TextView) syView.findViewById(R.id.RTotalNum);
        RWriter = (TextView) syView.findViewById(R.id.recruit_writer_id);
        Rcontent = (TextView) syView.findViewById(R.id.Rcontent);
        //현재 로그인정보 =! 글쓴이 정보 여기 조심하기
        //String tmp_id11 = prefs.getString("REG_FROM","");
        //RWriter.setText(tmp_id11);

        //textView.setText(num);
        applyBtn = (Button)syView.findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyR();
                Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), RecruitActivity.class);
                startActivity(intent);
                //finish();
                //뒤로가기 어케해
            }
        });
    }
    //왜 안먹지ㅠㅠㅠ
//    public void applyBtn(View v){
//        //applyR();
//        Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();
//        //Intent intent = new Intent(getActivity(), RecruitActivity.class);
//        //finish();
//    }
    public void applyR(){
        InsertDataTask isTask;
        try {
            String tmp_id = prefs.getString("REG_FROM","");//ID(mobno), 아직안됨 왜안들어가지
            //String tmp_pw = prefs.getString("REG_NAME","");//PW(name)
            jsonObject = new JSONObject();
            jsonObject.put("id",tmp_id);
            jsonObject.put("RNum",Bundle_num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://13.124.85.122:52273/applyR";
        isTask = new InsertDataTask(jsonObject);
        RequestForm req = new RequestForm(url);
        isTask.execute(req);
    }
}