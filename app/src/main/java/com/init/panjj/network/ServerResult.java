package com.init.panjj.network;

import org.json.JSONException;
import org.json.JSONObject;

public interface ServerResult {
    void sendErrorMessage(String str);

    void setDataFromServer(JSONObject jSONObject, int i) throws JSONException;
}
