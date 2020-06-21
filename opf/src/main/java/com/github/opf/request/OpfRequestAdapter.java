package com.github.opf.request;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by xyyz150
 */
public class OpfRequestAdapter implements OpfRequest {

    @JSONField(serialize = false)
    private OpfRequestContext opfRequestContext;

    @JSONField(serialize = false)
    public OpfRequestContext getOpfRequestContext() {
        return opfRequestContext;
    }

    @JSONField(serialize = false)
    public void setOpfRequestContext(OpfRequestContext opfRequestContext) {
        this.opfRequestContext = opfRequestContext;
    }
}
