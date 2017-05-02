package com.init.panj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.init.panj.clases.ShowErrorToast;
import com.init.panj.fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserLogin extends AppCompatActivity {
    StringRequest stringRequest;
    @Bind(R.id.mobilenumber)
    TextInputLayout imobile;
    @Bind(R.id.password)
    TextInputLayout ipassword;

TextView submit,login;
    EditText mobile,password;
    TextView newuser;
    String moblenumber,userpassword;
    String username = "", userimage = "", useremail = "", upassword = "",fbid="";
    SharedPreferences sharedPreferences=ImageLoaderInit.shared;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
        ButterKnife.bind(this);
        mobile = imobile.getEditText();
        password = ipassword.getEditText();
       imobile.setHint("Mobile Number");
        ipassword.setHint("Password");
        mobile.setInputType(InputType.TYPE_CLASS_PHONE);
        mobile.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});
        newuser= (TextView) findViewById(R.id.newuser);
        submit= (TextView) findViewById(R.id.submit);
        login= (TextView) findViewById(R.id.login);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moblenumber="+91"+mobile.getText().toString();
               userpassword=password.getText().toString();
             valid();
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, UserRegistration.class);
                startActivity(intent);
            }
        });
        if (ImageLoaderInit.shared.getString("fbid","0").equals("0"))
            login.setText("Login With Facebook");
        else
            login.setText("Log out");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
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
                                    Log.e("jobj",""+jsonObject.toString()+"  "+p);
                                    try {
                                        username = jsonObject.getString("name");
                                        id = jsonObject.getString("id") + "@facebook.com";
                                        userimage = "http://graph.facebook.com/"+jsonObject.getString("id")+"/picture?type=large";
                                                //(p.getProfilePictureUri(200, 200));
                                        try {
                                            useremail = jsonObject.getString("email");
                                        }
                                        catch(Exception e){}
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
    }


    private void valid() {
        if (moblenumber.isEmpty()||userpassword.isEmpty())
        {
            new ShowErrorToast(UserLogin.this,"Fill all Fields");
        }
        else
            userLogin();
    }

    private void userLogin() {
        stringRequest = new StringRequest(Request.Method.GET, "http://iiscandy.com/panj/Login?msisdn="+moblenumber+"&pass="+userpassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setValue(new JSONObject(response));
                        Log.e("uservalue", "" + response);
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
                stringStringHashMap.put("itemToFetch", 0 + "");
                stringStringHashMap.put("itemToFetcht", 0 + "");
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setValue(JSONObject jsonObject) {
        try {
            String st=jsonObject.getString("Result");
            if (st.equals("1")) {
                saveLogin();
                Toast.makeText(UserLogin.this, "Login Successfull", Toast.LENGTH_LONG).show();
                Intent it=new Intent(UserLogin.this,MainActivity.class);
                startActivity(it);
            }
            else
                Toast.makeText(UserLogin.this, "incorrect mobile number and password", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
    }

    private void saveLogin() {
        sharedPreferences= ImageLoaderInit.shared;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("mobile",moblenumber);
        editor.putString("password",userpassword);
        editor.putString("check","2");
        editor.commit();
        HomeFragment.login.setText("Logged in");
        /*Intent intent=new Intent(UserLogin.this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i)
            {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                {
                    return "";
                }
            }

            return null;
        }
    };
    private void saveToSharedPrefrance() {
        sharedPreferences= ImageLoaderInit.shared;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("useremail",useremail);
        editor.putString("userImage",userimage);
        editor.putString("fbid",fbid);
        editor.putString("mobile",fbid);
        editor.putString("check","1");
        editor.commit();
        sentToServer();
        HomeFragment.login.setText("Logged in");
        /*Intent intent=new Intent(UserLogin.this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }
    private void sentToServer() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/facebookLogin?fbid="+fbid+"&username="+username.replace(" ","")+"&imageurl="+userimage+"&email="+useremail, new Response.Listener<String>() {
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
            Log.e("fbtrue",""+jsonObject.getString("fbstatus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(UserLogin.this,"send",Toast.LENGTH_LONG).show();
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
    public boolean Login(){
        Log.e("user","called");
        if (login.getText().equals("Log out") && AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logOut();
            login.setText("Login");
            sharedPreferences.edit().clear().commit();
        } else
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        Profile p = Profile.getCurrentProfile();
        return true;
    }
}
