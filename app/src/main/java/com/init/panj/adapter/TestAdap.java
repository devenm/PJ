package com.init.panj.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.init.panj.R;
import com.init.panj.activity.MainActivity;
import com.init.panj.clases.CustomImageView;
import com.init.panj.fragments.MainFragment;
import com.init.panj.fragments.New_Video_home;
import com.init.panj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by deepak on 10/19/2015.
 */
public class TestAdap extends RecyclerView.Adapter<TestAdap.MyViewHolder> {
    ArrayList<ItemBean> list;
    ArrayList<ItemBean> urllist;
    Intent intent;
    int coun = 0;
    String tag;
    MainFragment mainFragment;
    ClickAction clickAction;

    //Fragment frag[]={newr SecondAct(),newr Yutube(),newr Audio(),newr Ring(),newr Wallpaper(),newr Schedule(),newr ContactUs()};
    // String color[] = {"#039be5","#ff6d00","#cddc39","#448aff","#42a5f5","#00bcd4","#8bc34a","#ffc107"};
    public TestAdap(MainFragment mainActivity, ArrayList<ItemBean> mainlist, ArrayList<ItemBean> url2, String tag, ClickAction clickAction) {
        mainFragment = mainActivity;
        list = mainlist;
        urllist = url2;
        this.tag = tag;
        this.clickAction = clickAction;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item_test, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final ItemBean itemBean = list.get(i);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("vlk","yes");
                clickAction.changeFragment(myViewHolder.getAdapterPosition());
            }
        });
        // myViewHolder.img.setImageResource(itemBean.displayicon);
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.tredcover).placeholder(R.mipmap.musicload).fit().into(myViewHolder.img);
        //  myViewHolder.title.setText(itemBean.tredname);
        //  myViewHolder.subt.setText(itemBean.treddesp);
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
                MainActivity.allurl = urllist;
                clickAction.changeFragment(myViewHolder.getAdapterPosition());
               // mainFragment.replaceFragment(new New_Video_home(), urllist.get(i).BT200, itemBean.tredcover, tag + "s", itemBean.tredname + " " + itemBean.treddesp, itemBean.id, myViewHolder.getAdapterPosition());
            }
        });
        ImageLoader.getInstance().loadImage(itemBean.tredcover, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                myViewHolder.img.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                myViewHolder.img.setImageBitmap(loadedImage);

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  TextView title, subt;
        CustomImageView img;
AVLoadingIndicatorView avLoadingIndicatorView;
        //  ProgressBarCircular pb;
        public MyViewHolder(View itemView) {
            super(itemView);
            // title = (TextView) itemView.findViewById(R.id.title);
            img = (CustomImageView) itemView.findViewById(R.id.trailerimage);
            // subt = (TextView) itemView.findViewById(R.id.subtitle);
            // pb= (ProgressBarCircular) itemView.findViewById(R.id.progress);
        }
    }

    public interface ClickAction {
        void changeFragment(int position);
    }
}
