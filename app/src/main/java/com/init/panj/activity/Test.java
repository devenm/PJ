package com.init.panj.activity;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
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
import com.init.panj.clases.ImageLoaderInit;

public class Test extends AppCompatActivity {
SimpleExoPlayerView simpleExoPlayerView;
    private Handler mainHandler;
    private Timeline.Window window;
    private EventLogger eventLogger;
    private DataSource.Factory mediaDataSourceFactory;
    SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        window = new Timeline.Window();
        eventLogger = new EventLogger();
        setContentView(R.layout.activity_test);
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
  player    = ExoPlayerFactory.newSimpleInstance(Test.this, trackSelector, loadControl);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);
        Uri uris= Uri.parse("http://iiscandyhlsori-vh.akamaihd.net/i/Music_Videos/Hindi/Detective_Byomkesh_Bakshy/Promo/Calcutta_Police_Deal_Nahi_Karta/Calcutta_Police_Deal_Nahi_Karta_Detective_Byomkesh_Bakshy_,200,1500,500,385,750,1000,600,_final.mp4.csmil/master.m3u8");
        DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);

        MediaSource videoSource = new ExtractorMediaSource(uris, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
        MediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse("file:///android_asset/myfile.vtt"),dataSourceFactory, Format.createTextSampleFormat("0", MimeTypes.TEXT_VTT, null, Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, "se", null, 0),1000);
// Plays the video with the sideloaded subtitle.
        MediaSource mediaSource=buildMediaSource(uris);
        Log.e("mds","hh "+mediaSource.toString());
        MergingMediaSource mergedSource = new MergingMediaSource(mediaSource, subtitleSource);
        player.prepare(mergedSource);
        player.setPlayWhenReady(true);


    }
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((ImageLoaderInit) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
    private MediaSource buildMediaSource(Uri uri) {

        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
