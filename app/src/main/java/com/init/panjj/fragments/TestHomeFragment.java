package com.init.panjj.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.AdapterLive;
import com.init.panjj.adapter.OnlyAdapter;
import com.init.panjj.otherclasses.AutoScrollViewPager;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.model.CutomBean;
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

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class TestHomeFragment extends Fragment {
    RecyclerView first_container, live_container;

    ArrayList<ItemBean> bannerlist, live_list;
    MainActivity act;
    ArrayList<ItemBean> banner_url, live_url;
    ArrayList<LiveTvBean> listlive;
    StringRequest stringRequest;
    OnlyAdapter onlyAdapter;
    AdapterLive liveTvAdaptor;
    SharedPreferences sharedPreferences;
    View rootView;
    String dtype = "m";
    ACProgressFlower progressDialog;
    LoadImageToView switchImage;
    AutoScrollViewPager viewPager;
    ImageView facebookloginbutton;
    private CallbackManager callbackManager;
    ScrollView scrollView;
    String username = "", userimage = "", useremail = "", password = "";
    HashMap<Integer, String> hashMap;
    ArrayList<CutomBean> sortlist;
    String first_name, second_name, third_name, fourth_name, fivth_name;
    ArrayList<CutomBean> tylist;
    ArrayList<ItemBean> mainlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
    }

    boolean flagg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!flagg) {
            flagg = true;
            bannerlist = new ArrayList<>();
            live_list = new ArrayList<>();
            banner_url = new ArrayList<>();
            live_url = new ArrayList<>();
            listlive = new ArrayList<>();
            hashMap = new HashMap<>();
            mainlist = new ArrayList<>();
            tylist = new ArrayList<>();
            progressDialog = new ACProgressFlower.Builder(getActivity())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Loading...")
                    .build();
            progressDialog.setCancelable(false);
        }
        rootView = inflater.inflate(R.layout.testhom, container, false);
        act = (MainActivity) getActivity();

        if (getResources().getBoolean(R.bool.istab)) {
            dtype = "t";
            Log.e("tablet", "yes");
        } else {
            dtype = "m";
            Log.e("phone", "yes");
        }
        scrollView = (ScrollView) rootView.findViewById(R.id.scrol);
        first_container = (RecyclerView) rootView.findViewById(R.id.trendung_recycler);
        live_container = (RecyclerView) rootView.findViewById(R.id.live_recycler);
        viewPager = (AutoScrollViewPager) rootView.findViewById(R.id.banercontent);
        facebookloginbutton = (ImageView) rootView.findViewById(R.id.facebooklogin);
        first_container.setHasFixedSize(false);
        first_container.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));
        live_container.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        switchImage = new LoadImageToView(bannerlist, banner_url);
        liveTvAdaptor = new AdapterLive(getActivity(), listlive);
        viewPager.setAdapter(switchImage);
        live_container.setAdapter(liveTvAdaptor);
        first_container.setNestedScrollingEnabled(false);
        onlyAdapter = new OnlyAdapter((MainActivity) getActivity(), tylist);
        first_container.setAdapter(onlyAdapter);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        circlePageIndicator.setRadius(4 * density);
        viewPager.setCurrentItem(0);
        viewPager.startAutoScroll();
        viewPager.setInterval(4000);
        viewPager.setScrollDurationFactor(2);
        if (flagg) {
            jsonRequest();
            jsonRequestBanner();
            // jsonRequestFeatured();
            //jsonRequestTrailer();
            //  jsonRequestTrending();
            // jsonRequestVideo();
            // recommendRequest();
            livetvdata();
        }
        //Create callback manager to handle login response
        return rootView;
    }

    private void livetvdata() {
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void setLiveTv(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("LiveStream");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    LiveTvBean liveTvBean = new LiveTvBean();
                    liveTvBean.setTvcount(jsonObject1.getString("count"));
                    liveTvBean.setTvimage(jsonObject1.getString("Image"));
                    liveTvBean.setTvname(jsonObject1.getString("Name"));
                    listlive.add(liveTvBean);
                }
                liveTvAdaptor.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("liveExcep", "" + e.toString());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void jsonRequestBanner() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeBannerList?stype=" + dtype, new Response.Listener<String>() {
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
            bannerlist.clear();
            banner_url.clear();
            progressDialog.dismiss();
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


            Log.e("bannererror", e.toString());
        }
    }

    class LoadImageToView extends PagerAdapter {
        ArrayList<ItemBean> list;
        ArrayList<ItemBean> bannrurl;

        public LoadImageToView(ArrayList<ItemBean> itemlist, ArrayList<ItemBean> url) {
            list = itemlist;
            bannrurl = url;


        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = getActivity().getLayoutInflater().inflate(R.layout.pagerlargeimage, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);
            ImageLoader.getInstance().displayImage(list.get(position).newcover, (ImageView) imageView);
            imageLayout.setTag(position);
            view.addView(imageLayout, 0);
            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.allurl = bannrurl;
                    act.replaceFragment(new New_Video_home(), bannrurl.get(position).BT200, list.get(position).newcover, "videos", list.get(position).albumname, list.get(position).id, position);
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


    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }

    private void jsonRequest() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/lpjson", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValue(new JSONObject(response));
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

    private void setJsonValue(JSONObject jsonObject) {
        try {
            JSONObject mainjson = jsonObject.getJSONObject("data");
            try {
                mainlist=new ArrayList<>();
                JSONArray first = mainjson.getJSONArray("First");
                for (int j = 0; j < first.length(); j++) {
                    JSONObject jsonObject2 = first.getJSONObject(j);
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
                    itemBean.tredname = jsonObject2.getString("Albumname");
                    itemBean.tredcover = jsonObject2.getString("medium");
                    itemBean.treddesp = jsonObject2.getString("Description");
                    itemBean.id = jsonObject2.getString("id");
                    first_name = jsonObject2.getString("Name");
                    mainlist.add(itemBean);
                }
                CutomBean cutomBean=new CutomBean();
                cutomBean.setType("First");
                cutomBean.setName(first_name);
                cutomBean.setArrayList(mainlist);
                tylist.add(cutomBean);
            } catch (Exception e) {
                Log.e("first", "" + e.toString());
            }
            try {
mainlist=new ArrayList<>();
                JSONArray second = mainjson.getJSONArray("Second");
                for (int j = 0; j < second.length(); j++) {
                    JSONObject jsonObject2 = second.getJSONObject(j);
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
                    itemBean.tredname = jsonObject2.getString("Albumname");
                    itemBean.tredcover = jsonObject2.getString("medium");
                    itemBean.id = jsonObject2.getString("id");
                    second_name = jsonObject2.getString("Name");
                    mainlist.add(itemBean);
                }
                CutomBean cutomBean=new CutomBean();
                cutomBean.setType("Second");
                cutomBean.setName(second_name);
                cutomBean.setArrayList(mainlist);
                tylist.add(cutomBean);

            } catch (Exception e) {
                Log.e("sec", "" + e.toString());
            }
            try {
mainlist=new ArrayList<>();
                JSONArray third = mainjson.getJSONArray("Third");
                for (int j = 0; j < third.length(); j++) {
                    JSONObject jsonObject2 = third.getJSONObject(j);
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
                    itemBean.tredname = jsonObject2.getString("Albumname");
                    itemBean.tredcover = jsonObject2.getString("medium");
                    itemBean.treddesp = jsonObject2.getString("Artist");
                    itemBean.id = jsonObject2.getString("id");
                    third_name = jsonObject2.getString("Name");
                    mainlist.add(itemBean);
                }
                CutomBean cutomBean=new CutomBean();
                cutomBean.setType("Third");
                cutomBean.setName(third_name);
                cutomBean.setArrayList(mainlist);
                tylist.add(cutomBean);
            } catch (Exception e) {
                Log.e("third", "" + e.toString());
            }
            try {
                mainlist=new ArrayList<>();
                JSONArray fourth = mainjson.getJSONArray("Fourth");
                for (int j = 0; j < fourth.length(); j++) {
                    JSONObject jsonObject2 = fourth.getJSONObject(j);
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
                    itemBean.tredname = jsonObject2.getString("Albumname");
                    itemBean.tredcover = jsonObject2.getString("medium");
                    itemBean.id = jsonObject2.getString("id");
                    fourth_name = jsonObject2.getString("Name");
                    mainlist.add(itemBean);
                }
                CutomBean cutomBean=new CutomBean();
                cutomBean.setType("Fourth");
                cutomBean.setName(fourth_name);
                cutomBean.setArrayList(mainlist);
                tylist.add(cutomBean);
            } catch (Exception e) {
                Log.e("fourth", "" + e.toString());
            }
            try {
mainlist=new ArrayList<>();
                JSONArray fivth = mainjson.getJSONArray("Fifth");
                for (int j = 0; j < fivth.length(); j++) {
                    JSONObject jsonObject2 = fivth.getJSONObject(j);
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
                    itemBean.tredname = jsonObject2.getString("Albumname");
                    itemBean.tredcover = jsonObject2.getString("medium");
                    itemBean.id = jsonObject2.getString("id");
                    fivth_name = jsonObject2.getString("Name");
                    mainlist.add(itemBean);
                }
                CutomBean cutomBean=new CutomBean();
                cutomBean.setType("Fifth");
                cutomBean.setName(fivth_name);
                cutomBean.setArrayList(mainlist);
                tylist.add(cutomBean);
            } catch (Exception e) {
                Log.e("fivth", "" + e.toString());
            }
            onlyAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }

    }

}
