package com.init.panjj.radioplayer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelections;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.init.panjj.R;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.Timer;
import java.util.TimerTask;

public class SongService1 extends Service implements AudioManager.OnAudioFocusChangeListener, TrackSelector.EventListener<MappingTrackSelector.MappedTrackInfo>, ExoPlayer.EventListener {
	String LOG_CLASS = "SongService";
	int NOTIFICATION_ID = 1111;
	public static final String NOTIFY_PREVIOUS = "com.init.panj.previous";
	public static final String NOTIFY_DELETE = "com.init.panj.delete";
	public static final String NOTIFY_PAUSE = "com.init.panj.pause";
	public static final String NOTIFY_PLAY = "com.init.panj.play";
	public static final String NOTIFY_NEXT = "com.init.panj.next";
	public static final String NOTIFY_START = "com.init.panj.start";
	private ComponentName remoteComponentName;
	private RemoteControlClient remoteControlClient;
	AudioManager audioManager;
	Bitmap mDummyAlbumArt;
	private static Timer timer;
	private static boolean currentVersionSupportBigNotification = false;
	private static boolean currentVersionSupportLockScreenControls = false;
	long sleptime;
	private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
	SharedPreferences.Editor editor;
	public static ExoPlayer simpleExoPlayer;
	private Handler mainHandler;
	private Timeline.Window window;
	private EventLogger eventLogger;
	private DataSource.Factory mediaDataSourceFactory;
	private static SimpleExoPlayer player;
	private MappingTrackSelector trackSelector;
	private TrackSelectionHelper trackSelectionHelper;
	private boolean playpausecheck=false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
		releasePlayer();
		eventLogger = new EventLogger();
		mediaDataSourceFactory = buildDataSourceFactory(true);
		mainHandler = new Handler();
		TrackSelection.Factory videoTrackSelectionFactory =
				new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
		trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
		trackSelector.addListener(this);
		trackSelector.addListener(eventLogger);
		trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
		simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
		simpleExoPlayer.addListener(this);
        currentVersionSupportBigNotification = UtilFunctions1.currentVersionSupportBigNotification();
       currentVersionSupportLockScreenControls = UtilFunctions1.currentVersionSupportLockScreenControls();
        timer = new Timer();
		super.onCreate();
	}

	@Override
	public void onTrackSelectionsChanged(TrackSelections<? extends MappingTrackSelector.MappedTrackInfo> trackSelections) {

	}

	@Override
	public void onLoadingChanged(boolean isLoading) {
if (isLoading){
	Rplayer.prog();
}
		else
	Rplayer.changeButton();
	}

	@Override
	public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
		if (playWhenReady&&simpleExoPlayer!=null) {
			try {
				simpleExoPlayer.setPlayWhenReady(playWhenReady);
				timer.scheduleAtFixedRate(new MainTask(), 0, 2000);
				Controls.playControl(getApplicationContext());
				Rplayer.changeButton();
			}
			catch (Exception e){

			}
		}
	}

	@Override
	public void onTimelineChanged(Timeline timeline, Object manifest) {

	}

	@Override
	public void onPlayerError(ExoPlaybackException error) {

	}

	@Override
	public void onPositionDiscontinuity() {

	}


	/**
	 * Send message from timer
	 * @author jonty.ankit
	 */
	private class MainTask extends TimerTask {
        public void run(){
            handler.sendEmptyMessage(0);
        }
    } 
	
	 private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
        	if(simpleExoPlayer != null){
				try {
					int progress = (int) ((simpleExoPlayer.getCurrentPosition() * 100) / simpleExoPlayer.getDuration());
					Integer i[] = new Integer[3];
					i[0] = (int)simpleExoPlayer.getCurrentPosition();
					i[1] =(int)simpleExoPlayer.getDuration();
					i[2] = progress;
        			PlayerConstants1.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants1.PROGRESSBAR_HANDLER.obtainMessage(0, i));
        		}catch(Exception e){
					Log.e("durationerror",e.toString());
				}

        	}

    	}
    }; 
	    
    @SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, final int flags, int startId) {
		try {
			Log.e("start","dfdsf");
			if(PlayerConstants1.SONGS_LIST1.size() <= 0){

			/*//	PlayerConstants1.SONGS_LIST = UtilFunctions1.listOfSongs(getApplicationContext());
				//PlayerConstants1.SONGS_LIST1=AudioAdap.feedItemList;
				PlayerConstants1.SONGS_LIST1=UtilFunctions1.listOfSongs1(getApplicationContext());*/
			}
			//MediaItem data = PlayerConstants1.SONGS_LIST.get(PlayerConstants1.SONG_NUMBER);
			RadioBean data1=PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER);
			if(currentVersionSupportLockScreenControls){
				RegisterRemoteClient();
			}
			//String songPath = data.getPath();
			//playSong(songPath, data);
			String songPath=data1.getRadiourl();
		playSong1(songPath,data1);
			newNotification();
			
			PlayerConstants1.SONG_CHANGE_HANDLER = new Handler(new Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					RadioBean data = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER);
					String songPath = data.getRadiourl();
					newNotification();
					try{
						playSong1(songPath, data);
						Rplayer.changeButton();
					}catch(Exception e){
						e.printStackTrace();
					}
					return false;
				}
			});
			PlayerConstants1.SEEKBAR_HANDLER=new Handler(new Callback() {
				@Override
				public boolean handleMessage(Message msg) {
						String message = (String) msg.obj;
						if (simpleExoPlayer != null && message.equalsIgnoreCase(SongService1.this.getResources().getString(R.string.seek_to))) {
							simpleExoPlayer.seekTo(PlayerConstants1.SEEK_TO);

						return false;
					}
					return false;
				}
			});
			PlayerConstants1.PLAY_PAUSE_HANDLER = new Handler(new Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					String message = (String)msg.obj;
					if(simpleExoPlayer == null)
						return false;
					if(message.equalsIgnoreCase(getResources().getString(R.string.play))){
						PlayerConstants1.SONG_PAUSED = false;
						if(currentVersionSupportLockScreenControls){
							//remoteControlClient.setTransportControlFlags(KEYCODE_MEDIA_PAUSE);
						}
						simpleExoPlayer.setPlayWhenReady(true);
					}else if(message.equalsIgnoreCase(getResources().getString(R.string.pause))){
						PlayerConstants1.SONG_PAUSED = true;
						if(currentVersionSupportLockScreenControls){
							//remoteControlClient.setPlaybackState();
						}
						simpleExoPlayer.setPlayWhenReady(false);
					}
					newNotification();
					try{
						Rplayer.changeButton();
						//MainActivity.changeButton();
					}catch(Exception e){}
					Log.d("TAG", "TAG Pressed: " + message);
					return false;
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}

	/**
	 * Notification
	 * Custom Bignotification is available from API 16
	 */
	@SuppressLint("NewApi")
	public void newNotification() {
		String songName = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioname();
		String albumName =getResources().getString(R.string.app_name);
		RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.notification);
		RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.bignotification);

		final Notification notification = new NotificationCompat.Builder(getApplicationContext())
        .setSmallIcon(R.mipmap.panjicon)
        .setContentTitle(""+getResources().getString(R.string.app_name)).build();
        notification.priority= Notification.PRIORITY_MAX;
		setListeners(simpleContentView);
		setListeners(expandedView);
		//notification.bigContentView = expandedView;
		notification.contentView = simpleContentView;
		if(currentVersionSupportBigNotification){
			notification.bigContentView = expandedView;
		}
		try {
			Bitmap albumArt = null;
			Log.e("albumart", "" + albumArt);
			if (PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioimage() != null) {

				String url = "http://www.planetradiocity.com/images/fka-channel/fka-our-rj/rj-love-guru1426591192.jpg";
				ImageLoader.getInstance().loadImage(PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getRadioimage(), new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

						super.onLoadingComplete(imageUri, view, loadedImage);

							notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, loadedImage);
							if (currentVersionSupportBigNotification) {
								notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, loadedImage);
							}

					}

				});

			}
			else {
				notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.panjicon);
				if (currentVersionSupportBigNotification) {
					notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.panjicon);
				}
			}
		}catch(Exception e){
			Log.e("service error",e.toString());
			e.printStackTrace();
		}
		if(PlayerConstants1.SONG_PAUSED){
			notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
		notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
			if(currentVersionSupportBigNotification){
				notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
				notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
			}
		}else{
		notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
			notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);
			if(currentVersionSupportBigNotification){
				notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
				notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
			}
		}

		notification.contentView.setTextViewText(R.id.textSongName, songName);
		notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
		if(currentVersionSupportBigNotification){
			notification.bigContentView.setTextViewText(R.id.textSongName, songName);
			notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
		}
		try {
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			startForeground(NOTIFICATION_ID, notification);
		}
		catch (Exception e){
			Log.e("","");
		}
	}

	/**
	 * Notification click listeners
	 * @param view
	 */
	public void setListeners(RemoteViews view) {
		Intent previous = new Intent(NOTIFY_PREVIOUS);
		Intent delete = new Intent(NOTIFY_DELETE);
		Intent pause = new Intent(NOTIFY_PAUSE);
		Intent next = new Intent(NOTIFY_NEXT);
		Intent play = new Intent(NOTIFY_PLAY);
		Intent start=new Intent(NOTIFY_START);
		
		PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

		PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnDelete, pDelete);
		
		PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPause, pPause);
		
		PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnNext, pNext);
		
		PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

		PendingIntent pStart = PendingIntent.getBroadcast(getApplicationContext(), 0, start, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.imageViewAlbumArt, pStart);
	}
	
	@Override
	public void onDestroy() {
		if(simpleExoPlayer != null){
			simpleExoPlayer.stop();
			simpleExoPlayer = null;
		}
		super.onDestroy();
	}

	/**
	 * Play song, Update Lockscreen fields
	 * @param songPath
	 * @param data
	 */
	@SuppressLint("NewApi")

	private void playSong1(String songPath, RadioBean data) {
		try {
			Rplayer.prog();
			Uri uri= Uri.parse("http://prclive1.listenon.in:8808/");
			DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
			DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
					Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter);
// Produces Extractor instances for parsing the media data.
			ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
			  MediaSource videoSource = new ExtractorMediaSource(uri,dataSourceFactory, extractorsFactory, null, null);
		//	MediaSource mediaSource=buildMediaSource(uri);
			simpleExoPlayer.prepare(videoSource);
			simpleExoPlayer.setPlayWhenReady(true);
			Rplayer.changeUI();
			Controls.playControl(getApplicationContext());
			Log.e("vsource",songPath+"vd"+videoSource);

			if(currentVersionSupportLockScreenControls){
				UpdateMetadata1(data);
				remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
			}
		} catch (Exception e) {
			Log.e("mderror",e.toString());
		}

	}
	@SuppressLint("NewApi")
	private void RegisterRemoteClient(){
		remoteComponentName = new ComponentName(getApplicationContext(), new NotificationBroadcast().ComponentName());
		 try {
		   if(remoteControlClient == null) {
			   audioManager.registerMediaButtonEventReceiver(remoteComponentName);
			   Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			   mediaButtonIntent.setComponent(remoteComponentName);
			   PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
			   remoteControlClient = new RemoteControlClient(mediaPendingIntent);
			   audioManager.registerRemoteControlClient(remoteControlClient);
		   }
		   remoteControlClient.setTransportControlFlags(
				   RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
						   RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
						   RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
						   RemoteControlClient.FLAG_KEY_MEDIA_STOP |
						   RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
						   RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
	  }catch(Exception ex) {
	  }
	}
	
	@SuppressLint("NewApi")



	private void UpdateMetadata1(RadioBean data){
		if (remoteControlClient == null)
			return;
		MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
		//metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, data.getAlbum());
		metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, data.getRadioname());
		metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, data.getRadioname());
	//	mDummyAlbumArt = NirmolakPlayer.albumcover;
		if(mDummyAlbumArt == null){
			mDummyAlbumArt = BitmapFactory.decodeResource(getResources(), R.mipmap.panjicon);
		}
		metadataEditor.putBitmap(MetadataEditor.BITMAP_KEY_ARTWORK, mDummyAlbumArt);
		metadataEditor.apply();
		audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	}
	@Override
	public void onAudioFocusChange(int focusChange) {
			if (focusChange<=0&&PlayerConstants1.SONG_PAUSED==false){
				Controls.pauseControl(getApplicationContext());
				playpausecheck=true;
			}
			else if (playpausecheck) {
				Controls.playControl(getApplicationContext());
				playpausecheck=false;
			}
	}
	public static boolean isConnected(Context context){
		NetworkInfo info =getNetworkInfo(context);
		return (info != null && info.isConnected());
	}
	public static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
		return ((BackgroundProcess) getApplication())
				.buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
	}
	private MediaSource buildMediaSource(Uri uri) {
		return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
				mainHandler, eventLogger);

	}
	private void releasePlayer() {
		if (player != null) {
			player.release();
			player = null;
			trackSelector = null;
			trackSelectionHelper = null;
			eventLogger = null;
		}
	}

}