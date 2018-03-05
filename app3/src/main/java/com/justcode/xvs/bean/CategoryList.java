package com.justcode.xvs.bean;

/**
 * Created by niejun on 2017/10/14.
 */

public class CategoryList {

    /**
     * label : Anal
     * url : /c/Anal-12
     */

    private String label;
    private String url;

    public CategoryList(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
