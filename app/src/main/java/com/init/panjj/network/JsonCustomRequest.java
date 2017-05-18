package com.init.panjj.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.util.Map;

public class JsonCustomRequest extends Request<JSONObject> {
    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public JsonCustomRequest(String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(0, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public JsonCustomRequest(int method, String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1)), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Throwable e) {
            return Response.error(new ParseError(e));
        }
    }

    protected void deliverResponse(JSONObject response) {
        this.listener.onResponse(response);
    }
}
