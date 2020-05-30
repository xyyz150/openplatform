package com.qimenapi.sropservice;


import com.qimenapi.request.Testrequest;
import com.qimenapi.response.TestResponse;
import com.qimenapi.sropentity.Testentity;
import com.srop.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyyz150 on 2015/6/24.
 * 奇门2.0版本接口
 */
@ServiceMethodBean(version = "2.0", needInSession = NeedInSessionType.NO, timeout = 60000, ignoreSign = IgnoreSignType.NO)
public class SropDeliveryService {

    @ServiceMethod(method = "qs.get", httpAction = HttpAction.POST)
    public Object getReturnOrder(Testrequest request) throws Throwable {
        Map<String, Object> map = new HashMap<String, Object>();
        TestResponse response = new TestResponse();
        response.setFlag("success");
        response.setCode("ok");
        response.setMessage("eee");
        response.setTotle(10);
        Testentity testentity = new Testentity();
        testentity.setTitle("title");
        response.setTestentity(testentity);
        return response;
    }

}
