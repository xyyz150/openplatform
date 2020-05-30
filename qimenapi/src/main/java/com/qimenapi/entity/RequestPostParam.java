package com.qimenapi.entity;

/**
 * Created by xyyz150 on 2016/9/13.
 */
public class RequestPostParam {
    private String method;
    private String format;
    private String app_key;
    private String app_serect;
    private String v;
    private String sessionkey;
    private String body;
    private String url;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_serect() {
        return app_serect;
    }

    public void setApp_serect(String app_serect) {
        this.app_serect = app_serect;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
