package com.init.panjj.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.init.panjj.activity.SubtitlePlayer;
import com.init.panjj.activity.UserLogin;
import com.init.panjj.adapter.TrendingAdaptor;
import com.init.panjj.adapter.MainContentAdoptor1;
import com.init.panjj.adapter.MainContentAdoptor3;
import com.init.panjj.adapter.SimilerAdopter;
import com.init.panjj.otherclasses.Download;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.otherclasses.SearchDialogArtist;
import com.init.panjj.model.CommentBean;
import com.init.panjj.model.Data;
import com.init.panjj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class Video_home extends Fragment {
    Dialog dialog;
    RecyclerView firstcontainer, secitemcontainer,similar;
public static ArrayList<ItemBean> recommendvideolist,forrecomnedurl,vrecommendurl;
     ArrayList<ItemBean> bannerlist,recomendtrailerlist,recommendforlist;
    TextView  feturedall, trailerall, songall;
    MainActivity act;
    ArrayList<ItemBean> url, singlalleurl, url2, url3, url4, url5,trecomendurl,singleurl;
    StringRequest stringRequest;
    TrendingAdaptor adoptor;
    MainContentAdoptor1 adoptor1;
    MainContentAdoptor3 adoptor3;
    LinearLayout recomondedtra,recomendvideo;
    View rootView;
    TextView info,download,like,views;
    ImageButton share;
    String shareurl="",dlinks;
    int cn = 0;
    ArrayList<ItemBean> list=new ArrayList<>();
    LoadImageToView switchImage;
    int p;
    ViewPager viewPager;
    SimilerAdopter similerAdopter;
    String videourl,cover;
    TextView similardata;
    String information;
    String id;
    String likecount,viewcount;
    int pos;
    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<Data> al;
String tag;
    String infoartist,infoproducer,infodirector,infomusicdirector,infotitle="null",infostarimg;
    ACProgressFlower progressDialog;
    public static void removeBackground() {
        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null)
            return rootView;
        rootView = inflater.inflate(R.layout.videos_home, container, false);
        act = (MainActivity) getActivity();
        progressDialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .build();
        progressDialog.setCancelable(false);
Bundle bundle=getArguments();
        videourl=bundle.getString("url");
        cover=bundle.getString("cat");
        information=bundle.getString("info");
        tag=bundle.getString("tag");
        id=bundle.getString("id");
        pos=bundle.getInt("pos");
        //  flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id.frame);
        firstcontainer = (RecyclerView) rootView.findViewById(R.id.firstcontent);
        secitemcontainer = (RecyclerView) rootView.findViewById(R.id.seccontent);
        similar= (RecyclerView) rootView.findViewById(R.id.similar);
        viewPager = (ViewPager) rootView.findViewById(R.id.banercontent);
        feturedall = (TextView) rootView.findViewById(R.id.featuredall);
        songall = (TextView) rootView.findViewById(R.id.songall);
        trailerall = (TextView) rootView.findViewById(R.id.trendmore);
        info= (TextView) rootView.findViewById(R.id.info);
        download= (TextView) rootView.findViewById(R.id.download);
        like= (TextView) rootView.findViewById(R.id.like);
        views= (TextView) rootView.findViewById(R.id.views);
        share= (ImageButton) rootView.findViewById(R.id.share);
        similardata= (TextView) rootView.findViewById(R.id.similardata);
        similardata.setText("Similar "+tag.replaceAll("[0-9]"," "));
        recomendvideo= (LinearLayout) rootView.findViewById(R.id.recomendvideolayout);
        recomondedtra= (LinearLayout) rootView.findViewById(R.id.recomendedlayout);
        firstcontainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        GridLayoutManager gdd = new GridLayoutManager(getActivity(), 2);
        secitemcontainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        similar.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        bannerlist = new ArrayList<>();
        recomendtrailerlist = new ArrayList<>();
        recommendvideolist = new ArrayList<>();
        recommendforlist = new ArrayList<>();
        url = new ArrayList<>();
        singleurl = new ArrayList<>();
        url2 = new ArrayList<>();
        url3 = new ArrayList<>();
        url4 = new ArrayList<>();
        url5 = new ArrayList<>();
        singleurl=new ArrayList<>();
        trecomendurl=new ArrayList<>();
        vrecommendurl=new ArrayList<>();
        forrecomnedurl=new ArrayList<>();
        adoptor = new TrendingAdaptor(getActivity(), recomendtrailerlist, trecomendurl);
        adoptor1 = new MainContentAdoptor1((MainActivity) getActivity(), recommendvideolist, vrecommendurl);
        adoptor3 = new MainContentAdoptor3(getActivity(), recommendforlist, forrecomnedurl);
        similerAdopter=new SimilerAdopter((MainActivity) getActivity(),list,singleurl);
        switchImage = new LoadImageToView(getActivity(), bannerlist, url);
        viewPager.setAdapter(switchImage);
        firstcontainer.setAdapter(adoptor);
        secitemcontainer.setAdapter(adoptor1);
        similar.setAdapter(similerAdopter);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDown();
              //  new Download(getActivity(),singleurl.get(0).dlink,information).execute();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs();
            }
        });
        songall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleVideo?skipdata=", "song", "song", "", "", 1);
            }
        });
        trailerall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleTrailers?skipdata=", "trailers", "song", "", "", 1);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BackgroundProcess.shared.getString("check", "0").equals("0"))
                {
                    Intent intent=new Intent(getActivity(), UserLogin.class);
startActivity(intent);
                }
                else {
                    if (BackgroundProcess.shared.getString("check", "0").equals("1")) {
                       // Toast.makeText(getActivity(), "Facebook Login", Toast.LENGTH_SHORT).show();
                        sendLike();
                    } else if (BackgroundProcess.shared.getString("check", "0").equals("2")) {
                       // Toast.makeText(getActivity(), "Mobile Login", Toast.LENGTH_SHORT).show();
                        sendMobLike();
                    }

                }
            }
        });
share.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (infotitle.equals("null")){
Toast.makeText(getActivity(),"please wait",Toast.LENGTH_LONG).show();
        }
        else {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
        //    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, infotitle + " Artist:- " + infoartist + " for more videos/movies download panj app  " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
            sharingIntent.putExtra(Intent.EXTRA_TEXT, infotitle +"   http://iiscandy.com/panj/vstream?id="+id);
                    startActivity(Intent.createChooser(sharingIntent, "Share With these app"));
        }
    }
});

        recommendRequest();
        recommendTrailer();
        addSimiler();
        al = new ArrayList<>();
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://switchboard.nrdc.org/blogs/dlashof/mission_impossible_4-1.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://switchboard.nrdc.org/blogs/dlashof/mission_impossible_4-1.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));

        return rootView;
    }

    private void dialogDown() {
        TextView title;
        RadioGroup radioGroup;
        dialog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_dialog);
        dialog.setCancelable(true);
        title= (TextView) dialog.findViewById(R.id.title);
        title.setText("Select Download quality");
        Button bt= (Button) dialog.findViewById(R.id.cancle);
        radioGroup= (RadioGroup) dialog.findViewById(R.id.qualitygroup);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//               RadioButton radioButton = (RadioButton) dialog.findViewById(checkedId);
//               radioButton.setChecked(true);

                // dialog.dismiss();
                switch (checkedId) {
                    case R.id.p200:
                        Log.e("durl",""+shareurl.replace("600","200"));
                       new Download(getActivity(),shareurl.replace("600","200"),infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p385:
                        new Download(getActivity(),dlinks.replace("600","385"),infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p500:
                        new Download(getActivity(),dlinks.replace("600","500"),infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p600:
                        new Download(getActivity(),dlinks.replace("600","600"),infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p750:
                        new Download(getActivity(),dlinks.replace("600","750"),infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p1000:
                        new Download(getActivity(),dlinks.replace("600","1000"),infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p1500:
                        new Download(getActivity(),dlinks.replace("600","1500"),infotitle).execute();
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();
    }

    private void sendMobLike() {
        Log.e("mobile",""+BackgroundProcess.shared.getString("mobile","0"));
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/insertlikes?vid="+id+"&fbid="+BackgroundProcess.shared.getString("mobile","0"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setLikeCount(new JSONObject(response));
                        Log.e("likeresult", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
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

    private void addSimiler() {

            stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/SingleVideo?id="+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            setValue(new JSONObject(response));
                            Log.e("simi", "" + response);
                        } catch (JSONException e) {
                            Log.e("error due to", e.getMessage());
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Baba error hai", "Error: " + error.getMessage());
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

    private void setValue(JSONObject jsonObject) {

        try {

            JSONObject jsonOb = jsonObject.getJSONObject("Video");
            try {
                JSONArray comnt = jsonOb.getJSONArray("Comments");
                for (int i = 0; i < comnt.length(); i++) {
                    JSONObject comment = comnt.getJSONObject(i);
                    CommentBean commentBean = new CommentBean();
                    commentBean.username = comment.getString("UserName");
                    // commentBean.profilepic=comment.getString("");
                    commentBean.comment = comment.getString("Comment");
                    commentBean.date = comment.getString("DateTime");
                    JSONObject js=jsonOb.getJSONObject("Urls");
                    ItemBean itemBean=new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8=js.getString("m3u8");
                    itemBean.dlink=js.getString("dlink");
                    dlinks=js.getString("dlink");
                    shareurl=js.getString("BT600");
singlalleurl.add(itemBean);
                }
MainActivity.allurl=singlalleurl;
            }
            catch (Exception e){
                Log.e("comment error",e.toString());
                similar.setVisibility(View.GONE);
                similardata.setVisibility(View.GONE);
            }

            try {

                JSONArray jsonArray = jsonOb.getJSONArray("SimilarVideos");
                if (jsonArray.length()==0)
                {
                    similar.setVisibility(View.GONE);
                    similardata.setVisibility(View.GONE);
                }
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject similerobject = jsonArray.getJSONObject(k);
                    ItemBean items = new ItemBean();
                    items.tredcover = similerobject.getString("medium");
                    items.tredname = similerobject.getString("videotitle");
                    items.id=similerobject.getString("videoId");
                    list.add(items);
                    Log.e("simi", "" + items.tredcover);
                }
            }
            catch (Exception e){
                similar.setVisibility(View.GONE);
                similardata.setVisibility(View.GONE);
            }
            for (int j = 0; j < jsonOb.length(); j++) {
                likecount=jsonOb.getString("likescount");
                viewcount=jsonOb.getString("ViewCount");
                information=jsonOb.getString("title")+"  "+jsonOb.getString("Artist")+" "+jsonOb.getString("genre")+" "+jsonOb.getString("Description");
infotitle=jsonOb.getString("title");
                infoartist=jsonOb.getString("Artist");
                infoproducer=jsonOb.getString("Producer");
                infodirector=jsonOb.getString("Director");
                infomusicdirector=jsonOb.getString("MusicDirector");
                infostarimg=jsonOb.getString("StarImage");

            }
            similerAdopter.notifyDataSetChanged();
            like.setText(""+likecount);
            views.setText(""+viewcount);
            try{
                for (int j = 0; j < jsonOb.length(); j++) {
                    JSONObject js = jsonOb.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8=js.getString("m3u8");
                    itemBean.dlink=js.getString("dlink");
                    dlinks=js.getString("dlink");
                    shareurl=js.getString("BT600");
                    singleurl.add(itemBean);
                }
            }
            catch (Exception e){

            }
        } catch (Exception e) {
            similar.setVisibility(View.GONE);
            similardata.setVisibility(View.GONE);
            Log.e("VideoError", e.toString());
        }

    }


    private void dialogs() {
        TextView songename,artistname,produsername,direcname,musicdirector;
        Button ok;
        ImageView img;
        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.information_dialog);
        songename= (TextView) dialog.findViewById(R.id.songname);
        artistname= (TextView) dialog.findViewById(R.id.artistname);
        produsername= (TextView) dialog.findViewById(R.id.producername);
        direcname= (TextView) dialog.findViewById(R.id.directorname);
        musicdirector= (TextView) dialog.findViewById(R.id.musicdirector);
        img= (ImageView) dialog.findViewById(R.id.img);
        ok= (Button) dialog.findViewById(R.id.ok);
        songename.setText(""+infotitle);
        artistname.setText(Html.fromHtml("Artist :- <font color='#B92436'>"+infoartist+"</font>"));
        produsername.setText("Producer :-"+infoproducer);
        direcname.setText("Director :-"+infodirector);
        musicdirector.setText("Music Director :-"+infomusicdirector);
        Picasso.with(getContext()).load(infostarimg).placeholder(R.mipmap.panjicon).into(img);
        artistname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                act.dialogshow(new SearchDialogArtist(),infoartist,"sd");

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

      /*  Log.e("data",""+information);
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setMessage(""+information);
        alert.setIcon(R.mipmap.panjicon);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();*/

    }

    private void recommendTrailer() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/RecommendedTrailers?msisdn=0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setRecommendTrailerValue(new JSONObject(response));
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
                stringStringHashMap.put("itemToFetch", recomendtrailerlist.size() + "");
                stringStringHashMap.put("itemToFetcht", recomendtrailerlist.size() + "");
                return stringStringHashMap;
            }
        };
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void setRecommendTrailerValue(JSONObject jsonObject) {
        try {
            JSONArray trending = jsonObject.getJSONArray("Recommended");
            if (trending.length()==0||trending.getString(0).equals("No Recomended Data"))
            {
                recomondedtra.setVisibility(View.GONE);
                firstcontainer.setVisibility(View.GONE);
            }
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
                trecomendurl.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.treddesp = jsonObject2.getString("Description");
                items.id = jsonObject2.getString("id");
                recomendtrailerlist.add(items);
            }
            adoptor.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("er", "" + e.toString());
recomondedtra.setVisibility(View.GONE);
            firstcontainer.setVisibility(View.GONE);
        }
    }

    //recommend video request
    private void recommendRequest() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/RecommendedVideos?msisdn=0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setRecommendValue(new JSONObject(response));
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
                stringStringHashMap.put("itemToFetch", recommendvideolist.size() + "");
                stringStringHashMap.put("itemToFetcht", recommendvideolist.size() + "");
                return stringStringHashMap;
            }
        };
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    //set recommended  item
    private void setRecommendValue(JSONObject jsonObject) {
        try {
            JSONArray trending = jsonObject.getJSONArray("Recommended");
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
                itemBean.m3u8=js.getString("m3u8");
                vrecommendurl.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.treddesp = jsonObject2.getString("Description");
                items.id = jsonObject2.getString("id");
                recommendvideolist.add(items);
            }
            adoptor1.notifyDataSetChanged();
            progressDialog.dismiss();
        } catch (Exception e) {
            Log.e("er", "" + e.toString());
recomendvideo.setVisibility(View.GONE);
            secitemcontainer.setVisibility(View.GONE);
        }
    }
    private void sendLike() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/insertlikes?vid="+id+"&fbid="+BackgroundProcess.shared.getString("fbid","0"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setLikeCount(new JSONObject(response));
                        Log.e("likeresult", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
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
    }

    private void setLikeCount(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("Like").equals("1")) {

                int i= Integer.parseInt(likecount)+1;
                //likecounttext.setText(""+i);
                Log.e("lk", "" + i);
                likecount= String.valueOf(i);
                like.setText(""+likecount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            if (jsonObject.getString("Unlike").equals("0")) {
                //  like.setColorFilter(Color.parseColor("#FFFFFF"));
                int i= Integer.parseInt(likecount)-1;
              //  likecounttext.setText(""+i);
                likecount= String.valueOf(i);
                Log.e("ulk", "" +likecount);
                like.setText(""+likecount);
            }
        }
        catch (Exception e){

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
            ImageLoader.getInstance().displayImage(cover, (ImageView) imageView);
            imageLayout.setTag(position);
            view.addView(imageLayout, 0);
            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.allurl=singleurl;
                    Intent it = new Intent(act, SubtitlePlayer.class);
                    it.putExtra("url", videourl);
                    it.putExtra("pos",0);
                    it.putExtra("id",id);
                    startActivity(it);
                   /* Intent it=new Intent(act, Playtest.class);
                    startActivity(it);*/

                }
            });
            return imageLayout;
        }

        @Override
        public int getCount() {
            return 1;
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

    public class MyAppAdapter extends BaseAdapter {


        public List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");

            //  Glide.with(MainActivity.this).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);
            ImageLoader.getInstance().loadImage(parkingList.get(position).getImagePath(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    //viewHolder.cardImage.setImageResource(R.mipmap.musicload);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    viewHolder.cardImage.setImageBitmap(loadedImage);
                }
            });
            return rowView;
        }
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;
    }
}
