package com.example.testremote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.page_list);
        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.page_list, null);
        View v2 = View.inflate(this, R.layout.page_main, null);
        View v3 = View.inflate(this, R.layout.page_zoom, null);
        sv.addView(v1);
        sv.addView(v2);
        sv.addView(v3);
        setContentView(sv);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "rhkdusdlrjteh", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Toast.makeText(TutorialActivity.this, "dddddd", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
