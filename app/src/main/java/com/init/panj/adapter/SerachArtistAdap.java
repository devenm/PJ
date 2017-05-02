package com.init.panj.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.init.panj.R;
import com.init.panj.activity.MainActivity;
import com.init.panj.clases.ProgressBarCircular;
import com.init.panj.clases.SearchDialog;
import com.init.panj.clases.SearchDialogArtist;
import com.init.panj.fragments.Video_home;
import com.init.panj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by deepak on 10/19/2015.
 */
public class SerachArtistAdap extends RecyclerView.Adapter<SerachArtistAdap.MyViewHolder> {
    ArrayList<ItemBean> list;
    ArrayList<ItemBean> urllist;
    MainActivity act;
    Intent intent;
    int coun=0;
    SearchDialogArtist searchDialog;
    public SerachArtistAdap(MainActivity mainActivity, ArrayList<ItemBean> mainlist, SearchDialogArtist searchDialog) {
        act=mainActivity;
        list=mainlist;
        this.searchDialog=searchDialog;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.maincontent, viewGroup,false));
    }



    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final ItemBean itemBean=list.get(i);
        myViewHolder.title.setText(itemBean.tredname);
        myViewHolder.subt.setText(itemBean.treddesp);
        myViewHolder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                it.putExtra("id",itemBean.id);
                act.startActivity(it);*/
                searchDialog.dismiss();
                act.replaceFragment(new Video_home(),"sdss",itemBean.tredcover,"Movies", itemBean.tredname + " " + itemBean.treddesp, itemBean.id, 1);

            }
        });
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.albumcover).placeholder(R.drawable.ic_profile).fit().into(myViewHolder.img);
        ImageLoader.getInstance().loadImage(itemBean.tredcover,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                myViewHolder.img.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                myViewHolder.img.setImageBitmap(loadedImage);
                myViewHolder.pb.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,subt;
        ImageView img;
        CardView cd;
        ProgressBarCircular pb;
        FrameLayout fm;
        public MyViewHolder(View itemView) {
            super(itemView);
            Display mDisplay = act.getWindowManager().getDefaultDisplay();
            final int width  = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            int framewitdh=width/2;
            fm= (FrameLayout) itemView.findViewById(R.id.mframe);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(framewitdh, height/3);
            fm.setLayoutParams(lp);
            pb= (ProgressBarCircular) itemView.findViewById(R.id.progress);
            title= (TextView) itemView.findViewById(R.id.title);
            img= (ImageView) itemView.findViewById(R.id.icon);
            subt= (TextView) itemView.findViewById(R.id.subtitle);
            cd= (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
