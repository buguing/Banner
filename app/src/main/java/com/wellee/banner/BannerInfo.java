package com.wellee.banner;

public class BannerInfo {
    private String url;
    private String title;

    public BannerInfo(String url) {
        this.url = url;
    }

    public BannerInfo(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
