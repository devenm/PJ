package com.init.panj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panj.R;
import com.init.panj.activity.MainActivity;
import com.init.panj.adapter.TrailerAdap;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.model.ItemBean;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by INIT on 1/30/2017.
 */

public class NewTrailer_Tab extends Fragment {
    RecyclerView trailercontainer;
    ArrayList<ItemBean> trailerlist;
    ArrayList<ItemBean>trailerurl;
    TrailerAdap trailerAdap;
    StringRequest stringRequest;
    View rootView;
    boolean flag = true;
    boolean flagg;
MainActivity act;
    int moreitem=0;
    GridLayoutManager gdd;
    String dtype="";
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!flagg) {
            flagg = true;
            trailerlist=new ArrayList<>();
            trailerurl=new ArrayList<>();
        }
        if(rootView!=null)
            return rootView;
        rootView = inflater.inflate(R.layout.recyclerview, container, false);
        act = (MainActivity) getActivity();
        trailercontainer= (RecyclerView) rootView.findViewById(R.id.recycler_container);
        avLoadingIndicatorView= (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        gdd = new GridLayoutManager(getActivity(), 3);
        trailercontainer.setLayoutManager(gdd);
        trailerAdap=new TrailerAdap((MainActivity) getActivity(),trailerlist,trailerurl,"Trailer");
        trailercontainer.setAdapter(trailerAdap);
        if(getResources().getBoolean(R.bool.istab)) {
            dtype="t";
            Log.e("tablet","yes");
        } else {
            dtype="m";
            Log.e("phone","yes");
        }
        videoRequest();
        trailercontainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = gdd.getChildCount();
                int totalItemCount = gdd.getItemCount();
                int pastVisiblesItems = gdd.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (stringRequest.hasHadResponseDelivered()) {
                        moreitem++;
                        videoRequest();
                    }
                    Log.e("item","visibleite"+visibleItemCount +" total"+totalItemCount+ " passvible"+pastVisiblesItems);
                }
            }
        });
        return rootView;
    }
    private void videoRequest() {
        avLoadingIndicatorView.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/MultipleTrailers?skipdata="+moreitem+"&stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setValue(new JSONObject(response));
                        Log.e("valuetrailer", "" + response);
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

    private void setValue(JSONObject jsonObject) {
        try {

            JSONObject jobj=jsonObject.getJSONObject("Trailers");
            JSONArray latest=jobj.getJSONArray("Latest");
            JSONArray fetured=jobj.getJSONArray("Featured");
            for (int i=0;i<latest.length();i++){
                JSONObject jsonObject2=latest.getJSONObject(i);
                JSONObject jsonObj=jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean=new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                trailerurl.add(itemBean);
                ItemBean item=new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                trailerlist.add(item);
            }
trailerAdap.notifyDataSetChanged();
            avLoadingIndicatorView.hide();
        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
    }

}
