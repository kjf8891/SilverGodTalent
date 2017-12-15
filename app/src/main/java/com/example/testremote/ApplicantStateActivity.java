package com.example.testremote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by seyeon on 2017-10-30.
 */

public class ApplicantStateActivity extends AppCompatActivity {

    ArrayList<User> items;
    ListView listView;
    UserAdapter UserAdapter;
    Button apprvBtn;
    JSONArray retJson;
    String passedRNum="1";
    Bundle bundle;

    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            retJson = ret;

            if(retJson.length()>0){
                for(int i = 0 ; i< retJson.length(); i++){
                    JSONObject json = retJson.getJSONObject(i);
                    String id = json.getString("id");
                    String name = json.getString("Name");
                    int RNum = json.getInt("RNum");
                    int approval = json.getInt("approval");
                    String num = String.valueOf(RNum);
                    Toast.makeText(ApplicantStateActivity.this, "id"+id, Toast.LENGTH_SHORT).show();
                    if(approval==0){
                        items.add(new User(num,id,name));
                    }
                }
            }else {
                Toast.makeText(ApplicantStateActivity.this, "length00000", Toast.LENGTH_SHORT).show();
            }
            UserAdapter.notifyDataSetChanged();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant);
        init();
    }

    void init(){
        items = new ArrayList<User>();
        listView = (ListView)findViewById(R.id.listview_user);
        apprvBtn = (Button)findViewById(R.id.apprvBtn);
        UserAdapter = new UserAdapter(getApplicationContext(),R.layout.row_user,items);
        listView.setAdapter(UserAdapter);

        if(getIntent().getBundleExtra("RApplyReq") != null){
            Log.d("ggg","RApplyReq");

            bundle = getIntent().getBundleExtra("RApplyReq");
            passedRNum = bundle.getString("Num");
            String title = bundle.getString("title");

            Log.d("ggggg",passedRNum);

            String url = "http://13.124.85.122:52273/findRMInfo";
            //초기화
            JSONObject jsonObject =new JSONObject();
            int tmptmp = Integer.parseInt(passedRNum);
            try {
                jsonObject.put("num",tmptmp);
                RequestForm req = new RequestForm(url,jsonObject);
                dwTask.execute(req);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
//    public void onBackPressed() {
//        //super.onBackPressed();
//        int count = getFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }
}
