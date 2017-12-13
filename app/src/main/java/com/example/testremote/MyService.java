package com.example.testremote;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Donghyun on 2017-12-13.
 */
public class MyService extends Service
{
    protected static AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected IncomingHandler mHandler;
    protected Messenger mServerMessenger;

    protected SpeechRecognitionListener mListener;

    protected boolean mIsListening;
    protected volatile boolean mIsCountDownOn;
    private static boolean mIsStreamSolo;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;
    private boolean mBoolVoiceRecoStarted;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,new Notification());

        /**
         * startForeground 를 사용하면 notification 을 보여주어야 하는데 없애기 위한 코드
         */

        Intent it = new Intent(this, MSGReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, it, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Speech Recognizing")
                .setContentText("Recognizing")
                .setSmallIcon(R.drawable.mic_btn)
                .setContentIntent(pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(startId, n);
        notificationManager.cancel(startId);


//        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        Notification notification;
//
//        Notification.Builder builder = new Notification.Builder(getApplicationContext());
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//
//            notification = builder
//                    .setContentTitle("")
//                    .setContentText("")
//                    .build();
//
//        }else{
//            notification = new Notification(0, "", System.currentTimeMillis());
//            //notification.setLatestEventInfo(getApplicationContext(), "", "", null);
//        }
//
//        notification = builder.getNotification();
//
//        nm.notify(startId, notification);
//        nm.cancel(startId);

        return super.onStartCommand(intent, flags, startId);



    }


    void init(){
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(mListener);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());


        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0); //비프 음소거

    }

    @Override
    public void onCreate()
    {
        unregisterRestartAlarm();

        super.onCreate();
        mHandler = new IncomingHandler(this);
        mServerMessenger = new Messenger(mHandler);
        mListener = new SpeechRecognitionListener();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        init();

       
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<MyService> mtarget;

        IncomingHandler(MyService target)
        {
            mtarget = new WeakReference<MyService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {


            final MyService target = mtarget.get();

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:




                    if (!target.mIsListening)
                    {
                        target.mSpeechRecognizer.destroy();

                        target.init();


                        target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                        target.mIsListening = true;
                        Log.e("TAG", "message start listening"); //$NON-NLS-1$
                    }
                    break;

                case MSG_RECOGNIZER_CANCEL:

                    target.mSpeechRecognizer.cancel();
                    target.mIsListening = false;

                    Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                    try {
                        target.mServerMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Log.e("TAG", "message canceled recognizer"); //$NON-NLS-1$
                    break;
            }
        }
    }

    // Count down timer for Jelly Bean work around
    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub
            Log.e("handleMessage","handleMessage");

        }

        @Override
        public void onFinish()
        {
            Log.e("onFinish","timer onFinish");

            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);

            }
            catch (RemoteException e)
            {

            }
        }
    };



    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.e("speech destroy","destroy");

        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE,0);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.e("onBind","bind ok");
        return mServerMessenger.getBinder();
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            // speech input will be processed, so there is no need for count down anymore
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                // turn off beep sound
                //mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM,true);

                mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            }

            if (mIsCountDownOn)
            {
                Log.e("begin","speech begin");
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
            //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {
Log.e("onBuffer","onBuffer");
        }

        @Override
        public void onEndOfSpeech()
        {
            Log.e("TAG", "onEndOfSpeech"); //$NON-NLS-1$
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try
            {
                Log.d("TAG", "error ?"); //$NON-NLS-1$

                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {
                Log.d("TAG", "error = " + error); //$NON-NLS-1$
            }

        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();


            }
            Log.e("TAG", "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            //Log.d(TAG, "onResults"); //$NON-NLS-1$



           ArrayList<String> res = (ArrayList)results.get(SpeechRecognizer.RESULTS_RECOGNITION);

            Log.e("onResult",res.get(0));

            if(res.get(0).equals("실행")){


                startActivity(new Intent(getApplicationContext(),MyPageListActivity.class));

            }
            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try
            {
                mServerMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

    }

    private void registerRestartAlarm(){

        Log.i("000 MyService" , "registerRestartAlarm" );
        Intent intent = new Intent(MyService.this,MSGReceiver.class);
        intent.setAction("ACTION.RESTART.MyService");
        PendingIntent sender = PendingIntent.getBroadcast(MyService.this,0,intent,0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1*1000;

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 알람 등록
         */
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,1*1000,sender);

    }

    private void unregisterRestartAlarm() {

        Log.i("000 MyService", "unregisterRestartAlarm");

        Intent intent = new Intent(MyService.this, MSGReceiver.class);
        intent.setAction("ACTION.RESTART.MyService");
        PendingIntent sender = PendingIntent.getBroadcast(MyService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /**
         * 알람 취소
         */
        alarmManager.cancel(sender);


    }


}