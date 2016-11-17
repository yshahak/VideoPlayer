package com.downtube.videos.vine;

/**
 * Created by B.E.L on 17/11/2016.
 */

public class VineVideo {

    private String vinePageUrl;
    private String vineThumbUrl;
    private String vineStreamUrl;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setVinePageUrl(String vinePageUrl) {
        this.vinePageUrl = vinePageUrl;
    }

    public void setVineThumbUrl(String vineThumbUrl) {
        this.vineThumbUrl = vineThumbUrl;
    }

    public void setVineStreamUrl(String vineStreamUrl) {
        this.vineStreamUrl = vineStreamUrl;
    }

    public String getId() {
        return id;
    }

    public String getVinePageUrl() {
        return vinePageUrl;
    }

    public String getVineThumbUrl() {
        return vineThumbUrl;
    }

    public String getVineStreamUrl() {
        return vineStreamUrl;
    }
}
