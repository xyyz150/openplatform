package com.github.opf.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OpfUtilsTest {

    @Test
    public void testSign() throws Exception {
        Map<String, String> map=new HashMap<String, String>();
        map.put("method","burgeon.r3.order.get");
        map.put("appKey","12345678");
        map.put("session","test");
        map.put("timestamp","2016-01-01 12:00:00");
        map.put("format","json");
        map.put("v","1.0");
        String body="{\"startTime\":\"2016-01-01 12:00:00\",\"endTime\":\"2016-01-02 12:00:00\",\"shopTitle\":\"xxxx店铺\"}";
        String sign= OpfUtils.sign(map,body,"helloworld");
        System.out.print(sign);
    }
}