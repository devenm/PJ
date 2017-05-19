package com.init.panjj.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.activity.SubtitlePlayer;
import com.init.panjj.fragments.MainFragment;
import com.init.panjj.model.ItemBean;
import com.init.panjj.model.UrlBean;
import com.init.panjj.otherclasses.CustomImageView;
import com.init.panjj.radioplayer.Controls;

import java.util.ArrayList;

/**
 * Created by deepak on 10/19/2015.
 */
public class TrailerAdap extends RecyclerView.Adapter<TrailerAdap.MyViewHolder> {
    ArrayList<ItemBean> list;
    ArrayList<ItemBean> urllist;
    MainActivity act;
    Intent intent;
    int coun=0;
    String tag;
    UrlBean urlBean;
    MainFragment mainFragment;
    //Fragment frag[]={newr SecondAct(),newr Yutube(),newr Audio(),newr Ring(),newr Wallpaper(),newr Schedule(),newr ContactUs()};
    // String color[] = {"#039be5","#ff6d00","#cddc39","#448aff","#42a5f5","#00bcd4","#8bc34a","#ffc107"};
    public TrailerAdap(MainActivity mainActivity, ArrayList<ItemBean> mainlist, ArrayList<ItemBean> url2, String tag) {
        act = mainActivity;
        list = mainlist;
        urllist=url2;
this.tag=tag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {

        final ItemBean itemBean = list.get(i);
        // myViewHolder.img.setImageResource(itemBean.displayicon);
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.tredcover).placeholder(R.mipmap.musicload).fit().into(myViewHolder.img);
if (itemBean.tredcover.equals("null"))
    myViewHolder.img.scaledImage("http://staticori.iiscandy.com/Music_Videos/Gurdeep/Hindi/Trailer/Live_Carefree/V_220_panj.jpg");
        else
        myViewHolder.img.scaledImage(itemBean.tredcover);
        myViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // act.replaceFragment(abc Audio(), itemBean.id, itemBean.tredcover, itemBean.tredname, itemBean.treddesp, "http://52.74.238.77/savaan_nirmolak/albumsong.php", "");

                /*Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                it.putExtra("id",itemBean.id);
                act.startActivity(it);*/
               /* MainActivity.allurl=urllist;
                act.replaceFragment(new New_Video_home(),urllist.get(i).BT200,itemBean.tredcover,tag+"s", itemBean.tredname + " " + itemBean.treddesp, itemBean.id, myViewHolder.getAdapterPosition());
       */
                Controls.pauseControl(act);
                MainActivity.allurl = urllist;
                Intent it = new Intent(act, SubtitlePlayer.class);
                it.putExtra("url", urllist.get(i).m3u8);
                it.putExtra("pos", i);
                it.putExtra("id",  list.get(i).id);
                act.startActivity(it);
            }
        });
       /* ImageLoader.getInstance().loadImage("http://staticori.iiscandy.com/Music_Videos/Kuldeep/Punjabi/Pasand/Songs/V_220_panj.jpg", new  SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                myViewHolder.img.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                myViewHolder.img.setImageBitmap(loadedImage);

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomImageView img;
        FrameLayout fm;
        public MyViewHolder(View itemView) {
            super(itemView);
            img = (CustomImageView) itemView.findViewById(R.id.trailerimage);


        }
    }
}
