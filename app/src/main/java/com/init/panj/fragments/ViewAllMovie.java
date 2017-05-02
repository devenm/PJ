package com.init.panj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.init.panj.R;
import com.init.panj.activity.MainActivity;
import com.init.panj.adapter.AllvideoAdaptor;
import com.init.panj.adapter.TrailerAdap;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.clases.ProgressBarCircular;
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
 * Created by deepak on 3/5/2016.
 */
public class ViewAllMovie extends Fragment {
    View v;
    TrailerAdap allvideoAdaptor;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemBean> arrayList=new ArrayList<>();
    StringRequest stringRequest;
    ArrayList<ItemBean> urllist=new ArrayList<>();
    int requst=1;
    AVLoadingIndicatorView progressBarCircular;
    String url,cat;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        url = bundle.getString("url");
        cat=bundle.getString("cat");
        Log.e("urls", "" + url + cat);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v!=null)
            return v;
        v=inflater.inflate(R.layout.recyclerview,container,false);
        recyclerView= (RecyclerView) v.findViewById(R.id.recycler_container);
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        allvideoAdaptor=new TrailerAdap((MainActivity) getActivity(),arrayList,urllist,"");
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(allvideoAdaptor);
        progressBarCircular= (AVLoadingIndicatorView) v.findViewById(R.id.avi);
            addItem();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && stringRequest.hasHadResponseDelivered()) {
                    requst++;
                    addItem();
                }
            }
        });
        return v;
    }

    private void addItem() {
        progressBarCircular.setVisibility(View.VISIBLE);
        Log.e("called","item");
        stringRequest = new StringRequest(Request.Method.POST, url+requst, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setValue(new JSONObject(response));
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
                stringStringHashMap.put("itemToFetch", arrayList.size() + "");
                stringStringHashMap.put("itemToFetcht", arrayList.size() + "");
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setValue(JSONObject jsonObject) {
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("Movies");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                JSONObject jsonObject2=jsonObject1.getJSONObject("Urls");
                ItemBean itemBean=new ItemBean();
                itemBean.BT200 = jsonObject2.getString("BT200");
                itemBean.BT385 = jsonObject2.getString("BT385");
                itemBean.BT500 = jsonObject2.getString("BT500");
                itemBean.BT600 = jsonObject2.getString("BT600");
                itemBean.BT750 = jsonObject2.getString("BT750");
                itemBean.BT1000 = jsonObject2.getString("BT1000");
                itemBean.BT1500 = jsonObject2.getString("BT1500");
                urllist.add(itemBean);
                ItemBean item=new ItemBean();
                item.tredname = jsonObject1.getString("Albumname");
                item.tredcover = jsonObject1.getString("high");
                item.treddesp = jsonObject1.getString("Description");
                item.id = jsonObject1.getString("id");
                arrayList.add(item);
            }
            allvideoAdaptor.notifyDataSetChanged();
            progressBarCircular.setVisibility(View.GONE);
        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
    }
}
