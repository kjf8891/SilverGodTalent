package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-11-17.
 */

public class AddRecruitFragment extends Fragment {
    View syView;
    Button completeBtn;
    EditText title;
    EditText content;
    EditText date;
    EditText total_num;
    EditText city;
    EditText ins_name;
    //EditText area;
    //TextView date;
    TextView id;
    TextView textView;

    String selectedArea;

    JSONObject jsonObject;
    JSONArray retJson;

    //스피너
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    ArrayList<String> list;

    //사용자 아이디 불러오기
    SharedPreferences prefs;

    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {
            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;
            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                String area = json.getString("area");
                list.add(area);
                Toast.makeText(getActivity(),"되나:"+area,Toast.LENGTH_SHORT).show();
            }
            dataAdapter.notifyDataSetChanged();
        }
    });


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_add_recruit,container,false);
        init();
        return syView;
    }
    public void init(){
        //사용자 아이디 불러오기
        prefs = getActivity().getSharedPreferences("Chat", 0);

        textView = (TextView)syView.findViewById(R.id.textView);
        completeBtn = (Button)syView.findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                Intent intent = new Intent(getActivity(), RecruitActivity.class);
                startActivity(intent);
            }
        });
        title = (EditText)syView.findViewById(R.id.edit_title);
        content = (EditText)syView.findViewById(R.id.edit_content);
        date = (EditText)syView.findViewById(R.id.edit_date);
        total_num = (EditText)syView.findViewById(R.id.RTotalNum);
        city = (EditText)syView.findViewById(R.id.RCity);
        ins_name = (EditText)syView.findViewById(R.id.RInstitution);
        id = (TextView)syView.findViewById(R.id.recruit_writer_id);
        String tmp_id11 = prefs.getString("REG_FROM","");
        id.setText(tmp_id11);
        //area = (EditText)syView.findViewById(R.id.RArea);
        //textView.setText(date.getText().toString());
        //TextView date = (TextView)syView.findViewById(R.id.date);//날짜는 자동으로 받아오기.... 어떻게?

        String url = "http://13.124.85.122:52273/getIList";
        //초기화

        JSONObject tmp1 =new JSONObject();
        RequestForm req = new RequestForm(url);
        dwTask.execute(req);

        //스피너 코드
        spinner = (Spinner)syView.findViewById(R.id.spinner);
        list = new ArrayList<String>();
        //dataAdapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_spinner_item, list);
        dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.row_spinner, list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "aaaaa"+position, Toast.LENGTH_SHORT).show();
                selectedArea = list.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
            jsonObject.put("content",content.getText().toString());
            jsonObject.put("date",date.getText().toString());
            //jsonObject.put("area",area.getText().toString());
            jsonObject.put("area",selectedArea);
            jsonObject.put("total_num",total_num.getText().toString());
            jsonObject.put("city",city.getText().toString());
            jsonObject.put("ins_name",ins_name.getText().toString());
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