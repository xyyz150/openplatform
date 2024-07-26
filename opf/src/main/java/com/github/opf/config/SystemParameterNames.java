/**
 * 版权声明： 版权所有 违者必究 2012
 * 日    期：12-6-5
 */package com.github.opf.config;

/**
 * 系统级参数的名称
 * Created by xyyz150
 */
public class SystemParameterNames {

    //方法的默认参数名
    private static final String METHOD = "method";

    //格式化默认参数名
    private static final String FORMAT = "format";

    //会话id默认参数名
    private static final String SESSION = "session";

    //应用键的默认参数名        ;
    private static final String APPKEY = "appKey";

    //服务版本号的默认参数名
    private static final String VERSION = "v";

    //时间戳的默认参数名
    private static final String TIMESTAMP = "timestamp";

    //签名的默认参数名
    private static final String SIGN = "sign";

    private static String method = METHOD;

    private static String format = FORMAT;

    private static String session = SESSION;

    private static String appKey = APPKEY;

    private static String version = VERSION;
    //时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2016-01-01 12:00:00
    private static String timestamp = TIMESTAMP;

    private static String sign = SIGN;

    public static String getMethod() {
        return method;
    }

    public static void setMethod(String method) {
        SystemParameterNames.method = method;
    }

    public static String getFormat() {
        return format;
    }

    public static void setFormat(String format) {
        SystemParameterNames.format = format;
    }

    public static String getSession() {
        return session;
    }

    public static void setSession(String session) {
        SystemParameterNames.session = session;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        SystemParameterNames.appKey = appKey;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        SystemParameterNames.version = version;
    }

    public static String getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(String timestamp) {
        SystemParameterNames.timestamp = timestamp;
    }

    public static String getSign() {
        return sign;
    }

    public static void setSign(String sign) {
        SystemParameterNames.sign = sign;
    }

}

