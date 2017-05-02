package com.init.panj.radioplayer;

import java.io.Serializable;

/**
 * Created by deepak on 10/2/2015.
 */
public class RadioBean implements Serializable {

       String radioname,radiourl,radioimage,radioid;
byte[] byteradioimg;


    public byte[] getByteradioimg() {
        return byteradioimg;
    }

    public void setByteradioimg(byte[] byteradioimg) {
        this.byteradioimg = byteradioimg;
    }

    public String getRadioname() {
        return radioname;
    }

    public void setRadioname(String radioname) {
        this.radioname = radioname;
    }

    public String getRadiourl() {
        return radiourl;
    }

    public void setRadiourl(String radiourl) {
        this.radiourl = radiourl;
    }

    public String getRadioimage() {
        return radioimage;
    }

    public void setRadioimage(String radioimage) {
        this.radioimage = radioimage;
    }

    public String getRadioid() {
        return radioid;
    }

    public void setRadioid(String radioid) {
        this.radioid = radioid;
    }
}
