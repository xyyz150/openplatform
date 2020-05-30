package com.srop.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xyyz150 on 2016/1/6.
 */
public class JsonUtils {
    protected static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper mapper;

    static {
        if(mapper==null) {
            mapper = ObjectMapperUtils.InitObjectMapper();
        }
    }

    public static String writeValueAsString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
            logger.error("json api序列化错误", e);
        }
        return null;
    }

    public static <T> T readObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("json api反序列化错误" + json, e);
        }
        return null;
    }

    public static <T> T readObject(InputStream inputStream, Class<T> clazz) {
        try {
            return mapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            logger.error("json api反序列化错误" + inputStream.toString(), e);
        }
        return null;
    }

    //判断对象是否值相同
    public static <T> Boolean equals(Object src, Object desc) {
        try
        {
            return writeValueAsString(src).equals(writeValueAsString(desc));
        }
        catch (Exception ex)
        {return false;}
    }
}
