package com.init.panj.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by deepak on 7/16/2016.
 */


public class DataWraper extends ArrayList<String> implements Serializable {

    private ArrayList<ItemBean> reclist;
private ArrayList<ItemBean>rurl;

    public DataWraper(ArrayList<ItemBean> data) {
        this.reclist = data;
    }

    public ArrayList<ItemBean> getReclist() {
        return this.reclist;
    }

}
