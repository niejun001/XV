package com.justcode.xvs.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/2/9.
 */

public class VideoCollection  extends DataSupport {
    private String name;
    private String imgurl;
    private String videourl;
    private String time;

    public VideoCollection() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
