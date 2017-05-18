package com.init.panjj.network;

import android.app.Activity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import de.hdodenhof.circleimageview.BuildConfig;

public class ShowVolleyError {
    public ShowVolleyError(Activity mainPage, VolleyError error) {
        String message = BuildConfig.VERSION_NAME;
        if (error instanceof NetworkError) {
            message = "Unable to connect to internet";
        } else if (error instanceof ServerError) {
            message = "Server is busy please try after some time";
        } else if (error instanceof AuthFailureError) {
            message = "Unable to communicate from server";
        } else if (error instanceof ParseError) {
            message = "Unknown response from server";
        } else if (error instanceof TimeoutError) {
            message = "Time out error,Please scroll again";
        }
    }
}
