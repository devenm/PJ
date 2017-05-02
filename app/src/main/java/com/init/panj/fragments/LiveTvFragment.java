package com.init.panj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.init.panj.R;
import com.init.panj.adapter.AdapterLive;
import com.init.panj.adapter.LiveTvAdaptor;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.model.LiveTvBean;
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

public class LiveTvFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterLive liveTvAdaptor;
    ArrayList<LiveTvBean> list;
    StringRequest stringRequest;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.livetvfrag,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_container);
        avLoadingIndicatorView= (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        list=new ArrayList<>();
        liveTvAdaptor=new AdapterLive(getActivity(),list);
        recyclerView.setAdapter(liveTvAdaptor);
        livetvdata();
        return view;
    }
    private void livetvdata() {
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/LiveStreamChannels", new Response.Listener<String>() {
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
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void setLiveTv(JSONObject jsonObject) {
        try {
            avLoadingIndicatorView.setVisibility(View.GONE);
            if (jsonObject!=null)
            {
                JSONArray jsonArray=jsonObject.getJSONArray("LiveStream");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    LiveTvBean liveTvBean=new LiveTvBean();
                    liveTvBean.setTvcount(jsonObject1.getString("count"));
                    liveTvBean.setTvimage(jsonObject1.getString("Image"));
                    liveTvBean.setTvname(jsonObject1.getString("Name"));
                    list.add(liveTvBean);
                }
                liveTvAdaptor.notifyDataSetChanged();
            }        }
        catch (Exception e){
            Log.e("liveExcep",""+e.toString());
        }
    }
}
