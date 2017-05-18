package com.init.panjj.network;

public final class ServerUrls {
    public final String moviesurl;
    public final String trailerurl;
    public final String urlbase;
    public final String videourl;

    public ServerUrls() {
        this.urlbase = "http://iiscandy.com/panj/";
        this.moviesurl = "http://iiscandy.com/panj/SectionPlayList?id=1&";
        this.videourl = "http://iiscandy.com/panj/MultipleVideo?skipdata=";
        this.trailerurl = "http://iiscandy.com/panj/MultipleTrailers?skipdata=";
    }
}
