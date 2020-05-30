package com.srop.utils;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import java.text.SimpleDateFormat;

/**
 * Created by xyyz150 on 2015/7/14.
 */
public class ObjectMapperUtils {

    public static ObjectMapper InitObjectMapper(){
        ObjectMapper jsonObjectMapper=new ObjectMapper();
        //AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();

        // 属性值为null的不序列化
        jsonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        jsonObjectMapper.configure(SerializationFeature.INDENT_OUTPUT,true);
        //是否环绕根元素
        jsonObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        jsonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        jsonObjectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        // 允许属性名无引号
        jsonObjectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 允许属性值使用单引号
        jsonObjectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonObjectMapper.setDateFormat(formatter);

        return jsonObjectMapper;
    }
}
