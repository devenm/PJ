package com.init.panj.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.init.panj.R;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.model.LiveTvBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LiveStreamPlay extends AppCompatActivity implements OnPreparedListener {
    EMVideoView emVideoView;
    String url,urls;
    StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        emVideoView = (EMVideoView)findViewById(R.id.video_view);
        url=getIntent().getStringExtra("url");
        livetvdata();
        //setVideo("http://iiscandy.com/panj/LiveStreamplay?channel="+url);
    }

    private void setVideo(String url) {
        emVideoView.setOnPreparedListener(this);
        emVideoView.setVideoURI(Uri.parse(url));
    }

    @Override
    public void onPrepared() {
        emVideoView.setVisibility(View.VISIBLE);
        emVideoView.start();
    }
    private void livetvdata() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/LiveStreamplay?channel="+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setLiveTv(new JSONObject(response));
                        Log.e("value", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("oho Error ", "Error: " + error.toString());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void setLiveTv(JSONObject jsonObject) {
        try {
            if (jsonObject!=null)
            {
                JSONArray jsonArray=jsonObject.getJSONArray("LiveStream");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                   urls=jsonObject1.getString("url");
                }
                setVideo(urls);
            }        }
        catch (Exception e){
            Log.e("liveExcep",""+e.toString());
        }
    }
}
