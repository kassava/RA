package ru.android.shiz.ra.model;

/**
 * Created by kassava on 06.09.16.
 */
public class InfoPicture implements Info {

    private String url;

    public InfoPicture(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
