package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seyeon on 2017-11-17.
 */

public class DetailRecruitFragment extends Fragment implements OnMapReadyCallback {
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
    String latitude ;
    String longitude;
    String institution_id;
    List<NameValuePair> params;

    private GoogleMap mMap; // 구글맵

    private MapView mapView = null;

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
                latitude = json.getString("latitude");
                longitude = json.getString("longitude");
                institution_id = json.getString("institution_id");

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

        mapView = (MapView)syView.findViewById(R.id.map);
        mapView.getMapAsync(this);

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

        new DetailRecruitFragment.RApplyReq().execute(prefs.getString("REG_FROM",""),Bundle_num,title);

    }

    private class RApplyReq extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            Log.d("loglogloglog",args[0]);

                params.add(new BasicNameValuePair("id", args[0]));
                params.add(new BasicNameValuePair("Num", args[1]));
            params.add(new BasicNameValuePair("title", args[2]));

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/RApplyReq",params);
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // progress.dismiss();
//            try {
//                String res = json.getString("response");
//                if(res.equals("Success")) {
//
//                    // startActivity(new Intent(getApplicationContext(),UserActivity.class));
//                }else{
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(institution);
        markerOptions.snippet(institution);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(25));



    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView != null){

            mapView.onCreate(savedInstanceState);
        }
    }
}