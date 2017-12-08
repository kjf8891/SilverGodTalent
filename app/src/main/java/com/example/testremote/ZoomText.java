package com.example.testremote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Donghyun on 2017-12-05.
 */

public class ZoomText extends TextView {

    static PopupWindow popupWindow;
    Button btOk=null;
    TextView tvPop;
    TextView tvOrigin;
    boolean isMarquee = false;


    public ZoomText(Context context) {
        super(context);
        tvOrigin = this;
        this.setClickable(true);
        this.setLongClickable(true);
        //this.setSelected(true);
    }

    public ZoomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        tvOrigin = this;
        this.setClickable(true);
        this.setLongClickable(true);

      //  initialize();
    }

    public ZoomText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tvOrigin = this;
        this.setClickable(true);
        this.setLongClickable(true);
        //this.setSelected(true);
        //initialize();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);

        Log.e("touch","dd");

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                _handler.postDelayed(_longPressed,LONG_PRESS_TIME);
//                mScaleDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                //  _handler.removeCallbacks(_longPressed);
                break;
            case MotionEvent.ACTION_UP:
                if(popupWindow!=null) {
                    popupWindow.dismiss();
                    //isLongClicked = false;
                }
                //_handler.removeCallbacks(_longPressed);
                break;

        }

        return true;
    }

    @Override
    public boolean performLongClick() {

        setMarqueeSpeed(this,1.3f,false);

        Log.e("itemLongclick", this.getText().toString());
        Toast.makeText(getContext(), "dd", Toast.LENGTH_SHORT).show();
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        popupWindow = new PopupWindow(popupView, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        tvPop = (TextView) popupView.findViewById(R.id.edit_pop);

        tvPop.requestFocus();
        tvPop.setTextSize(tvPop.getTextSize() * 1.3f);
        tvPop.setText(this.getText().toString());
        tvPop.setSelected(true);
        //popupWindow.showAtLocation(popupView, Gravity.CENTER,(int)getX(),(int)getY());
        popupWindow.showAsDropDown(this,0, 0 -  (int)((3.5)*this.getHeight()));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.update();

        return true;

    }

    @Override
    public boolean performClick() {

        Log.e("zt","onclick");
        if(isMarquee == false) {
            this.setSelected(true);
            isMarquee = true;
        }
        else {
            this.setSelected(false);
            isMarquee = false;
        }
        return true;

    }

    protected void setMarqueeSpeed(ZoomText tv, float speed,
                                   boolean speedIsMultiplier) {

        try {
            Field f = tv.getClass().getDeclaredField("mMarquee");
            f.setAccessible(true);
            Object marquee = f.get(tv);
            if (marquee != null) {
                Field mf = marquee.getClass().getDeclaredField("mScrollUnit");
                mf.setAccessible(true);
                float newSpeed = speed;
                if (speedIsMultiplier) {
                    newSpeed = mf.getFloat(marquee) * speed;
                }
                mf.setFloat(marquee, newSpeed);
// Log.i(this.getClass().getSimpleName(), String.format(
// "%s marquee speed set to %f", tv, newSpeed));
            }
        } catch (Exception e) {
// ignore, not implemented in current API level
//            Log.i(this.getClass().getSimpleName(), String.format(
//                    "%s marquee speed set fail fail fail to %f", tv, 0));
            Log.e("speed errer","error");
        }
    }

//    @Override
//    public boolean onLongClick(View v) {
//
//        isLongClicked = true;
//        Log.e("longclick","ok");
//        Toast.makeText(getContext(),"dd",Toast.LENGTH_SHORT).show();
//        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = layoutInflater.inflate(R.layout.popup, null);
//        popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvPop = (TextView)popupView.findViewById(R.id.edit_pop);
//
//        tvPop.requestFocus();
//        tvPop.setTextSize(tvPop.getTextSize()*1.3f);
//        tvPop.setText(this.getText().toString());
//        //popupWindow.showAtLocation(popupView, Gravity.CENTER,(int)getX(),(int)getY());
//        popupWindow.showAsDropDown(this,0,0);
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setFocusable(true);
//        popupWindow.update();
//
//        return true;
//    }
}
