package com.example.testremote;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends Activity {
    SharedPreferences prefs;
    List<NameValuePair> params;



    EditText chat_msg;
    Button send_btn;
    Bundle bundle;
    TableLayout tab;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        prefs = getSharedPreferences("Chat", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", "");   //채팅방 나갈 때 현재 채팅중인 사람 비워야댐
        edit.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tab = (TableLayout)findViewById(R.id.tab);

        prefs = getSharedPreferences("Chat", 0);
        bundle = getIntent().getBundleExtra("INFO");
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", bundle.getString("mobno"));
        edit.commit();

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


        if(bundle.getString("name") != null){
            TableRow tr1 = new TableRow(getApplicationContext());
            tr1.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml("<b>"+bundle.getString("name")+" : </b>"+bundle.getString("msg")));
            tr1.addView(textview);
            tab.addView(tr1);

        }

        chat_msg = (EditText)findViewById(R.id.chat_msg);
        send_btn = (Button)findViewById(R.id.sendbtn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr2 = new TableRow(getApplicationContext());
                tr2.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#A901DB"));
                textview.setText(Html.fromHtml("<b>You : </b>" + chat_msg.getText().toString()));
                tr2.addView(textview);
                tab.addView(tr2);
                new Send().execute(chat_msg.getText().toString());
            }
        });


    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();

            if(name.equals("Msg")) {
                String str = intent.getStringExtra("msg");
                String str1 = intent.getStringExtra("fromname");
                String str2 = intent.getStringExtra("fromu");
                if (str2.equals(bundle.getString("mobno"))) {

                    TableRow tr1 = new TableRow(getApplicationContext());
                    tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView textview = new TextView(getApplicationContext());
                    textview.setTextSize(20);
                    textview.setTextColor(Color.parseColor("#0B0719"));
                    textview.setText(Html.fromHtml("<b>" + str1 + " : </b>" + str));
                    tr1.addView(textview);
                    tab.addView(tr1);
                }
            }



        }
    };

    public void tmpclick(View view) {
        Intent intent = new Intent(getApplicationContext(), TempActivity.class);
        startActivity(intent);
    }

    private class Send extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("from", prefs.getString("REG_FROM","")));
            params.add(new BasicNameValuePair("fromn", prefs.getString("FROM_NAME","")));
            params.add(new BasicNameValuePair("to", bundle.getString("mobno")));
            params.add((new BasicNameValuePair("msg",args[0])));

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/send",params);
            Log.d("sendOK","OKOKOKOKOKOK");
            return jObj;



        }
        @Override
        protected void onPostExecute(JSONObject json) {
            chat_msg.setText("");

            String res = null;
            try {
                res = json.getString("response");
                if(res.equals("Failure")){
                    Toast.makeText(getApplicationContext(),"The user has logged out. You cant send message anymore !",Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}