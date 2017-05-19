package com.init.panjj.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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
import com.init.panjj.fragments.New_Video_home;
import com.init.panjj.model.CommentBean;
import com.init.panjj.model.ItemBean;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.otherclasses.CustomImageView;
import com.init.panjj.otherclasses.Download;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import at.blogc.android.views.ExpandableTextView;

import static com.init.panjj.activity.MainActivity.allurl;

public class SubtitlePlayer extends AppCompatActivity implements MediaPlayer.OnInfoListener {
    VideoView myVideoView, videoview;
    MediaController mediaControls;
    String information;
    String likecount, viewcount;
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
    ImageButton reload, cancel;
    RecyclerView recommendContent;
    RecommnedAdapter recommnedAdapter;
    ArrayList<ItemBean> rlist;
    ArrayList<ItemBean> rurl;
    ImageView revomendedoneimageview;
    LinearLayout linearLayoutrecomen, potraitview;
    TextView reconetitle, reconedesc, ptitle, pdesc;
    SimpleExoPlayerView simpleExoPlayerView;
    private Handler mainHandler;
    private Timeline.Window window;
    private EventLogger eventLogger;
    private DataSource.Factory mediaDataSourceFactory;
    SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    static int a = 10;
    boolean recomandplay = false;
    TextView info, download, share, views;
    String shareurl = "", dlinks;
    ExpandableTextView video_desc;

    public static ArrayList<ItemBean> recommendvideolist, vrecommendurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendvideolist = new ArrayList<>();
        vrecommendurl = new ArrayList<>();
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
        player = ExoPlayerFactory.newSimpleInstance(SubtitlePlayer.this, trackSelector, loadControl);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);

        video_desc = (ExpandableTextView) findViewById(R.id.video_desc);
        download = (TextView) findViewById(R.id.download);
        views = (TextView) findViewById(R.id.views);
        share = (TextView) findViewById(R.id.share);
        info = (TextView) findViewById(R.id.info);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDown();
                //  new Download(getActivity(),singleurl.get(0).dlink,information).execute();
            }
        });
       /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0, (int) getResources().getDimension(R.dimen.nagetive),0);
        downloadlayout.setLayoutParams(layoutParams);*/
        // video_desc.setVisibility(View.GONE);
        url = new ArrayList<>();
        singleurl = new ArrayList<>();
        url2 = new ArrayList<>();
        url3 = new ArrayList<>();
        url4 = new ArrayList<>();
        url5 = new ArrayList<>();
        singleurl = new ArrayList<>();
        trecomendurl = new ArrayList<>();
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_desc.isExpanded()) {
                    // video_desc.setVisibility(View.VISIBLE);
                    video_desc.collapse();

                } else {
                    video_desc.expand();
                    //  video_desc.setVisibility(View.GONE);

                }
            }
        });
        if (savedInstanceState == null) {
//        getSupportActionBar().setTitle("");
            SharedPreferences sp = BackgroundProcess.shared;
            SharedPreferences.Editor ed = sp.edit();
            ed.commit();
            url = allurl;
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
            Log.e("checkid", "" + BackgroundProcess.shared.getInt("posi", R.id.p200));
            position = getIntent().getIntExtra("pos", 0);
            pos = position;
            id = getIntent().getStringExtra("id");
            playurl = url.get(position).m3u8;
           /* rlist = New_Video_home.recommendvideolist;
            rurl = New_Video_home.vrecommendurl;*/
            Log.e("playurl", "" + id + url.get(position).BT1500);
            recommendContent = (RecyclerView) findViewById(R.id.recomcontent);
            recommendContent.setLayoutManager(new GridLayoutManager(SubtitlePlayer.this, 3));
            recommnedAdapter = new RecommnedAdapter(this, recommendvideolist, vrecommendurl);
            recommendContent.setAdapter(recommnedAdapter);
            quality = (ImageButton) findViewById(R.id.quality);
            myVideoView = (VideoView) findViewById(R.id.videoView);
            videoview = (VideoView) findViewById(R.id.videoViewAdd);
            time = (TextView) findViewById(R.id.time);
            frameLayout = (FrameLayout) findViewById(R.id.add);
            mMediaPlayer = new MediaPlayer();
            sk = (SeekBar) findViewById(R.id.seekBar);
            reload = (ImageButton) findViewById(R.id.reload);
            cancel = (ImageButton) findViewById(R.id.cancle);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reload.setVisibility(View.GONE);
                    recomandplay = true;
                    linearLayoutrecomen.setVisibility(View.GONE);
                    mainHandler.removeCallbacks(mUpdateTimeTask);
                }
            });
            linearLayoutrecomen = (LinearLayout) findViewById(R.id.recommendone);
            potraitview = (LinearLayout) findViewById(R.id.potraitlayout);
            revomendedoneimageview = (ImageView) findViewById(R.id.recomoneimageview);
            reconetitle = (TextView) findViewById(R.id.reconetitle);
            reconedesc = (TextView) findViewById(R.id.reconedesc);
            ptitle = (TextView) findViewById(R.id.potraittitle);
            pdesc = (TextView) findViewById(R.id.potraitdesc);
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
            android.widget.RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myVideoView.getLayoutParams();
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
                    Log.e("exo", "loading" + isLoading);
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_ENDED) {
                        Log.e("exo", "end" + playbackState + " " + ExoPlayer.STATE_ENDED);
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                            ;
                        else {
                            reload.setVisibility(View.VISIBLE);
                            linearLayoutrecomen.setVisibility(View.VISIBLE);
                            frameLayout.setVisibility(View.VISIBLE);
                            reconetitle.setText(" " + recommendvideolist.get(0).tredname);
                            reconedesc.setText("Set in Paris, Befikre is a free spirited, contemporary love story of Dharam and Shyra");
                            ImageLoader.getInstance().loadImage(recommendvideolist.get(0).tredcover, new SimpleImageLoadingListener() {
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
                }

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    Log.e("exo", "timeline" + timeline);
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.e("exo", "error" + error);
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
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infotitle.equals("null")) {
                    Toast.makeText(SubtitlePlayer.this, "please wait", Toast.LENGTH_LONG).show();
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    //    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, infotitle + " Artist:- " + infoartist + " for more videos/movies download panj app  " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, infotitle + "   http://iiscandy.com/panj/vstream?id=" + id);
                    startActivity(Intent.createChooser(sharingIntent, "Share With these app"));
                }
            }
        });
        recommendRequest();
        addSimiler();
    }

    private void setSubTitlePlayer(String playurll) {
        position = pos;
        playurl = playurll;
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
            player.seekTo(seekto);
            mHandler.postDelayed(mUpdateTimeTask, 1000);
        } else if (playurll.contains(".mp4")) {
            Uri uris = Uri.parse(playurll);
            DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);

            MediaSource videoSource = new ExtractorMediaSource(uris, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
            MediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse("file:///android_asset/myfile.vtt"), dataSourceFactory, Format.createTextSampleFormat("0", MimeTypes.TEXT_VTT, null, Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, "se", null, 0), 1000);

            MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
            player.seekTo(seekto);
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
                        Log.e("m3u8", "22" + position);
                        seekto = player.getCurrentPosition();
                        setSubTitlePlayer(url.get(position).m3u8);
                        saveChildPosition(checkedId, 0);
                        break;
                    case R.id.p200:
                        seekto = player.getCurrentPosition();
                        // TODO Something
                        Log.e("vv", "fgfg" + url.get(pos).BT200);
                        setSubTitlePlayer(url.get(pos).BT200);
                        saveChildPosition(checkedId, 0);
                        break;
                    case R.id.p385:
                        seekto = player.getCurrentPosition();
                        saveChildPosition(checkedId, 1);
                        setSubTitlePlayer(url.get(position).BT385);
                        break;
                    case R.id.p500:
                        seekto = player.getCurrentPosition();
                        saveChildPosition(checkedId, 2);
                        setSubTitlePlayer(url.get(position).BT500);
                        break;
                    case R.id.p600:
                        seekto = player.getCurrentPosition();
                        saveChildPosition(checkedId, 3);
                        setSubTitlePlayer(url.get(position).BT600);
                        break;
                    case R.id.p750:
                        seekto = player.getCurrentPosition();
                        saveChildPosition(checkedId, 4);
                        setSubTitlePlayer(url.get(position).BT750);
                        break;
                    case R.id.p1000:
                        seekto = player.getCurrentPosition();
                        saveChildPosition(checkedId, 5);
                        setSubTitlePlayer(url.get(position).BT1000);
                        break;
                    case R.id.p1500:
                        seekto = player.getCurrentPosition();
                        saveChildPosition(checkedId, 6);
                        setSubTitlePlayer(url.get(position).BT1500);
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
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
            int i = 0;
            if (player.getCurrentPosition() > 300000) {

                if (i == 0) {
                    mHandler.postDelayed(this, 1000000);
                    player.setPlayWhenReady(false);
                    alertdial();
                    i = 1 + 10;
                }

            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (player.getCurrentPosition() >= player.getDuration() - 10000 && recomandplay == false) {
                    reload.setVisibility(View.VISIBLE);
                    linearLayoutrecomen.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    if (a >= 0) {
                        reconetitle.setText(Html.fromHtml("<b>Next Video in: <b>" + a + " seconds </br>" + rlist.get(0).tredname));
                        mHandler.postDelayed(this, 1000);
                    } else {
                        reconetitle.setText(Html.fromHtml(recommendvideolist.get(0).tredname));


                    }
                    reconedesc.setText("Set in Paris, Befikre is a free spirited, contemporary love story of Dharam and Shyra");
                    a--;
                    ImageLoader.getInstance().loadImage(recommendvideolist.get(0).tredcover, new SimpleImageLoadingListener() {
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
                    allurl = rrurl;
                    url=rrurl;
                    // act.replaceFragment(new Video_home(),"zdsd",itemBean.tredcover,"videos"+i, itemBean.tredname + " " + itemBean.treddesp, itemBean.id, 1);
                    pos = position;
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
            return rrlist.size();
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
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(framewitdh, (int) getResources().getDimension(R.dimen.moviescontainer_height));
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
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
            simpleExoPlayerView.setMinimumHeight(height / 2);
            potraitview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        final AlertDialog.Builder alert = new AlertDialog.Builder(SubtitlePlayer.this);
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
        return ((BackgroundProcess) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private MediaSource buildMediaSource(Uri uri) {

        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);

    }

    private void dialogDown() {
        TextView title;
        RadioGroup radioGroup;
        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_dialog);
        dialog.setCancelable(true);
        title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Select Download quality");
        Button bt = (Button) dialog.findViewById(R.id.cancle);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.qualitygroup);
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
                        Log.e("durl", "" + shareurl.replace("600", "200"));
                        new Download(getApplicationContext(), dlinks.replace("600", "200"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p385:
                        new Download(getApplicationContext(), dlinks.replace("600", "385"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p500:
                        new Download(getApplicationContext(), dlinks.replace("600", "500"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p600:
                        new Download(getApplicationContext(), dlinks.replace("600", "600"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p750:
                        new Download(getApplicationContext(), dlinks.replace("600", "750"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p1000:
                        new Download(getApplicationContext(), dlinks.replace("600", "1000"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p1500:
                        new Download(getApplicationContext(), dlinks.replace("600", "1500"), infotitle).execute();
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();
    }

    private void addSimiler() {

        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/SingleVideo?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setValues(new JSONObject(response));
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

    ArrayList<ItemBean> url, singlalleurl, url2, url3, url4, url5, trecomendurl, singleurl;
    String infoartist, infoproducer, infodirector, infomusicdirector, infotitle = "null", infostarimg;

    ArrayList<ItemBean> list = new ArrayList<>();

    private void setValues(JSONObject jsonObject) {

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
                    JSONObject js = jsonOb.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    itemBean.dlink = js.getString("dlink");
                    dlinks = js.getString("dlink");
                    shareurl = js.getString("BT600");
                    singlalleurl.add(itemBean);
                }
                //allurl = singlalleurl;
            } catch (Exception e) {
                Log.e("comment error", e.toString());
                //recommendContent.setVisibility(View.GONE);

            }

            try {
                JSONArray jsonArray = jsonOb.getJSONArray("SimilarVideos");
                if (jsonArray.length() == 0) {
                }
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject similerobject = jsonArray.getJSONObject(k);
                    ItemBean items = new ItemBean();
                    items.tredcover = similerobject.getString("medium");
                    items.tredname = similerobject.getString("videotitle");
                    items.id = similerobject.getString("videoId");
                    list.add(items);
                    Log.e("simi", "" + items.tredcover);
                }
            } catch (Exception e) {


            }
            for (int j = 0; j < jsonOb.length(); j++) {
                likecount = jsonOb.getString("likescount");
                viewcount = jsonOb.getString("ViewCount");
                information = jsonOb.getString("title") + "  " + jsonOb.getString("Artist") + " " + jsonOb.getString("genre") + " " + jsonOb.getString("Description");
                infotitle = jsonOb.isNull("title") ? " " : "  " + jsonOb.getString("title");
                infoartist = jsonOb.isNull("Artist") ? " " : " " + jsonOb.getString("Artist");
                infoproducer = jsonOb.isNull("Producer") ? " " : " " + jsonOb.getString("Producer");
                infodirector = jsonOb.isNull("Director") ? " " : " " + jsonOb.getString("Director");
                infomusicdirector = jsonOb.isNull("MusicDirector") ? "" : " " + jsonOb.getString("MusicDirector");
                infostarimg = jsonOb.isNull("StarImage") ? "" : jsonOb.getString("StarImage");

            }
            views.setText("Views : " + viewcount);
            video_desc.setText(Html.fromHtml(infotitle + infoartist + infomusicdirector + "<br><font color='#c7c5c5'>" + "Producer : " + infoproducer + "<br> Director : " + infodirector));

            try {
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
                    itemBean.m3u8 = js.getString("m3u8");
                    itemBean.dlink = js.getString("dlink");
                    dlinks = js.getString("dlink");
                    shareurl = js.getString("BT600");
                    singleurl.add(itemBean);
                }
            } catch (Exception e) {

            }
        } catch (Exception e) {

            Log.e("VideoError", e.toString());
        }

    }

    private void recommendRequest() {
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
                itemBean.m3u8 = js.getString("m3u8");
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
            recommnedAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("er", "" + e.toString());


        }
    }
}
