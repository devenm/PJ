package com.init.panj.adapter;

/**
 * Created by deepak on 11/7/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.init.panj.R;
import com.init.panj.activity.LiveStreamPlay;
import com.init.panj.fragments.RadioFragment;
import com.init.panj.model.LiveTvBean;
import com.init.panj.radioplayer.RadioBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;


/**
 * Created by deepak on 11/7/2016.
 */

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.MyViewHolder> {
    ArrayList<RadioBean> list;
    Activity act;
    Bitmap bitmap;
    ClickAction clickAction;
    public RadioAdapter(Activity act, ArrayList<RadioBean> list, ClickAction clickAction) {
        this.list=list;
        this.act=act;
        this.clickAction=clickAction;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.livead,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        if (list.size() > 0) {
            final RadioBean liveTvBean=list.get(position);
            myViewHolder.title.setText(""+liveTvBean.getRadioname());
            ImageLoader.getInstance().loadImage(liveTvBean.getRadioimage(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    myViewHolder.imageView.setImageResource(R.drawable.logo_fade);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    myViewHolder.imageView.setImageBitmap(loadedImage);
                   // Bitmap blurredBitmap = new Bluer(act).blur(loadedImage);
                   // myViewHolder.img.setImageBitmap(blurredBitmap);
                }
            });
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction.onAction(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.icon);
            title= (TextView) itemView.findViewById(R.id.title);
        }
    }
   public interface ClickAction{

        void onAction(int position);
    }
}
