package com.opf.sample.service;

import com.github.opf.annotation.*;
import com.opf.sample.model.TestEntity;
import com.opf.sample.request.TestRequest;
import com.opf.sample.response.TestResponse;



@ServiceMethodBean(version = "2.0", needInSession = NeedInSessionType.NO, timeout = 60000, ignoreSign = IgnoreSignType.NO)
public class SampleService {
    @ServiceMethod(method = "opf.test", httpAction = HttpAction.POST)
    public Object test(TestRequest request) throws Throwable {
        TestResponse response = new TestResponse();
        response.setFlag("success");
        response.setCode("ok");
        response.setMessage("");
        response.setTotal(10);
        TestEntity testentity = new TestEntity();
        testentity.setTitle("title");
        response.setTestEntity(testentity);
        return response;
    }
}
