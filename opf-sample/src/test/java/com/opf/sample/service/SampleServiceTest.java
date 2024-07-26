package com.opf.sample.service;

import cn.hutool.http.HttpUtil;
import com.github.opf.Constants;
import com.github.opf.utils.OpfUtils;
import com.opf.sample.util.HttpHelper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Author: xyyz150
 * Date: 2020/6/21 18:11
 * Description:
 */
public class SampleServiceTest {
    @Test
    public void test() throws Exception {
        String appKey="appKey1",appSecret="123456",session="session1";
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "burgeon.r3.demo");
        map.put("timestamp", "2024-07-22 17:58:49");
        map.put("format", "json");
        map.put("appKey", appKey);
        map.put("v", "1.0");
        map.put("session", session);
        String body = "{\"code\":\"1111\",\"name\":\"aaaabbbbcccc\"}";
        String sign = OpfUtils.sign(map, body, appSecret);
        map.put("sign", sign);
        String url = "http://localhost:8080/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
        String str = HttpHelper.doPostJson(url, body);
        System.out.print(str);
    }

}