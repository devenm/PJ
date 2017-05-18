package com.init.panjj.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.facebook.share.internal.ShareConstants;
import com.wang.avi.indicators.LineScalePartyIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.BuildConfig;

public class ServerRequest {
    private HashMap<String, String> data;
    int requestType;
    private ServerResult serverResult;
    private String serverUrl;

    public ServerRequest(ServerResult serverResult, String serverUrl, HashMap<String, String> data, int calledFor, int requestType) {
        this.serverResult = serverResult;
        this.serverUrl = serverUrl;
        this.data = data;
        this.requestType = requestType;
        requestToServer(calledFor);
    }

    private void requestToServer(final int calledFor) {
        JsonCustomRequest jsonCustomRequest = new JsonCustomRequest(this.requestType, this.serverUrl, this.data, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ServerRequest.this.serverResult.setDataFromServer(response, calledFor);
                } catch (JSONException e) {
                    Log.e("json", BuildConfig.VERSION_NAME + e);
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ServerRequest.this.serverResult.sendErrorMessage(ServerRequest.this.showVolleyError(error));
            }
        });
        jsonCustomRequest.setRetryPolicy(new DefaultRetryPolicy(75000, 0, LineScalePartyIndicator.SCALE)).setTag(ShareConstants.WEB_DIALOG_PARAM_DATA);
        VolleyInstance.getInstance().getRequestQueue().add(jsonCustomRequest);
    }

    private String showVolleyError(VolleyError error) {
        String message = BuildConfig.VERSION_NAME;
        if (error instanceof NetworkError) {
            return "Unable to connect to internet";
        }
        if (error instanceof ServerError) {
            return "Server is busy please try after some time";
        }
        if (error instanceof AuthFailureError) {
            return "Unable to communicate from server";
        }
        if (error instanceof ParseError) {
            return "Unknown response from server";
        }
        if (error instanceof TimeoutError) {
            return "Time out error,Please scroll again";
        }
        return message;
    }
}
