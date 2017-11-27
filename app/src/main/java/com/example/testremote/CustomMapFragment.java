package com.example.testremote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Donghyun on 2017-10-24.
 */
public class CustomMapFragment extends SupportMapFragment {

    View view;
    TouchableWrapper mTouchView;
    public boolean isTouch =false;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view =  super.onCreateView(layoutInflater, viewGroup, bundle);
        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(view);
        return mTouchView;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public class TouchableWrapper extends FrameLayout{

        public TouchableWrapper(Context context){super(context);}

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isTouch = true;
                    break;
                case MotionEvent.ACTION_UP:
                    isTouch = false;
                    break;

            }

            return super.dispatchTouchEvent(ev);
        }
    }
}
