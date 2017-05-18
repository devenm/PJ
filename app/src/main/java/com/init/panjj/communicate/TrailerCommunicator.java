package com.init.panjj.communicate;

import android.util.Log;

import com.init.panjj.model.ItemBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class TrailerCommunicator {
    Communicator communicator;

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public void processdata(JSONObject jsonObject) {
        ArrayList<ItemBean> latestllist = new ArrayList();
        ArrayList<ItemBean> latesturl = new ArrayList();
        try {
            JSONArray latest = jsonObject.getJSONObject("Trailers").getJSONArray("Featured");
            for (int i = 0; i < latest.length(); i++) {
                JSONObject jsonObject2 = latest.getJSONObject(i);
                JSONObject jsonObj = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = jsonObj.getString("BT200");
                itemBean.BT385 = jsonObj.getString("BT385");
                itemBean.BT500 = jsonObj.getString("BT500");
                itemBean.BT600 = jsonObj.getString("BT600");
                itemBean.BT750 = jsonObj.getString("BT750");
                itemBean.BT1000 = jsonObj.getString("BT1000");
                itemBean.BT1500 = jsonObj.getString("BT1500");
                latesturl.add(itemBean);
                ItemBean item = new ItemBean();
                item.tredname = jsonObject2.getString("Albumname");
                item.tredcover = jsonObject2.getString("medium");
                item.treddesp = jsonObject2.getString("Description");
                item.id = jsonObject2.getString("id");
                latestllist.add(item);
            }
        } catch (JSONException e) {
            Log.e("movieerror", BuildConfig.VERSION_NAME + e.toString());
            e.printStackTrace();
        }
        this.communicator.data(latestllist, latesturl);
    }
}
