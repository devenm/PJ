package com.init.panjj.fragments;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.TrendingAdaptor;
import com.init.panjj.adapter.New_MainContentAdoptor;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.model.ItemBean;
import com.init.panjj.model.LiveTvBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by INIT on 1/4/2017.
 */

public class New_Fragments extends Fragment{
    RecyclerView firstcontainer, secitemcontainer;
    ArrayList<ItemBean> trenging_list,movies_list;
     ArrayList<ItemBean> bannerlist;
    MainActivity act;
    ArrayList<ItemBean> trending_url,movies_url,banner_url;
    ArrayList<LiveTvBean> listlive;
    StringRequest stringRequest;
    TrendingAdaptor moviesadap;
    New_MainContentAdoptor trendingadap;
  LoadImageToView switchImage;
    int p;
    ViewPager viewPager;
    boolean flagg;
    View rootView;

CirclePageIndicator circlePageIndicator;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!flagg) {
            flagg = true;
            bannerlist=new ArrayList<>();
            banner_url=new ArrayList<>();
         trenging_list=new ArrayList<>();
            movies_list=new ArrayList<>();
            trending_url=new ArrayList<>();
            movies_url=new ArrayList<>();
        }
        rootView = inflater.inflate(R.layout.new_home, container, false);
        act = (MainActivity) getActivity();
        firstcontainer = (RecyclerView) rootView.findViewById(R.id.firstcontent);
        secitemcontainer = (RecyclerView) rootView.findViewById(R.id.seccontent);
        viewPager = (ViewPager) rootView.findViewById(R.id.banercontent);
        circlePageIndicator= (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        firstcontainer.setHasFixedSize(false);
        firstcontainer.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false));
         secitemcontainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        moviesadap = new TrendingAdaptor(getActivity(), movies_list, movies_url);
        trendingadap = new New_MainContentAdoptor((MainActivity) getActivity(), trenging_list, trending_url);
        switchImage = new LoadImageToView(getActivity(), bannerlist, banner_url);
        viewPager.setAdapter(switchImage);
        circlePageIndicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        circlePageIndicator.setRadius(4 * density);
        firstcontainer.setAdapter(trendingadap);
        secitemcontainer.setAdapter(moviesadap);
        if (flagg) {
            //jsonRequest();
            jsonRequestBanner();
            jsonRequestTrending();


        }

        return rootView;
    }
    private void jsonRequestBanner() {

        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeBannerList?stype=t", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueBanner(new JSONObject(response));
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

    private void setJsonValueBanner(JSONObject jsonObject) {
        try {
            JSONArray banner = jsonObject.getJSONArray("data");
            for (int i = 0; i < banner.length(); i++) {
                JSONObject bannerdata = banner.getJSONObject(i);
                JSONObject js = bannerdata.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                banner_url.add(itemBean);
                ItemBean items = new ItemBean();
                items.albumname = bannerdata.getString("bannerurl");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.newcover = bannerdata.getString("bannerurl");
                } else
                    items.newcover = bannerdata.getString("bannerurl");
                items.id = bannerdata.getString("id");
                bannerlist.add(items);
            }
            switchImage.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
        } catch (Exception e) {

        }
    }
    private void jsonRequestTrending() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeTranding?stype=t", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueTrending(new JSONObject(response));
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

    private void setJsonValueTrending(JSONObject jsonObject) {
        try {
            JSONArray trending = jsonObject.getJSONArray("data");
            for (int j = 0; j < trending.length(); j++) {
                JSONObject jsonObject2 = trending.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                trending_url.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.treddesp = jsonObject2.getString("Description");
                items.id = jsonObject2.getString("id");
                trenging_list.add(items);
            }
            trendingadap.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }
    class LoadImageToView extends PagerAdapter {
        ArrayList<ItemBean> list;
        ArrayList<ItemBean> bannrurl;

        public LoadImageToView(FragmentActivity activity, ArrayList<ItemBean> itemlist, ArrayList<ItemBean> url) {
            list = itemlist;
            bannrurl = url;
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            Log.e("viewpager", "call");
            View imageLayout = getActivity().getLayoutInflater().inflate(R.layout.pagerlargeimage, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);
            ImageLoader.getInstance().displayImage(list.get(position).newcover, (ImageView) imageView);
            imageLayout.setTag(position);
            view.addView(imageLayout, 0);
            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent it = new Intent(act, PlayVideo.class);
                    it.putExtra("list", new UrlBean(bannrurl));
                    it.putExtra("position", position);
                    it.putExtra("id", list.get(position).id);
                    startActivity(it);*/
                  /*  Intent it = new Intent(act, FullPlayAct.class);
                    it.putExtra("url",bannrurl.get(position).BT500);
                    startActivity(it);*/
                    Log.e("bannerid", "" + list.get(position).id);
                    MainActivity.allurl = bannrurl;
                    act.replaceFragment(new Video_home(), bannrurl.get(position).BT200, list.get(position).newcover, "videos", list.get(position).albumname, list.get(position).id, position);
                    /*Intent it = new Intent(act, DummyPlay.class);
                  startActivity(it);*/
                }
            });
            return imageLayout;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
