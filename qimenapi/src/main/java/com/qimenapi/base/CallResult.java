package com.qimenapi.base;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by xyyz150 on 2016/1/21.
 */
public class CallResult {
    public  CallResult()
    {
        this.result=true;
    }
    @JsonProperty("Result")
    boolean result;
    @JsonProperty("Message")
    String message;
    Object o;
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object geto() {
        return o;
    }

    public void seto(Object o) {
        this.o = o;
    }
}
