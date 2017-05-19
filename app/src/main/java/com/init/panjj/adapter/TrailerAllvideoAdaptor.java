package com.init.panjj.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.activity.SubtitlePlayer;
import com.init.panjj.model.ItemBean;
import com.init.panjj.otherclasses.CustomImageView;
import com.init.panjj.otherclasses.ProgressBarCircular;
import com.init.panjj.radioplayer.Controls;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class TrailerAllvideoAdaptor extends Adapter<TrailerAllvideoAdaptor.MyViewHolder> {
    MainActivity act;
    int coun;
    Intent intent;
    ArrayList<ItemBean> list;

    /* renamed from: com.init.panj.adapter.MoviesAllvideoAdaptor.1 */
    class C05161 implements OnClickListener {
        final /* synthetic */ ItemBean val$itemBean;
        final /* synthetic */ MyViewHolder val$myViewHolder;

        C05161(ItemBean itemBean, MyViewHolder myViewHolder) {
            this.val$itemBean = itemBean;
            this.val$myViewHolder = myViewHolder;
        }

        public void onClick(View v) {
           /* MainActivity.allurl = MoviesAllvideoAdaptor.this.list;
            MoviesAllvideoAdaptor.this.act.replaceFragment(new New_Video_home(), "cxzc", this.val$itemBean.tredcover, "videos" + this.val$itemBean.id, this.val$itemBean.tredname + " " + this.val$itemBean.treddesp, this.val$itemBean.id, this.val$myViewHolder.getAdapterPosition());
     */
            }
    }

    public class MyViewHolder extends ViewHolder {
        FrameLayout fm;
        CustomImageView img;
        ProgressBarCircular pb;
        TextView subt;
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            Display mDisplay = TrailerAllvideoAdaptor.this.act.getWindowManager().getDefaultDisplay();
            int width = mDisplay.getWidth();
            int framewitdh = width / 2;
            Log.e("hshdfkjsf", BuildConfig.VERSION_NAME + mDisplay.getHeight());
            this.fm = (FrameLayout) itemView.findViewById(R.id.mframe);
            this.fm.setLayoutParams(new LayoutParams(framewitdh, (int) TrailerAllvideoAdaptor.this.act.getResources().getDimension(R.dimen.moviescontainer_height)));
            this.pb = (ProgressBarCircular) itemView.findViewById(R.id.progress);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.img = (CustomImageView) itemView.findViewById(R.id.icon);
            this.subt = (TextView) itemView.findViewById(R.id.subtitle);
        }
    }

    public TrailerAllvideoAdaptor(Activity activity, ArrayList<ItemBean> mainlist) {
        this.coun = 0;
        this.act = (MainActivity) activity;
        this.list = mainlist;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_content, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        ItemBean itemBean = (ItemBean) this.list.get(i);
        myViewHolder.title.setText(itemBean.tredname);
        String temyear = itemBean.year;
        myViewHolder.subt.setText(itemBean.genre + " | " + itemBean.language + " | " + (temyear.isEmpty() ? BuildConfig.VERSION_NAME : temyear.substring(temyear.lastIndexOf("/") + 1, temyear.indexOf(" "))));
        myViewHolder.img.scaledImage(itemBean.tredcover);
        myViewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.pauseControl(act);
                MainActivity.allurl = list;
                Intent it = new Intent(act, SubtitlePlayer.class);
                it.putExtra("url", list.get(i).m3u8);
                it.putExtra("pos", i);
                it.putExtra("id",  list.get(i).id);
                act.startActivity(it);
            }
        });
    }

    public int getItemCount() {
        Log.e("list", BuildConfig.VERSION_NAME + this.list.size());
        return this.list.size();
    }
}
