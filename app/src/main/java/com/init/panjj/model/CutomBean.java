package com.init.panjj.model;

import java.util.ArrayList;

/**
 * Created by INIT on 2/18/2017.
 */

public class CutomBean {
    String ActualName;
    ArrayList<ItemBean> arrayList;
    String name;
    String type;

    public String getActualName() {
        return this.ActualName;
    }

    public void setActualName(String actualName) {
        this.ActualName = actualName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ItemBean> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<ItemBean> arrayList) {
        this.arrayList = arrayList;
    }
}
