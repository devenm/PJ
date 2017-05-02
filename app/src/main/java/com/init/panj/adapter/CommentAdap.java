package com.init.panj.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.init.panj.R;
import com.init.panj.clases.ProgressBarCircular;
import com.init.panj.model.CommentBean;
import com.init.panj.model.ItemBean;
import com.init.panj.model.UrlBean;

import java.util.ArrayList;
/**
 * Created by deepak on 10/19/2015.
 */
public class CommentAdap extends RecyclerView.Adapter<CommentAdap.MyViewHolder> {
    ArrayList<CommentBean> list;
    ArrayList<ItemBean> urllist;
    Activity act;
    Intent intent;
    RecyclerView recyclerView;
    int coun=0;
    UrlBean urlBean;
    //Fragment frag[]={newr SecondAct(),newr Yutube(),newr Audio(),newr Ring(),newr Wallpaper(),newr Schedule(),newr ContactUs()};
    // String color[] = {"#039be5","#ff6d00","#cddc39","#448aff","#42a5f5","#00bcd4","#8bc34a","#ffc107"};
    public CommentAdap(Activity mainActivity, ArrayList<CommentBean> mainlist, RecyclerView commentrecycler) {
        act = mainActivity;
        list = mainlist;
recyclerView=commentrecycler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comentadop, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 500; //height recycleviewer
            recyclerView.setLayoutParams(params);

        final CommentBean itemBean = list.get(i);
        // myViewHolder.img.setImageResource(itemBean.displayicon);
        // Picasso.with(myViewHolder.itemView.getContext()).load(itemBean.tredcover).placeholder(R.mipmap.musicload).fit().into(myViewHolder.img);
        myViewHolder.usernsme.setText(itemBean.username);
        myViewHolder.comment.setText(itemBean.comment);
        myViewHolder.date.setText(itemBean.date);
        Log.e("liststst",""+list);
        // myViewHolder.subt.setText(itemBean.treddesp);
        /*myViewHolder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // act.replaceFragment(abc Audio(), itemBean.id, itemBean.tredcover, itemBean.tredname, itemBean.treddesp, "http://52.74.238.77/savaan_nirmolak/albumsong.php", "");

                Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                act.startActivity(it);
            }
        });*/
       /* ImageLoader.getInstance().loadImage(itemBean.profilepic, new  SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                myViewHolder.profileimage.setImageResource(R.mipmap.musicload);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                myViewHolder.profileimage.setImageBitmap(loadedImage);
                //myViewHolder.pb.setVisibility(View.GONE);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView usernsme, comment,date;
        ImageView profileimage;
        CardView cd;
        ProgressBarCircular pb;
        public MyViewHolder(View itemView) {
            super(itemView);
            usernsme = (TextView) itemView.findViewById(R.id.username);
            profileimage = (ImageView) itemView.findViewById(R.id.profile_image);
            comment = (TextView) itemView.findViewById(R.id.comment);
            date= (TextView) itemView.findViewById(R.id.date);
//            cd = (CardView) itemView.findViewById(R.id.card_view);
//            pb= (ProgressBarCircular) itemView.findViewById(R.id.progress);
        }
    }
}
