package com.init.panjj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.internal.NativeProtocol;
import com.facebook.share.internal.ShareConstants;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.TrailerAdap;
import com.init.panjj.model.ItemBean;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 4/27/2016.
 */
public class ViewAll_Tab extends DialogFragment {
    TrailerAdap allvideoAdaptor;
    ImageView back;
    String cat;
    String datchange;
    ArrayList<ItemBean> featuredlist;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemBean> latestlist;
    String name;
    AVLoadingIndicatorView progressBarCircular;
    RecyclerView recyclerView;
    int requst;
    StringRequest stringRequest;
    TextView title;
    String url;
    ArrayList<ItemBean> urllist;
    ArrayList<ItemBean> urllist1;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME,android.R.style.Theme_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_view_all, container, false);
        this.latestlist = new ArrayList();
        this.featuredlist = new ArrayList();
        this.urllist = new ArrayList();
        this.urllist1 = new ArrayList();
        this.requst = 1;
        this.datchange = "";
        this.back = (ImageView) this.view.findViewById(R.id.back);
        this.title = (TextView) this.view.findViewById(R.id.title);
        Bundle bundle = getArguments();
        this.url = bundle.getString(NativeProtocol.WEB_DIALOG_URL);
        this.cat = bundle.getString("cat");
        this.datchange = bundle.getString("id");
        this.name = bundle.getString(ShareConstants.WEB_DIALOG_PARAM_NAME);
        Log.e("urls",""+ this.url + this.datchange);
        this.datchange = this.datchange.replace(" ", "");
        this.recyclerView = (RecyclerView) this.view.findViewById(R.id.recycler_container);
        this.gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.allvideoAdaptor = new TrailerAdap((MainActivity) getActivity(), this.latestlist, this.urllist, "");
        this.recyclerView.setLayoutManager(this.gridLayoutManager);
        this.recyclerView.setAdapter(this.allvideoAdaptor);
        this.progressBarCircular = (AVLoadingIndicatorView) this.view.findViewById(R.id.avi);
        this.allvideoAdaptor = new TrailerAdap((MainActivity) getActivity(), this.featuredlist, this.urllist1, "");
        this.recyclerView.setAdapter(this.allvideoAdaptor);
        this.title.setText("" + this.name);
        this.featuredlist.clear();
        this.urllist.clear();
        this.allvideoAdaptor = new TrailerAdap((MainActivity) getActivity(), this.featuredlist, this.urllist1, "");
        this.recyclerView.setAdapter(this.allvideoAdaptor);
        addItem();
        this.title.setText(""+ this.name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
dismiss();
            }
        });
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
        return view;
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
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
            allvideoAdaptor.notifyDataSetChanged();
            progressBarCircular.setVisibility(View.GONE);
        } else if (true) {
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setValue(JSONObject jsonObject) {
        try {
            this.progressBarCircular.setVisibility(View.GONE);
            JSONArray fetured = jsonObject.getJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA);
            for (int i = 0; i < fetured.length(); i++) {
                JSONObject jsonObject2 = fetured.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                itemBean.m3u8 = jsonObj.getString("m3u8");
                this.urllist1.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                this.featuredlist.add(item);
            }
            this.allvideoAdaptor.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("data set error", e.toString());
            e.printStackTrace();
            this.progressBarCircular.setVisibility(View.GONE);
        }
    }
    }

