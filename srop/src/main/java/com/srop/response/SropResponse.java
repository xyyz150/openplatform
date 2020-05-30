package com.srop.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by xyyz150 on 2016/1/6.
 */
@XStreamAlias("error_response")
public class SropResponse {

    @XStreamAlias("flag")
    private String flag;

    @XStreamAlias("code")
    private String code;

    @XStreamAlias("message")
    private String message;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
