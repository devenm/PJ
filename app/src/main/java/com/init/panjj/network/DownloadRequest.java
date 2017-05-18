package com.init.panjj.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class DownloadRequest extends Request<byte[]> {
    private final Listener<byte[]> mListener;

    public DownloadRequest(int method, String mUrl, Listener<byte[]> listener, ErrorListener errorListener) {
        super(method, mUrl, errorListener);
        setShouldCache(false);
        this.mListener = listener;
    }

    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    protected void deliverResponse(byte[] response) {
        this.mListener.onResponse(response);
    }
}
