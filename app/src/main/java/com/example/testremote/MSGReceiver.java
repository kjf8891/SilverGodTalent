package com.example.testremote;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import static com.example.testremote.BuildConfig.DEBUG;

public class MSGReceiver  extends WakefulBroadcastReceiver {

    private int mBindFlag;

    private Messenger mServiceMessenger;

    boolean isCreatedOnReceiver = false;

   // private MSGReceiver msgReceiver;


    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            if (DEBUG) {Log.e("TAG", "onServiceConnected");} //$NON-NLS-1$

            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
            msg.what = MyService.MSG_RECOGNIZER_START_LISTENING;

            try
            {
                mServiceMessenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            if (DEBUG) {Log.e("TAG", "onServiceDisconnected");} //$NON-NLS-1$
            mServiceMessenger = null;
        }

    }; // mServiceConnection


    @Override
    public void onReceive(Context context, Intent intent) {



        SharedPreferences settings = context.getSharedPreferences("settings",0);
        boolean isRecogChecked = settings.getBoolean("isRecogChecked",false);


        Log.e("000 RestartService" , "RestartService called : " + intent.getAction());

        /**
         * 서비스 죽일때 알람으로 다시 서비스 등록
         */
        if(intent.getAction().equals("ACTION.RESTART.MyService")){

            Log.i("000 RestartService" ,"ACTION.RESTART.MyService " + String.valueOf(isRecogChecked) );

            Intent i = new Intent(context,MyService.class);
            context.startService(i);
        }
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)&&isRecogChecked){

            Log.e("000 RestartService" ,"ON ACTION.RESTART.MyService " + String.valueOf(isRecogChecked));

            // = new MSGReceiver();
            Intent service = new Intent(context, MyService.class);


            IntentFilter intentFilter = new IntentFilter("com.example.testremote.MyService");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            context.registerReceiver(this,intentFilter);

            isCreatedOnReceiver = true;

            Intent i = new Intent(context,MyService.class);

            context.startService(i);


            mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
            context.bindService(new Intent(context, MyService.class), mServiceConnection, mBindFlag);


        }
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)&&isRecogChecked){

            Log.e("000 RestartService" ,"OFF ACTION.END.MyService " );

            Intent i = new Intent(context,MyService.class);

            if(isCreatedOnReceiver) {
                context.unbindService(mServiceConnection);
                isCreatedOnReceiver = false;
                context.unregisterReceiver(this);

            }

            context.stopService(i);

        }

        /**
         * 폰 재시작 할때 서비스 등록
         */
//        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
//
//            Log.i("MsgReceiver" , "ACTION_BOOT_COMPLETED" );
//            Intent i = new Intent(context,MyService.class);
//            context.startService(i);
//
//        }


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
        }else if(extras.getString("mypagelistclick") != null){
            Log.d("mypagelistclick", "mypagelistclick");

            Intent msgrcv = new Intent("mypagelistclick");

            msgrcv.putExtra("now_activity",extras.getString("now_activity"));
            msgrcv.putExtra("activity_change", extras.getString("activity_change"));
            msgrcv.putExtra("sender_id", extras.getString("sender_id"));

            msgrcv.putExtra("position", extras.getString("position"));
            msgrcv.putExtra("mypage_type", extras.getString("mypage_type"));



            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("activity_change") != null){
            Log.d("activity_changeeeeeee", "activity_change");

            Intent msgrcv = new Intent("activity_change");

            msgrcv.putExtra("now_activity",extras.getString("now_activity"));
            msgrcv.putExtra("activity_change", extras.getString("activity_change"));
            msgrcv.putExtra("sender_id", extras.getString("sender_id"));
            //msgrcv.putExtra("pw_edittext", extras.getString("pw_edittext"));
            //msgrcv.putExtra("fromname", extras.getString("pw_edittext"));



            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("RApplyReq") != null){   //채팅
            Intent msgrcv = new Intent("RApplyReq");
            msgrcv.putExtra("RApplyReq", extras.getString("RApplyReq"));
            msgrcv.putExtra("id", extras.getString("id"));
            msgrcv.putExtra("Num", extras.getString("Num"));
            msgrcv.putExtra("title", extras.getString("title"));

            Log.d("reme1" , extras.getString("Num"));


            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

            ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("nickname") != null){     //SignInEditSend
            Log.d("SignInEditSend", "SignInEditSend");

            Intent msgrcv = new Intent("SignInEditSend");

            msgrcv.putExtra("now_activity",extras.getString("now_activity"));
            msgrcv.putExtra("nickname", extras.getString("nickname"));
            msgrcv.putExtra("pw", extras.getString("pw"));
            msgrcv.putExtra("name", extras.getString("name"));
            msgrcv.putExtra("sender_id", extras.getString("sender_id"));
            //msgrcv.putExtra("pw_edittext", extras.getString("pw_edittext"));
            //msgrcv.putExtra("fromname", extras.getString("pw_edittext"));



            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("interest_position") != null){
            Log.d("InterestCheckSend", "InterestCheckSend");

            Intent msgrcv = new Intent("InterestCheckSend");

            msgrcv.putExtra("now_activity",extras.getString("now_activity"));
            msgrcv.putExtra("interest_position", extras.getString("interest_position")); //포지션
            msgrcv.putExtra("interest_clicked", extras.getString("interest_clicked")); //포지션
            msgrcv.putExtra("sender_id", extras.getString("sender_id"));

            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            setResultCode(Activity.RESULT_OK);
        }else if(extras.getString("mypage_type") != null){
            if(extras.getString("mypage_type").equals("Club")){

                Intent msgrcv = new Intent("Notice_Club");
                msgrcv.putExtra("content", extras.getString("content"));
                msgrcv.putExtra("from_sender_Nickname", extras.getString("from_sender_Nickname"));
                msgrcv.putExtra("from_sender_id", extras.getString("from_sender_id"));
                msgrcv.putExtra("date", extras.getString("date"));
                msgrcv.putExtra("Num", extras.getString("Num"));
                msgrcv.putExtra("mypage_type", extras.getString("mypage_type"));

                Log.d("reme1" ,  extras.getString("from_sender_Nickname"));


                LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

                ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
                setResultCode(Activity.RESULT_OK);



            }else  if(extras.getString("mypage_type").equals("Recruit")){

                Intent msgrcv = new Intent("Notice_Recruit");
                msgrcv.putExtra("content", extras.getString("content"));
                msgrcv.putExtra("from_sender_Nickname", extras.getString("from_sender_Nickname"));
                msgrcv.putExtra("from_sender_id", extras.getString("from_sender_id"));
                msgrcv.putExtra("date", extras.getString("date"));
                msgrcv.putExtra("Num", extras.getString("Num"));
                msgrcv.putExtra("mypage_type", extras.getString("mypage_type"));

                Log.d("reme1" ,  extras.getString("from_sender_Nickname"));


                LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

                ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
                setResultCode(Activity.RESULT_OK);



            }
        }
        else if(extras.getString("fromu") != null){   //채팅
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