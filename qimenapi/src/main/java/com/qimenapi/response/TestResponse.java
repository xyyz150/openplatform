package com.qimenapi.response;

import com.qimenapi.sropentity.Testentity;
import com.srop.response.SropResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by xyyz150 on 2016/1/6.
 */
@XStreamAlias("response")
public class TestResponse extends SropResponse {
    @XStreamAlias("totle")
    private int totle;

    @XStreamAlias("testentity")
    private Testentity testentity;

    public int getTotle() {
        return totle;
    }

    public void setTotle(int totle) {
        this.totle = totle;
    }

    public Testentity getTestentity() {
        return testentity;
    }

    public void setTestentity(Testentity testentity) {
        this.testentity = testentity;
    }
}
