package com.init.panj.model;

import java.util.ArrayList;

/**
 * Created by INIT on 2/18/2017.
 */

public class CutomBean {
    String type;
    ArrayList<ItemBean> arrayList;
String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ItemBean> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ItemBean> arrayList) {
        this.arrayList = arrayList;
    }
}
