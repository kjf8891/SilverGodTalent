package com.example.testremote;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
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

/**
 * Created by seyeon on 2017-10-30.
 */

public class RecruitActivity extends AppCompatActivity implements android.location.LocationListener {

    LocationManager locationManager;

    TextView tv_location;

    ListView listView;
    Toolbar toolbar;
    TextView textView;
    TextView title;
    LatLng cur_loc;

    //ArrayList<MGroup> items;
    ArrayList<Recruit> items;
    ArrayList<Recruit> sortedItems;
    //MGroupAdapter MGroupAdapter;
    RecruitAdapter RecruitAdapter;
    JSONArray retJson;


    //디비에서 데이터 불러오기, 구인 공고 목록
    DownloadWebPageTask dwTask = new DownloadWebPageTask(new DownloadWebPageTask.AsyncResponse() {
        @Override
        public void processFinish(JSONArray ret) throws JSONException {
            Log.e("err","processFinish");
            //Toast.makeText(getApplicationContext(),"processFinish:",Toast.LENGTH_SHORT).show();
            retJson = ret;
            for(int i = 0 ; i< retJson.length(); i++){
                JSONObject json = retJson.getJSONObject(i);
                String title = json.getString("RTitle");
                String num = json.getString("RNum");
                String date = json.getString("date");
                String city = json.getString("city");
                //위도 경도 여기 있음
                String latitude = json.getString("latitude");
                String longitude = json.getString("longitude");
                //String date = json.getString("date");
                //String location = json.getString("location");
                //items.add(new MGroup(title,date,location));
                items.add(new Recruit(num,title,date,city,latitude,longitude));
                //   Toast.makeText(getApplicationContext(),"되나:"+area,Toast.LENGTH_SHORT).show();
            }
            RecruitAdapter.notifyDataSetChanged();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);
        init();
    }

    void init(){
        textView = (TextView)findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listview);
        tv_location = (TextView)findViewById(R.id.tv_location);

        items = new ArrayList<Recruit>();
        //items.add(new Recruit("1","userID1","Content1","Title1","2017-03-03"));
//        items.add(new Recruit("2","userID2","content2","Special Dancing Practice","2017-12-02"));
//        items.add(new Recruit("3","한자를 공부합시다","2017-10-23","금천구","2017-12-02"));
//        items.add(new Recruit("4","노래! 어렵지 않아요~","2017-08-31","강남구","2017-12-02"));
//        items.add(new Recruit("5","다함께 배드민턴","2017-09-03","광진구","2017-12-02"));
//        items.add(new Recruit("6","빠르게 배우는 바둑교실","2017-11-22","성북구","2017-12-02"));
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

       sortedItems = items;

        double[] distances = new double[items.size()];

        double[] tmp;

        Location startPoint = new Location("start");
        startPoint.setLatitude(cur_loc.latitude);
        startPoint.setLongitude(cur_loc.longitude);

        Location endPoint = new Location("end");


        for(int i = 0 ; i <items.size() ; i++){

            endPoint.setLatitude(items.get(i).getLatLng().latitude);
            endPoint.setLongitude(items.get(i).getLatLng().longitude);

            distances[i] = startPoint.distanceTo(endPoint);
            Log.e("distances",i+" : "+distances[i]);

        }


//        quickSort(distances,0,it

        quicksort(distances, 0, distances.length-1);

        for(int i = 0 ; i <items.size() ; i++){

            Log.e("distances",i+" : "+distances[i]);

        }
        items = sortedItems;

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


//    public int partition(double arr[], int left, int right) {
//
//        double pivot = arr[(left + right) / 2];
//
//        while (left <= right) {
//            Log.e("while","dd");
//
//            while ((arr[left] <= pivot) && (left < right)) {
//                left++;
//                Log.e("left","dd");
//
//            }
//            while ((arr[right] > pivot) && (left < right)) {
//                right--;
//                Log.e("right","dd");
//
//            }
//            if (left < right) {
//                double temp = arr[left];
//                arr[left] = arr[right];
//                arr[right] = temp;
//
////                Recruit rTmp = sortedItems.get(left);
////                sortedItems.set(left,sortedItems.get(right));
////                sortedItems.set(right,rTmp);
//            }
//        }
//
//        return left;
//    }
//
//    public void quickSort(double arr[], int left, int right) {
//
//        if (left < right) {
//            int pivotNewIndex = partition(arr, left, right);
//
//            Log.e("pre","dd");
//            quickSort(arr, left, pivotNewIndex - 1);
//            Log.e("middle","dd");
//
//            quickSort(arr, pivotNewIndex + 1, right);
//            Log.e("end","dd");
//
//        }
//
//
//    }

}
