package com.justcode.xvs.bean;

/**
 * Created by 聂军 on 2017/8/7.
 */

public class Videolist{
    private String name;
    private String imgurl;
    private String videourl;

    public Videolist(String name, String img, String videourl) {
        this.name = name;
        this.imgurl = img;
        this.videourl = videourl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }
}
