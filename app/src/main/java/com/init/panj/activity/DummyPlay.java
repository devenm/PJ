package com.init.panj.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;

import com.init.panj.R;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.model.ItemBean;

import java.util.ArrayList;

public class DummyPlay extends AppCompatActivity implements MediaPlayer.OnInfoListener {
    VideoView myVideoView;
    MediaController mediaControls;
    ProgressBar prog;
    ArrayList<ItemBean> url=new ArrayList<>();
    int position,pos=0;
    String playurl;
    boolean needResume;
    Dialog dialog;
    RadioGroup radioGroup;
    ImageButton play,pause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummyplay);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*final Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");*/
        SharedPreferences sp = ImageLoaderInit.shared;
        SharedPreferences.Editor ed = sp.edit();
        ed.commit();
        url=MainActivity.allurl;
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        dialog=new Dialog(DummyPlay.this,android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        dialog.setTitle("Quality");
        Button bt= (Button) dialog.findViewById(R.id.cancle);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        radioGroup= (RadioGroup) dialog.findViewById(R.id.qualitygroup);
        Log.e("checkid", "" + ImageLoaderInit.shared.getInt("posi", R.id.p200));
        RadioButton radioButton= (RadioButton) dialog.findViewById(ImageLoaderInit.shared.getInt("posi", R.id.p200));
        // playurl="http://iiscandyhlsori-vh.akamaihd.net/i/Music_Videos/Hindi/Zid/Angry_Girls_Fighting/Angry_Girls_Fighting_,200,1500,500,385,750,1000,600,_final.mp4.csmil/master.m3u8";
        playurl="http://staticori.iiscandy.com/Music_Videos/Hindi/FAN/FAN_Official_Trailer/FAN_Official_Trailer_Shah_Rukh_Khan_200_final.mp4";
        position=getIntent().getIntExtra("pos",0);
        // playurl=url.get(position).BT200;
        Log.e("playurl",""+playurl+position);
        myVideoView = (VideoView) findViewById(R.id.videoView);
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) myVideoView.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        myVideoView.setLayoutParams(params);
        play= (ImageButton) findViewById(R.id.play);
        pause= (ImageButton) findViewById(R.id.pause);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                myVideoView.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                myVideoView.pause();
            }
        });
        prog= (ProgressBar) findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            myVideoView.setOnInfoListener(this);
        }
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                prog.setVisibility(View.GONE);
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
               /* if (play.isShown())
                    getSupportActionBar().hide();
                else
                    getSupportActionBar().show();
                return false;*/
                return true;
            }
        });
        setVideo(playurl);
    }
    private void setVideo(String url) {
        Log.e("urllll", "z" + url);
        prog.setVisibility(View.VISIBLE);
        try {
            Uri video = Uri.parse(url);
            myVideoView.setVideoURI(video);
           //myVideoView.setMediaController(mediaControls);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();
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
                prog.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //The buffering is done, resume playing
                if (needResume)
                    startPlayer();
                prog.setVisibility(View.GONE);
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
    public void dialog(){
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
                        setVideo("http://iiscandyhlsori-vh.akamaihd.net/i/Music_Videos/Hindi/Zid/Angry_Girls_Fighting/Angry_Girls_Fighting_,200,1500,500,385,750,1000,600,_final.mp4.csmil/master.m3u8");
                        saveChildPosition(checkedId,0);
                    case R.id.p200:
                        // TODO Something
                        Log.e("id", "" + radioGroup.getChildAt(0));
                        setVideo(url.get(position).BT200);
                        saveChildPosition(checkedId,0);
                        break;
                    case R.id.p385:
                        saveChildPosition(checkedId,1);
                        setVideo(url.get(position).BT385);
                        break;
                    case R.id.p500:
                        saveChildPosition(checkedId,2);
                        setVideo(url.get(position).BT500);
                        break;
                    case R.id.p600:
                        saveChildPosition(checkedId,3);
                        setVideo(url.get(position).BT600);
                        break;
                    case R.id.p750:
                        saveChildPosition(checkedId,4);
                        setVideo(url.get(position).BT750);
                        break;
                    case R.id.p1000:
                        saveChildPosition(checkedId,5);
                        setVideo(url.get(position).BT1000);
                        break;
                    case R.id.p1500:
                        saveChildPosition(checkedId,6);
                        setVideo(url.get(position).BT1500);
                        break;
                }
            }

            private void saveChildPosition(int i,int k) {
                SharedPreferences sp = ImageLoaderInit.shared;
                SharedPreferences.Editor ed = sp.edit();
                ed.putInt("posi", i);
                ed.putInt("type",k);
                ed.commit();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bt200) {
            dialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
