package com.example.testremote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Donghyun on 2017-11-17.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder>{

    private ArrayList<InterestData> mDataset;
   // private View.OnClickListener mClickListener = new MyOnClickListener;
   // private final View.OnClickListener mOnClickListener = new InterestActivity.RecyclerOnClickListener();



    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }

    private OnItemClickListener mItemClickListener;

    public InterestAdapter(ArrayList<InterestData> myDataset,OnItemClickListener ItemClickListener){
        mDataset = myDataset;
        this.mItemClickListener = ItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        public ToggleButton mBtn1;
      //  public ToggleButton mBtn2;

        public ViewHolder(View view){
            super(view);
            this.view = view;

           mBtn1 = (ToggleButton)view.findViewById(R.id.toggle1);
        //    mBtn2 = (ToggleButton)view.findViewById(R.id.toggle2);

        }
    }

    @Override
    public InterestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_view,parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int pos;
        pos = position;

        holder.mBtn1.setText(mDataset.get(position).area);
        holder.mBtn1.setTextOn(mDataset.get(position).area);
        holder.mBtn1.setTextOff(mDataset.get(position).area);

        holder.mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.OnItemClick(v,pos);
            }
        });


//        holder.mBtn2.setText(mDataset.get(position).area);
//        holder.mBtn2.setTextOn(mDataset.get(position).area);
//        holder.mBtn2.setTextOff(mDataset.get(position).area);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

interface MyOnClickListener{

    void onClick(View v);

}

class InterestData{

    public String area;

    public InterestData(String area){

        this.area = area;
    }

}

