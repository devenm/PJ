package com.init.panjj.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.otherclasses.ProgressBarCircular;
import com.init.panjj.fragments.Video_home;
import com.init.panjj.model.ItemBean;
import com.init.panjj.model.UrlBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
/**
 * Created by deepak on 10/19/2015.
 */
public class SimilerAdopter extends RecyclerView.Adapter<SimilerAdopter.MyViewHolder> {
    ArrayList<ItemBean> list;
    ArrayList<ItemBean> urllist;
    MainActivity act;
    Intent intent;
    int coun=0;
    UrlBean urlBean;
    //Fragment frag[]={newr SecondAct(),newr Yutube(),newr Audio(),newr Ring(),newr Wallpaper(),newr Schedule(),newr ContactUs()};
    // String color[] = {"#039be5","#ff6d00","#cddc39","#448aff","#42a5f5","#00bcd4","#8bc34a","#ffc107"};
    public SimilerAdopter(MainActivity mainActivity, ArrayList<ItemBean> mainlist, ArrayList<ItemBean> singleurl) {
        act = mainActivity;
        list = mainlist;
urllist=singleurl;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.maincontent, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {

        final ItemBean itemBean = list.get(i);
        // myViewHolder.img.setImageResource(itemBean.displayicon);
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.tredcover).placeholder(R.mipmap.musicload).fit().into(myViewHolder.img);
        myViewHolder.title.setText(itemBean.tredname);
        // myViewHolder.subt.setText(itemBean.treddesp);
        myViewHolder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // act.replaceFragment(abc Audio(), itemBean.id, itemBean.tredcover, itemBean.tredname, itemBean.treddesp, "http://52.74.238.77/savaan_nirmolak/albumsong.php", "");

              /*  Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                act.startActivity(it);*/
                MainActivity.allurl=urllist;
                act.replaceFragment(new Video_home(),urllist.get(i).BT200,itemBean.tredcover,"Videos"+itemBean.id, itemBean.tredname + " " + itemBean.treddesp, itemBean.id, myViewHolder.getAdapterPosition());

            }
        });
        ImageLoader.getInstance().loadImage(itemBean.tredcover, new  SimpleImageLoadingListener() {
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
        TextView title, subt;
        ImageView img;
        CardView cd;
        ProgressBarCircular pb;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            img = (ImageView) itemView.findViewById(R.id.icon);
            subt = (TextView) itemView.findViewById(R.id.subtitle);
            cd = (CardView) itemView.findViewById(R.id.card_view);
            pb= (ProgressBarCircular) itemView.findViewById(R.id.progress);
        }
    }
}
