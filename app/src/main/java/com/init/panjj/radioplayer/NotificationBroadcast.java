package com.init.panjj.radioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.init.panjj.activity.MainActivity;


public class NotificationBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("broadstart","dfsf");
		if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                	if(!PlayerConstants1.SONG_PAUSED){
    					Controls.pauseControl(context);
                	}else{
    					Controls.playControl(context);
                	}
                	break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
					Log.e("play","playyyyyy1");
                	break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
					Log.e("play","playyyyyy2");
                	break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
					Log.e("play","playyyyyy3");
                	break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                	Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                	Controls.nextControl(context);
                	break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                	Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                	Controls.previousControl(context);
                	break;
            }
		}  else{
            	if (intent.getAction().equals(SongService1.NOTIFY_PLAY)) {
    					Controls.playControl(context);
        		} else if (intent.getAction().equals(SongService1.NOTIFY_PAUSE)) {


    				Controls.pauseControl(context);
        		} else if (intent.getAction().equals(SongService1.NOTIFY_NEXT)) {
PlayerConstants1.SONG_CHANGED=false;
        			Controls.nextControl(context);
        		} else if (intent.getAction().equals(SongService1.NOTIFY_DELETE)) {
Controls.pauseControl(context);
					Intent i = new Intent(context, SongService1.class);
					context.stopService(i);
//

        		}else if (intent.getAction().equals(SongService1.NOTIFY_PREVIOUS)) {
					PlayerConstants1.SONG_CHANGED=false;
    				Controls.previousControl(context);
        		}
				else if (intent.getAction().equals(SongService1.NOTIFY_START)) {
					Intent in = new Intent(context, MainActivity.class);
			        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(in);
				}
		}
	}
	
	public String ComponentName() {
		return this.getClass().getName(); 
	}
}
