package com.init.panj.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.init.panj.R;
import com.init.panj.activity.MainActivity;
import com.init.panj.clases.ProgressBarCircular;
import com.init.panj.fragments.Video_home;
import com.init.panj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by deepak on 3/5/2016.
 */
public class AllvideoAdaptor extends RecyclerView.Adapter<AllvideoAdaptor.MyViewHolder> {
ArrayList<ItemBean> list;
MainActivity act;
    ArrayList<ItemBean> urllist;
    public AllvideoAdaptor(Activity activity, ArrayList<ItemBean> arrayList, ArrayList<ItemBean> Urllist) {
     act= (MainActivity) activity;
        list=arrayList;
        urllist=Urllist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.maincontent,null));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ItemBean itemBean = list.get(position);
        // myViewHolder.img.setImageResource(itemBean.displayicon);
        holder.title.setText(itemBean.tredname);
        holder.subt.setText(itemBean.treddesp);
        holder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",position);
                it.putExtra("id",itemBean.id);
                act.startActivity(it);*/
                act.replaceFragment(new Video_home(),urllist.get(position).BT200,itemBean.tredcover,"alv", itemBean.tredname + " " + itemBean.treddesp, itemBean.id, 1);


            }
        });

        ImageLoader.getInstance().loadImage(itemBean.tredcover, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.img.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                holder.img.setImageBitmap(loadedImage);
               holder.pb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, subt;
        ImageView img;
        CardView cd;
        ProgressBarCircular pb;
        public MyViewHolder(View itemView) {
            super(itemView);
            pb= (ProgressBarCircular) itemView.findViewById(R.id.progress);
            title = (TextView) itemView.findViewById(R.id.title);
            img = (ImageView) itemView.findViewById(R.id.icon);
            subt = (TextView) itemView.findViewById(R.id.subtitle);
            cd = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
