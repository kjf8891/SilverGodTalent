package com.example.testremote;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.text.TextUtils.isEmpty;

/**
 * Created by seyeon on 2017-10-30.
 */

public class RecruitActivity extends AppCompatActivity implements android.location.LocationListener {

    LocationManager locationManager;

    ImageButton searchBtn;
    TextView tv_location;

    ListView listView;
    Toolbar toolbar;
    TextView textView;
    TextView title;
    LatLng cur_loc;

    String selectedArea;


    //ArrayList<MGroup> items;
    ArrayList<Recruit> items;
    ArrayList<Recruit> sortedItems;
    ArrayList<Recruit> tmpItems;
    ArrayList<Recruit> OriginItems;

    //MGroupAdapter MGroupAdapter;
    RecruitAdapter RecruitAdapter;
    JSONArray retJson;

    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    //CustomAdapter dataAdapter;
    ArrayList<String> list;
    SharedPreferences recruit_pref;


    //디비에서 데이터 불러오기, 구인 공고 목록
    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {
            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;
            for(int i = 0 ; i< retJson.length(); i++) {

                JSONArray jsonarray = retJson.getJSONArray(i);

                for (int j = 0; j < jsonarray.length(); j++) {

                    JSONObject json = jsonarray.getJSONObject(j);

                    if (json.has("RTitle")) {

                        String title = json.getString("RTitle");
                        String num = json.getString("RNum");
                        String date = json.getString("date");
                        String city = json.getString("city");
                        //위도 경도 여기 있음
                        String latitude = json.getString("latitude");
                        String longitude = json.getString("longitude");
                        String area = json.getString("interest");
                        //String date = json.getString("date");
                        //String location = json.getString("location");
                        //items.add(new MGroup(title,date,location));

                        items.add(new Recruit(num, title, date, city, latitude, longitude, area));
                        Log.d("RecruitA",title);

                    } else {
                        String area = json.getString("area");


                        list.add(area);
                    }
                    //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
                }
            }

            OriginItems.clear();
            OriginItems.addAll(items);
            Log.e("originItems size", ""+OriginItems.size());
            Log.e("originItems size", ""+items.size());

            RecruitAdapter.notifyDataSetChanged();
            dataAdapter.notifyDataSetChanged();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);
        init();
    }
    void init(){
        recruit_pref = getSharedPreferences("Chat2",0);

        if(isEmpty(recruit_pref.getString("recruit_first",""))){
            Toast.makeText(this, "처음이야", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),RecruitTutoActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = recruit_pref.edit();
            editor.putString("recruit_first","1");
            editor.commit();
        }else{
            Toast.makeText(getApplicationContext(), "처음아니야", Toast.LENGTH_SHORT).show();
        }

        textView = (TextView)findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listview);
        tv_location = (TextView)findViewById(R.id.tv_location);

        items = new ArrayList<Recruit>();
        OriginItems = new ArrayList<>();
        tmpItems = new ArrayList<>();

        RecruitAdapter = new RecruitAdapter(getApplicationContext(),R.layout.row_recruit,items);
        listView.setAdapter(RecruitAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recruit item = (Recruit) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getnoticeRecruitNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getnoticeRecruitNum().toString());

                DetailRecruitFragment fragment = new DetailRecruitFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout,fragment)
                        .commit();

            }
        });

        String url = "http://13.124.85.122:52273/getRList";
        //초기화

        JSONObject tmp =new JSONObject();
        RequestForm req = new RequestForm(url);
        dwTask.execute(req);


        RecruitAdapter.notifyDataSetChanged();
        Log.e("itemsitems size", ""+items.size());



        //Image spinner
//        searchBtn = (ImageButton)findViewById(R.id.searchBtn);
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                spinner.performClick();
//            }
//        });
        spinner = (Spinner)findViewById(R.id.spinner);
        list = new ArrayList<String>();
        //list.add("     ");
        list.add("All");
        int hidingItemIndex = 0;
        //dataAdapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_spinner_item, list);

        dataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.row_spinner, list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                view.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(), "aaaaa"+position, Toast.LENGTH_SHORT).show();
                selectedArea = list.get(position).toString();

                //Toast.makeText(getApplicationContext(),"fuck",Toast.LENGTH_SHORT).show();
               showInterestedItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void addRecruitNoticeBtn(View v){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout,new AddRecruitFragment())
                .commit();
    }

    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        //startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getLocation(View view) {
        RecruitAdapter.notifyDataSetChanged();
        Log.e("getLocation size", ""+items.size());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(RecruitActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        } else {
//        locationManager.requestLocationUpdates(GPS_PROVIDER, 5000, 100, this);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, this);
            locationManager.requestSingleUpdate(GPS_PROVIDER, this, null);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());


            Location location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                cur_loc = new LatLng(location.getLatitude(),location.getLongitude());

                //Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();
                tv_location.setText(address);

                sortByDistance();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    void sortByDistance(){

        sortedItems = new ArrayList<>();
        sortedItems.clear();
        sortedItems.addAll(OriginItems);


        Log.e("size",items.size()+"");
        double[] distances = new double[items.size()];

        double[] tmp;

        Location startPoint = new Location("start");
        startPoint.setLatitude(cur_loc.latitude);
        startPoint.setLongitude(cur_loc.longitude);

        Location endPoint = new Location("end");


        for(int i = 0 ; i <OriginItems.size() ; i++){

            endPoint.setLatitude(OriginItems.get(i).getLatLng().latitude);
            endPoint.setLongitude(OriginItems.get(i).getLatLng().longitude);

            distances[i] = startPoint.distanceTo(endPoint);
            Log.e("distances",i+" : "+distances[i]);

        }


//        quickSort(distances,0,it

        quicksort(distances, 0, distances.length-1);

        for(int i = 0 ; i <sortedItems.size() ; i++){

            Log.e("distances",i+" : "+distances[i]);

        }


        //tmpItems = items;
        items.clear();
        items.addAll(sortedItems);



        RecruitAdapter.notifyDataSetChanged();

    }

    void quicksort(double[]a, int lo, int hi) {
        if (lo < hi) {
            int q = hoare_partition(a, lo, hi);
            quicksort(a, lo, q);
            quicksort(a, q + 1, hi);
        }
    }

    int hoare_partition(double[] a, int lo, int hi) {

        double pivot = a[lo];
        int i = lo;
        int j = hi;

        while (true) {

            while (a[i] < pivot) i++;

            while (a[j] > pivot) j--;

            if (i >= j) {
                return j;
            }
            swap(a, i, j);
            i++;
            j--;

        }
    }

    void swap(double[] a, int i, int j) {
        double temp = a[i];
        a[i] = a[j];
        a[j] = temp;

        Recruit rTmp = sortedItems.get(i);
        sortedItems.set(i,sortedItems.get(j));
        sortedItems.set(j,rTmp);
    }


    void showInterestedItems(){

        RecruitAdapter.notifyDataSetChanged();
        Log.e("showInterested", ""+items.size());

        items.addAll(OriginItems);
        RecruitAdapter.notifyDataSetChanged();
        if(selectedArea.equals("     ")){
            items.clear();
            items.addAll(OriginItems);
            RecruitAdapter.notifyDataSetChanged();
        }
        if(selectedArea.equals("All")){

            items.clear();
            items.addAll(OriginItems);
            RecruitAdapter.notifyDataSetChanged();

        }else {

            tmpItems.clear();

            for (int i = 0; i < OriginItems.size(); i++) {

                if (OriginItems.get(i).getnoticeInterest().equals(selectedArea)) {

                    tmpItems.add(OriginItems.get(i));
                    Log.e("interested", OriginItems.get(i).getnoticeRecruitTitle());

                }

            }
            // OriginItems = items;
            //   items = new ArrayList<>();
            //  RecruitAdapter.updateData(tmpItems);
            items.clear();
            items.addAll(tmpItems);
            RecruitAdapter.notifyDataSetChanged();
        }
    }
}
