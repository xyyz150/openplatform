package com.opf.sample.service;

import com.github.opf.annotation.*;
import com.opf.sample.model.TestEntity;
import com.opf.sample.request.TestRequest;
import com.opf.sample.response.TestResponse;



@ServiceMethodBean(version = "1.0")
public class SampleService {
    @ServiceMethod(method = "opf.test")
    public TestResponse test(TestRequest request) throws Throwable {
        TestResponse response = new TestResponse();
        response.setCode("0");
        response.setMessage("success");
        response.setTotal(10);
        TestEntity testentity = new TestEntity();
        testentity.setTitle("title");
        response.setTestEntity(testentity);
        return response;
    }
}
