package com.init.panjj.otherclasses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.SearchMovieAdap;
import com.init.panjj.adapter.SearchVideoAdap;
import com.init.panjj.adapter.SearchatrailerAdap;
import com.init.panjj.model.ItemBean;
import com.wang.avi.indicators.LineScalePartyIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressFlower;
import cc.cloudist.acplibrary.ACProgressFlower.Builder;
import de.hdodenhof.circleimageview.BuildConfig;

public class SearchDialog extends DialogFragment {
    MainActivity activity;
    ImageView back;
    RecyclerView firstcontainer;
    LinearLayout mivie;
    ArrayList<ItemBean> movielist;
    ACProgressFlower progressDialog;
    SearchMovieAdap searchMovieAdap;
    SearchVideoAdap searchVideoAdap;
    SearchatrailerAdap searchatrailerAdap;
    String searchval;
    RecyclerView secitemcontainer;
    EditText serchbox;
    StringRequest stringRequest;
    RecyclerView thirdcontainer;
    LinearLayout trailer;
    ArrayList<ItemBean> trailerlist;
    ArrayList<ItemBean> url1;
    ArrayList<ItemBean> url2;
    ArrayList<ItemBean> url3;
    LinearLayout video;
    ArrayList<ItemBean> videolist;
    View view;

    /* renamed from: com.init.panj.clases.SearchDialog.1 */
    class C05331 implements OnClickListener {
        C05331() {
        }

        public void onClick(View v) {
            SearchDialog.this.dismiss();
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialog.2 */
    class C05342 implements OnKeyListener {
        C05342() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == 66) {
                SearchDialog.this.searchval = SearchDialog.this.serchbox.getText().toString();
                Log.e("keycall", "Enter pressed" + SearchDialog.this.searchval);
                if (SearchDialog.this.stringRequest.hasHadResponseDelivered()) {
                    SearchDialog.this.jsonRequest();
                }
                ((InputMethodManager) SearchDialog.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            return false;
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialog.3 */
    class C09513 implements Listener<String> {
        C09513() {
        }

        public void onResponse(String response) {
            if (response != null) {
                try {
                    SearchDialog.this.setJsonValue(new JSONObject(response));
                    Log.e("value", BuildConfig.VERSION_NAME + response);
                } catch (JSONException e) {
                    Log.e("error due to", e.getMessage());
                }
            }
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialog.4 */
    class C09524 implements ErrorListener {
        C09524() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("oho Error ", "Error: " + error.toString());
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialog.5 */
    class C10715 extends StringRequest {
        C10715(int x0, String x1, Listener x2, ErrorListener x3) {
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> stringStringHashMap = new HashMap();
            stringStringHashMap.put("itemToFetch", SearchDialog.this.trailerlist.size() + BuildConfig.VERSION_NAME);
            stringStringHashMap.put("itemToFetcht", SearchDialog.this.trailerlist.size() + BuildConfig.VERSION_NAME);
            return stringStringHashMap;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.trailerlist = new ArrayList();
        this.movielist = new ArrayList();
        this.videolist = new ArrayList();
        this.url1 = new ArrayList();
        this.url2 = new ArrayList();
        this.url3 = new ArrayList();
        //setStyle(2, 16973830);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.search_dialog, container, false);
        this.activity = (MainActivity) getActivity();
        this.back = (ImageView) this.view.findViewById(R.id.back);
        this.serchbox = (EditText) this.view.findViewById(R.id.searchbox);
        this.firstcontainer = (RecyclerView) this.view.findViewById(R.id.trailercontainer);
        this.secitemcontainer = (RecyclerView) this.view.findViewById(R.id.moviescontainer);
        this.thirdcontainer = (RecyclerView) this.view.findViewById(R.id.videocontainer);
        this.firstcontainer.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.secitemcontainer.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.thirdcontainer.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.searchatrailerAdap = new SearchatrailerAdap(this.activity, this.trailerlist, this.url1, this);
        this.searchMovieAdap = new SearchMovieAdap(this.activity, this.movielist, this.url2, this);
        this.searchVideoAdap = new SearchVideoAdap(this.activity, this.videolist, this.url3, this);
        this.firstcontainer.setAdapter(this.searchatrailerAdap);
        this.secitemcontainer.setAdapter(this.searchMovieAdap);
        this.thirdcontainer.setAdapter(this.searchVideoAdap);
        this.trailer = (LinearLayout) this.view.findViewById(R.id.trailer);
        this.mivie = (LinearLayout) this.view.findViewById(R.id.movies);
        this.video = (LinearLayout) this.view.findViewById(R.id.videosong);
        this.searchval = getArguments().getString("searchval");
        this.serchbox.setText(BuildConfig.VERSION_NAME + this.searchval);
        this.progressDialog = new Builder(getActivity()).direction(100).themeColor(-1).text("Loading...").build();
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        jsonRequest();
        this.back.setOnClickListener(new C05331());
        this.serchbox.setOnKeyListener(new C05342());
        return this.view;
    }

    private void jsonRequest() {
        this.stringRequest = new C10715(1, "http://iiscandy.com/panj/SearchItem?text=" + this.searchval + "&stype=m", new C09513(), new C09524());
        BackgroundProcess.getInstance().addToRequestQueue(this.stringRequest);
        this.stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, LineScalePartyIndicator.SCALE));
    }

    private void setJsonValue(JSONObject jsonObject) {
        try {
            int j;
            JSONObject jsonObject2;
            ItemBean items;
            this.progressDialog.dismiss();
            JSONObject mainjson = jsonObject.getJSONObject("SearchResult");
            try {
                this.trailerlist.clear();
                JSONArray trailors = mainjson.getJSONArray("Trailer");
                for (j = 0; j < trailors.length(); j++) {
                    jsonObject2 = trailors.getJSONObject(j);
                    items = new ItemBean();
                    items.tredname = jsonObject2.getString("videotitle");
                    if (MainActivity.isConnectionFast(0, 0) && MainActivity.isConnectionFast(0, 1) && MainActivity.isConnectionFast(0, 2) && MainActivity.isConnectionFast(0, 3) && MainActivity.isConnectionFast(0, 4)) {
                        items.tredcover = jsonObject2.getString("medium");
                    } else {
                        items.tredcover = jsonObject2.getString("medium");
                    }
                    items.id = jsonObject2.getString("videoId");
                    this.trailerlist.add(items);
                }
                this.searchatrailerAdap.notifyDataSetChanged();
            } catch (Exception e) {
                this.trailer.setVisibility(View.GONE);
            }
            try {
                this.videolist.clear();
                JSONArray videos = mainjson.getJSONArray("Videos");
                for (j = 0; j < videos.length(); j++) {
                    jsonObject2 = videos.getJSONObject(j);
                    items = new ItemBean();
                    items.albumname = jsonObject2.getString("videotitle");
                    if (MainActivity.isConnectionFast(0, 0) && MainActivity.isConnectionFast(0, 1) && MainActivity.isConnectionFast(0, 2) && MainActivity.isConnectionFast(0, 3) && MainActivity.isConnectionFast(0, 4)) {
                        items.albumcover = jsonObject2.getString("medium");
                    } else {
                        items.albumcover = jsonObject2.getString("medium");
                    }
                    items.id = jsonObject2.getString("videoId");
                    this.videolist.add(items);
                }
                this.searchVideoAdap.notifyDataSetChanged();
            } catch (Exception e2) {
                this.video.setVisibility(View.GONE);
            }
            try {
                this.movielist.clear();
                JSONArray Featured = mainjson.getJSONArray("Movies");
                Log.e("videolength", BuildConfig.VERSION_NAME + Featured.length());
                if (Featured == null) {
                    this.mivie.setVisibility(View.GONE);
                }
                for (j = 0; j < Featured.length(); j++) {
                    jsonObject2 = Featured.getJSONObject(j);
                    items = new ItemBean();
                    items.albumname = jsonObject2.getString("videotitle");
                    if (MainActivity.isConnectionFast(0, 0) && MainActivity.isConnectionFast(0, 1) && MainActivity.isConnectionFast(0, 2) && MainActivity.isConnectionFast(0, 3) && MainActivity.isConnectionFast(0, 4)) {
                        items.albumcover = jsonObject2.getString("medium");
                    } else {
                        items.albumcover = jsonObject2.getString("medium");
                    }
                    items.id = jsonObject2.getString("videoId");
                    this.movielist.add(items);
                }
                this.searchMovieAdap.notifyDataSetChanged();
            } catch (Exception e3) {
                this.mivie.setVisibility(View.GONE);
            }
        } catch (Exception e4) {
        }
    }
}
