package com.init.panj.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.init.panj.R;

public class Playtest extends AppCompatActivity implements OnPreparedListener {
    EMVideoView emVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtest);
        emVideoView = (EMVideoView)findViewById(R.id.video_view);
        emVideoView.setOnPreparedListener(this);
        emVideoView.setVideoURI(Uri.parse("http://iiscandyhlsori-vh.akamaihd.net/i/Music_Videos/Hindi/FAN/FAN_Official_Trailer/FAN_Official_Trailer_Shah_Rukh_Khan_,200,1500,500,385,750,1000,600,_final.mp4.csmil/master.m3u8"));
    }

    @Override
    public void onPrepared() {
        emVideoView.start();
    }
}
