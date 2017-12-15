package com.example.testremote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RecruitTutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.page_list, null);
        //View v2 = View.inflate(this, R.layout.page_main, null);
        View v3 = View.inflate(this, R.layout.page_zoom, null);
        sv.addView(v1);
        //sv.addView(v2);
        sv.addView(v3);
        setContentView(sv);
        //setContentView(R.layout.activity_recruit_tuto);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RecruitTutoActivity.this, "dddddd", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
