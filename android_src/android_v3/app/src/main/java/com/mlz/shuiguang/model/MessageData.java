package com.mlz.shuiguang.model;

import android.graphics.drawable.Drawable;

/**
 * Created by dehualai on 2/15/17.
 */

public class MessageData {
    String title;
    String brief;
    String html_url;
    int thumbnailResource;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public int getThumbnail() {
        return thumbnailResource;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnailResource = thumbnail;
    }
}
