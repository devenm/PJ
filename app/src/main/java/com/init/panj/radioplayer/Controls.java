package com.init.panj.radioplayer;

import android.content.Context;

import com.init.panj.R;


public class Controls {
	static String LOG_CLASS = "Controls";
	public static void playControl(Context context) {
		sendMessage(context.getResources().getString(R.string.play));
	}

	public static void pauseControl(Context context) {
		sendMessage(context.getResources().getString(R.string.pause));
	}
	public static void seekToControl(Context context) {
		sendSeekBarMessage(context.getResources().getString(R.string.seek_to));
	}

	public static void nextControl(Context context) {
		boolean isServiceRunning = UtilFunctions1.isServiceRunning(SongService1.class.getName(), context);
		if (!isServiceRunning)
			return;
		if(PlayerConstants1.SONGS_LIST1.size() > 0 ){
			if(PlayerConstants1.SONG_NUMBER < (PlayerConstants1.SONGS_LIST1.size()-1)){
				PlayerConstants1.SONG_NUMBER++;
				PlayerConstants1.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants1.SONG_CHANGE_HANDLER.obtainMessage());
			}else{
				PlayerConstants1.SONG_NUMBER = 0;
				PlayerConstants1.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants1.SONG_CHANGE_HANDLER.obtainMessage());
			}
		}
		PlayerConstants1.SONG_PAUSED = false;
	}

	public static void previousControl(Context context) {
		boolean isServiceRunning = UtilFunctions1.isServiceRunning(SongService1.class.getName(), context);
		if (!isServiceRunning)
			return;
		if(PlayerConstants1.SONGS_LIST1.size() > 0 ){
			if(PlayerConstants1.SONG_NUMBER > 0){
				PlayerConstants1.SONG_NUMBER--;
				PlayerConstants1.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants1.SONG_CHANGE_HANDLER.obtainMessage());
			}else{
				PlayerConstants1.SONG_NUMBER = PlayerConstants1.SONGS_LIST1.size() - 1;
				PlayerConstants1.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants1.SONG_CHANGE_HANDLER.obtainMessage());
			}
		}
		PlayerConstants1.SONG_PAUSED = false;
	}
	
	private static void sendMessage(String message) {
		try{
			PlayerConstants1.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants1.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
		}catch(Exception e){}
	}
	private static void sendSeekBarMessage(String message) {
		try {
			PlayerConstants1.SEEKBAR_HANDLER.sendMessage(PlayerConstants1.SEEKBAR_HANDLER.obtainMessage(0, message));
		} catch (Exception e) {
		}
	}
}
