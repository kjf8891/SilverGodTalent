package com.example.testremote;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by seyeon on 2017-10-30.
 */

public class WantedActivity extends AppCompatActivity implements android.location.LocationListener {

    LocationManager locationManager;


    ListView listView;
    Toolbar toolbar;
    TextView textView;
    TextView title;

    TextView tv_location;

    //ArrayList<MGroup> items;
    ArrayList<NoticeWanted> items;
    //MGroupAdapter MGroupAdapter;
    WantedAdapter WantedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted);
        init();



    }

    void init(){
        textView = (TextView)findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listview);
        tv_location = (TextView)findViewById(R.id.tv_location);

        items = new ArrayList<NoticeWanted>();
        items.add(new NoticeWanted("1","userID1","Content1","Title1","2017-03-03"));
        items.add(new NoticeWanted("2","userID2","content2","Special Dancing Practice","2017-12-02"));
        items.add(new NoticeWanted("3","한자를 공부합시다","2017-10-23","금천구","2017-12-02"));
        items.add(new NoticeWanted("4","노래! 어렵지 않아요~","2017-08-31","강남구","2017-12-02"));
        items.add(new NoticeWanted("5","다함께 배드민턴","2017-09-03","광진구","2017-12-02"));
        items.add(new NoticeWanted("6","빠르게 배우는 바둑교실","2017-11-22","성북구","2017-12-02"));
        WantedAdapter = new WantedAdapter(getApplicationContext(),R.layout.row_wanted,items);
        listView.setAdapter(WantedAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeWanted item = (NoticeWanted) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getnoticeWantedNum().toString(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle(1);
                bundle.putString("num",item.getnoticeWantedNum().toString());

                DetailWantedFragment fragment = new DetailWantedFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,fragment)
                        .commit();

            }
        });
    }

    public void addWantedNoticeBtn(View v){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout,new AddWantedFragment())
                .commit();
    }

    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
//
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        Toast.makeText(this, "Current Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//        Log.e("onLocationChanged", "onLocationChanged");
//
//        Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
//        try {
//            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();
//
//            //Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();
//            tv_location.setText(address);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
            // ActivityCompat.requestPermissions(WantedActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        }
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

            //Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();
            tv_location.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }
