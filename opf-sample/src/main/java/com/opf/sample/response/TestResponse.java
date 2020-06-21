package com.opf.sample.response;

import com.github.opf.response.OpfResponse;
import com.opf.sample.model.TestEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by xyyz150
 */
@XStreamAlias("response")
public class TestResponse extends OpfResponse {
    @XStreamAlias("total")
    private int total;

    @XStreamAlias("testEntity")
    private TestEntity testEntity;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public TestEntity getTestEntity() {
        return testEntity;
    }

    public void setTestEntity(TestEntity testEntity) {
        this.testEntity = testEntity;
    }
}
