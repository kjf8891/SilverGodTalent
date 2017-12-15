package com.example.testremote;


import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MSGService extends IntentService {

    SharedPreferences prefs;
    NotificationCompat.Builder notification;
    NotificationManager manager;
    List<NameValuePair> params;

    public MSGService() {
        super("MSGService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        FirebaseMessaging.getInstance();
        FirebaseInstanceId.getInstance();

        //FirebaseInstanceId fcm = FirebaseInstanceId.getInstance();

        Log.d("reme2" , "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        String messageType = gcm.getMessageType(intent);
        prefs = getSharedPreferences("Chat", 0);


        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("L2C","Error");

            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("L2C","Error");

            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                if(extras.getString("type") != null){   //젊은이가 인증번호 받는 곳
                    Log.d("MSGService : 인증번호받는곳",extras.getString("needer_id"));
                    sendNotification(extras.getString("random_number"),extras.getString("needer_id") ); //인증번호와 어르신 토큰

                }else if(extras.getString("Helper_authorization") != null){
                    Log.d("HELP_FLAG","HELP_FLAG");

                    //젊은이가 인증 완료했다는 메시지를 받으면 젊은이와 연결
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("HELP_FLAG", "1");
                    edit.putString("helper_id", extras.getString("helper_id"));
                    edit.commit();


                    ((LoginActivity2)LoginActivity2.loginContext).NeederScreenInfoSend();
                    Log.d("getclassname",GetClassName(getApplicationContext()));
                    Log.d("helper_id",extras.getString("helper_id"));

                    NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext()).setOngoing(true);
                    builder.setSmallIcon(R.drawable.cast_ic_notification_play);
                    builder.setTicker("Sample");
                    builder.setNumber(10);
                    builder.setContentTitle("Title");
                    builder.setContentText("");

                    Notification noti = builder.build();
                    Intent intent2 = new Intent("remote_noti");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);

                    RemoteViews contentview = new RemoteViews(getPackageName(),R.layout.notification_activity);
                    contentview.setOnClickPendingIntent(R.id.button,pendingIntent);
                    noti.contentView = contentview;
                    nm.notify(1,noti);

                }else if(extras.getString("from_sender_Nickname") != null){
                    Log.d("MSGServif Nickname",extras.getString("from_sender_Nickname"));
                    sendNotification(extras.getString("content"),extras.getString("mypage_type"),extras.getString("from_sender_Nickname") ,extras.getString("date"),extras.getString("Num")); //인증번호와 어르신 토큰

                }else if(extras.getString("RApplyReq") != null){
                    Log.d("MSGServif Nickname",extras.getString("title"));
                    sendNotification(extras.getString("id"),extras.getString("mypage_type"),extras.getString("from_sender_Nickname") ,extras.getString("date"),extras.getString("Num")); //인증번호와 어르신 토큰
                    Bundle args = new Bundle();
                    args.putString("id", extras.getString("id"));
                    args.putString("Num", extras.getString("Num"));
                    args.putString("title", extras.getString("title"));

                    Intent chat = new Intent(this, ApplicantStateActivity.class);
                    chat.putExtra("RApplyReq", args);

                    notification = new NotificationCompat.Builder(this);
                    notification.setContentTitle(extras.getString("title"));
                    notification.setContentText(extras.getString("id"));
                    notification.setTicker("New Apply!");
                    notification.setSmallIcon(R.mipmap.ic_launcher);

                    PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                            chat, PendingIntent.FLAG_CANCEL_CURRENT);
                    notification.setContentIntent(contentIntent);
                    notification.setAutoCancel(true);
                    manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, notification.build());
                }
                else{
                    if(!prefs.getString("CURRENT_ACTIVE","").equals(extras.getString("fromu"))) {   //채팅메시지 받는곳
                        Log.d("sendnotify","cccccccccccccccccc");
                        Log.d("rererere",prefs.getString("CURRENT_ACTIVE",""));
                        Log.d("rererere2",extras.getString("fromu"));

                        sendNotification(extras.getString("msg"), extras.getString("fromu"), extras.getString("name"));
                    }
                    Log.i("TAG", "Received: " + extras.getString("msg"));
                }

            }
        }
        MSGReceiver.completeWakefulIntent(intent);
    }

    private class NeederScreenInfoSend extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();

            //GetClassName(getApplicationContext());

            //params.add(new BasicNameValuePair("type", "helper_request"));
            params.add(new BasicNameValuePair("screen_info", GetClassName(getApplicationContext())));
            params.add((new BasicNameValuePair("mobno_edittext", prefs.getString("needer_id",""))));
            params.add((new BasicNameValuePair("pw_edittext", prefs.getString("needer_id",""))));
            params.add((new BasicNameValuePair("helper_mobno", prefs.getString("needer_id",""))));
            params.add((new BasicNameValuePair("needer_id", prefs.getString("needer_id",""))));


            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/NeederScreenInfoSend",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public String GetClassName(Context context){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        return runningTaskInfos.get(0).topActivity.getClassName();
    }
    private void sendNotification(String content, String mypage_type,String nickname, String date, String Num) {

        Log.d("Numumum", Num);
        Bundle args = new Bundle();
        args.putString("Num", Num);
        args.putString("mypage_type", mypage_type);

        Intent chat = new Intent(this, MyPageActivity.class);
        chat.putExtra("PushStart", args);

        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(nickname);
        notification.setContentText(content);
        notification.setTicker("New Message !");
        notification.setSmallIcon(R.mipmap.ic_launcher);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                chat, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

    private void sendNotification(String msg,String mobno,String name) {

        Bundle args = new Bundle();
        args.putString("mobno", mobno);
        args.putString("name", name);
        args.putString("msg", msg);
        Intent chat = new Intent(this, ChatActivity.class);
        chat.putExtra("INFO", args);
        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(name);
        notification.setContentText(msg);
        notification.setTicker("New Message !");
        notification.setSmallIcon(R.mipmap.ic_launcher);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                chat, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

    private void sendNotification(String random_number, String needer_id) {

        Bundle args = new Bundle();
        args.putString("random_number", random_number);
        args.putString("needer_id", needer_id);
        Intent login = new Intent(this, StartActivity.class);

        login.putExtra("random_number",args);
//        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle("인증번호를 입력하세요.");
        notification.setContentText(random_number);
        notification.setTicker("도움주기 인증번호");
        notification.setSmallIcon(R.mipmap.ic_launcher);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                login, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }


}