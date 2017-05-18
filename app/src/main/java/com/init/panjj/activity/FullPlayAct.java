package com.init.panjj.activity;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.init.panjj.R;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.otherclasses.ProgressBarCircular;
import com.init.panjj.fragments.Video_home;
import com.init.panjj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FullPlayAct extends AppCompatActivity implements MediaPlayer.OnInfoListener, OnPreparedListener {
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
    EMVideoView emVideoView;
    static long seekto = 0;
    ImageButton reload,cancel;
    RecyclerView recommendContent;
    RecommnedAdapter recommnedAdapter;
    ArrayList<ItemBean> rlist;
    ArrayList<ItemBean> rurl;
    ImageView revomendedoneimageview;
    LinearLayout linearLayoutrecomen, potraitview;
    TextView reconetitle, reconedesc,ptitle,pdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
//        getSupportActionBar().setTitle("");
            SharedPreferences sp = BackgroundProcess.shared;
            SharedPreferences.Editor ed = sp.edit();
            ed.commit();
            url = MainActivity.allurl;
            if (mediaControls == null) {
                mediaControls = new MediaController(this);
            }
            dialog = new Dialog(FullPlayAct.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
            id = getIntent().getStringExtra("id");
            playurl = url.get(position).m3u8;
            rlist = Video_home.recommendvideolist;
            rurl = Video_home.vrecommendurl;
            Log.e("playurl", "" + playurl + position + " ....  " + rlist.get(0).tredname + "" + rurl.size());
            recommendContent = (RecyclerView) findViewById(R.id.recomcontent);
            recommendContent.setLayoutManager(new GridLayoutManager(FullPlayAct.this, 2));
            recommnedAdapter = new RecommnedAdapter(this, rlist, rurl);
            recommendContent.setAdapter(recommnedAdapter);
            recommnedAdapter.notifyDataSetChanged();
            quality = (ImageButton) findViewById(R.id.quality);
            myVideoView = (VideoView) findViewById(R.id.videoView);
            emVideoView = (EMVideoView) findViewById(R.id.video_view);
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
                    linearLayoutrecomen.setVisibility(View.GONE);
                }
            });
            linearLayoutrecomen = (LinearLayout) findViewById(R.id.recommendone);
            potraitview = (LinearLayout) findViewById(R.id.potraitlayout);
            revomendedoneimageview = (ImageView) findViewById(R.id.recomoneimageview);
            reconetitle = (TextView) findViewById(R.id.reconetitle);
            reconedesc = (TextView) findViewById(R.id.reconedesc);
            ptitle= (TextView) findViewById(R.id.potraittitle);
pdesc= (TextView) findViewById(R.id.potraitdesc);
            revomendedoneimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    frameLayout.setVisibility(View.GONE);
                    emVideoView.reset();
                    seekto = 0;
                    setVideo(rurl.get(0).m3u8);
                }
            });
            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.setVisibility(View.GONE);
                    emVideoView.reset();
                    seekto = 0;
                    setVideo(playurl);
                }
            });
            Display mDisplay = getWindowManager().getDefaultDisplay();
            final int width = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            emVideoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
            emVideoView.setMinimumHeight(height / 2);
          //  potraitview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
            FrameLayout.LayoutParams buttonLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/2);
            buttonLayoutParams.setMargins(0, 0, 0, 50);
            potraitview.setLayoutParams(buttonLayoutParams);
            //   initActivityScreenOrientPortrait();
            //  String pl="http://52.74.238.77/bhaiamar/SampleVideo_1280x720_1mb.mp4";
            String pl = "http://staticori.iiscandy.com/Music_Videos/Punjabi/SAADEY_CM_SAAB/Saadey_CM_Saab_Teaser/CM_Saab_teaser_200_final.mp4";
            // setAdd(url.get(position).BT200);
            // setAdd(pl);
            Log.e("saveinsta", "" + savedInstanceState);
            setVideo(playurl);
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
            emVideoView.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion() {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
                    else {
                        reload.setVisibility(View.VISIBLE);
                        linearLayoutrecomen.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.VISIBLE);
                        emVideoView.setVisibility(View.VISIBLE);
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
                    }
                }
            });
            quality.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog();
                }
            });
            // setVideo(playurl);
            addViews();
        } else
        {
            emVideoView= (EMVideoView) getLastCustomNonConfigurationInstance();
           emVideoView.start();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return emVideoView;
    }

    private void setAdd(String url) {
        quality.setVisibility(View.GONE);
        Uri video = Uri.parse(url);
        videoview.setVideoURI(video);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer = mp;
                mp.start();
                sk.setProgress(0);
                sk.setMax(mp.getDuration());
                mHandler.postDelayed(mUpdateTimeTask, 100);
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //  isMediaPlayed=true;
                mHandler.removeCallbacks(mUpdateTimeTask);
                setVideo(playurl);

            }
        });
    }


    private void setVideo(String url) {
        // url="http://wpc.fc2a.edgecastcdn.net/hls-live/20FC2A/ecpl/pl/jdDellhiAajTak85au.m3u8?3c0e986a453f9d57304a806b441b3f2b2ed8a2673c56e63821bf4a795704d2bb31347b13b186d277641b8389514290295786";
        seekto = emVideoView.getCurrentPosition();
        Log.e("seek", "val" + seekto);
        quality.setVisibility(View.VISIBLE);
        //  frameLayout.setVisibility(View.GONE);
        //videoview.setVisibility(View.GONE);
        /*Log.e("urllll", "z" + url);
        prog.setVisibility(View.VISIBLE);
        try {
            Uri video = Uri.parse(url);
            myVideoView.setVideoURI(video);
            myVideoView.setMediaController(mediaControls);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();*/
        emVideoView.setOnPreparedListener(this);
        emVideoView.setVideoURI(Uri.parse(url));


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
                        setVideo(url.get(position).m3u8);
                        saveChildPosition(checkedId, 0);
                        Log.e("m3u8", "" + radioGroup.getChildAt(0));
                        break;
                    case R.id.p200:
                        // TODO Something
                        Log.e("id", "" + radioGroup.getChildAt(0));
                        setVideo(url.get(position).BT200);
                        saveChildPosition(checkedId, 0);
                        break;
                    case R.id.p385:
                        saveChildPosition(checkedId, 1);
                        setVideo(url.get(position).BT385);
                        break;
                    case R.id.p500:
                        saveChildPosition(checkedId, 2);
                        setVideo(url.get(position).BT500);
                        break;
                    case R.id.p600:
                        saveChildPosition(checkedId, 3);
                        setVideo(url.get(position).BT600);
                        break;
                    case R.id.p750:
                        saveChildPosition(checkedId, 4);
                        setVideo(url.get(position).BT750);
                        break;
                    case R.id.p1000:
                        saveChildPosition(checkedId, 5);
                        setVideo(url.get(position).BT1000);
                        break;
                    case R.id.p1500:
                        saveChildPosition(checkedId, 6);
                        setVideo(url.get(position).BT1500);
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
            int i=0;
            if (emVideoView.getCurrentPosition()>300000){

               if (i==0){
                   mHandler.postDelayed(this,1000000);
                emVideoView.pause();
                alertdial();
               i=1+10;
               }

            }

           /* finalTime = mMediaPlayer.getDuration();
            startTime = mMediaPlayer.getCurrentPosition();
            int startTime1 = (int) Math.floor(TimeUnit.MILLISECONDS.toMinutes((long) mMediaPlayer.getCurrentPosition()));
                Log.e("time", finalTime + " " + startTime);
            // Displaying Total Duration time

            if(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)>100){
                time.setText("0:0");
            }
            else
                time.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))-TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                )));
            float progress = ((float) startTime / (float) finalTime) * 100;
            sk.setProgress((int) startTime);*/

            mHandler.postDelayed(this, 10000);

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onPrepared() {
        emVideoView.setVisibility(View.VISIBLE);
        emVideoView.start();
        emVideoView.seekTo((int) seekto);
        mHandler.postDelayed(mUpdateTimeTask, 10000);

    }

    class RecommnedAdapter extends RecyclerView.Adapter<RecommnedAdapter.MyView> {
        ArrayList<ItemBean> rrlist, rrurl;
        FullPlayAct fullPlayAct;

        public RecommnedAdapter(FullPlayAct fullPlayAct, ArrayList<ItemBean> rlist, ArrayList<ItemBean> rurl) {
            rrlist = rlist;
            rrurl = rurl;
            this.fullPlayAct = fullPlayAct;
        }

        @Override
        public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.maincontent, null));
        }

        @Override
        public void onBindViewHolder(final MyView myViewHolder, final int position) {
            final ItemBean itemBean = rrlist.get(position);
            Log.e("r", "hh" + itemBean.tredname);
            myViewHolder.title.setText(itemBean.tredname);
            myViewHolder.subt.setText(itemBean.newdesc);
            myViewHolder.cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Intent it=new Intent(act,PlayVideo.class);
                it.putExtra("list",new UrlBean(urllist));
                it.putExtra("position",i);
                it.putExtra("id",itemBean.id);
                act.startActivity(it);*/
                    MainActivity.allurl = url;
                    // act.replaceFragment(new Video_home(),"zdsd",itemBean.tredcover,"videos"+i, itemBean.tredname + " " + itemBean.treddesp, itemBean.id, 1);
                    emVideoView.reset();
                    seekto = 0;
                    setVideo(rrurl.get(position).m3u8);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            frameLayout.setVisibility(View.GONE);
            reload.setVisibility(View.GONE);
            linearLayoutrecomen.setVisibility(View.GONE);
            potraitview.setVisibility(View.GONE);
            emVideoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            frameLayout.setVisibility(View.VISIBLE);
            potraitview.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);
            linearLayoutrecomen.setVisibility(View.GONE);
            Display mDisplay = getWindowManager().getDefaultDisplay();
            final int width = mDisplay.getWidth();
            final int height = mDisplay.getHeight();
            emVideoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
            emVideoView.setMinimumHeight(height / 2);
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

        final AlertDialog.Builder alert=new AlertDialog.Builder(FullPlayAct.this);
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
}
