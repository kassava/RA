package ru.android.shiz.ra.model;

import android.support.annotation.StringRes;

/**
 * Created by kassava on 06.09.16.
 */
public class InfoText implements Info {

    @StringRes
    private int titleRes;
    private String text;

    public InfoText(int titleRes, String text) {
        this.titleRes = titleRes;
        this.text = text;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
