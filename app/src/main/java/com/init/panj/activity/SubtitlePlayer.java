package com.init.panj.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
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
import com.init.panj.R;
import com.init.panj.adapter.MainContentAdoptor3;
import com.init.panj.clases.CustomImageView;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.clases.ProgressBarCircular;
import com.init.panj.clases.SavedState;
import com.init.panj.fragments.New_Video_home;
import com.init.panj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SubtitlePlayer extends AppCompatActivity implements MediaPlayer.OnInfoListener {
    VideoView myVideoView, videoview;
    MediaController mediaControls;
    ArrayList<ItemBean> url = new ArrayList<>();
    int position, pos = 0;
    String playurl;
    boolean needResume;
    Dialog dialog;
    RadioGroup radioGroup;
    StringRequest stringRequest;
    String id = "";
    Handler mHandler = new Handler();
    SeekBar sk;
    private double startTime = 0;
    private double finalTime = 0;
    static MediaPlayer mMediaPlayer;
    TextView time;
    FrameLayout frameLayout;
    boolean isMediaPlayed;
    ImageButton quality;
    static long seekto = 0;
    ImageButton reload,cancel;
    RecyclerView recommendContent;
    RecommnedAdapter recommnedAdapter;
    ArrayList<ItemBean> rlist;
    ArrayList<ItemBean> rurl;
    ImageView revomendedoneimageview;
    LinearLayout linearLayoutrecomen, potraitview;
    TextView reconetitle, reconedesc,ptitle,pdesc;
    SimpleExoPlayerView simpleExoPlayerView;
    private Handler mainHandler;
    private Timeline.Window window;
    private EventLogger eventLogger;
    private DataSource.Factory mediaDataSourceFactory;
    SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  static   int a=10;
    boolean recomandplay=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        window = new Timeline.Window();
        eventLogger = new EventLogger();
        setContentView(R.layout.activity_subtitle_player);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        final TrackSelector trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player    = ExoPlayerFactory.newSimpleInstance(SubtitlePlayer.this, trackSelector, loadControl);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);

        if (savedInstanceState == null) {
//        getSupportActionBar().setTitle("");
            SharedPreferences sp = ImageLoaderInit.shared;
            SharedPreferences.Editor ed = sp.edit();
            ed.commit();
            url = MainActivity.allurl;
            if (mediaControls == null) {
                mediaControls = new MediaController(this);
            }
            dialog = new Dialog(SubtitlePlayer.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
            Log.e("checkid", "" + ImageLoaderInit.shared.getInt("posi", R.id.p200));
            position = getIntent().getIntExtra("pos", 0);
            id = getIntent().getStringExtra("id");
            playurl = url.get(position).m3u8;
            rlist = New_Video_home.recommendvideolist;
            rurl = New_Video_home.vrecommendurl;
            Log.e("playurl", "" + playurl + position + " ....  " + rlist.get(0).tredname + "" + rurl.size());
            recommendContent = (RecyclerView) findViewById(R.id.recomcontent);
            recommendContent.setLayoutManager(new GridLayoutManager(SubtitlePlayer.this, 3));
            recommnedAdapter = new RecommnedAdapter(this, rlist, rurl);
            recommendContent.setAdapter(recommnedAdapter);
            recommnedAdapter.notifyDataSetChanged();
            quality = (ImageButton) findViewById(R.id.quality);
            myVideoView = (VideoView) findViewById(R.id.videoView);
            videoview = (VideoView) findViewById(R.id.videoViewAdd);
            time = (TextView) findViewById(R.id.time);
            frameLayout = (FrameLayout) findViewById(R.id.add);
            mMediaPlayer = new MediaPlayer();
            sk = (SeekBar) findViewById(R.id.seekBar);
            reload = (ImageButton) findViewById(R.id.reload);
            cancel= (ImageButton) findViewById(R.id.cancle);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reload.setVisibility(View.GONE);
                    recomandplay=true;
                    linearLayoutrecomen.setVisibility(View.GONE);
                    mainHandler.removeCallbacks(mUpdateTimeTask);
                }
            });
            linearLayoutrecomen = (LinearLayout) findViewById(R.id.recommendone);
            potraitview = (LinearLayout) findViewById(R.id.potraitlayout);
            revomendedoneimageview = (ImageView) findViewById(R.id.recomoneimageview);
            reconetitle = (TextView) findViewById(R.id.reconetitle);
            reconedesc = (TextView) findViewById(R.id.reconedesc);
            ptitle= (TextView) findViewById(R.id.potraittitle);
            pdesc= (TextView) findViewById(R.id.potraitdesc);
            linearLayoutrecomen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    frameLayout.setVisibility(View.GONE);
                    player.stop();
                    seekto = 0;
                   // setVideo(rurl.get(0).m3u8);
                    setSubTitlePlayer(New_Video_home.vrecommendurl.get(0).m3u8);
                }
            });
            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.setVisibility(View.GONE);
                    player.stop();
                    seekto = 0;
                   // setVideo(playurl);
                    setSubTitlePlayer(playurl);
                }
            });
            Display mDisplay = getWindowManager().getDefaultDisplay();
            final int width = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
           /* simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
            simpleExoPlayerView.setMinimumHeight(height / 2);
            //  potraitview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
            FrameLayout.LayoutParams buttonLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/2);
            buttonLayoutParams.setMargins(0, 0, 0, 50);
            potraitview.setLayoutParams(buttonLayoutParams);*/
            //   initActivityScreenOrientPortrait();
            //  String pl="http://52.74.238.77/bhaiamar/SampleVideo_1280x720_1mb.mp4";
            String pl = "http://staticori.iiscandy.com/Music_Videos/Punjabi/SAADEY_CM_SAAB/Saadey_CM_Saab_Teaser/CM_Saab_teaser_200_final.mp4";
            // setAdd(url.get(position).BT200);
            // setAdd(pl);
            Log.e("saveinsta", "" + savedInstanceState);
            setSubTitlePlayer(playurl);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) myVideoView.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoview.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                myVideoView.setOnInfoListener(this);

            }
            videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("mperror", "ssss");
                    return false;
                }
            });
            myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    myVideoView.seekTo(pos);
                    if (pos == 0) {
                        myVideoView.start();
                    } else {
                        myVideoView.pause();
                    }
                }
            });
            myVideoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (getSupportActionBar().isShowing())
                        getSupportActionBar().hide();
                    else
                        getSupportActionBar().show();
                    return false;
                }
            });
            myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
               /* position=position+1;
                playurl=url.get(position).BT200;
                setVideo(playurl);*/
                }
            });
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
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
                        else {
                            reload.setVisibility(View.VISIBLE);
                            linearLayoutrecomen.setVisibility(View.VISIBLE);
                            frameLayout.setVisibility(View.VISIBLE);
                            reconetitle.setText(" " + New_Video_home.recommendvideolist.get(0).tredname);
                            reconedesc.setText("Set in Paris, Befikre is a free spirited, contemporary love story of Dharam and Shyra");
                            ImageLoader.getInstance().loadImage(New_Video_home.recommendvideolist.get(0).tredcover, new SimpleImageLoadingListener() {
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
                    if (playbackState==ExoPlayer.STATE_READY);
                       // player.setPlayWhenReady(true);
                        //player.seekTo(seekto);
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
            quality.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog();
                }
            });
            addViews();
        }
    }

    private void setSubTitlePlayer(String playurll) {
        playurl=playurll;
       // Uri uris= Uri.parse("http://iiscandyhlsori-vh.akamaihd.net/i/Music_Videos/Hindi/Detective_Byomkesh_Bakshy/Promo/Calcutta_Police_Deal_Nahi_Karta/Calcutta_Police_Deal_Nahi_Karta_Detective_Byomkesh_Bakshy_,200,1500,500,385,750,1000,600,_final.mp4.csmil/master.m3u8");
dialog.dismiss();
      if (playurll.contains(".m3u8")) {
          Uri uris = Uri.parse(playurll);
          DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
          DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);

          MediaSource videoSource = new ExtractorMediaSource(uris, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
          MediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse("file:///android_asset/myfile.vtt"), dataSourceFactory, Format.createTextSampleFormat("0", MimeTypes.TEXT_VTT, null, Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, "se", null, 0), 1000);
// Plays the video with the sideloaded subtitle.
          MediaSource mediaSource = buildMediaSource(uris);
          Log.e("mds", "hh " + mediaSource.toString());
          MergingMediaSource mergedSource = new MergingMediaSource(mediaSource, subtitleSource);
          player.prepare(mediaSource);
          player.setPlayWhenReady(true);
          mHandler.postDelayed(mUpdateTimeTask, 1000);
      }
       else if (playurll.contains(".mp4"))
      {
          Uri uris = Uri.parse(playurll);
          DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
          DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);

          MediaSource videoSource = new ExtractorMediaSource(uris, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
          MediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse("file:///android_asset/myfile.vtt"), dataSourceFactory, Format.createTextSampleFormat("0", MimeTypes.TEXT_VTT, null, Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, "se", null, 0), 1000);

          MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
          player.prepare(videoSource);
          player.setPlayWhenReady(true);
          mHandler.postDelayed(mUpdateTimeTask, 10000);
      }
    }



    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //Begin buffer, pause playing
                if (mp.isPlaying()) {
                    stopPlayer();
                    needResume = true;
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //The buffering is done, resume playing
                if (needResume)
                    startPlayer();

                break;
        }
        return true;
    }

    private void startPlayer() {
        myVideoView.start();
    }

    private void stopPlayer() {
        myVideoView.pause();
    }

    public void dialog() {
        url = MainActivity.allurl;
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
                        seekto=player.getCurrentPosition();
                        setSubTitlePlayer(url.get(position).m3u8);
                        saveChildPosition(checkedId, 0);
                        Log.e("m3u8", "" + radioGroup.getChildAt(0));
                        break;
                    case R.id.p200:
                        seekto=player.getCurrentPosition();
                        // TODO Something
                        Log.e("id", "" + radioGroup.getChildAt(0));
                        setSubTitlePlayer(url.get(position).BT200);
                        saveChildPosition(checkedId, 0);
                        break;
                    case R.id.p385:
                        seekto=player.getCurrentPosition();
                        saveChildPosition(checkedId, 1);
                        setSubTitlePlayer(url.get(position).BT385);
                        break;
                    case R.id.p500:
                        seekto=player.getCurrentPosition();
                        saveChildPosition(checkedId, 2);
                        setSubTitlePlayer(url.get(position).BT500);
                        break;
                    case R.id.p600:
                        seekto=player.getCurrentPosition();
                        saveChildPosition(checkedId, 3);
                        setSubTitlePlayer(url.get(position).BT600);
                        break;
                    case R.id.p750:
                        seekto=player.getCurrentPosition();
                        saveChildPosition(checkedId, 4);
                        setSubTitlePlayer(url.get(position).BT750);
                        break;
                    case R.id.p1000:
                        seekto=player.getCurrentPosition();
                        saveChildPosition(checkedId, 5);
                        setSubTitlePlayer(url.get(position).BT1000);
                        break;
                    case R.id.p1500:
                        seekto=player.getCurrentPosition();
                        saveChildPosition(checkedId, 6);
                        setSubTitlePlayer(url.get(position).BT1500);
                        break;

                }
            }

            private void saveChildPosition(int i, int k) {
                SharedPreferences sp = ImageLoaderInit.shared;
                SharedPreferences.Editor ed = sp.edit();
                ed.putInt("posi", i);
                ed.putInt("type", k);
                ed.commit();
                dialog.cancel();
            }
        });
        dialog.show();
    }


    private void addViews() {

        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/insertview?vid=" + id, new Response.Listener<String>() {
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
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void setValue(JSONObject jsonObject) {
        try {
            Log.e("status", "" + jsonObject.getString("View"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void timer() {
        finalTime = mMediaPlayer.getDuration();
        startTime = mMediaPlayer.getCurrentPosition();
        int startTime1 = (int) Math.floor(TimeUnit.MILLISECONDS.toMinutes((long) mMediaPlayer.getCurrentPosition()));
        //    Log.e("time", finalTime + " " + startTime);
        // Displaying Total Duration time

        if (TimeUnit.MILLISECONDS.toMinutes((long) finalTime) > 100) {
            time.setText("0:0");
        } else
            time.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            (TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)) - TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );
        // Displaying time completed playing
           /* secduration.setText(String.format("%d :%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );*/
        float progress = ((float) startTime / (float) finalTime) * 100;
        sk.setProgress((int) startTime);


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
                    if (a>=0) {
                        reconetitle.setText(Html.fromHtml("<b>Next Video in: <b>" + a + " seconds </br>" + rlist.get(0).tredname));
                        mHandler.postDelayed(this, 1000);
                    }  else{
                        reconetitle.setText(Html.fromHtml(New_Video_home.recommendvideolist.get(0).tredname));


                    }
                    reconedesc.setText("Set in Paris, Befikre is a free spirited, contemporary love story of Dharam and Shyra");
                    a--;
                    ImageLoader.getInstance().loadImage(New_Video_home.recommendvideolist.get(0).tredcover, new SimpleImageLoadingListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mUpdateTimeTask);
        player.stop();

    }



    class RecommnedAdapter extends RecyclerView.Adapter<RecommnedAdapter.MyView> {
        ArrayList<ItemBean> rrlist, rrurl;
        SubtitlePlayer fullPlayAct;

        public RecommnedAdapter(SubtitlePlayer fullPlayAct, ArrayList<ItemBean> rlist, ArrayList<ItemBean> rurl) {
            rrlist = rlist;
            rrurl = rurl;
            this.fullPlayAct = fullPlayAct;
        }

        @Override
        public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, null));
        }

        @Override
        public void onBindViewHolder(final MyView myViewHolder, final int position) {
            final ItemBean itemBean = rrlist.get(position);
            Log.e("r", "hh" + itemBean.tredname);
            //myViewHolder.title.setText(itemBean.tredname);
           // myViewHolder.subt.setText(itemBean.newdesc);
            myViewHolder.img.scaledImage(itemBean.tredcover);
            myViewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                it.putExtra("id",itemBean.id);
                act.startActivity(it);*/
                    MainActivity.allurl = url;
                    // act.replaceFragment(new Video_home(),"zdsd",itemBean.tredcover,"videos"+i, itemBean.tredname + " " + itemBean.treddesp, itemBean.id, 1);
                    player.stop();
                    seekto = 0;
                    setSubTitlePlayer(rrurl.get(position).m3u8);
                }
            });

           /* ImageLoader.getInstance().loadImage(itemBean.tredcover, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    myViewHolder.img.setImageResource(R.drawable.logo_fade);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    myViewHolder.img.setImageBitmap(loadedImage);

                }
            });*/
        }

        @Override
        public int getItemCount() {
            return rlist.size();
        }

        public class MyView extends RecyclerView.ViewHolder {
          //  TextView title, subt;
            CustomImageView img;
           // CardView cd;
          //  ProgressBarCircular pb;
           FrameLayout fm;

            public MyView(View itemView) {
                super(itemView);
                Display mDisplay = getWindowManager().getDefaultDisplay();
                final int width = mDisplay.getWidth();
                final int height = mDisplay.getHeight();
                int framewitdh = width / 3;
                Log.e("hshdfkjsf", "" + height);
                fm = (FrameLayout) itemView.findViewById(R.id.mframe);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(framewitdh, (int)getResources().getDimension(R.dimen.moviescontainer_height));
                fm.setLayoutParams(lp);
                img = (CustomImageView) itemView.findViewById(R.id.trailerimage);
                //subt = (TextView) itemView.findViewById(R.id.subtitle);
              //  cd = (CardView) itemView.findViewById(R.id.card_view);
            }
        }
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
            reconetitle.setVisibility(View.VISIBLE);
            reconedesc.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
          //  Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            frameLayout.setVisibility(View.VISIBLE);
            potraitview.setVisibility(View.VISIBLE);
            recommendContent.setVisibility(View.VISIBLE);
            reconetitle.setVisibility(View.GONE);
            reconedesc.setVisibility(View.GONE);
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

    private void initActivityScreenOrientPortrait() {
        // Avoid screen rotations (use the manifests android:screenOrientation setting)
        // Set this to nosensor or potrait

        // Set window fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Test if it is VISUAL in portrait mode by simply checking it's size
        boolean bIsVisualPortrait = (metrics.heightPixels >= metrics.widthPixels);

        if (!bIsVisualPortrait) {
            // Swap the orientation to match the VISUAL portrait mode
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }

    }
    private void alertdial() {

        final AlertDialog.Builder alert=new AlertDialog.Builder(SubtitlePlayer.this);
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
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((ImageLoaderInit) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
    private MediaSource buildMediaSource(Uri uri) {

        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);

    }

}
