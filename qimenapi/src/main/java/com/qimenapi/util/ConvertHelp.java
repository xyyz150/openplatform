package com.qimenapi.util;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xyyz150 on 2014/11/17.
 */
public class ConvertHelp {
    public static long ToLong(Object o) {
        if (o == null) return 0L;
        String str = o.toString();
        if (str.equals("")) return 0L;
        return Long.parseLong(o.toString());
    }

    public static double ToDouble(Object o) {
        if (o == null) return 0;
        String str = o.toString();
        if (str.equals("")) return 0;
        return Double.parseDouble(o.toString());
    }

    public static int ToInt(Object o) {
        if (o == null) return 0;
        String str = o.toString();
        if (str.equals("")) return 0;
        return Integer.parseInt(o.toString());
    }

    public static String ToString(Object o) {
        if (o == null) return "";
        String str = o.toString();
        if (str.isEmpty()) return "";
        return str;
    }

    public static Date ToDate(Object o) {
        Date date = null;
        if (o == null) return date;
        String strdate = o.toString();
        if (StringHelp.IsNullOrEmpty(strdate)) {
            return date;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(strdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            try {
                date = sdf1.parse(strdate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return date;
    }

    public static Date ToDateTime(Object o) {
        Date date = null;
        if (o == null) return date;
        String strdate = o.toString();
        if (StringHelp.IsNullOrEmpty(strdate)) {
            return date;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(strdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }
//
//    public static String ToJson(Object o) throws Exception {
//        StringWriter sw = new StringWriter(); // serialize
//        ObjectMapper mapper = new CMappingJacksonObjectMapper();
//        MappingJsonFactory jsonFactory = new MappingJsonFactory();
//        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
//        mapper.writeValue(jsonGenerator, o);
//        sw.close();
//        return sw.getBuffer().toString();
//    }
//
//    public static <T> T ToObjectFromJson(String json, Class<T> c) throws Exception {
//        CMappingJacksonObjectMapper objectMapper = new CMappingJacksonObjectMapper();
//        T result = objectMapper.readValue(json, c);
//        return result;
//    }

    public static <TBasicType> List<TBasicType> ToBasicTypeList(List<Map<String, Object>> mapList) {
        List<TBasicType> list = new ArrayList<TBasicType>();
        for (Map<String, Object> map : mapList) {
            for (Object o : map.values()) {
                list.add((TBasicType) o);
                break;
            }
        }
        return list;
    }

    public static Boolean ToBoolean(Object o) {
        if (o == null) return null;
        String str = o.toString();
        if (str.equals("")) return null;
        return Boolean.parseBoolean(o.toString());
    }
}

