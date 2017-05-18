package com.init.panjj.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.init.panjj.R;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.model.CommentBean;
import com.init.panjj.model.ItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RecieveExo extends AppCompatActivity implements OnPreparedListener, OnCompletionListener {
EMVideoView exoVideoView;
    String url="",id;
    ArrayList<ItemBean> rlist;
    ArrayList<ItemBean> rurl;
    ImageView revomendedoneimageview;
    LinearLayout linearLayoutrecomen, potraitview;
    TextView reconetitle, reconedesc;
    StringRequest stringRequest;
    String imageurl;
    FrameLayout frameLayout;
    ImageButton quality,reload;
    ImageView cancle;
    Dialog dialog;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_exo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rlist=new ArrayList<>();
        rurl=new ArrayList<>();
        exoVideoView= (EMVideoView) findViewById(R.id.video_view);
        frameLayout = (FrameLayout) findViewById(R.id.add);
        quality = (ImageButton) findViewById(R.id.quality);
        cancle= (ImageView) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutrecomen.setVisibility(View.GONE);
            }
        });
        Intent intent=getIntent();
        url=intent.getDataString();
        id=url.substring(url.indexOf("="));
        setVideo(url.substring(0,url.indexOf("id=")));
     // Toast.makeText(RecieveExo.this,"id= "+id+" "+url.substring(0,url.indexOf("id=")),Toast.LENGTH_LONG).show();
        addSimiler(id);
        dialogCreate();
        exoVideoView.setOnCompletionListener(this);
        reload = (ImageButton) findViewById(R.id.reload);
        linearLayoutrecomen = (LinearLayout) findViewById(R.id.recommendone);
        potraitview = (LinearLayout) findViewById(R.id.potraitlayout);
        revomendedoneimageview = (ImageView) findViewById(R.id.recomoneimageview);
        reconetitle = (TextView) findViewById(R.id.reconetitle);
        reconedesc = (TextView) findViewById(R.id.reconedesc);
        revomendedoneimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVideo(rurl.get(0).m3u8);
            }
        });
        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exoVideoView.reset();
                setVideo(rurl.get(0).m3u8);
            }
        });
    }

    private void dialogCreate() {
        dialog = new Dialog(RecieveExo.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        dialog.setTitle("Quality");
        Button bt = (Button) dialog.findViewById(R.id.cancle);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        radioGroup = (RadioGroup) dialog.findViewById(R.id.qualitygroup);

    }

    private void setVideo(String url) {
        frameLayout.setVisibility(View.GONE);
        exoVideoView.setOnPreparedListener(this);
        exoVideoView.setVideoURI(Uri.parse(url));


    }

    @Override
    public void onPrepared() {
        exoVideoView.start();
    }

    @Override
    public void onBackPressed() {
    Intent intent=new Intent(RecieveExo.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
    private void addSimiler(String iid) {
String ids=iid.replace("=","");
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/SingleVideo?id="+ids, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setValue(new JSONObject(response));
                        Log.e("simi", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                return stringStringHashMap;
            }
        };
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setValue(JSONObject jsonObject) {

        try {

            JSONObject jsonOb = jsonObject.getJSONObject("Video");
            try {
                JSONArray comnt = jsonOb.getJSONArray("Comments");
                for (int i = 0; i < comnt.length(); i++) {
                    JSONObject comment = comnt.getJSONObject(i);
                    CommentBean commentBean = new CommentBean();
                    commentBean.username = comment.getString("UserName");
                    // commentBean.profilepic=comment.getString("");
                    commentBean.comment = comment.getString("Comment");
                    commentBean.date = comment.getString("DateTime");
                    JSONObject js=jsonOb.getJSONObject("Urls");
                    ItemBean itemBean=new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8=js.getString("m3u8");
                    itemBean.dlink=js.getString("dlink");
                    rurl.add(itemBean);
                }
                MainActivity.allurl=rurl;
            }
            catch (Exception e){
                Log.e("comment error",e.toString());

            }

            try {

                JSONArray jsonArray = jsonOb.getJSONArray("SimilarVideos");
                if (jsonArray.length()==0)
                {

                }
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject similerobject = jsonArray.getJSONObject(k);
                    ItemBean items = new ItemBean();
                    items.tredcover = similerobject.getString("Default");
                    items.tredname = similerobject.getString("videotitle");
                    items.id=similerobject.getString("videoId");
                    rlist.add(items);
                    Log.e("simi", "" + items.tredname);
                }
            }
            catch (Exception e){
            }
            for (int j = 0; j < jsonOb.length(); j++) {
               /* likecount=jsonOb.getString("likescount");
                viewcount=jsonOb.getString("ViewCount");
                information=jsonOb.getString("title")+"  "+jsonOb.getString("Artist")+" "+jsonOb.getString("genre")+" "+jsonOb.getString("Description");
                infotitle=jsonOb.getString("title");
                infoartist=jsonOb.getString("Artist");
                infoproducer=jsonOb.getString("Producer");
                infodirector=jsonOb.getString("Director");
                infomusicdirector=jsonOb.getString("MusicDirector");*/
                imageurl=jsonOb.getString("StarImage");

            }
            try{
                for (int j = 0; j < jsonOb.length(); j++) {
                    JSONObject js = jsonOb.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8=js.getString("m3u8");
                    itemBean.dlink=js.getString("dlink");
                    rurl.add(itemBean);
                }
            }
            catch (Exception e){

            }
        } catch (Exception e) {

            Log.e("VideoError", e.toString());
        }

    }

    @Override
    public void onCompletion() {
        frameLayout.setVisibility(View.VISIBLE);
        exoVideoView.setVisibility(View.VISIBLE);
        reconetitle.setText(" " + rlist.get(0).tredname);
        reconedesc.setText(" " + rlist.get(0).treddesp);
        ImageLoader.getInstance().loadImage(rlist.get(0).tredcover, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                revomendedoneimageview.setImageResource(R.drawable.logo_fade);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                super.onLoadingComplete(imageUri, view, loadedImage);
                revomendedoneimageview.setImageBitmap(loadedImage);

            }
        });

    }
}
