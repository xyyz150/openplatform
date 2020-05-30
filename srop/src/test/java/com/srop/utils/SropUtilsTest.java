package com.srop.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SropUtilsTest {

    @Test
    public void testSign() throws Exception {
        Map<String, String> map=new HashMap<String, String>();
        map.put("a","11");
        map.put("b","22");
        String sign= SropUtils.sign(map,"{a:123}","123");
        System.out.print(sign);
    }
}