package com.ufxmeng.je.funradio.bean;

import java.util.Date;

/**
 * Created by JE on 6/5/2016.
 */
public class PodcastDetail {

    int imageResID;
    String podcastMp3Url;
    String title;
    Date pubDate;
    String duration;

    public int getImageResID() {
        return imageResID;
    }

    public void setImageResID(int imageResID) {
        this.imageResID = imageResID;
    }

    public String getPodcastMp3Url() {
        return podcastMp3Url;
    }

    public void setPodcastMp3Url(String podcastMp3Url) {
        this.podcastMp3Url = podcastMp3Url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "PodcastDetail{" +
                "imageResID=" + imageResID +
                ", podcastMp3Url='" + podcastMp3Url + '\'' +
                ", title='" + title + '\'' +
                ", pubDate=" + pubDate +
                ", duration='" + duration + '\'' +
                '}';
    }
}
