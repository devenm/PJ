package com.init.panjj.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deepak on 7/16/2016.
 */
public class PitemBean implements Parcelable {
    public String newname,newcover,newdesc;
    public String tredname,tredcover,treddesp;
    public String albumname,albumcover,albumdesp;
    public String disptitle;
    public  String BT200,BT385,BT500,BT600,BT750,BT1000,BT1500,m3u8;
    public String id;
    public  String burl;
    public String dlink;
    int img;
    String wallpaper;
    String mainimage;
    public String subtitle;
    public int displayicon;

    public PitemBean(Parcel in) {
        newname = in.readString();
        newcover = in.readString();
        newdesc = in.readString();
        tredname = in.readString();
        tredcover = in.readString();
        treddesp = in.readString();
        albumname = in.readString();
        albumcover = in.readString();
        albumdesp = in.readString();
        disptitle = in.readString();
        BT200 = in.readString();
        BT385 = in.readString();
        BT500 = in.readString();
        BT600 = in.readString();
        BT750 = in.readString();
        BT1000 = in.readString();
        BT1500 = in.readString();
        m3u8 = in.readString();
        id = in.readString();
        burl = in.readString();
        dlink = in.readString();
        img = in.readInt();
        wallpaper = in.readString();
        mainimage = in.readString();
        subtitle = in.readString();
        displayicon = in.readInt();
    }

    public static final Creator<PitemBean> CREATOR = new Creator<PitemBean>() {
        @Override
        public PitemBean createFromParcel(Parcel in) {
            return new PitemBean(in);
        }

        @Override
        public PitemBean[] newArray(int size) {
            return new PitemBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newname);
        dest.writeString(newcover);
        dest.writeString(newdesc);
        dest.writeString(tredname);
        dest.writeString(tredcover);
        dest.writeString(treddesp);
        dest.writeString(albumname);
        dest.writeString(albumcover);
        dest.writeString(albumdesp);
        dest.writeString(disptitle);
        dest.writeString(BT200);
        dest.writeString(BT385);
        dest.writeString(BT500);
        dest.writeString(BT600);
        dest.writeString(BT750);
        dest.writeString(BT1000);
        dest.writeString(BT1500);
        dest.writeString(m3u8);
        dest.writeString(id);
        dest.writeString(burl);
        dest.writeString(dlink);
        dest.writeInt(img);
        dest.writeString(wallpaper);
        dest.writeString(mainimage);
        dest.writeString(subtitle);
        dest.writeInt(displayicon);
    }
}
