package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
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
    Button btn_move_W;
    Button applyBtn;

    //TextView textView;
    TextView Rnum;
    TextView Rtitle;
    TextView Rcontent;
    TextView Rcity;
    TextView Rlocation;
    TextView textView;

    String Bundle_num;
    JSONArray retJson;
    //String num;

    String title;
    String num;
    String date;
    String city;
    //String location;
    String content;
    //String leader;
    String institution;

    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            retJson = ret;
            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                title = json.getString("RTitle");
                num = json.getString("RNum");
                date = json.getString("date");
                city = json.getString("city");
                //location = json.getString("location");
                content = json.getString("content");
                institution = json.getString("institution");
                //leader = json.getString("leader");
                Log.d("gffgggggg",title);
                Toast.makeText(getActivity(),"되나:"+title,Toast.LENGTH_SHORT).show();

                Rnum.setText(Bundle_num);
                Rtitle.setText(title);
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

        Bundle arguments = this.getArguments();
        Bundle_num = arguments.getString("num");//글번호!!!!

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

        textView = (TextView) syView.findViewById(R.id.textView);

        Rnum = (TextView) syView.findViewById(R.id.Rnum);;
        Rtitle = (TextView) syView.findViewById(R.id.Rtitle);
        Rcontent = (TextView) syView.findViewById(R.id.Rcontent);
        //textView.setText(num);
        btn_move_W = (Button)syView.findViewById(R.id.btn_move_W);
        applyBtn = (Button)syView.findViewById(R.id.applyBtn);
        btn_move_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecruitActivity.class);
                startActivity(intent);
            }
        });
    }
    public void applyBtn(View v){
//        Intent intent = new Intent(getActivity(), MentoringActivity.class);
//        startActivity(intent);
        Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();

    }
}