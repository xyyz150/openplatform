package com.opf.sample.request;


import com.github.opf.request.OpfRequest;

/**
 * Created by xyyz150
 */
public class TestRequest implements OpfRequest {
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
