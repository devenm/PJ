package com.init.panj.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
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
import com.init.panj.R;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.clases.SearchDialog;
import com.init.panj.fragments.HomeFragment;
import com.init.panj.fragments.MainFragment;
import com.init.panj.fragments.Movies_mainPage;
import com.init.panj.fragments.Trailers_tab;
import com.init.panj.fragments.Video_tab;
import com.init.panj.model.ItemBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    public List<String> mFragmentTitleList = new ArrayList<>();
    public static Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    ProgressDialog progressDialog;
    FrameLayout frameLayout;

    Trailers_tab trailers_tab = new Trailers_tab();
    Video_tab video_tab = new Video_tab();
    Movies_mainPage movies_mainPage = new Movies_mainPage();
    HomeFragment homeFragment = new HomeFragment();
    ImageView panj;
    // TextView trailer, movie, video, livetv;
    public static ArrayList<ItemBean> allurl = new ArrayList<>();
    EditText searchbox;
    public static String searchboxvalue;
    SearchDialog searchDialog;
    CallbackManager callbackManager;
    String username = "", userimage = "", useremail = "", password = "", fbid = "";
    SharedPreferences sharedPreferences = ImageLoaderInit.shared;
    StringRequest stringRequest;
    SearchDialog newFragment;

    ImageView homebutton, search;
    public static String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("phonehw", width + "  " + height);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.init.panj",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("logerror",""+e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.e("logerror1",""+e.toString());
        }*/

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitleTextColor(Color.parseColor("#f44336"));
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // getSupportActionBar().setIcon(R.drawable.toolbaricon);
        getSupportActionBar().setTitle("");
        homebutton = (ImageView) findViewById(R.id.home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = drawerFragment.mDrawerLayout;
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.END);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        panj = (ImageView) findViewById(R.id.panj);
        search = (ImageView) findViewById(R.id.search);
       /* trailer = (TextView) findViewById(R.id.trailer);
        movie = (TextView) findViewById(R.id.movie);
        video = (TextView) findViewById(R.id.video);
        livetv = (TextView) findViewById(R.id.livetv);*/
        searchbox = (EditText) findViewById(R.id.searchsong);
      /*  viewPager = (CustomViewPager) findViewById(R.id.pager);
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);*/
        // searchDialog=new SearchDialog(MainActivity.this);
        searchbox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    searchboxvalue = searchbox.getText().toString();
                    // Log.e("keycall", "Enter pressed" + "" + searchbox.getText().toString());
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // searchDialog.init(searchboxvalue);
                    dialogshow(new SearchDialog(), searchboxvalue, "dialog");
                }
                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogshow(new SearchDialog(), "", "dialog");
            }
        });
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        if (savedInstanceState == null) {
            replaceFragment(new MainFragment(), "", "0", "home", "", "", 1);
            Log.e("inst", "sss");
        }
        // getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Tabview(), "0").addToBackStack(null).commit();
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
       /* getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                    finish();

            }
        });*/

        panj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, Test.class);
                startActivity(intent);*/
           /* replaceFragment(homeFragment,"","","home", "", "", 1);
            movie.setBackgroundColor(Color.parseColor("#00111111"));
            trailer.setBackgroundColor(Color.parseColor("#00111111"));
            video.setBackgroundColor(Color.parseColor("#00111111"));*/
            }
        });
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                HomeFragment.login.setText("Log out");
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
                                        Log.e("Userinfo", "" + userimage + "," + username + "," + "" + useremail);
                                        saveToSharedPrefrance();
                                    } catch (Exception e) {
                                        Log.e("value", e + "");
                                    } finally {
                                        if (useremail.length() == 0) {
                                            useremail = id;
                                            // uploadData();
                                        } else {
                                        }
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

    }


    public void dialogshow(DialogFragment newFragment, String searchval, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("searchval", searchval);
        newFragment.setArguments(bundle);
        newFragment.show(ft, "dialog");
    }


    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                //replaceFragment(new MainFragment(), "", "", "home", "", "", 1);
                MainFragment mainFragments= (MainFragment) getSupportFragmentManager().findFragmentByTag("home");
                if (mainFragments!=null)
                    mainFragments.openlive(position);
                //removeFromBackStact();
                title = "Home";
                break;
            case 1:
               MainFragment mainFragment= (MainFragment) getSupportFragmentManager().findFragmentByTag("home");
                if (mainFragment!=null)
                    mainFragment.openlive(4);
                // replaceFragment(new LiveTvFragment(), "", "", "radio", "", "", 1);
                break;
            case 2:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Panj App download from google play store https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(sharingIntent, "Share With these app"));
                break;
            case 3:
                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case 4:
                if (ImageLoaderInit.shared.getString("check", "0").equals("0")) {
                    ImageLoaderInit.shared.edit().clear().commit();
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                } else {
                    ImageLoaderInit.shared.edit().clear().commit();
                    Toast.makeText(MainActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
                break;
            case 5:
                if (ImageLoaderInit.shared.getString("check", "0").equals("0"))
                    Toast.makeText(MainActivity.this, "Please Log In First", Toast.LENGTH_LONG).show();
                else {
                    ImageLoaderInit.shared.edit().clear().commit();
                    HomeFragment.login.setText("Login");
                    Toast.makeText(MainActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return true; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return true;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return true; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public void replaceFragment(Fragment fragment, String url, String cat, String tag, String info, String id, int adapterPosition) {
        removeFromBackStact();
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        if (frag == null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("cat", cat);
            bundle.putString("info", info);
            bundle.putString("tag", tag);
            bundle.putString("id", id);
            bundle.putInt("pos", adapterPosition);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment, tag).addToBackStack(null).commit();
        }
    }

    private void removeFromBackStact() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
            if (!isFinishing())
                manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() == 1) {
            finish();
        } else
            getSupportFragmentManager().popBackStack();


    }

    public boolean Login() {
        Log.e("user", "called");
        if (HomeFragment.login.getText().equals("Log out") && AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logOut();
            HomeFragment.login.setText("Login");
            sharedPreferences.edit().clear().commit();
        } else
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        Profile p = Profile.getCurrentProfile();
        return true;
    }

    private void saveToSharedPrefrance() {
        sharedPreferences = ImageLoaderInit.shared;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("useremail", useremail);
        editor.putString("userImage", userimage);
        editor.putString("fbid", fbid);
        editor.commit();
        sentToServer();
    }

    private void sentToServer() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/facebookLogin?fbid=" + fbid + "&username=" + username + "&imageurl=" + userimage + "&email=" + useremail, new Response.Listener<String>() {
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
               /* stringStringHashMap.put("itemToFetch", itemlist.size() + "");
                stringStringHashMap.put("itemToFetcht", itemlist1.size() + "");*/
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void check(JSONObject jsonObject) {
        try {
            Log.e("fbtrue", "" + jsonObject.getString("fbstatus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "send", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

   /* private class PageAdap extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public PageAdap(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
}
