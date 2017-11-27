package com.example.testremote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 혜진 on 2017-10-24.
 */

public class MyPage_BucketAdapter extends RecyclerView.Adapter<MyPage_BucketAdapter.ViewHolder> {

    Context context;
    List<MyPage_BucketItem> items;

    public MyPage_BucketAdapter(Context context,List<MyPage_BucketItem> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public MyPage_BucketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_bucket,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyPage_BucketAdapter.ViewHolder holder, int position) {
        MyPage_BucketItem item = items.get(position);
        holder.bucketItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"테스트",Toast.LENGTH_LONG).show();


            }
        });
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout bucketItemLayout;
        TextView title;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            bucketItemLayout = (LinearLayout)itemView.findViewById(R.id.mypage_bucket);
            title = (TextView)itemView.findViewById(R.id.bucket_title);
            date = (TextView)itemView.findViewById(R.id.bucket_date);
        }
    }
}
