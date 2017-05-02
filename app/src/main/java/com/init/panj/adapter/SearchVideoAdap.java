package com.init.panj.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.init.panj.clases.CustomImageView;
import com.init.panj.clases.ProgressBarCircular;
import com.init.panj.clases.SearchDialog;
import com.init.panj.fragments.New_Video_home;
import com.init.panj.fragments.Video_home;
import com.init.panj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by deepak on 10/19/2015.
 */
public class SearchVideoAdap extends RecyclerView.Adapter<SearchVideoAdap.MyViewHolder> {
    ArrayList<ItemBean> list;
    ArrayList<ItemBean> urllist;
    MainActivity act;
    Intent intent;
    int coun=0;
    SearchDialog searchDialog;
    public SearchVideoAdap(MainActivity mainActivity, ArrayList<ItemBean> mainlist, ArrayList<ItemBean> url3, SearchDialog searchDialog) {
        act=mainActivity;
        list=mainlist;
        urllist=url3;
        this.searchDialog=searchDialog;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup,false));
    }



    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final ItemBean itemBean=list.get(i);
      /*  myViewHolder.title.setText(itemBean.albumname);
        myViewHolder.subt.setText(itemBean.albumdesp);*/
        myViewHolder.img.scaledImage(itemBean.albumcover);
        myViewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                it.putExtra("id",itemBean.id);
                act.startActivity(it);*/

                MainActivity.allurl=urllist;
                act.replaceFragment(new New_Video_home(),"sdss",itemBean.albumcover,"Movies", itemBean.albumname + " " + itemBean.albumdesp, itemBean.id, 1);
                searchDialog.dismiss();
            }
        });
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.albumcover).placeholder(R.drawable.ic_profile).fit().into(myViewHolder.img);
        /*ImageLoader.getInstance().loadImage(itemBean.albumcover,new SimpleImageLoadingListener() {
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
     //   ProgressBarCircular pb;
        FrameLayout fm;
        public MyViewHolder(View itemView) {
            super(itemView);
            Display mDisplay = act.getWindowManager().getDefaultDisplay();
            final int width  = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            int framewitdh=width/3;
            Log.e("hshdfkjsf",""+height);
            fm= (FrameLayout) itemView.findViewById(R.id.mframe);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(framewitdh, (int) act.getResources().getDimension(R.dimen.moviescontainer_height));
            fm.setLayoutParams(lp);
           // pb= (ProgressBarCircular) itemView.findViewById(R.id.progress);
           // title= (TextView) itemView.findViewById(R.id.title);
            img= (CustomImageView) itemView.findViewById(R.id.trailerimage);
           // subt= (TextView) itemView.findViewById(R.id.subtitle);
           // cd= (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
