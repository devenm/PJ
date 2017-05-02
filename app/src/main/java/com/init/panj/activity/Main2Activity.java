package com.init.panj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.init.panj.R;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    @Bind(R.id.input_name) EditText username;
    @Bind(R.id.input_password) EditText userpassword;
    @Bind(R.id.login)
    TextView _signupButton;
    @Bind(R.id.signup)
            TextView signup;
    @Bind(R.id.forgetpassword)
    TextView forget;
    String name,password;
    StringRequest stringRequest;
    SharedPreferences sharedPreferences;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String userimage,useremail,fbid;
    ACProgressFlower progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuserlogin);
        ButterKnife.bind(this);
        progressDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .build();
        progressDialog.setCancelable(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Main2Activity.this, UserRegistration.class);
        startActivity(intent);
    }
});
            LoginManager.getInstance().logOut();
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
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
                                    JSONObject jsonObject = response.getJSONObject();

                                    String id = "";
                                    Profile p = Profile.getCurrentProfile();
                                    Log.e("jobj",""+jsonObject.toString()+"  "+p);
                                    try {
                                        name = jsonObject.getString("name");
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
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void saveToSharedPrefrance() {
        sharedPreferences= ImageLoaderInit.shared;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",name);
        editor.putString("useremail",useremail);
        editor.putString("userImage",userimage);
        editor.putString("fbid",fbid);
        editor.putString("mobile",fbid);
        editor.putString("check","1");
        editor.commit();
        sentToServer();
    }
    private void sentToServer() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/facebookLogin?fbid="+fbid+"&username="+name.replace(" ","")+"&imageurl="+userimage+"&email="+useremail, new Response.Listener<String>() {
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
        progressDialog.dismiss();
        try {
            Log.e("fbtrue",""+jsonObject.getString("fbstatus"));
            Intent it=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(it);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(Main2Activity.this,"send",Toast.LENGTH_LONG).show();
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
    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }
userLogin();
        // TODO: Implement your own signup logic here.

    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        name = username.getText().toString();
        password = userpassword.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            username.setError("Enter Username / Mobile Number ");
            username.setSelected(true);
username.setFocusableInTouchMode(true);
            valid = false;
        } else {
            username.setError(null);
        }

        if (name.isEmpty()) {
            username.setError("Invalid UserName");
            valid = false;
        } else {
            username.setError(null);
        }


        if (password.isEmpty()) {
            userpassword.setError("Invalid Password");
            valid = false;
        } else {
            userpassword.setError(null);
        }

        return valid;
    }
    private void userLogin() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.GET, "http://iiscandy.com/panj/Login?msisdn="+name+"&pass="+password, new Response.Listener<String>() {
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
            progressDialog.dismiss();
            String st=jsonObject.getString("Result");
            if (st.equals("1")) {
                saveLogin();
                Toast.makeText(Main2Activity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                Intent it=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(it);
            }
            else
                Toast.makeText(Main2Activity.this, "incorrect mobile number and password", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
    }
    private void saveLogin() {
        sharedPreferences= ImageLoaderInit.shared;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("mobile",name);
        editor.putString("password",password);
        editor.putString("check","2");
        editor.commit();
        HomeFragment.login.setText("Logged in");
        /*Intent intent=new Intent(UserLogin.this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent it=new Intent(Main2Activity.this,MainActivity.class);
        startActivity(it);
        finish();
    }
}

