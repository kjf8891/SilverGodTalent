package com.example.testremote;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by 혜진 on 2017-11-13.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIDService";
    String token;

    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences prefs;
        prefs = getSharedPreferences("Chat", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("REG_ID", token);
        edit.commit();

        Log.d("tokentokend" , token);
       // Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();
    }
}
