package com.example.testremote;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class MSGReceiver  extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("helper_random_recive2", "helper_random_reiceve");
        Bundle extras = intent.getExtras();
        if(extras.getString("type") != null){       //젊은이가 인증번호 받는 곳


            Intent msgrcv = new Intent("HELPER_NUMBER");
            msgrcv.putExtra("random_number", extras.getString("random_number"));

            Log.d("helper_random_recive3", "helper_random_reiceve");

            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("Helper_authorization") != null) {     //어르신이 인증 ok를 받았을 때

            Log.d("Helper_authorization", "Helper_authorization");

            Intent msgrcv = new Intent("HELPER_AUTHORIZATION");
            msgrcv.putExtra("Helper_authorization", extras.getString("Helper_authorization"));
            msgrcv.putExtra("helper_id", extras.getString("helper_id"));


            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            ComponentName comp = new ComponentName(context.getPackageName(), MSGService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);

        }else if(extras.getString("screeninfo") != null){
            Log.d("screen_info", "screeninfo");

            Intent msgrcv = new Intent("screeninfo");

            msgrcv.putExtra("screeninfo", extras.getString("screeninfo"));
            msgrcv.putExtra("mobno_edittext", extras.getString("mobno_edittext"));
            msgrcv.putExtra("pw_edittext", extras.getString("pw_edittext"));
            //msgrcv.putExtra("fromname", extras.getString("pw_edittext"));



            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("activity_change") != null){
            Log.d("activity_changeeeeeee", "activity_change");

            Intent msgrcv = new Intent("activity_change");

            msgrcv.putExtra("activity_change", extras.getString("activity_change"));
            msgrcv.putExtra("sender_id", extras.getString("sender_id"));
            //msgrcv.putExtra("pw_edittext", extras.getString("pw_edittext"));
            //msgrcv.putExtra("fromname", extras.getString("pw_edittext"));



            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            setResultCode(Activity.RESULT_OK);
        }
        else{   //채팅
            Intent msgrcv = new Intent("Msg");
            msgrcv.putExtra("msg", extras.getString("msg"));
            msgrcv.putExtra("fromu", extras.getString("fromu"));
            msgrcv.putExtra("fromname", extras.getString("name"));

            Log.d("reme1" , extras.getString("msg"));


            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

            ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
    }
}