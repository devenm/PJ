package com.init.panjj.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panjj.R;
import com.init.panjj.adapter.RadioAdapter;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.radioplayer.RadioBean;
import com.init.panjj.radioplayer.Rplayer;
import com.init.panjj.radioplayer.SongService1;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 11/7/2016.
 */

public class RadioFragment extends Fragment implements RadioAdapter.ClickAction{
    RecyclerView recyclerView;
    RadioAdapter radioAdapter;
    ArrayList<RadioBean> radiolist;
    StringRequest stringRequest;
    FloatingActionButton floatingActionButton;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.livetvfrag,container,false);
        floatingActionButton= (FloatingActionButton) view.findViewById(R.id.radioplayfab);
        floatingActionButton.setVisibility(View.VISIBLE);
        avLoadingIndicatorView= (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        radiolist=new ArrayList<>();
        radioAdapter=new RadioAdapter(getActivity(),radiolist,this);
        recyclerView.setAdapter(radioAdapter);
        livetvdata();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Rplayer.class);
                intent.putExtra("list",radiolist);
                intent.putExtra("position",1010);
                startActivity(intent);
            }
        });
        if (isMyServiceRunning(SongService1.class))
            floatingActionButton.setVisibility(View.VISIBLE);
        else
        floatingActionButton.setVisibility(View.GONE);
        return view;
    }
    private void livetvdata() {
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/LiveRadioChannels", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setLiveTv(new JSONObject(response));
                        Log.e("value", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("oho Error ", "Error: " + error.toString());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                return stringStringHashMap;
            }
        };
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void setLiveTv(JSONObject jsonObject) {
        try {
            if (jsonObject!=null)
            {
                avLoadingIndicatorView.setVisibility(View.GONE);
                JSONArray jsonArray=jsonObject.getJSONArray("LiveStream");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    RadioBean radioBean=new RadioBean();
                    radioBean.setRadioid(jsonObject1.getString("count"));
                    radioBean.setByteradioimg(jsonObject1.getString("Image").getBytes());
                    radioBean.setRadioname(jsonObject1.getString("Name"));
                    radioBean.setRadioimage(jsonObject1.getString("Image"));
                    radiolist.add(radioBean);
                }
                radioAdapter.notifyDataSetChanged();
            }        }
        catch (Exception e){
            Log.e("liveExcep",""+e.toString());
        }
    }

    @Override
    public void onAction(int position) {
        Intent intent = new Intent(getActivity(), Rplayer.class);
        intent.putExtra("list", radiolist);
        intent.putExtra("position",position);
         startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMyServiceRunning(SongService1.class))
            floatingActionButton.setVisibility(View.VISIBLE);
        else
            floatingActionButton.setVisibility(View.GONE);
    }
}
