package com.ufxmeng.je.funradio.bean;

/**
 * Created by JE on 6/11/2016.
 */
public class WidgetItem {
    int resID;
    String podcastUrl;

    public WidgetItem(int resID, String podcastUrl) {
        this.resID = resID;
        this.podcastUrl = podcastUrl;
    }

    public int getResID() {
        return resID;
    }

    public String getPodcastUrl() {
        return podcastUrl;
    }
}
