package com.init.panjj.communicate;

import android.util.Log;

import com.init.panjj.model.CutomBean;
import com.init.panjj.model.ItemBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class MoviesCommunicator {
    Communicator communicator;

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public void processdata(JSONObject jsonObject) {
        ArrayList<CutomBean> cutomBeanArrayList = new ArrayList();
        cutomBeanArrayList.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("Content");
            for (int i = 0; i < jsonArray.length(); i++) {
                ArrayList<ItemBean> playarraylist = new ArrayList();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                CutomBean cutomBean = new CutomBean();
                cutomBean.setActualName(jsonObject1.getString("SectionName"));
                JSONArray playlist = jsonObject1.getJSONArray("PlayList");
                for (int j = 0; j < playlist.length(); j++) {
                    JSONObject plist = playlist.getJSONObject(j);
                    ItemBean itemBean = new ItemBean();
                    JSONObject js = plist.getJSONObject("Urls");
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    itemBean.tredname = plist.getString("Albumname");
                    itemBean.tredcover = plist.getString("medium");
                    itemBean.id = plist.getString("id");
                    itemBean.genre = plist.getString("genre");
                    itemBean.year = plist.getString("published_at");
                    itemBean.language = plist.getString("LanguageName");
                    playarraylist.add(itemBean);
                }
                cutomBean.setArrayList(playarraylist);
                cutomBeanArrayList.add(cutomBean);
            }
        } catch (JSONException e) {
            Log.e("movieerror", BuildConfig.VERSION_NAME + e.toString());
            e.printStackTrace();
        }
        this.communicator.data(cutomBeanArrayList);
    }
}
