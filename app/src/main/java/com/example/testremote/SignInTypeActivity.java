package com.example.testremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInTypeActivity extends AppCompatActivity {

    ImageButton btn_general;
    ImageButton btn_company;


    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);
        prefs = getApplicationContext().getSharedPreferences("Chat", 0);

        btn_general = (ImageButton)findViewById(R.id.btn_general);

        btn_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);

                startActivity(mIntent);

            }
        });

        btn_company = (ImageButton)findViewById(R.id.btn_company);

        btn_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent mIntent = new Intent(getApplicationContext(), CompanySignInActivity.class);

                startActivityForResult(mIntent,4);

            }
        });

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
             //       nickname.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
               //     pw.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                 //   name.setText(data.getStringExtra("stt_result"));
                    Toast.makeText(getApplicationContext(),"성공아니냐",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("stt error", "실패다!");
                    Toast.makeText(getApplicationContext(), "실패왜뜨냐", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if(resultCode == RESULT_OK) {
                    finish();
                }

        }

    }


}
