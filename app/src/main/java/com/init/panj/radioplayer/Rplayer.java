package com.init.panj.radioplayer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.init.panj.R;
import com.init.panj.adapter.RadioAdapter;
import com.init.panj.clases.Bluer;
import com.init.panj.model.LiveTvBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 8/25/2016.
 */
public class Rplayer extends AppCompatActivity implements RadioAdapter.ClickAction {
    static LinearLayout alayout,seeklayout,dragviewe;
    static TextView songname, artistname,duration;
    Handler mHandler = new Handler();
    static ImageButton playbt, next, pre, pausebt, share;
    static AVLoadingIndicatorView mainprog,ppb;
    static SeekBar volumeSeekbar;
    int pos;
    static ImageView songimage,imgback;
    static Context context;
    boolean checkbitmap;
   public static Bitmap defalultbt, urlbitmap;
    static String check;
    public static TextView mainsongname, mainArtist;
    public static ImageView mainimg,down;
    public static ImageButton mainplay, mainpause;
    public static FrameLayout playershow;
    static int posi;
    private static final String TAG = "DemoActivity";
    private static SlidingUpPanelLayout mLayout;
    ArrayList<RadioBean> radioBeanArrayList=new ArrayList<>();
int getPos;
    Intent intent;
    RecyclerView recyclerView;
    RadioAdapter radioAdapter;
    AVLoadingIndicatorView avLoadingIndicatorView;
    private AudioManager audioManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_demo);
intent=getIntent();
        radioBeanArrayList= (ArrayList<RadioBean>) intent.getSerializableExtra("list");
        getPos=intent.getIntExtra("position",0);

        init();
        initControls();
      //  initR();
        context = getApplicationContext();
        // setsong(pos,new Sdcard(Player.this).getPlayList());
        defalultbt = BitmapFactory.decodeResource(getResources(), R.drawable.panj);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset==1.0||slideOffset>0) {
                    playershow.setBackgroundColor(Color.TRANSPARENT);
                }
                else if (slideOffset==0.0) {
                }

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e(TAG, "onPanelStateChanged " + newState);
                // t.setText("helloooooo");
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    changeButton();
                    playershow.setBackgroundColor(Color.BLACK);
                    down.setVisibility(View.GONE);
                    mainimg.setVisibility(View.VISIBLE);
                    mainsongname.setVisibility(View.VISIBLE);
                    mainArtist.setVisibility(View.VISIBLE);
                }
                else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    playershow.setBackgroundColor(Color.TRANSPARENT);
                    share.setVisibility(View.VISIBLE);
                    mainpause.setVisibility(View.GONE);
                    mainplay.setVisibility(View.GONE);
                    down.setVisibility(View.VISIBLE);
                    mainimg.setVisibility(View.GONE);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mainplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.playControl(getApplicationContext());
            }
        });
        mainpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.pauseControl(getApplicationContext());

            }
        });
        netCheck();
        if (getPos==1010){
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

        else
        {
            setsong(getPos,radioBeanArrayList);
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }

        recyclerView= (RecyclerView) findViewById(R.id.recycler_container);
        avLoadingIndicatorView= (AVLoadingIndicatorView) findViewById(R.id.avi);
        avLoadingIndicatorView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        radioAdapter=new RadioAdapter(Rplayer.this,radioBeanArrayList,this);
        recyclerView.setAdapter(radioAdapter);
        radioAdapter.notifyDataSetChanged();

    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void netCheck() {
        if (isNetworkAvailable())
        {}
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Network Error");
            builder.setMessage("please check your network connection");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            });
            builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
    private void init() {
        getViews();
        setListner();
        changeUI();
    }
    private void setListner() {
        playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.playControl(getApplicationContext());
                else
                    setsong(getPos,radioBeanArrayList);
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(getApplicationContext());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.nextControl(getApplicationContext());
            }
        });
        pausebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.pauseControl(getApplicationContext());


            }
        });

        seeklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
songimage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
        songname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        artistname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
share.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
         sharingIntent.putExtra(Intent.EXTRA_TEXT, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioname()+ "  Download panj app for more ardio channels");
        startActivity(Intent.createChooser(sharingIntent, "Share With these app"));
    }
});
    }
    public static void changeUI() {
        updateUI();
        changeButton();
        updateseekbar();
    }


    private static void updateseekbar() {
        PlayerConstants1.PROGRESSBAR_HANDLER = new C03066();
    }

    @Override
    public void onAction(int position) {
        setsong(position,radioBeanArrayList);
    }

    static class C03066 extends Handler {
        C03066() {
        }

        public void handleMessage(Message msg) {
            Log.e("called", "handler" + msg);
            Integer[] i = (Integer[]) msg.obj;
            duration.setText(UtilFunctions1.getDuration1((long) i[0].intValue()));
            /*seekBar.setProgress(i[0].intValue());
            seekBar.setMax(i[1].intValue());
            long milliseconds = UtilFunctions1.getDuration11(i[1].intValue());
            if (TimeUnit.MILLISECONDS.toMinutes((long) milliseconds) > 100)
                secduration.setText("00:00");
            else {
                String time = (String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
                        TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) milliseconds))));
                secduration.setText(time);
            }*/

        }
    }

    public static void changeButton() {
        mainprog.setVisibility(View.GONE);
        ppb.setVisibility(View.GONE);
        if (PlayerConstants1.SONG_PAUSED) {
            pausebt.setVisibility(View.GONE);
            playbt.setVisibility(View.VISIBLE);
            if (mLayout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED) {
                share.setVisibility(View.GONE);
                mainplay.setVisibility(View.VISIBLE);
                mainpause.setVisibility(View.GONE);
            }

        } else {
            pausebt.setVisibility(View.VISIBLE);
            playbt.setVisibility(View.GONE);
            if (mLayout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED) {
                share.setVisibility(View.GONE);
                mainplay.setVisibility(View.GONE);
                mainpause.setVisibility(View.VISIBLE);
            }
        }
    }

    private static void updateUI() {
        try {
            seeklayout.setVisibility(View.VISIBLE);
            String songName = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioname();
            songname.setText(songName);
            mainsongname.setText(songName);
            artistname.setText("" + PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioname());
           mainArtist.setText("" + PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioname());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String ur=PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioimage();
            String url="https://i.ytimg.com/vi/wzY8Bzz9FS4/maxresdefault.jpg";
                ImageLoader.getInstance().loadImage(ur, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        songimage.setImageResource(R.drawable.logo_fade);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        super.onLoadingComplete(imageUri, view, loadedImage);
                        if (PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioimage() != null) {
                            ImageViewAnimatedChange(context, songimage, loadedImage);
                           songimage.setAdjustViewBounds(true);
                             songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                           songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            Bitmap blurredBitmap = new Bluer(context).blur1(loadedImage);
                            mainimg.setImageBitmap(loadedImage);
                            BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                            if (background != null && dragviewe != null) {
                                dragviewe.setBackgroundDrawable(background);
                                alayout.setBackgroundDrawable(background);
                            }

                        } else {
                            ImageViewAnimatedChange(context, songimage, defalultbt);
                            // songimage.setImageBitmap(defalultbt); //associated cover art in bitmap
                            mainimg.setImageBitmap(defalultbt);
                            songimage.setAdjustViewBounds(true);
                           // songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            Bitmap blurredBitmap = new Bluer(context).blur1(defalultbt);
                            BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                            if (background != null && alayout != null) {
                                alayout.setBackgroundDrawable(background);

                            }
                        }
                    }
                });
        } catch (Exception e) {
           Log.e("imger",""+e.toString());
        }
    }

    private void getViews() {
        playershow = (FrameLayout) findViewById(R.id.layout);
        mainplay = (ImageButton) findViewById(R.id.mainplay);
        mainimg = (ImageView) findViewById(R.id.stripimage);
        mainsongname = (TextView) findViewById(R.id.mainsongname);
        mainpause = (ImageButton) findViewById(R.id.pausem);
        mainprog = (AVLoadingIndicatorView) findViewById(R.id.songprog);
        mainArtist = (TextView) findViewById(R.id.mainartist);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        playbt = (ImageButton) findViewById(R.id.play);
        pausebt = (ImageButton) findViewById(R.id.pause);
        songname = (TextView) findViewById(R.id.songname);
        volumeSeekbar = (SeekBar) findViewById(R.id.seekBar);
        next = (ImageButton) findViewById(R.id.next);
        pre = (ImageButton) findViewById(R.id.previous);
        share = (ImageButton) findViewById(R.id.share);
        songimage = (ImageView) findViewById(R.id.songimage);
        artistname = (TextView) findViewById(R.id.artist);
        alayout = (LinearLayout) findViewById(R.id.layout1);
        seeklayout= (LinearLayout) findViewById(R.id.seeklayout);
        ppb= (AVLoadingIndicatorView) findViewById(R.id.psongprog);
        dragviewe= (LinearLayout) findViewById(R.id.dragView);
        down= (ImageView) findViewById(R.id.down);
        imgback= (ImageView) findViewById(R.id.imgback);
        duration= (TextView) findViewById(R.id.duration);
        share.setVisibility(View.VISIBLE);
    }


    public static void prog() {
         mainprog.setVisibility(View.VISIBLE);
        ppb.setVisibility(View.VISIBLE);
        pausebt.setVisibility(View.GONE);
        playbt.setVisibility(View.GONE);


    }

    public void setsong(final int position, ArrayList<RadioBean> feedItemList) {
        prog();
        Intent i = new Intent(Rplayer.this, SongService1.class);
        stopService(i);
        Log.e("url", "" + feedItemList.get(position).getRadiourl());
        playbt.setVisibility(View.GONE);
        pausebt.setVisibility(View.GONE);
        PlayerConstants1.SONG_PAUSED = false;
        PlayerConstants1.SONG_CHANGED = false;
        PlayerConstants1.SONG_NUMBER = position;
        PlayerConstants1.SONGS_LIST1 = feedItemList;
//                        boolean isServiceRunning = UtilFunctions1.isServiceRunning(SongService1.class.getName(), getApplicationContext());
//                        if (!isServiceRunning) {
//                            Intent i = new Intent(getApplicationContext(), SongService1.class);
//                            startService(i);
//                        } else {
//                            try {
//
//                                PlayerConstants1.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants1.SONG_CHANGE_HANDLER.obtainMessage());
//                            }
//                            catch (Exception e){
//                                Log.e("srror h",e.toString());
//
//                            }
//
//                            }


        //  Log.e("songid", item.getAudioId());
        Intent intent = new Intent(getApplicationContext(), SongService1.class);
        intent.setAction(Constants.ACTION_PLAY);
        startService(intent);
    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_in.setDuration(1000);
        anim_out.setDuration(1000);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
    private void initControls() {
        try {

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
