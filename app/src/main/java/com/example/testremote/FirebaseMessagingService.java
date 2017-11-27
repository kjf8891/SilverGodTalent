package com.example.testremote;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by 혜진 on 2017-11-13.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    public String title,contents,imgurl;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if(remoteMessage.getData().get("type") != null){

        }else if(remoteMessage.getData().get("Helper_authorization") != null){

        }else if(remoteMessage.getData().get("screeninfo") != null){
            Log.d("screen_info", "screeninfo2");

        }else if(remoteMessage.getData().get("activity_change") != null){
            Log.d("activity_change", "activity_change2");

        }
        else {
            //sendPushNotification(remoteMessage.getData().get("msg"));
        }
    }

    private void sendPushNotification(String message){
        System.out.println("received message : " + message);
        Log.d("reme" , message);








//
//
//
//        try {
//
//
//
//
//            JSONObject jsonRootObject = new JSONObject(message);
//
//
//
//
//            title = jsonRootObject.getString("title");
//
//            contents = jsonRootObject.getString("contents");
//
//            imgurl = jsonRootObject.getString("imgurl");
//
//
//
//
//
//
//
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//
//        }
//
//
//
//
//        Bitmap bitmap = getBitmapFromURL(imgurl);
//
//
//
//
//
//
//
//        Intent intent = new Intent(this, MainActivity.class);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//
//                PendingIntent.FLAG_ONE_SHOT);
//
//
//
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//
//                .setSmallIcon(R.drawable.bar_icon)
//
//                .setLargeIcon(bitmap)
//
//                .setContentTitle(title)
//
//                .setContentText(contents)
//
//                .setAutoCancel(true)
//
//                .setSound(defaultSoundUri).setLights(000000255,500,2000)
//
//                .setContentIntent(pendingIntent);
//
//
//
//
//
//
//
//
//
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
//
//        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
//
//        wakelock.acquire(5000);
//
//
//
//
//
//
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//    }
//
//
//
//
//
//
//
//
//
//
//    public Bitmap getBitmapFromURL(String strURL) {
//
//        try {
//
//            URL url = new URL(strURL);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setDoInput(true);
//
//            connection.connect();
//
//            InputStream input = connection.getInputStream();
//
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//
//            return myBitmap;
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//            return null;
//
//        }
//
    }



}
