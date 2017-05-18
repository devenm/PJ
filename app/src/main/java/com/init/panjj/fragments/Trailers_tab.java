package com.init.panjj.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsLogger;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.Movies_itemAdap;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class Trailers_tab extends Fragment {
    RecyclerView fivth;
    ArrayList<ItemBean>latestlist,featuredlist,spinerdatlist;
    Button more;
    MainActivity act;
    ArrayList<ItemBean>latesturl,featuredurl,spinerdaturl;
    Movies_itemAdap movies_itemAdap;
    StringRequest stringRequest;
    View rootView;
    private Context mContext;
    boolean flag = true;
    ACProgressFlower progressDialog;
    boolean flagg;
    ImageView bannerimage;
    Button latest,featured;
    TextView contentchange;
    String tag;
    String datachange="featured";
    Spinner genre;
    HashMap<String,String> getSpinerid=new HashMap<>();
  ArrayAdapter<String> spinnerAdapter;
    int spcount=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!flagg) {
            flagg = true;
            latestlist=new ArrayList<>();
            featuredlist=new ArrayList<>();
            spinerdatlist=new ArrayList<>();
            latesturl=new ArrayList<>();
            featuredurl=new ArrayList<>();
            spinerdaturl=new ArrayList<>();
              progressDialog = new ACProgressFlower.Builder(getActivity())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Loading...")
                    .build();
           // progressDialog.show();
        }
        if(rootView!=null)
            return rootView;
        rootView = inflater.inflate(R.layout.trailer_tab, container, false);
        act = (MainActivity) getActivity();
        Bundle bundle=getArguments();
        //tag=bundle.getString("tag");
        tag=MainFragment.tag;
        fivth= (RecyclerView) rootView.findViewById(R.id.firstcontent);
        bannerimage = (ImageView) rootView.findViewById(R.id.banercontent);
        GridLayoutManager gdd = new GridLayoutManager(getActivity(), 3,LinearLayoutManager.VERTICAL,false);
        fivth.setLayoutManager(gdd);
        movies_itemAdap=new Movies_itemAdap((MainActivity) getActivity(),featuredlist,featuredurl,tag);
        fivth.setAdapter(movies_itemAdap);
        more= (Button) rootView.findViewById(R.id.more);
        latest= (Button) rootView.findViewById(R.id.latest);
        featured= (Button) rootView.findViewById(R.id.featured);
        contentchange= (TextView) rootView.findViewById(R.id.contentchange);
        genre= (Spinner) rootView.findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(spinnerAdapter);
        spinnerAdapter.add("Geners");
        getSpinerid.put("Geners", "100");
        contentchange.setText("Trailers");
        fivth.setNestedScrollingEnabled(false);
        fivth.setScrollingTouchSlop(0);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleTrailers?skipdata=", "trailers", "", "Videos", datachange, 1);
            }
        });

        if (flagg){
            videoRequest();
            addSpinerItem();
        }
        ImageLoader.getInstance().displayImage("http://iiscandy.com/images/bgimage.jpg", bannerimage);
        featured.setBackgroundColor(Color.parseColor("#ae8080"));
        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datachange = "latest";
                latest.setBackgroundColor(Color.parseColor("#ae8080"));
                featured.setBackgroundColor(Color.parseColor("#876262"));
                movies_itemAdap = new Movies_itemAdap((MainActivity) getActivity(), latestlist, latesturl, tag);
                fivth.setAdapter(movies_itemAdap);
                movies_itemAdap.notifyDataSetChanged();
            }
        });
        featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datachange="featured";
                latest.setBackgroundColor(Color.parseColor("#876262"));
                featured.setBackgroundColor(Color.parseColor("#ae8080"));
                movies_itemAdap=new Movies_itemAdap((MainActivity) getActivity(),featuredlist,featuredurl,tag);
                fivth.setAdapter(movies_itemAdap);
                movies_itemAdap.notifyDataSetChanged();
            }
        });
        genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("spiner value",""+spinnerAdapter.getItem(position)+"  "+getSpinerid.get(spinnerAdapter.getItem(position)));
               if (spcount>0) {
                   addGenersContent(getSpinerid.get(spinnerAdapter.getItem(position)));
               spcount++;
               }
                else
                   spcount++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    private void addGenersContent(String gid) {
        progressDialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .build();
      //  progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/MultipleGenreContent?vid=2&skipdata=0&gid="+gid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setGenersContent(new JSONObject(response));
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

    private void setGenersContent(JSONObject jsonObject) {
        try {
            spinerdaturl.clear();
            spinerdatlist.clear();
            JSONArray jsonArray=jsonObject.getJSONArray("Content");
            if (jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = jsonObject2.getString("BT200");
                    itemBean.BT385 = jsonObject2.getString("BT385");
                    itemBean.BT500 = jsonObject2.getString("BT500");
                    itemBean.BT600 = jsonObject2.getString("BT600");
                    itemBean.BT750 = jsonObject2.getString("BT750");
                    itemBean.BT1000 = jsonObject2.getString("BT1000");
                    itemBean.BT1500 = jsonObject2.getString("BT1500");
                    itemBean.m3u8 = jsonObject2.getString("m3u8");
                    spinerdaturl.add(itemBean);
                    ItemBean item = new ItemBean();
                    item.tredname = jsonObject1.getString("Albumname");
                    item.tredcover = jsonObject1.getString("medium");
                    item.treddesp = jsonObject1.getString("Description");
                    item.id = jsonObject1.getString("id");
                    spinerdatlist.add(item);
                }
                movies_itemAdap = new Movies_itemAdap((MainActivity) getActivity(), spinerdatlist, spinerdaturl, tag);
                fivth.setAdapter(movies_itemAdap);
                movies_itemAdap.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            else
            {
             Log.e("dd","zxs");
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"No Item Found",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
    }


    private void addSpinerItem() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/genre", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setSpinerItem(new JSONObject(response));
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

    private void setSpinerItem(JSONObject jsonObject) {
        try {

            JSONArray jarry=jsonObject.getJSONArray("Geners");
            for (int i=0;i<jarry.length();i++){
                JSONObject object=jarry.getJSONObject(i);
                spinnerAdapter.add(object.getString("name"));
                getSpinerid.put(object.getString("name"),object.getString("gid"));
            }
            spinnerAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
    }

    private void videoRequest() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/MultipleTrailers?skipdata=0", new Response.Listener<String>() {
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
                latesturl.add(itemBean);
                ItemBean item=new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                latestlist.add(item);
            }
            for (int i=0;i<latest.length();i++){
                JSONObject jsonObject2=fetured.getJSONObject(i);
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
                featuredurl.add(itemBean);
                ItemBean item=new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                featuredlist.add(item);
            }
            movies_itemAdap.notifyDataSetChanged();
            progressDialog.dismiss();
        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
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
    public static void selectSpinnerItemByValue(Spinner spnr, long value){
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++)
        {
            if(adapter.getItemId(position) == value)
            {
                spnr.setSelection(position);
                return;
            }
        } }
}
