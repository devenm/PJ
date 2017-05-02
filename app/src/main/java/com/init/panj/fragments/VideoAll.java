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
public class VideoAll extends Fragment {
    View v;
    TrailerAdap allvideoAdaptor;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemBean> latestlist = new ArrayList<>();
    ArrayList<ItemBean> featuredlist = new ArrayList<>();
    StringRequest stringRequest;
    ArrayList<ItemBean> urllist = new ArrayList<>();
    ArrayList<ItemBean> urllist1 = new ArrayList<>();
    int requst = 1;
    AVLoadingIndicatorView progressBarCircular;
    String url, cat;
    String datchange = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        url = bundle.getString("url");
        cat = bundle.getString("cat");
        datchange = bundle.getString("id");
        Log.e("urls", "" + url + cat);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null)
            return v;
        v = inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_container);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        allvideoAdaptor = new TrailerAdap((MainActivity) getActivity(), latestlist, urllist,"");
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(allvideoAdaptor);
        progressBarCircular = (AVLoadingIndicatorView) v.findViewById(R.id.avi);
        if (cat.equals("song"))
            addItem();
        else if (cat.equals("movie"))
            addMovieItem();
        else if (cat.equals("trailers"))
            addTrailerItem();
        else if (cat.equals("trending"))
            addMovieItem();
        else if (cat.equals("recom"))
            addTrailerItem();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && stringRequest.hasHadResponseDelivered()) {
                    requst++;
                    if (cat.equals("song"))
                        addItem();
                    else if (cat.equals("movie"))
                        addMovieItem();
                    else if (cat.equals("trailers"))
                        addTrailerItem();
                }
            }
        });
        return v;
    }

    private void addMovieItem() {
        progressBarCircular.setVisibility(View.VISIBLE);
        Log.e("called", "item");
        stringRequest = new StringRequest(Request.Method.POST, url + requst, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setMovieValue(new JSONObject(response));
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
                stringStringHashMap.put("itemToFetch", latestlist.size() + "");
                stringStringHashMap.put("itemToFetcht", featuredlist.size() + "");
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setMovieValue(JSONObject jsonObject) {
        try {
            JSONObject jobj = jsonObject.getJSONObject("Movies");
            JSONArray latest = jobj.getJSONArray("Latest");
            JSONArray fetured = jobj.getJSONArray("Featured");
            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = latest.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                urllist.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                latestlist.add(item);
            }
            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = fetured.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                urllist1.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                featuredlist.add(item);
            }
            notifydata();
        } catch (JSONException e) {
            Log.e("data set error", e.toString());
            progressBarCircular.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void addTrailerItem() {
        progressBarCircular.setVisibility(View.VISIBLE);
        Log.e("called", "item");
        stringRequest = new StringRequest(Request.Method.POST, url + requst, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setTrailerValue(new JSONObject(response));
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
                stringStringHashMap.put("itemToFetch", latestlist.size() + "");
                stringStringHashMap.put("itemToFetcht", featuredlist.size() + "");
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setTrailerValue(JSONObject jsonObject) {
        try {
            JSONObject jobj = jsonObject.getJSONObject("Trailers");
            JSONArray latest = jobj.getJSONArray("Latest");
            JSONArray fetured = jobj.getJSONArray("Featured");
            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = latest.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                urllist.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                latestlist.add(item);
            }
            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = fetured.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                urllist1.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                featuredlist.add(item);
            }
            notifydata();
        } catch (JSONException e) {
            Log.e("data set error", e.toString());
            progressBarCircular.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void notifydata() {
        if (datchange.equals("latest")) {
            Log.e("datachange","latest");
            allvideoAdaptor = new TrailerAdap((MainActivity) getActivity(), latestlist, urllist,"");
            recyclerView.setAdapter(allvideoAdaptor);
            allvideoAdaptor.notifyDataSetChanged();
            progressBarCircular.setVisibility(View.GONE);
        } else if (datchange.equals("featured")) {
            allvideoAdaptor = new TrailerAdap((MainActivity) getActivity(), featuredlist, urllist1,"");
            recyclerView.setAdapter(allvideoAdaptor);
            allvideoAdaptor.notifyDataSetChanged();
            progressBarCircular.setVisibility(View.GONE);
        }

    }


    private void addItem() {
        progressBarCircular.setVisibility(View.VISIBLE);
        Log.e("called", "item");
        stringRequest = new StringRequest(Request.Method.POST, url + requst, new Response.Listener<String>() {
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
                stringStringHashMap.put("itemToFetch", latestlist.size() + "");
                stringStringHashMap.put("itemToFetcht", featuredlist.size() + "");
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setValue(JSONObject jsonObject) {
        try {
            JSONObject jobj = jsonObject.getJSONObject("Videos");
            JSONArray latest = jobj.getJSONArray("Latest");
            JSONArray fetured = jobj.getJSONArray("Featured");
            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = latest.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                urllist.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                latestlist.add(item);
            }

            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = fetured.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                // jsonObj.keys();
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                urllist1.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                featuredlist.add(item);
            }
            notifydata();
        } catch (JSONException e) {
            Log.e("data set error", e.toString());
            e.printStackTrace();
            progressBarCircular.setVisibility(View.GONE);
        }
    }
}