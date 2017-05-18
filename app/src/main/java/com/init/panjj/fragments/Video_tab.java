package com.init.panjj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.Movies_itemAdap;
import com.init.panjj.adapter.TestAdap;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.model.ItemBean;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Video_tab extends Fragment implements TestAdap.ClickAction {
    RecyclerView videoconntainer;
    ArrayList<ItemBean> latestlist, featuredlist, spinerdatlist;
    MainActivity act;
    ArrayList<ItemBean> latesturl, featuredurl, spinerdaturl;
    Movies_itemAdap movies_itemAdap;
    StringRequest stringRequest;
    View rootView;
    boolean flagg;
    String datachange = "featured";
    String dtype = "";
    AVLoadingIndicatorView progressBar;
    static int moreitem = 0;
    GridLayoutManager gdd;
    String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!flagg) {
            flagg = true;
            latestlist = new ArrayList<>();
            featuredlist = new ArrayList<>();
            spinerdatlist = new ArrayList<>();
            latesturl = new ArrayList<>();
            featuredurl = new ArrayList<>();
            spinerdaturl = new ArrayList<>();
        }
        if (rootView != null)
            return rootView;
        rootView = inflater.inflate(R.layout.recyclerview, container, false);
        act = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        // tag=bundle.getString("tag");
        tag = MainFragment.tag;
        videoconntainer = (RecyclerView) rootView.findViewById(R.id.recycler_container);
        progressBar = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        gdd = new GridLayoutManager(getActivity(), 3);
       // videoconntainer.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
      videoconntainer.setLayoutManager(gdd);
        //  movies_itemAdap = new TestAdap(new MainFragment(), featuredlist, featuredurl, tag, this);
        movies_itemAdap = new Movies_itemAdap((MainActivity) getActivity(), featuredlist, featuredurl, tag);
        videoconntainer.setAdapter(movies_itemAdap);
        if (getResources().getBoolean(R.bool.istab)) {
            dtype = "t";
            Log.e("tablet", "yes");
        } else {
            dtype = "m";
            Log.e("phone", "yes");
        }
        if (flagg) {
            videoRequest();
            // addSpinerItem();
        }

        videoconntainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    Log.e("item", "visibleite" + visibleItemCount + " total" + totalItemCount + " passvible" + pastVisiblesItems);
                }
            }
        });
        return rootView;
    }

    private void videoRequest() {
        progressBar.setVisibility(View.VISIBLE);
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/MultipleVideo?skipdata=" + moreitem + "&stype=" + dtype, new Response.Listener<String>() {
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
                latesturl.add(itemBean);
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
                featuredurl.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                featuredlist.add(item);
            }
            movies_itemAdap.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            Log.e("data set error", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void changeFragment(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        New_Video_home new_video_home = New_Video_home.getInstance(featuredlist.get(position));
        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.onlyhome, new_video_home).addToBackStack(null).commit();
    }
}
