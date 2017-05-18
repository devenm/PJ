package com.init.panjj.otherclasses;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.share.internal.ShareConstants;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.ArrayList;

public class BackgroundProcess extends Application {
    public static final String TAG;
    private static BackgroundProcess mInstance;
    public static SharedPreferences shared;
    private RequestQueue mRequestQueue;
    ArrayList<String> showAdsIn;
    protected String userAgent;

    static {
        TAG = BackgroundProcess.class.getSimpleName();
    }

    public static synchronized BackgroundProcess getInstance() {
        BackgroundProcess backgroundProcess;
        synchronized (BackgroundProcess.class) {
            if (mInstance == null) {
                backgroundProcess = new BackgroundProcess();
                mInstance = backgroundProcess;
            } else {
                backgroundProcess = mInstance;
            }
        }
        return backgroundProcess;
    }

    public Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        shared = getSharedPreferences(ShareConstants.WEB_DIALOG_PARAM_NAME, 0);
        ImageLoader.getInstance().init(new Builder(getApplicationContext()).threadPriority(3).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(52428800).tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build()).writeDebugLogs().build());
    }

    public Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory((Context) this, (TransferListener) bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(this.userAgent, bandwidthMeter);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        CharSequence tag = "yes";
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }
}
