package com.init.panjj.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.init.panjj.otherclasses.BackgroundProcess;

public class VolleyInstance {
    private static VolleyInstance mInstance;
    private RequestQueue volleyRequest;

    static {
        mInstance = null;
    }

    private VolleyInstance() {
        this.volleyRequest = getRequestQueue();
    }

    public static VolleyInstance getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyInstance();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (this.volleyRequest == null) {
            this.volleyRequest = Volley.newRequestQueue(BackgroundProcess.getInstance().getAppContext());
        }
        return this.volleyRequest;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.volleyRequest != null) {
            this.volleyRequest.cancelAll(tag);
        }
    }
}
