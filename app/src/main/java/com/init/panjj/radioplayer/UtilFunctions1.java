package com.init.panjj.radioplayer;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;


import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UtilFunctions1 {
	static String LOG_CLASS = "UtilFunctions";

	/**
	 * Check if service is running or not
	 * @param serviceName
	 * @param context
	 * @return
	 */
	public static boolean isServiceRunning(String serviceName, Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if(serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Read the songs present in external storage
	 * @param context
	 * @return
	 */


	public static ArrayList<RadioBean> listOfSongs1(Context context){
		ArrayList<RadioBean> listOfSongs1 = new ArrayList<RadioBean>();
		//listOfSongs1= AudioAdap.feedItemList;
		return listOfSongs1;
	}

	public static Bitmap getAlbumart(Context context, Long album_id){
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
	    try{
	        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
	        ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
	        if (pfd != null){
	            FileDescriptor fd = pfd.getFileDescriptor();
	            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
	            pfd = null;
	            fd = null;
	        }
	    } catch(Error ee){}
	    catch (Exception e) {}
	    return bm;
	}

	/**
	 * @param context
	 * @return
	 */

	/**
	 * Convert milliseconds into time hh:mm:ss
	 * @param milliseconds
	 * @return time in String
	 */
	public static String getDuration(long milliseconds) {
	/*	long sec = (milliseconds / 1000) % 60;
		long min = (milliseconds / (60 * 1000))%60;
		long hour = milliseconds / (60 * 60 * 1000);

		String s = (sec < 10) ? "0" + sec : "" + sec;
		String m = (min < 10) ? "0" + min : "" + min;
		String h = "" + hour;

//		String time = (String.format("%d : %d",
//				TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
//				TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) -
//						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) milliseconds))));
		if(hour > 0) {
			time = h + ":" + m + ":" + s;
		} else {
			time = m + ":" + s;
		}*/
		String time="";
		return time;
	}
	public static String getDuration1(long milliseconds) {
		/*long sec = (milliseconds / 1000) % 60;
		long min = (milliseconds / (60 * 1000))%60;
		long hour = milliseconds / (60 * 60 * 1000);

		String s = (sec < 10) ? "0" + sec : "" + sec;
		String m = (min < 10) ? "0" + min : "" + min;
		String h = "" + hour;*/

		String time = (String.format("%d : %d",
				TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
				TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) milliseconds))));
		/*if(hour > 0) {
			time = h + ":" + m + ":" + s;
		} else {
			time = m + ":" + s;
		}*/
		return time;
	}
	public static long getDuration11(long milliseconds) {
		/*long sec = (milliseconds / 1000) % 60;
		long min = (milliseconds / (60 * 1000))%60;
		long hour = milliseconds / (60 * 60 * 1000);

		String s = (sec < 10) ? "0" + sec : "" + sec;
		String m = (min < 10) ? "0" + min : "" + min;
		String h = "" + hour;*/

		/*String time = (String.format("%d : %d",
				TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
				TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) milliseconds))));*/
		/*if(hour > 0) {
			time = h + ":" + m + ":" + s;
		} else {
			time = m + ":" + s;
		}*/
		return milliseconds;
	}
	
	public static boolean currentVersionSupportBigNotification() {
		int sdkVersion = android.os.Build.VERSION.SDK_INT;
		if(sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			return true;
		}
		return false;
	}
	
	public static boolean currentVersionSupportLockScreenControls() {
		int sdkVersion = android.os.Build.VERSION.SDK_INT;
		if(sdkVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			return true;
		}
		return false;
	}
}
