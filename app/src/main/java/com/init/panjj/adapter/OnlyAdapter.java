package com.init.panjj.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.model.CutomBean;
import com.init.panjj.model.ItemBean;

import java.util.ArrayList;

/**
 * Created by INIT on 2/20/2017.
 */
public class OnlyAdapter extends RecyclerView.Adapter<OnlyAdapter.MyViewHolder>{
    int first=0, sec=1,third=2;
AllInOnAdapter allInOnAdapter;
    MainActivity mainActivity;
   ArrayList<ItemBean> mainlist;
    ArrayList<CutomBean> typelist;

    public OnlyAdapter(MainActivity activity, ArrayList<CutomBean> tylist) {
        typelist=tylist;
       mainActivity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_recycler,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (typelist.size()>0) {
            Log.e("callbind", "" +typelist.get(3).getArrayList());
            CutomBean cutomBean = typelist.get(position);
            switch (cutomBean.getType()) {
                case "First":
                    allInOnAdapter = new AllInOnAdapter(mainActivity, cutomBean.getArrayList());
                    holder.recyclerView.setAdapter(allInOnAdapter);
                    holder.name.setText(""+cutomBean.getName());
                case "Second":
                    allInOnAdapter = new AllInOnAdapter(mainActivity, cutomBean.getArrayList());
                    holder.recyclerView.setAdapter(allInOnAdapter);
                    holder.name.setText(""+cutomBean.getName());
                case "Third":
                    allInOnAdapter = new AllInOnAdapter(mainActivity, cutomBean.getArrayList());
                    holder.recyclerView.setAdapter(allInOnAdapter);
                    holder.name.setText(""+cutomBean.getName());
                case "Fourth":
                    allInOnAdapter = new AllInOnAdapter(mainActivity, cutomBean.getArrayList());
                    holder.recyclerView.setAdapter(allInOnAdapter);
                    holder.name.setText(""+cutomBean.getName());
                case "Fifth":
                    allInOnAdapter = new AllInOnAdapter(mainActivity, cutomBean.getArrayList());
                    holder.recyclerView.setAdapter(allInOnAdapter);
                    holder.name.setText(""+cutomBean.getName());
            }
        }

    }

   /* private void setthirdItem(SetThird holder, int position) {
        Log.e("call3",""+holder);

    }

    private void setSecItem(SetSec holder, int position) {
        Log.e("call2",""+holder);

    }

    private void setFurstItem(SetFirst holder, int position) {
        Log.e("call1",""+holder);

    }*/

   /* @Override
    public int getItemViewType(int position) {
        Log.e("callview",""+position);
        switch (position)
        {
            case 0:
                return first;
            case 1:
                return sec;
            case 2:
                return third;
            default:
                return -1;
        }
    }*/

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView name,see_all;
        public MyViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.cat_name);
            see_all= (TextView) itemView.findViewById(R.id.see_all);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.onlycontainer);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL,false));
        }
    }

   /* private class SetFirst extends MyViewHolder {
        RecyclerView firstitem;
        public SetFirst(View inflate) {
            super(inflate);
            firstitem= (RecyclerView) itemView.findViewById(R.id.childcontent);
            firstitem.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL,false));
            commenAdapter=new CommenAdapter(mainActivity,arrayList1);
            firstitem.setAdapter(commenAdapter);
        }
    }

    private class SetSec extends MyViewHolder {
        RecyclerView secitem;
        public SetSec(View inflate) {
            super(inflate);
            secitem= (RecyclerView) itemView.findViewById(R.id.childcontent);
            secitem.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL,false));
            commenAdapterSec=new CommenAdapterSec(mainActivity,arrayList2);
            secitem.setAdapter(commenAdapterSec);
        }
    }

    private class SetThird extends MyViewHolder{
        RecyclerView thirditem;
        public SetThird(View inflate) {
            super(inflate);
            thirditem= (RecyclerView) itemView.findViewById(R.id.childcontent);
            thirditem.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL,false));
            commenAdapterThird=new CommenAdapterThird(mainActivity,arrayList3);
            thirditem.setAdapter(commenAdapterThird);
        }
    }
*/

}
