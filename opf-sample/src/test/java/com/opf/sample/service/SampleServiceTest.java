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
        String appKey="appKey1",appSecret="123456",accessToken="accessToken1";
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", "opf.test");
        map.put("timestamp", "2020-05-30 11:08:49");
        map.put("format", "json");
        map.put("appKey", appKey);
        map.put("v", "2.0");
//        map.put("sign_method", "md5");
        map.put("accessToken", accessToken);
        String body = "{\"code\":\"1111\",\"name\":\"aaaabbbbcccc\"}";
        String sign = OpfUtils.sign(map, body, appSecret);
        map.put("sign", sign);
        String url = "http://localhost:8080/router?" + HttpHelper.buildQuery(map, Constants.UTF8);
        String str = HttpHelper.doPost(url, body);
        System.out.print(str);
    }

}