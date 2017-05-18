package com.init.panjj.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.init.panjj.R;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.otherclasses.ProgressBarCircular;
import com.init.panjj.model.CommentBean;
import com.init.panjj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PanjRecive extends AppCompatActivity   {
    Handler mHandler = new Handler();
    String url="",id;
    ArrayList<ItemBean> rlist;
    ArrayList<ItemBean> rurl;
    ImageView revomendedoneimageview;
    LinearLayout linearLayoutrecomen, potraitview;
    TextView reconetitle, reconedesc;
    StringRequest stringRequest;
    String imageurl;
    FrameLayout frameLayout;
    ImageButton quality,reload;
    ImageView cancle;
    Dialog dialog;
    RadioGroup radioGroup;
    SimpleExoPlayerView simpleExoPlayerView;
    private Handler mainHandler;
    private Timeline.Window window;
    private EventLogger eventLogger;
    private DataSource.Factory mediaDataSourceFactory;
    SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    int seekto=0;
    static  int a=10;
    RecyclerView recommendContent;
    RecommnedAdapter recommnedAdapter;
    boolean recomandplay=false;
    String playurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        window = new Timeline.Window();
        eventLogger = new EventLogger();
        setContentView(R.layout.activity_panj_recive);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player    = ExoPlayerFactory.newSimpleInstance(PanjRecive.this, trackSelector, loadControl);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);
        rlist=new ArrayList<>();
        rurl=new ArrayList<>();
        frameLayout = (FrameLayout) findViewById(R.id.add);
        quality = (ImageButton) findViewById(R.id.quality);
        cancle= (ImageView) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload.setVisibility(View.GONE);
                recomandplay=true;
                linearLayoutrecomen.setVisibility(View.GONE);
                mainHandler.removeCallbacks(mUpdateTimeTask);

            }
        });
        Intent intent=getIntent();
        url=intent.getDataString();
        id=url.substring(url.indexOf("="));
        recommendContent = (RecyclerView) findViewById(R.id.recomcontent);
        recommendContent.setLayoutManager(new GridLayoutManager(PanjRecive.this, 2));
        recommnedAdapter = new RecommnedAdapter(PanjRecive.this, rlist, rurl);
        recommendContent.setAdapter(recommnedAdapter);
       // setVideo(url.substring(0,url.indexOf("id=")));
        // Toast.makeText(RecieveExo.this,"id= "+id+" "+url.substring(0,url.indexOf("id=")),Toast.LENGTH_LONG).show();
        addSimiler(id);
        dialogCreate();
        reload = (ImageButton) findViewById(R.id.reload);
        linearLayoutrecomen = (LinearLayout) findViewById(R.id.recommendone);
        potraitview = (LinearLayout) findViewById(R.id.potraitlayout);
        revomendedoneimageview = (ImageView) findViewById(R.id.recomoneimageview);
        reconetitle = (TextView) findViewById(R.id.reconetitle);
        reconedesc = (TextView) findViewById(R.id.reconedesc);
        revomendedoneimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.GONE);
                player.stop();
                seekto = 0;
                // setVideo(rurl.get(0).m3u8);
                setSubTitlePlayer(rurl.get(0).m3u8);
            }
        });
        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frameLayout.setVisibility(View.GONE);
                player.stop();
                seekto = 0;
                // setVideo(playurl);
                setSubTitlePlayer(rurl.get(0).m3u8);
            }
        });
        Display mDisplay = getWindowManager().getDefaultDisplay();
        final int width = mDisplay.getWidth();
        final int height = mDisplay.getHeight();
        simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
        simpleExoPlayerView.setMinimumHeight(height / 2);
        //  potraitview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
        FrameLayout.LayoutParams buttonLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/2);
        buttonLayoutParams.setMargins(0, 0, 0, 50);
        potraitview.setLayoutParams(buttonLayoutParams);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.e("exo","loading"+isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState==ExoPlayer.STATE_ENDED)
                {
                    Log.e("exo","end"+playbackState+" "+ExoPlayer.STATE_ENDED);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                        linearLayoutrecomen.setVisibility(View.GONE);
                    else {
                        reload.setVisibility(View.VISIBLE);
                        linearLayoutrecomen.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.VISIBLE);
                        reconetitle.setText(" " + rlist.get(0).tredname);
                        reconedesc.setText("Set in Paris, Befikre is a free spirited, contemporary love story of Dharam and Shyra");
                        ImageLoader.getInstance().loadImage(rlist.get(0).tredcover, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                revomendedoneimageview.setImageResource(R.drawable.logo_fade);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                super.onLoadingComplete(imageUri, view, loadedImage);
                                revomendedoneimageview.setImageBitmap(loadedImage);

                            }
                        });
                    }
                }
                if (playbackState==ExoPlayer.STATE_READY)
                    Log.e("exo","ready"+playbackState+" "+ExoPlayer.STATE_ENDED);
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.e("exo","timeline"+timeline);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e("exo","error"+error);
            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
    }

    private void dialogCreate() {
        dialog = new Dialog(PanjRecive.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        dialog.setTitle("Quality");
        Button bt = (Button) dialog.findViewById(R.id.cancle);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        radioGroup = (RadioGroup) dialog.findViewById(R.id.qualitygroup);

    }


    private void setSubTitlePlayer(String playurl) {
        // Uri uris= Uri.parse("http://iiscandyhlsori-vh.akamaihd.net/i/Music_Videos/Hindi/Detective_Byomkesh_Bakshy/Promo/Calcutta_Police_Deal_Nahi_Karta/Calcutta_Police_Deal_Nahi_Karta_Detective_Byomkesh_Bakshy_,200,1500,500,385,750,1000,600,_final.mp4.csmil/master.m3u8");
        Log.e("mds", "hh  call");
        if (playurl.contains(".m3u8")) {
            Uri uris = Uri.parse(playurl);
            DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);

            MediaSource videoSource = new ExtractorMediaSource(uris, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
            MediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse("file:///android_asset/myfile.vtt"), dataSourceFactory, Format.createTextSampleFormat("0", MimeTypes.TEXT_VTT, null, Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, "se", null, 0), 1000);
// Plays the video with the sideloaded subtitle.
            MediaSource mediaSource = buildMediaSource(uris);
            Log.e("mds", "hh " + mediaSource.toString());
            MergingMediaSource mergedSource = new MergingMediaSource(mediaSource, subtitleSource);
            player.prepare(mergedSource);
            player.setPlayWhenReady(true);
            mHandler.postDelayed(mUpdateTimeTask, 10000);
        }
        else if (playurl.contains(".mp4"))
        {
            Uri uris = Uri.parse(playurl);
            DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);

            MediaSource videoSource = new ExtractorMediaSource(uris, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
            MediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse("file:///android_asset/myfile.vtt"), dataSourceFactory, Format.createTextSampleFormat("0", MimeTypes.TEXT_VTT, null, Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, "se", null, 0), 1000);

            MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
            player.prepare(mergedSource);
            player.setPlayWhenReady(true);
            mHandler.postDelayed(mUpdateTimeTask, 10000);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PanjRecive.this,MainActivity.class);
        startActivity(intent);
        player.stop();
        super.onBackPressed();
    }
    private void addSimiler(String iid) {
        String ids=iid.replace("=","");
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/SingleVideo?id="+ids, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setValue(new JSONObject(response));
                        Log.e("simip", "" + response);
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
                                   }
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
                rurl.add(itemBean);
                Log.e("called", "yeaas"+js.getString("dlink"));
                MainActivity.allurl=rurl;
            setSubTitlePlayer(rurl.get(0).m3u8);
            }
            catch (Exception e){
                Log.e("comment error",e.toString());

            }

            try {

                JSONArray jsonArray = jsonOb.getJSONArray("SimilarVideos");
                if (jsonArray.length()==0)
                {

                }
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject similerobject = jsonArray.getJSONObject(k);
                    ItemBean items = new ItemBean();
                    items.tredcover = similerobject.getString("Default");
                    items.tredname = similerobject.getString("videotitle");
                    items.id=similerobject.getString("videoId");
                    rlist.add(items);

                }
                recommnedAdapter.notifyDataSetChanged();
            }
            catch (Exception e){
            }
            for (int j = 0; j < jsonOb.length(); j++) {
               /* likecount=jsonOb.getString("likescount");
                viewcount=jsonOb.getString("ViewCount");
                information=jsonOb.getString("title")+"  "+jsonOb.getString("Artist")+" "+jsonOb.getString("genre")+" "+jsonOb.getString("Description");
                infotitle=jsonOb.getString("title");
                infoartist=jsonOb.getString("Artist");
                infoproducer=jsonOb.getString("Producer");
                infodirector=jsonOb.getString("Director");
                infomusicdirector=jsonOb.getString("MusicDirector");*/
                imageurl=jsonOb.getString("StarImage");

            }
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
                    rurl.add(itemBean);
                }
            }
            catch (Exception e){

            }
        } catch (Exception e) {

            Log.e("VideoError", e.toString());
        }

    }
/*
    @Override
    public void onCompletion() {
        frameLayout.setVisibility(View.VISIBLE);
        reconetitle.setText(" " + rlist.get(0).tredname);
        reconedesc.setText(" " + rlist.get(0).treddesp);
        ImageLoader.getInstance().loadImage(rlist.get(0).tredcover, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                revomendedoneimageview.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                revomendedoneimageview.setImageBitmap(loadedImage);

            }
        });

    }*/

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((BackgroundProcess) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
    private MediaSource buildMediaSource(Uri uri) {

        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);

    }
    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            int i=0;
            if (player.getCurrentPosition()>300000){

                if (i==0){
                    mHandler.postDelayed(this,1000000);
                    player.setPlayWhenReady(false);
                    alertdial();
                    i=1+10;
                }

            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (player.getCurrentPosition() >= player.getDuration() - 10000&&recomandplay==false) {
                    reload.setVisibility(View.VISIBLE);
                    linearLayoutrecomen.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    if (a>=0)
                        reconetitle.setText(Html.fromHtml("<b>Next Video in: <b>"+ a +" seconds </br>" + rlist.get(0).tredname));
                    else{
                        reconetitle.setText(Html.fromHtml(rlist.get(0).tredname));
                    recomandplay=true;
                    }
                    reconedesc.setText("Set in Paris, Befikre is a free spirited, contemporary love story of Dharam and Shyra");
                    a--;
                      ImageLoader.getInstance().loadImage(rlist.get(0).tredcover, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            revomendedoneimageview.setImageResource(R.drawable.logo_fade);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            super.onLoadingComplete(imageUri, view, loadedImage);
                            revomendedoneimageview.setImageBitmap(loadedImage);

                        }
                    });
                }
            }
            mHandler.postDelayed(this, 1000);

        }
    };
    private void alertdial() {

        final AlertDialog.Builder alert=new AlertDialog.Builder(PanjRecive.this);
        alert.setIcon(R.mipmap.panjicon);
        alert.setTitle("Subscription");
        alert.setMessage("Please subscribe here");
        alert.setCancelable(true);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            frameLayout.setVisibility(View.GONE);
            reload.setVisibility(View.GONE);
            linearLayoutrecomen.setVisibility(View.GONE);
            potraitview.setVisibility(View.GONE);
            simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            frameLayout.setVisibility(View.VISIBLE);
            potraitview.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);
            linearLayoutrecomen.setVisibility(View.GONE);
            Display mDisplay = getWindowManager().getDefaultDisplay();
            final int width = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
            simpleExoPlayerView.setMinimumHeight(height / 2);
            potraitview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
        }
    }
    class RecommnedAdapter extends RecyclerView.Adapter<PanjRecive.RecommnedAdapter.MyView> {
        ArrayList<ItemBean> rrlist, rrurl;
        PanjRecive fullPlayAct;

        public RecommnedAdapter(PanjRecive fullPlayAct, ArrayList<ItemBean> rlist, ArrayList<ItemBean> rurl) {
            rrlist = rlist;
            rrurl = rurl;
            this.fullPlayAct = fullPlayAct;
        }

        @Override
        public PanjRecive.RecommnedAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PanjRecive.RecommnedAdapter.MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.maincontent, null));
        }

        @Override
        public void onBindViewHolder(final PanjRecive.RecommnedAdapter.MyView myViewHolder, final int position) {
            final ItemBean itemBean = rrlist.get(position);
            Log.e("r", "hh" + itemBean.tredname);
            myViewHolder.title.setText(itemBean.tredname);
            myViewHolder.subt.setText(itemBean.newdesc);
            myViewHolder.cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   player.stop();
                    seekto = 0;
                    setSubTitlePlayer(rrurl.get(position).m3u8);
                }
            });

            ImageLoader.getInstance().loadImage(itemBean.tredcover, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    myViewHolder.img.setImageResource(R.drawable.logo_fade);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    myViewHolder.img.setImageBitmap(loadedImage);
                    myViewHolder.pb.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return rlist.size();
        }

        public class MyView extends RecyclerView.ViewHolder {
            TextView title, subt;
            ImageView img;
            CardView cd;
            ProgressBarCircular pb;
            FrameLayout fm;

            public MyView(View itemView) {
                super(itemView);
            /*Display mDisplay =getWindowManager().getDefaultDisplay();
            final int width  = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            int framewitdh=width/2;
            Log.e("hshdfkjsf",""+height);*/
                fm = (FrameLayout) itemView.findViewById(R.id.mframe);
                //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(framewitdh, height/3);
                //fm.setLayoutParams(lp);
                pb = (ProgressBarCircular) itemView.findViewById(R.id.progress);
                title = (TextView) itemView.findViewById(R.id.title);
                img = (ImageView) itemView.findViewById(R.id.icon);
                subt = (TextView) itemView.findViewById(R.id.subtitle);
                cd = (CardView) itemView.findViewById(R.id.card_view);
            }
        }
    }
    public void dialog() {
        dialog.show();
//        radioButton.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//               RadioButton radioButton = (RadioButton) dialog.findViewById(checkedId);
//               radioButton.setChecked(true);

                // dialog.dismiss();
                switch (checkedId) {
                    case R.id.auto:
                        setSubTitlePlayer(rurl.get(0).m3u8);
                        saveChildPosition(checkedId, 0);
                        Log.e("m3u8", "" + radioGroup.getChildAt(0));
                        break;
                    case R.id.p200:
                        // TODO Something
                        Log.e("id", "" + radioGroup.getChildAt(0));
                        setSubTitlePlayer(rurl.get(0).BT200);
                        saveChildPosition(checkedId, 0);
                        break;
                    case R.id.p385:
                        saveChildPosition(checkedId, 1);
                        setSubTitlePlayer(rurl.get(0).BT385);
                        break;
                    case R.id.p500:
                        saveChildPosition(checkedId, 2);
                        setSubTitlePlayer(rurl.get(0).BT500);
                        break;
                    case R.id.p600:
                        saveChildPosition(checkedId, 3);
                        setSubTitlePlayer(rurl.get(0).BT600);
                        break;
                    case R.id.p750:
                        saveChildPosition(checkedId, 4);
                        setSubTitlePlayer(rurl.get(0).BT750);
                        break;
                    case R.id.p1000:
                        saveChildPosition(checkedId, 5);
                        setSubTitlePlayer(rurl.get(0).BT1000);
                        break;
                    case R.id.p1500:
                        saveChildPosition(checkedId, 6);
                        setSubTitlePlayer(rurl.get(0).BT1500);
                        break;

                }
            }

            private void saveChildPosition(int i, int k) {
                SharedPreferences sp = BackgroundProcess.shared;
                SharedPreferences.Editor ed = sp.edit();
                ed.putInt("posi", i);
                ed.putInt("type", k);
                ed.commit();
                dialog.cancel();
            }
        });
        dialog.show();
    }

}
