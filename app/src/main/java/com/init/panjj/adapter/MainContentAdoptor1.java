package com.init.panjj.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.otherclasses.CustomImageView;
import com.init.panjj.otherclasses.ProgressBarCircular;
import com.init.panjj.fragments.New_Video_home;
import com.init.panjj.model.ItemBean;
import com.init.panjj.model.UrlBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by deepak on 10/19/2015.
 */
public class MainContentAdoptor1 extends RecyclerView.Adapter<MainContentAdoptor1.MyViewHolder> {
    ArrayList<ItemBean> list;
    ArrayList<ItemBean> urllist;
    MainActivity act;
    Intent intent;
    int coun = 0;
    UrlBean urlBean;

    //Fragment frag[]={newr SecondAct(),newr Yutube(),newr Audio(),newr Ring(),newr Wallpaper(),newr Schedule(),newr ContactUs()};
    // String color[] = {"#039be5","#ff6d00","#cddc39","#448aff","#42a5f5","#00bcd4","#8bc34a","#ffc107"};
    public MainContentAdoptor1(MainActivity mainActivity, ArrayList<ItemBean> mainlist, ArrayList<ItemBean> url2) {
        act = mainActivity;
        list = mainlist;
        urllist = url2;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_maincontent, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder,  final int i) {

        final ItemBean itemBean = list.get(myViewHolder.getAdapterPosition());
        // myViewHolder.img.setImageResource(itemBean.displayicon);
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.tredcover).placeholder(R.mipmap.musicload).fit().into(myViewHolder.img);
       myViewHolder.title.setText(itemBean.tredname);
      //   myViewHolder.subt.setText(itemBean.treddesp);
        myViewHolder.img.scaledImage(itemBean.tredcover);
        myViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // act.replaceFragment(abc Audio(), itemBean.id, itemBean.tredcover, itemBean.tredname, itemBean.treddesp, "http://52.74.238.77/savaan_nirmolak/albumsong.php", "");
               /* Intent it = new Intent(act, PlayVideo.class);
                it.putExtra("list", new UrlBean(urllist));
                it.putExtra("position", myViewHolder.getAdapterPosition());
                it.putExtra("id", itemBean.id);
                act.startActivity(it);*/
                MainActivity.allurl=urllist;
                act.replaceFragment(new New_Video_home(),"njhjh",itemBean.tredcover,"Videos"+itemBean.id, itemBean.tredname + " " + itemBean.treddesp, itemBean.id, 1);
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
                myViewHolder.pb.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.e("third",""+list.size());
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, subt;
        CustomImageView img;
        ProgressBarCircular pb;
        FrameLayout fm;
        public MyViewHolder(View itemView) {
            super(itemView);
            Display mDisplay = act.getWindowManager().getDefaultDisplay();
            final int width  = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            int framewitdh=width/3;
            Log.e("hshdfkjsf",""+height);
            fm= (FrameLayout) itemView.findViewById(R.id.mframe);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(framewitdh, (int) act.getResources().getDimension(R.dimen.trendingcontainer_height));
            fm.setLayoutParams(lp);

           // fm.setMinimumWidth(framewitdh);
            title = (TextView) itemView.findViewById(R.id.title);
            img = (CustomImageView) itemView.findViewById(R.id.icon);
            subt = (TextView) itemView.findViewById(R.id.subtitle);
            pb = (ProgressBarCircular) itemView.findViewById(R.id.progress);
        }
    }
}
