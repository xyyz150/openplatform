package com.github.opf.utils;

import com.alibaba.fastjson.JSON;

/**
 * Author: xyyz150
 * Date: 2020/6/20 15:38
 * Description:
 */
public class JsonUtils {

    public static <T> T parse(String json,Class<T> type){
        return JSON.parseObject(json,type);
    }

    public static String toJsonString(Object object){
        return JSON.toJSONString(object);
    }
}
