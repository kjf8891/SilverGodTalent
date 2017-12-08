package com.example.testremote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InterestActivity extends AppCompatActivity {

    String tmp;
    JSONArray retJson;
    //ArrayList<InterestData> interests;
    String url;
    RequestForm req;
    ArrayList<String> checkedInterest;

    ArrayList<Boolean> isChecked;

    JSONObject userinfo;
    SharedPreferences prefs;
    List<NameValuePair> params;

    private RecyclerView mRecyclerView;
    private InterestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InterestData> myDataset;


    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {

            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;





            for(int i = 0 ; i< retJson.length(); i++){

                JSONObject json = retJson.getJSONObject(i);

                String area = json.getString("area");

                isChecked.add(false);
                myDataset.add(new InterestData(area));

                //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
            }

            mAdapter.notifyDataSetChanged();

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);


        try {
            userinfo = new JSONObject(getIntent().getStringExtra("userinfo")); //signin 액티비티에서 데이터 받아옴

            Log.e("UserInfo", "Passed UserInfo: " + userinfo.getString("Nickname"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        init();

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new InterestAdapter(myDataset, new InterestAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {


                Toast.makeText(getApplicationContext(),String.valueOf(isChecked.get(position)),Toast.LENGTH_SHORT);

                if(!isChecked.get(position))
                {
                    checkedInterest.add(myDataset.get(position).area.toString());
                    isChecked.set(position,true);
                    Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"add",Toast.LENGTH_SHORT).show();

                }else{
                    checkedInterest.remove(myDataset.get(position).area.toString());
                    isChecked.set(position,false);
                    Toast.makeText(getApplicationContext(),""+myDataset.get(position).area.toString()+"delete",Toast.LENGTH_SHORT).show();

                }

                Log.e("interests",position+checkedInterest.toString());

            }
        });
        mRecyclerView.setAdapter(mAdapter);


    }

    private class Login extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
           // Log.d("loglogloglog",args[0]);

//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putString("REG_FROM", id.getText().toString());
//            edit.putString("FROM_NAME", pw.getText().toString());
//            edit.commit();

            params.add(new BasicNameValuePair("name", prefs.getString("FROM_NAME","")));//pw
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));//아이디
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));//토큰

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/login",params);
            return jObj;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            // progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Sucessfully Registered")) {
//                    Fragment reg = new UserFragment();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, reg);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.addToBackStack(null);
//                    ft.commit();
                    startActivity(new Intent(getApplicationContext(),UserActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    Button complete;



    void init(){

        myDataset = new ArrayList<>();
        checkedInterest = new ArrayList<>();
        isChecked = new ArrayList<>();

        url = "http://13.124.85.122:52273/getInterest";
        req = new RequestForm(url);


        dwTask.execute(req);

        complete = (Button)findViewById(R.id.complete);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray interests = new JSONArray();

                for(int i = 0 ; i < checkedInterest.size();i++){

                    JSONObject iObject = new JSONObject();

                    try {
                        iObject.put("checkedArea",checkedInterest.get(i));
                        interests.put(iObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    userinfo.put("interests",interests);
                    Log.e("jsoninput",userinfo.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                InsertDataTask insertTask = new InsertDataTask();

                url = "http://13.124.85.122:52273/pushData";
                req = new RequestForm(url,userinfo);


                insertTask.execute(req);

                new InterestActivity.Login().execute();
                Log.e("userinfo 전송","userinfo 전송");
            }
        });







    }


    public void selInterest(View v){

        if(((ToggleButton)v).isChecked())
            Toast.makeText(this,v.getId()+ "checked",Toast.LENGTH_SHORT ).show();
        else
            Toast.makeText(this,v.getId()+ "not checked",Toast.LENGTH_SHORT ).show();

        //배열같은곳에 누를때마다 getText해서 뭘 골랐는지 넣어두고
        //선택완료 버튼을 누를때 디비에 넣으면 될것.
    }


}


