package com.example.testremote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 혜진 on 2017-11-17.
 */

public class UserActivity extends Activity {
    ListView list;
    ArrayList<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
    Button refresh,logout;
    List<NameValuePair> params;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fragment);
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);

        list = (ListView)findViewById(R.id.listView);
        refresh = (Button)findViewById(R.id.refresh);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logout().execute();



            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
//                Fragment reg = new UserFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, reg);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
                    startActivity(new Intent(getApplicationContext(),UserActivity.class));
            }
        });
        new Load().execute();


    }

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));
            JSONArray jAry = json.getJSONArray("http://13.124.85.122:8080/getuser",params);

            return jAry;
        }
        @Override
        protected void onPostExecute(JSONArray json) {
            for(int i = 0; i < json.length(); i++){
                JSONObject c = null;
                try {
                    c = json.getJSONObject(i);
                    String name = c.getString("name");
                    String mobno = c.getString("mobno");
                    Log.d("Chat-list", name + mobno);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", name);
                    map.put("mobno", mobno);
                    users.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ListAdapter adapter = new SimpleAdapter(getApplicationContext(), users,
                    R.layout.user_list_single,
                    new String[] { "name","mobno" }, new int[] {
                    R.id.name, R.id.mobno});
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bundle args = new Bundle();
                    args.putString("mobno", users.get(position).get("mobno"));
                    Intent chat = new Intent(getApplicationContext(), ChatActivity.class);
                    chat.putExtra("INFO", args);
                    startActivity(chat);
                }
            });
        }
    }
    private class Logout extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));
            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/logout",params);

            SharedPreferences.Editor edit = prefs.edit();
            edit.remove("REG_FROM");
            //edit.putString("REG_FROM","");
            edit.commit();

            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            String res = null;
            try {
                res = json.getString("response");
                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                if(res.equals("Removed Sucessfully")) {
//                    Fragment reg = new LoginFragment();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, reg);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                    SharedPreferences.Editor edit = prefs.edit();
//                    edit.putString("REG_FROM", "");
//                    edit.commit();
                    startActivity(new Intent(getApplicationContext(),LoginActivity2.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}