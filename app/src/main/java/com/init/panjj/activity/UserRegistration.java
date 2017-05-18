package com.init.panjj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panjj.R;
import com.init.panjj.otherclasses.BackgroundProcess;
import com.init.panjj.otherclasses.ShowErrorToast;
import com.init.panjj.fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by deepak on 4/7/2016.
 */
public class UserRegistration extends AppCompatActivity {
    StringRequest stringRequest;
    @Bind(R.id.firstname)
    EditText firstname;
    @Bind(R.id.phonenumber)
    EditText mobile;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
            EditText password;
    @Bind(R.id.repassword)
    EditText rpassword;
    @Bind(R.id.submit)
    TextView submit;
    private String ufirstname, uemail, uphone,upassword,urpassword;
    SharedPreferences sharedPreferences;
    ACProgressFlower progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
        ButterKnife.bind(this);
        progressDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .build();
        progressDialog.setCancelable(false);

        mobile.setInputType(InputType.TYPE_CLASS_PHONE);
        mobile.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(10)});
submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ufirstname=firstname.getText().toString();
        uemail=email.getText().toString();
        uphone="+91"+mobile.getText().toString();
        upassword=password.getText().toString();
urpassword=rpassword.getText().toString();
        Log.e("All valuse",""+ufirstname+uemail+uphone+upassword);
       valid();
    }
});
    }

    private void valid() {
        if (ufirstname.equals("")||uemail.equals("")||uphone.equals("")||upassword.equals("")||urpassword.equals(""))
        {
            new ShowErrorToast(UserRegistration.this,"Plaese Fill All Fields");
        }
        else if (emailcheck()&&phonecheck()&&passwordcheck())
        {
          userLogin();
        }

    }

    private boolean passwordcheck() {

        if (upassword.equals(urpassword)) {
            return true;
        }
        else {
            password.setError("Password Mismatch");
            return false;
        }

    }

    private boolean emailcheck() {
        if (!uemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()){
        return true;
    }
            else {
            email.setError("Enter Valid Email");
            return false;
        }

    }

    private boolean phonecheck() {
        if (uphone.length()<10) {
            mobile.setError("Enter Valid Mobile No.");
            return false;
        }
        else
       return true;
    }

    private void userLogin() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.GET, "http://iiscandy.com/panj/CreateUser?fn="+ufirstname+"&msisdn="+uphone+"&pass="+upassword+"&email="+uemail, new Response.Listener<String>() {
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
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setValue(JSONObject jsonObject) {
        try {
            progressDialog.dismiss();
            String st=jsonObject.getString("Result");
            if (st.equalsIgnoreCase("User Created Succesfully")) {
                saveToSharedPrefrance();
                Toast.makeText(UserRegistration.this, "" + st, Toast.LENGTH_LONG).show();
                Intent it=new Intent(UserRegistration.this,MainActivity.class);
                startActivity(it);
                finish();
            }
            else
                Toast.makeText(UserRegistration.this, "" + st, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Log.e("data set error",e.toString());
            e.printStackTrace();
        }
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
        sharedPreferences= BackgroundProcess.shared;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",ufirstname);
        editor.putString("mobile",uphone);
        editor.putString("check","2");
        editor.commit();
        HomeFragment.login.setText("Logged in");
    }
}
