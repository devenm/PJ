package com.init.panjj.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.activity.SubtitlePlayer;
import com.init.panjj.activity.UserLogin;
import com.init.panjj.adapter.AdapterLive;
import com.init.panjj.adapter.MainContentAdoptor1;
import com.init.panjj.adapter.MainContentAdoptor2;
import com.init.panjj.adapter.MainContentAdoptor3;
import com.init.panjj.adapter.Trailer_Adaptor;
import com.init.panjj.adapter.TrendingAdaptor;
import com.init.panjj.model.CutomBean;
import com.init.panjj.model.ItemBean;
import com.init.panjj.model.LiveTvBean;
import com.init.panjj.otherclasses.AutoScrollViewPager;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.radioplayer.Controls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class HomeFragment extends Fragment {
    RecyclerView first_container,second_container, third_container, fourth_container, fivth_container,live_container;

    ArrayList<ItemBean> bannerlist, first_list, second_list,third_list, fourth_list, fivth_list, live_list;
    TextView first_all, second_all, third_all,fourth_all,fivth_all;
    MainActivity act;
    ArrayList<ItemBean> banner_url, firsturl, secondurl, third_url, fourth_url, fivth_url, live_url;
    ArrayList<LiveTvBean> listlive;
    StringRequest stringRequest;
    TrendingAdaptor trendingAdap;
    MainContentAdoptor1 newVideos_adap;
    MainContentAdoptor2 movies_adap;
    MainContentAdoptor3 recomended_adap;
    Trailer_Adaptor trailer_adap;
    AdapterLive liveTvAdaptor;
    SharedPreferences sharedPreferences;
    View rootView;
    TextView first_items, sec_items, third_items,fourth_item,fivth_items;
   String dtype="m";
    ACProgressFlower progressDialog;
    LoadImageToView switchImage;
    AutoScrollViewPager viewPager;
    ImageView facebookloginbutton;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    String fbid;
    ImageView loginicon;
    ScrollView scrollView;
     String username = "", userimage = "", useremail = "", password = "";
public static TextView login;
HashMap<Integer,String> hashMap;
ArrayList<CutomBean> sortlist;
    String first_name,second_name,third_name,fourth_name,fivth_name;
    ArrayList<String> tylist;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
    }
    boolean flagg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!flagg) {
            flagg = true;
            bannerlist=new ArrayList<>();
            first_list = new ArrayList<>();
            second_list=new ArrayList<>();
            third_list = new ArrayList<>();
            fourth_list = new ArrayList<>();
            fivth_list = new ArrayList<>();
            live_list = new ArrayList<>();
            banner_url = new ArrayList<>();
            firsturl = new ArrayList<>();
            third_url = new ArrayList<>();
            fourth_url = new ArrayList<>();
            fivth_url = new ArrayList<>();
            secondurl=new ArrayList<>();
            live_url = new ArrayList<>();
            listlive=new ArrayList<>();
            hashMap=new HashMap<>();
            sortlist=new ArrayList<>();
            tylist=new ArrayList<>();
            progressDialog = new ACProgressFlower.Builder(getActivity())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Loading...")
                    .build();
            progressDialog.setCancelable(false);
        }
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        act = (MainActivity) getActivity();

        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);

        if(getResources().getBoolean(R.bool.istab)) {
            dtype="t";
            Log.e("tablet","yes");
        } else {
            dtype="m";
            Log.e("phone","yes");
        }
scrollView= (ScrollView) rootView.findViewById(R.id.scrol);
        second_container = (RecyclerView) rootView.findViewById(R.id.second_recycler);
        first_container = (RecyclerView) rootView.findViewById(R.id.first_recyler);
        third_container = (RecyclerView) rootView.findViewById(R.id.third_recycler);
        fourth_container = (RecyclerView) rootView.findViewById(R.id.fourth_recycler);
        fivth_container = (RecyclerView) rootView.findViewById(R.id.fivth_recycler);
        live_container = (RecyclerView) rootView.findViewById(R.id.live_recycler);
        viewPager = (AutoScrollViewPager) rootView.findViewById(R.id.banercontent);
        first_all = (TextView) rootView.findViewById(R.id.first_all);
        third_all = (TextView) rootView.findViewById(R.id.third_all);
        fourth_all= (TextView) rootView.findViewById(R.id.fourth_all);
        fivth_all= (TextView) rootView.findViewById(R.id.fivth_all);
        second_all = (TextView) rootView.findViewById(R.id.second_all);
        facebookloginbutton = (ImageView) rootView.findViewById(R.id.facebooklogin);
        login= (TextView) rootView.findViewById(R.id.login);
        loginicon= (ImageView) rootView.findViewById(R.id.loginimg);
        first_container.setHasFixedSize(false);
        first_container.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false));
        second_container.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        third_container.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fourth_container.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fivth_container.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        live_container.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        trendingAdap = new TrendingAdaptor(getActivity(), first_list, firsturl);
        newVideos_adap = new MainContentAdoptor1((MainActivity) getActivity(), third_list, third_url);
        movies_adap = new MainContentAdoptor2((MainActivity) getActivity(), fourth_list, fourth_url);
        recomended_adap = new MainContentAdoptor3(getActivity(), fivth_list, fivth_url);
        trailer_adap = new Trailer_Adaptor((MainActivity) getActivity(), second_list, secondurl);
        switchImage = new LoadImageToView(bannerlist, banner_url);
        liveTvAdaptor=new AdapterLive(getActivity(),listlive);
        viewPager.setAdapter(switchImage);
        first_container.setAdapter(trendingAdap);
        third_container.setAdapter(newVideos_adap);
        fourth_container.setAdapter(movies_adap);
        fivth_container.setAdapter(recomended_adap);
        live_container.setAdapter(liveTvAdaptor);
        second_container.setAdapter(trailer_adap);
        first_items = (TextView) rootView.findViewById(R.id.firstitem);
        sec_items = (TextView) rootView.findViewById(R.id.seconditem);
        third_items = (TextView) rootView.findViewById(R.id.third_item);
        fourth_item = (TextView) rootView.findViewById(R.id.fourth_item);
        fivth_items = (TextView) rootView.findViewById(R.id.fivthitem);
        first_items.setText("");
        sec_items.setText("");
        third_items.setText("");
        fourth_item.setText("");
        fivth_items.setText("");
        if (BackgroundProcess.shared.getString("check","0").equals("0"))
            login.setText("Login");
        else
        login.setText("Logged in");
        third_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleVideo?skipdata=", "song", "all", "dd", "", 1);
              //  act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleVideo?skipdata=", "song", "vd", "", "featured", 1);
dialogshow(new ViewAll_Tab(),"http://iiscandy.com/panj/MultipleVideo?skipdata=", "song", "", "featured", 1," New Song");
            }
        });
        second_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleTrailers?skipdata=", "trailers", "all", "ll", "", 1);
               /// act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleTrailers?skipdata=", "trailers", "", "Videos", "featured", 1);
                dialogshow(new ViewAll_Tab(), "http://iiscandy.com/panj/MultipleTrailers?skipdata=", "trailers", "Videos", "featured", 1,"Trailers ");

            }
        });
        first_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "mv", "Videos", "featured", 1);
                dialogshow(new ViewAll_Tab(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "Videos", "featured", 1," Featured");

                       }
        });
        fourth_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "mv", "Videos", "featured", 1);
                dialogshow(new ViewAll_Tab(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "Videos", "featured", 1," Trending");

                // act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "all", "dd", "", 1);
            }
        });
        fivth_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "mv", "Videos", "featured", 1);
                dialogshow(new ViewAll_Tab(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "Videos", "featured", 1," Recommended");

                // act.replaceFragment(new VideoAll(), "http://iiscandy.com/panj/MultipleMovies?skipdata=", "movie", "all", "dd", "", 1);
            }
        });
       CirclePageIndicator circlePageIndicator= (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        circlePageIndicator.setRadius(4 * density);
        viewPager.setCurrentItem(0);
        viewPager.startAutoScroll();
        viewPager.setInterval(4000);
        viewPager.setScrollDurationFactor(2);
        if (flagg) {
            jsonRequest();
           jsonRequestBanner();
           // jsonRequestFeatured();
            //jsonRequestTrailer();
          //  jsonRequestTrending();
           // jsonRequestVideo();
           // recommendRequest();
            livetvdata();
        }
        //Create callback manager to handle login response
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                if (loginResult.getAccessToken() != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    Log.e("jsss","true");
                                    JSONObject jsonObject = response.getJSONObject();
                                  fbid= "";
                                    Profile p = Profile.getCurrentProfile();
                                    try {
                                        username = jsonObject.getString("name").replace(" ","%20");
                                        fbid = jsonObject.getString("id") + "@facebook.com";
                                        userimage = "" + (p.getProfilePictureUri(200, 200));
                                        useremail = jsonObject.getString("email");
                                        Log.e("Userinfo", "" + userimage + "," + username + "," + "" + useremail+" "+fbid);
                                         saveToSharedPrefrance();
                                    } catch (Exception e) {
                                        Log.e("valuer", e + "");
                                    } finally {
                                        if (useremail.length() == 0) {
                                            useremail = fbid;
                                            //uploadData();
                                        } else {
                                        }
                                        //  uploadData();

                                    }
                                }
                            }
                    );
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();

                }
            }

            @Override
            public void onCancel() {
                // App code
                Log.i("cance", "LoginManager FacebookCallback onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("eroro", "LoginManager FacebookCallback onError");
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                login.setText("Log out");
                if (loginResult.getAccessToken() != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    JSONObject jsonObject = response.getJSONObject();
                                    String id = "";
                                    Profile p = Profile.getCurrentProfile();
                                    try {
                                        username = jsonObject.getString("name");
                                        id = jsonObject.getString("id") + "@facebook.com";
                                        userimage = "" + (p.getProfilePictureUri(200, 200));
                                        useremail = jsonObject.getString("email");
                                        Log.e("Userinfo",""+userimage+","+username+","+""+useremail);
                                        saveToSharedPrefrance();
                                    } catch (Exception e) {
                                        Log.e("value", e + "");
                                    } finally {
                                        if (useremail.length() == 0) {
                                            useremail = id;
                                           // uploadData();
                                        } else{}
                                        //uploadData();

                                    }
                                }
                            }
                    );
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                    //updateUI();
                }

            }


            @Override
            public void onCancel() {
                Log.i("cance", "LoginManager FacebookCallback onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("eroro", "LoginManager FacebookCallback onError");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (login.getText().equals("Log out") && AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
                    LoginManager.getInstance().logOut();
                    login.setText("Facebook Login");
                } else
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email"));*/
             //   act.Login();
                if (BackgroundProcess.shared.getString("check", "0").equals("0"))
                {
                    Intent intent=new Intent(getActivity(), UserLogin.class);
                    startActivity(intent);
                }
                else
                {
                    if (login.getText().toString().equals("Loged in")){
//                        login.setText("Log in");
//                        BackgroundProcess.shared.edit().clear().commit();
                        Toast.makeText(getActivity(),"Already loged in",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return rootView;
    }



    public void dialogshow(DialogFragment newFragment, String url, String song, String vd, String tag, int i,String name) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("cat", song);
        bundle.putString("info", vd);
        bundle.putString("tag", tag);
        bundle.putString("id", song);
        bundle.putString("name", name);
        bundle.putInt("pos", i);
        newFragment.setArguments(bundle);
        newFragment.show(ft, "dialog");
    }

    private void livetvdata() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/LiveStreamChannels", new Response.Listener<String>() {
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void setLiveTv(JSONObject jsonObject) {
        try {
            if (jsonObject!=null)
            {
                JSONArray jsonArray=jsonObject.getJSONArray("LiveStream");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    LiveTvBean liveTvBean=new LiveTvBean();
                    liveTvBean.setTvcount(jsonObject1.getString("count"));
                    liveTvBean.setTvimage(jsonObject1.getString("Image"));
                    liveTvBean.setTvname(jsonObject1.getString("Name"));
                    listlive.add(liveTvBean);
                }
                liveTvAdaptor.notifyDataSetChanged();
            }        }
        catch (Exception e){
Log.e("liveExcep",""+e.toString());
        }
    }


    private void saveToSharedPrefrance() {
        sharedPreferences=BackgroundProcess.shared;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("useremail",useremail);
        editor.putString("userImage",userimage);
        editor.putString("fbid",fbid);
        editor.commit();
        sentToServer();
    }

    private void sentToServer() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/facebookLogin?fbid="+fbid+"&username="+username+"&imageurl="+userimage+"&email="+useremail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        check(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void check(JSONObject jsonObject) {
        try {
            Log.e("fbtrue",""+jsonObject.getString("fbstatus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(),"send",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void recommendRequest() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/RecommendedVideos?msisdn=0&stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setRecommendValue(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void setRecommendValue(JSONObject jsonObject) {
        try {
            JSONArray trending = jsonObject.getJSONArray("Recommended");
            for (int j = 0; j < trending.length(); j++) {
                JSONObject jsonObject2 = trending.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                fivth_url.add(itemBean);
                ItemBean items = new ItemBean();
                items.albumname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.albumcover = jsonObject2.getString("medium");
                } else
                    items.albumcover = jsonObject2.getString("medium");
                items.albumdesp = jsonObject2.getString("Description");
                items.id = jsonObject2.getString("id");
                fivth_list.add(items);
                hashMap.put(jsonObject2.getInt("position"),jsonObject2.getString("Name"));
            }
            checkposition(hashMap);
            recomended_adap.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("er", "" + e.toString());

        }
    }

    private void checkposition(HashMap<Integer, String> hashMap) {
        Log.e("hash","call"+hashMap.size());
        Iterator myVeryOwnIterator = hashMap.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            int key= (int) myVeryOwnIterator.next();
            String value=hashMap.get(key);
          Log.e("hashvalue", "Key: "+key+" Value: "+value);
        }
    }

    private void jsonRequestBanner() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeBannerList?stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueBanner(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setJsonValueBanner(JSONObject jsonObject) {
        try {
            bannerlist.clear();
            banner_url.clear();
            progressDialog.dismiss();
            JSONArray banner = jsonObject.getJSONArray("data");
            for (int i = 0; i < banner.length(); i++) {
                JSONObject bannerdata = banner.getJSONObject(i);
                JSONObject js = bannerdata.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                banner_url.add(itemBean);
                ItemBean items = new ItemBean();
                items.albumname = bannerdata.getString("bannerurl");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.newcover = bannerdata.getString("bannerurl");
                } else
                    items.newcover = bannerdata.getString("bannerurl");
                items.id = bannerdata.getString("id");
                bannerlist.add(items);
            }
            switchImage.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
        } catch (Exception e) {


Log.e("bannererror",e .toString());
        }
    }
    private void jsonRequestTrailer() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeTrailerList?stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueTrailer(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setJsonValueTrailer(JSONObject jsonObject) {
        try {

            JSONArray trailors = jsonObject.getJSONArray("data");
            for (int j = 0; j < trailors.length(); j++) {
                JSONObject jsonObject2 = trailors.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                secondurl.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                itemBean.treddesp=jsonObject2.getString("Description");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.id = jsonObject2.getString("id");
                second_list.add(items);
                hashMap.put(jsonObject2.getInt("position"),jsonObject2.getString("Name"));
            }
            trailer_adap.notifyDataSetChanged();

        } catch (Exception e) {

        }
    }

    private void jsonRequestVideo() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeNewVideo?stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueVideo(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setJsonValueVideo(JSONObject jsonObject) {
        try {

            JSONArray videos = jsonObject.getJSONArray("data");
            for (int j = 0; j < videos.length(); j++) {
                JSONObject jsonObject2 = videos.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                third_url.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.treddesp = jsonObject2.getString("Artist");
                items.id = jsonObject2.getString("id");
                third_list.add(items);
                hashMap.put(jsonObject2.getInt("position"),jsonObject2.getString("Name"));
            }
            newVideos_adap.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    private void jsonRequestFeatured() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeFeaturedList?stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueFeatured(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setJsonValueFeatured(JSONObject jsonObject) {
        try {
            JSONArray Featured = jsonObject.getJSONArray("data");
            for (int j = 0; j < Featured.length(); j++) {
                JSONObject jsonObject2 = Featured.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                fourth_url.add(itemBean);
                ItemBean items = new ItemBean();
                items.albumname = jsonObject2.getString("Albumname");
                items.albumdesp = jsonObject2.getString("Description");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.albumcover = jsonObject2.getString("medium");
                } else
                    items.albumcover = jsonObject2.getString("medium");
                items.id = jsonObject2.getString("id");
                fourth_list.add(items);
                hashMap.put(jsonObject2.getInt("position"),jsonObject2.getString("Name"));
            }
            movies_adap.notifyDataSetChanged();
        } catch (Exception e) {

        }
}
    private void jsonRequestTrending() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/HomeTranding?stype="+dtype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValueTrending(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setJsonValueTrending(JSONObject jsonObject) {
        try {
            JSONArray trending = jsonObject.getJSONArray("data");
            for (int j = 0; j < trending.length(); j++) {
                JSONObject jsonObject2 = trending.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                firsturl.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.treddesp = jsonObject2.getString("Description");
                items.id = jsonObject2.getString("id");
                first_list.add(items);
                hashMap.put(jsonObject2.getInt("position"),jsonObject2.getString("Name"));
            }
            trendingAdap.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    class LoadImageToView extends PagerAdapter {
        ArrayList<ItemBean> list;
        ArrayList<ItemBean> bannrurl;

        public LoadImageToView(ArrayList<ItemBean> itemlist, ArrayList<ItemBean> url) {
            list = itemlist;
            bannrurl = url;


        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = getActivity().getLayoutInflater().inflate(R.layout.pagerlargeimage, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);
            ImageLoader.getInstance().displayImage(list.get(position).newcover, (ImageView) imageView);
            imageLayout.setTag(position);
            view.addView(imageLayout, 0);
            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("id",""+bannerlist.get(position).id);
                   /* MainActivity.allurl = bannrurl;
                    act.replaceFragment(new New_Video_home(), bannrurl.get(position).BT200, list.get(position).newcover, "videos", list.get(position).albumname, list.get(position).id, position);
                  */  Controls.pauseControl(getActivity());
                    MainActivity.allurl = banner_url;
                    Intent it = new Intent(act, SubtitlePlayer.class);
                    it.putExtra("url", banner_url.get(position).m3u8);
                    it.putExtra("pos", position);
                    it.putExtra("id",  bannerlist.get(position).id);
                    startActivity(it);
               }
            });
            return imageLayout;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }
    private void jsonRequest() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/lpjson", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setJsonValue(new JSONObject(response));
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private void setJsonValue(JSONObject jsonObject) {
        try {
            JSONObject mainjson = jsonObject.getJSONObject("data");
            try {
                JSONArray first = mainjson.getJSONArray("First");
                for (int j = 0; j < first.length(); j++) {
                    JSONObject jsonObject2 = first.getJSONObject(j);
                    JSONObject js = jsonObject2.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    firsturl.add(itemBean);
                    ItemBean items = new ItemBean();
                    items.tredname = jsonObject2.getString("title");
                        items.tredcover = jsonObject2.getString("medium");
                    items.treddesp = jsonObject2.getString("Description");
                    items.id = jsonObject2.getString("id");
                    first_name=jsonObject2.getString("Name");
                    first_list.add(items);
                }
                trendingAdap = new TrendingAdaptor(getActivity(), first_list, firsturl);
                first_container.setAdapter(trendingAdap);
                trailer_adap.notifyDataSetChanged();
                first_items.setText(""+first_name);
                tylist.add(first_name);
                Log.e("first",""+first_list.size());
            } catch (Exception e) {
Log.e("first",""+e.toString());
            }
            try {

                JSONArray second = mainjson.getJSONArray("Second");
                for (int j = 0; j < second.length(); j++) {
                    JSONObject jsonObject2 = second.getJSONObject(j);
                    JSONObject js = jsonObject2.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    secondurl.add(itemBean);
                    ItemBean items = new ItemBean();
                    items.tredname = jsonObject2.getString("title");
                        items.tredcover = jsonObject2.getString("medium");
                    items.id = jsonObject2.getString("id");
                    second_name=jsonObject2.getString("Name");
                    second_list.add(items);
                }
                sec_items.setText(""+second_name);
                trendingAdap=new TrendingAdaptor(getActivity(),second_list,secondurl);
                second_container.setAdapter(trendingAdap);
                trendingAdap.notifyDataSetChanged();
                tylist.add(second_name);

            } catch (Exception e) {
                Log.e("sec",""+e.toString());
            }
            try {
                JSONArray third = mainjson.getJSONArray("Third");
                for (int j = 0; j < third.length(); j++) {

                    JSONObject jsonObject2 = third.getJSONObject(j);
                    JSONObject js = jsonObject2.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    third_url.add(itemBean);
                    ItemBean items = new ItemBean();
                    items.tredname = jsonObject2.getString("title");
                        items.tredcover = jsonObject2.getString("medium");
                    items.treddesp = jsonObject2.getString("Artist");
                    items.id = jsonObject2.getString("id");
                    third_name=jsonObject2.getString("Name");
                    third_list.add(items);
                }
                Log.e("thirdlist",""+third_list.size()+"  "+third_list);
                newVideos_adap=new MainContentAdoptor1((MainActivity) getActivity(),third_list,third_url);
                third_container.setAdapter(newVideos_adap);
                newVideos_adap.notifyDataSetChanged();
                third_items.setText(""+third_name);
                tylist.add(third_name);

            } catch (Exception e) {
                Log.e("third",""+e.toString());
            }
            try {
                JSONArray fourth = mainjson.getJSONArray("Fourth");
                for (int j = 0; j < fourth.length(); j++) {
                    JSONObject jsonObject2 = fourth.getJSONObject(j);
                    JSONObject js = jsonObject2.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    fourth_url.add(itemBean);
                    ItemBean items = new ItemBean();
                    items.tredname = jsonObject2.getString("title");
                        items.tredcover = jsonObject2.getString("medium");
                    items.id = jsonObject2.getString("id");
                    fourth_name=jsonObject2.getString("Name");
                    fourth_list.add(items);
                }
                movies_adap=new MainContentAdoptor2((MainActivity) getActivity(),fourth_list,fourth_url);
                fourth_container.setAdapter(movies_adap);
                movies_adap.notifyDataSetChanged();
                fourth_item.setText(""+fourth_name);
                tylist.add(fourth_name);
            } catch (Exception e) {
                Log.e("fourth",""+e.toString());
            }
            try {

                JSONArray fivth = mainjson.getJSONArray("Fifth");
                for (int j = 0; j < fivth.length(); j++) {
                    JSONObject jsonObject2 = fivth.getJSONObject(j);
                    JSONObject js = jsonObject2.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    fivth_url.add(itemBean);
                    ItemBean items = new ItemBean();
                    items.albumname = jsonObject2.getString("title");
                        items.albumcover = jsonObject2.getString("medium");
                    items.id = jsonObject2.getString("id");
                    fivth_name=jsonObject2.getString("Name");
                    fivth_list.add(items);
                }
                recomended_adap=new MainContentAdoptor3(getActivity(),fivth_list,fivth_url);
                fivth_container.setAdapter(recomended_adap);
                recomended_adap.notifyDataSetChanged();
                fivth_items.setText(""+fivth_name);
                tylist.add(fivth_name);
            } catch (Exception e) {
                Log.e("fivth",""+e.toString());
            }
        } catch (Exception e) {
            Log.e("main",""+e.toString());
        }

    }

}
