package com.init.panj.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.init.panj.R;
import com.init.panj.activity.FullPlayAct;
import com.init.panj.activity.LiveStreamPlay;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by deepak on 11/7/2016.
 */

public class LiveTvAdaptor extends RecyclerView.Adapter<LiveTvAdaptor.MyViewHolder> {
    ArrayList<String> list;
    ArrayList<String> listurl;
    Activity act;
    public LiveTvAdaptor(Activity act,ArrayList<String> list, ArrayList<String> listurl) {
        this.list=list;
        this.listurl=listurl;
        this.act=act;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.live_adaptor_design,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        ImageLoader.getInstance().loadImage(list.get(position), new  SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                myViewHolder.imageView.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                myViewHolder.imageView.setImageBitmap(loadedImage);

            }
        });
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(act, LiveStreamPlay.class);
                intent.putExtra("url",listurl.get(position));
                act.startActivity(intent);            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.livetvthumnails);
        }
    }
}
