package com.example.testremote;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LoginFragment extends Fragment {
    SharedPreferences prefs;
    EditText name, mobno, helper_mobno;
    Button login,help_btn;
    List<NameValuePair> params;
    ProgressDialog progress;


    Button authorization_btn;
    EditText authorization_edit;
    Switch authorization_switch;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        prefs = getActivity().getSharedPreferences("Chat", 0);

        name = (EditText)view.findViewById(R.id.name);
        mobno = (EditText)view.findViewById(R.id.mobno);
        login = (Button)view.findViewById(R.id.log_btn);
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Registering ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        help_btn = (Button)view.findViewById(R.id.help_btn);
        helper_mobno = (EditText)view.findViewById(R.id.help_mobno);
        authorization_btn = (Button)view.findViewById(R.id.authorization_ok_btn);
        authorization_edit = (EditText)view.findViewById(R.id.authorization_number);
        authorization_switch = (Switch)view.findViewById(R.id.helping_switch);


        authorization_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    help_btn.setVisibility(View.GONE);
                    helper_mobno.setVisibility(View.GONE);
                    authorization_btn.setVisibility(View.VISIBLE);
                    authorization_edit.setVisibility(View.VISIBLE);
                }else{
                    help_btn.setVisibility(View.VISIBLE);
                    helper_mobno.setVisibility(View.VISIBLE);
                    authorization_btn.setVisibility(View.GONE);
                    authorization_edit.setVisibility(View.GONE);
                }
            }
        });

        authorization_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                Log.d("loginstart", "loginininini");
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("REG_FROM", mobno.getText().toString());
                edit.putString("FROM_NAME", name.getText().toString());
                edit.commit();

                String smobno = mobno.getText().toString();
                String sname = name.getText().toString();

                new Login().execute(sname,smobno);

            }
        });

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress.show();
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("HELP_FLAG", "1");
                edit.commit();


                String h_smobno = helper_mobno.getText().toString();


                Log.d("fdfdfdfdfdf",prefs.getString("HELP_FLAG",""));

                Random random = new Random();
                int x[] = new int[4];
                String random_number = "";
                for(int i =0; i<4; i++) {
                    x[i] = random.nextInt(10);
                    random_number += x[i];
                }

                Log.d("nnnnnn",random_number);

                new Help().execute(h_smobno,random_number);
            }
        });

        return view;
    }


    private class Help extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();

            params = new ArrayList<NameValuePair>();
            Log.d("hphp",args[0]);

            //params.add(new BasicNameValuePair("type", "helper_request"));
            params.add(new BasicNameValuePair("helper_mobno", args[0]));
            params.add(new BasicNameValuePair("random_number", args[1]));
            params.add((new BasicNameValuePair("needer_id",prefs.getString("REG_ID",""))));



            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/help",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Success")) {

                }else{
                    Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class Login extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            Log.d("loglogloglog",args[0]);

            params.add(new BasicNameValuePair("name", args[0]));
            params.add(new BasicNameValuePair("mobno", args[1]));
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/login",params);
            return jObj;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Sucessfully Registered")) {
                    Fragment reg = new UserFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, reg);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }else{
                    Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}