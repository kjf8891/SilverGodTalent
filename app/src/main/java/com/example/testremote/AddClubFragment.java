package com.example.testremote;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by seyeon on 2017-11-17.
 */

public class AddClubFragment extends Fragment {
    View syView;
    Button completeBtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        syView = inflater.inflate(R.layout.layout_add_club,container,false);
        init();
        return syView;
    }
    public void init(){
        completeBtn = (Button)syView.findViewById(R.id.btn_move_C);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClubActivity.class);
                startActivity(intent);
            }
        });
    }
}