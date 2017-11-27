package com.example.testremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class StartActivity extends AppCompatActivity {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "904205865931";
    String authorizedEntity = "904205865931"; // Project id from Google Developer Console
    String scope = "GCM";

    static final String TAG = "L2C";

    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;

    Bundle bundle;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        intent = new Intent(getApplicationContext(), LoginActivity2.class);

        FirebaseMessaging.getInstance().subscribeToTopic("notice");

        prefs = getSharedPreferences("Chat", 0);
        context = getApplicationContext();

//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putString("REG_ID", FirebaseInstanceId.getInstance().getToken());
//        edit.commit();

        bundle = getIntent().getBundleExtra("random_number");   //인증번호 번들
        if(bundle!= null){
            if(bundle.getString("random_number") != null){
                Bundle args = new Bundle();
                args.putString("random_number", bundle.getString("random_number"));
                args.putString("needer_id", bundle.getString("needer_id"));

                intent = new Intent(this, LoginActivity2.class);
                intent.putExtra("random_number",args);
                //startActivity(login);
            }
        }else {

            if (!prefs.getString("REG_FROM", "").isEmpty()) {
//                Fragment user = new UserFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, user);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
                intent = new Intent(getApplicationContext(), MainMenuActivity.class );
                //startActivity(intent);

                Log.d("Main_IF:REG_FROM",prefs.getString("REG_FROM", ""));
                startActivity(new Intent(this,UserActivity.class));
            } else if (!prefs.getString("REG_ID", "").isEmpty()) {
//            Fragment reg = new LoginFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, reg);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
                Log.d("Main_IF:REG_ID",prefs.getString("REG_ID", ""));
                intent = new Intent(this, LoginActivity2.class);
                //startActivity(new Intent(this, LoginActivity.class));
            } else if (checkPlayServices()) {
                Log.d("Main_IF:Register","Register");
                new StartActivity.Register().execute();
                //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
            }
        }



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
//                startActivity(intent);


                startActivity(intent);

            }
        }, 2000);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private class Register extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

//                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(context);
//                    //regid = gcm.register(SENDER_ID);
//
//
//                    regid = InstanceID.getInstance(context).getToken(authorizedEntity,scope);
//                    Log.e("RegId",regid);
//
//                    SharedPreferences.Editor edit = prefs.edit();
//                    edit.putString("REG_ID", regid);
//                    edit.commit();
//
//                }

            FirebaseInstanceIDService t = new FirebaseInstanceIDService();

            regid = FirebaseInstanceId.getInstance().getToken();


            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("REG_ID", t.token);
            edit.commit();

            Log.d("gdgdgdgdgd","gdgdgdgdgdg");
            //Log.d("ggdgdgdgdgdg", t.token);
            Log.d("reg_id_register",prefs.getString("REG_ID",""));
            return  regid;


        }
        @Override
        protected void onPostExecute(String json) {
//            Fragment reg = new LoginFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, reg);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
            //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }
}
