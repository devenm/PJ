package com.init.panjj.otherclasses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.SerachArtistAdap;
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

public class SearchDialogArtist extends DialogFragment {
    MainActivity activity;
    RecyclerView artistdtacontainer;
    ImageView back;
    ACProgressFlower progressDialog;
    SerachArtistAdap searchatrailerAdap;
    ArrayList<ItemBean> searchlist;
    TextView searchres;
    String searchval;
    EditText serchbox;
    StringRequest stringRequest;
    View view;

    /* renamed from: com.init.panj.clases.SearchDialogArtist.1 */
    class C05351 implements OnClickListener {
        C05351() {
        }

        public void onClick(View v) {
            SearchDialogArtist.this.dismiss();
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialogArtist.2 */
    class C05362 implements OnKeyListener {
        C05362() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == 66) {
                SearchDialogArtist.this.searchval = SearchDialogArtist.this.serchbox.getText().toString();
                Log.e("keycall", "Enter pressed" + SearchDialogArtist.this.searchval);
                if (SearchDialogArtist.this.stringRequest.hasHadResponseDelivered()) {
                    SearchDialogArtist.this.jsonRequest();
                }
                ((InputMethodManager) SearchDialogArtist.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            return false;
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialogArtist.3 */
    class C09533 implements Listener<String> {
        C09533() {
        }

        public void onResponse(String response) {
            if (response != null) {
                try {
                    SearchDialogArtist.this.setJsonValue(new JSONObject(response));
                    Log.e("value", BuildConfig.VERSION_NAME + response);
                } catch (JSONException e) {
                    Log.e("error due to", e.getMessage());
                }
            }
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialogArtist.4 */
    class C09544 implements ErrorListener {
        C09544() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("oho Error ", "Error: " + error.toString());
        }
    }

    /* renamed from: com.init.panj.clases.SearchDialogArtist.5 */
    class C10725 extends StringRequest {
        C10725(int x0, String x1, Listener x2, ErrorListener x3) {
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> stringStringHashMap = new HashMap();
            stringStringHashMap.put("itemToFetch", SearchDialogArtist.this.searchlist.size() + BuildConfig.VERSION_NAME);
            return stringStringHashMap;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.searchlist = new ArrayList();
       // setStyle(2, 16973830);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.artist_search, container, false);
        this.activity = (MainActivity) getActivity();
        this.back = (ImageView) this.view.findViewById(R.id.back);
        this.searchres = (TextView) this.view.findViewById(R.id.serchres);
        this.serchbox = (EditText) this.view.findViewById(R.id.searchbox);
        this.artistdtacontainer = (RecyclerView) this.view.findViewById(R.id.artiscontent);
        this.artistdtacontainer.setLayoutManager(new GridLayoutManager(this.activity, 2));
        this.searchatrailerAdap = new SerachArtistAdap(this.activity, this.searchlist, this);
        this.artistdtacontainer.setAdapter(this.searchatrailerAdap);
        this.searchval = getArguments().getString("searchval");
        this.serchbox.setText(BuildConfig.VERSION_NAME + this.searchval);
        this.searchres.setText("Search Result :-" + this.searchval);
        this.progressDialog = new Builder(getActivity()).direction(100).themeColor(-1).text("Loading...").build();
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        jsonRequest();
        this.back.setOnClickListener(new C05351());
        this.serchbox.setOnKeyListener(new C05362());
        return this.view;
    }

    private void jsonRequest() {
        this.stringRequest = new C10725(1, "http://iiscandy.com/panj/SearchArtist?artist=" + this.searchval.replace(" ", "%20"), new C09533(), new C09544());
        BackgroundProcess.getInstance().addToRequestQueue(this.stringRequest);
        this.stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, LineScalePartyIndicator.SCALE));
    }

    private void setJsonValue(JSONObject jsonObject) {
        try {
            this.progressDialog.dismiss();
            try {
                JSONArray mainjson = jsonObject.getJSONArray("SearchResult");
                for (int j = 0; j < mainjson.length(); j++) {
                    JSONObject jsonObject2 = mainjson.getJSONObject(j);
                    ItemBean items = new ItemBean();
                    items.tredname = jsonObject2.getString("videotitle");
                    if (MainActivity.isConnectionFast(0, 0) && MainActivity.isConnectionFast(0, 1) && MainActivity.isConnectionFast(0, 2) && MainActivity.isConnectionFast(0, 3) && MainActivity.isConnectionFast(0, 4)) {
                        items.tredcover = jsonObject2.getString("high");
                    } else {
                        items.tredcover = jsonObject2.getString("high");
                    }
                    items.id = jsonObject2.getString("videoId");
                    this.searchlist.add(items);
                }
                this.searchatrailerAdap.notifyDataSetChanged();
            } catch (Exception e) {
            }
        } catch (Exception e2) {
        }
    }
}
