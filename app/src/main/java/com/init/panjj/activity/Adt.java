package com.init.panjj.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.init.panjj.R;

/**
 * Created by deepak on 3/31/2016.
 */
public class Adt extends RecyclerView.Adapter<Adt.MyviewHolder> {
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
new MyviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comingsoon,parent,false));
        return new MyviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comingsoon,parent,false));
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public MyviewHolder(View itemView) {
            super(itemView);
        }
    }
}
