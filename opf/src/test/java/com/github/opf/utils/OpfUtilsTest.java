package com.github.opf.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OpfUtilsTest {

    @Test
    public void testSign() throws Exception {
        Map<String, String> map=new HashMap<String, String>();
        map.put("a","11");
        map.put("b","22");
        String sign= OpfUtils.sign(map,"{a:123}","123");
        System.out.print(sign);
    }
}