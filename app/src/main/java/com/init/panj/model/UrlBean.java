package com.init.panj.model;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by deepak on 2/20/2016.
 */
public class UrlBean implements Serializable {
    private ArrayList<ItemBean> url;

    public UrlBean(ArrayList<ItemBean> data) {
        this.url = data;
    }

    public ArrayList<ItemBean> getUrl() {
        return this.url;
    }
}
