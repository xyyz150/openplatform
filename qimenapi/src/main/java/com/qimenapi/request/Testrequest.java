package com.qimenapi.request;

import com.srop.request.SropRequestAdaputer;

/**
 * Created by xyyz150 on 2016/1/6.
 */
public class Testrequest extends SropRequestAdaputer {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
