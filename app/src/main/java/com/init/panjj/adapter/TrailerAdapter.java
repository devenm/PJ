package com.init.panjj.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.fragments.ViewAll_Tab;
import com.init.panjj.model.CutomBean;
import com.init.panjj.model.ItemBean;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class TrailerAdapter extends Adapter<TrailerAdapter.MyViewHolder> {
    TrailerAllvideoAdaptor allInOnAdapter;
    MainActivity mainActivity;
    ArrayList<ItemBean> mainlist;
    ArrayList<CutomBean> typelist;

    /* renamed from: com.init.panj.adapter.MoviesAdapter.1 */
    class C05151 implements OnClickListener {
        final /* synthetic */ CutomBean val$cutomBean;

        C05151(CutomBean cutomBean) {
            this.val$cutomBean = cutomBean;
        }

        public void onClick(View v) {
            mainActivity.dialogshow(new ViewAll_Tab(), "http://iiscandy.com//panj/PanjSeaAll?name=", this.val$cutomBean.getActualName(), BuildConfig.VERSION_NAME, "featured", 1, this.val$cutomBean.getActualName());
        }
    }

    public class MyViewHolder extends ViewHolder {
        TextView name;
        RecyclerView recyclerView;
        TextView see_all;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.cat_name);
            this.see_all = (TextView) itemView.findViewById(R.id.see_all);
            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.onlycontainer);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), 0, false));
        }
    }

    public TrailerAdapter(MainActivity activity, ArrayList<CutomBean> tylist) {
        this.typelist = tylist;
        this.mainActivity = activity;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_movies_recycler, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (this.typelist.size() > 0) {
            CutomBean cutomBean = (CutomBean) this.typelist.get(position);
            holder.see_all.setOnClickListener(new C05151(cutomBean));
            this.allInOnAdapter = new TrailerAllvideoAdaptor(this.mainActivity, cutomBean.getArrayList());
            holder.recyclerView.setAdapter(this.allInOnAdapter);
            holder.name.setText(BuildConfig.VERSION_NAME + cutomBean.getActualName());
        }
    }

    public int getItemCount() {
        return this.typelist.size();
    }
}
