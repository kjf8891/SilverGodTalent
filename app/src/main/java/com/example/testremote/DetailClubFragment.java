package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by seyeon on 2017-11-17.
 */

public class DetailClubFragment extends Fragment {
    View syView;
    Button btn_move_W;
    Button applyBtn;
    TextView textView;
    String num;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_detail_club,container,false);
        init();
        return syView;
    }
    public void init(){
        Bundle arguments = this.getArguments();
        num = arguments.getString("num");
        textView = (TextView) syView.findViewById(R.id.textView);
        //textView.setText(num);
        btn_move_W = (Button)syView.findViewById(R.id.btn_move_W);
        applyBtn = (Button)syView.findViewById(R.id.applyBtn);
        btn_move_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClubActivity.class);
                startActivity(intent);
            }
        });

    }
    public void applyBtn(View v){
//        Intent intent = new Intent(getActivity(), MentoringActivity.class);
//        startActivity(intent);
        Toast.makeText(getActivity(), "Apply Completed", Toast.LENGTH_LONG).show();

    }
}