package com.example.testremote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompanySignInActivity extends AppCompatActivity {

    Button btn_STT;
    Button btn_next;

    EditText nickname;
    EditText name;
    EditText pw;
    EditText id;
    TextView location;
    double lng;
    double lat;

    SharedPreferences prefs;
    List<NameValuePair> params;

    final int PLACE_PICKER_REQUEST = 333;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companysignin);
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);

        nickname = (EditText)findViewById(R.id.signin_nickname);
        name = (EditText)findViewById(R.id.signin_name);
        pw = (EditText)findViewById(R.id.signin_pw);
        id = (EditText)findViewById(R.id.signin_id);
        location = (TextView) findViewById(R.id.signin_location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setSelected(true);
            }
        });

        btn_next = (Button)findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject tmp = new JSONObject();
                try {

                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_FROM", id.getText().toString());
                    edit.putString("FROM_NAME", pw.getText().toString());
                    edit.commit();

                    String smobno = id.getText().toString();
                    String spw = pw.getText().toString();

                   // new LoginActivity2.Login().execute(spw,smobno);

                    tmp.put("Nickname",nickname.getText());
                    tmp.put("Name",name.getText());
                    tmp.put("PW",pw.getText());
                    tmp.put("ID",id.getText());
                    tmp.put("lng",lng);
                    tmp.put("lat",lat);
                    //tmp.put("reg_id",prefs.getString("REG_ID",""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                InsertDataTask insertTask = new InsertDataTask(tmp);

                String url = "http://13.124.85.122:52273/pushCompanyData";
                RequestForm req = new RequestForm(url);


                insertTask.execute(req);

                new CompanySignInActivity.Login().execute();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();


            }
        });

        setUserNumber();
//        btn_STT = (Button)findViewById(R.id);
//
//        btn_STT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),STTActivity.class);
//                startActivity(i);
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case 0:
                if(resultCode == RESULT_OK) {
                    nickname.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
                    pw.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    name.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = null;
                    place = PlacePicker.getPlace(this, data);

                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                    lng = place.getLatLng().longitude;
                    lat = place.getLatLng().latitude;

                    location.setText(place.getAddress());

                }
                break;

        }

    }

    public void callSTT(View view) {

        Intent i = new Intent(getApplicationContext(),STTActivity.class);

        switch (view.getId()) {
            case R.id.btn_nickname:

                startActivityForResult(i, 0);
                break;
            case R.id.btn_pw:

                startActivityForResult(i, 1);
                break;
            case R.id.btn_name:

                startActivityForResult(i, 2);
                break;

        }

    }


    void setUserNumber(){  // 번호 읽어서 국제번호로 변환 후 id란에 Setting


        TelephonyManager t = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = "";

        String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"}; //번호 가져오기
        Object object = getContentResolver().query(Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, "data"),
                main_data, "mimetype=?",
                new String[]{"vnd.android.cursor.item/phone_v2"},
                "is_primary DESC");
        if (object != null) {
            do {
                if (!((Cursor) (object)).moveToNext())
                    break;
                phoneNumber = ((Cursor) (object)).getString(4);
                Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_SHORT).show();

            } while (true);
            ((Cursor) (object)).close();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
            phoneNumber= PhoneNumberUtils.formatNumberToE164(phoneNumber,t.getNetworkCountryIso().toUpperCase());
            Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_SHORT).show();
            id.setText(phoneNumber);
            //id.setEnabled(false);
        }




    }

    public void searchLocation(View view) {


        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private class Login extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            // Log.d("loglogloglog",args[0]);

//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putString("REG_FROM", id.getText().toString());
//            edit.putString("FROM_NAME", pw.getText().toString());
//            edit.commit();

            params.add(new BasicNameValuePair("name", prefs.getString("FROM_NAME","")));//pw
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));//아이디
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));//토큰

            JSONObject jObj = json.getJSONFromUrl("http://13.124.85.122:8080/login",params);
            return jObj;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            // progress.dismiss();
            try {
                String res = json.getString("response");
                if(res.equals("Sucessfully Registered")) {
//                    Fragment reg = new UserFragment();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, reg);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                    ft.addToBackStack(null);
//                    ft.commit();
                    startActivity(new Intent(getApplicationContext(),UserActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
