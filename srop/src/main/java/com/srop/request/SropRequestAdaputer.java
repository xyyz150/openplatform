package com.srop.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Created by xyyz150 on 2016/1/7.
 */
public class SropRequestAdaputer implements SropRequest {

    @JsonIgnore
    private SropRequestContext sropRequestContext;

    @JsonIgnore
    public SropRequestContext getSropRequestContext() {
        return sropRequestContext;
    }

    @JsonIgnore
    public void setSropRequestContext(SropRequestContext sropRequestContext) {
        this.sropRequestContext = sropRequestContext;
    }
}
