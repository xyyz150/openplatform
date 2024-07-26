package com.github.opf.response;

/**
 * Created by xyyz150
 */
public enum MainErrorType {
    INVALID_APP_KEY("10","invalid.appKey-@@"),
    INVALID_SESSION("20","invalid.session-@@"),
    INVALID_METHOD("30","invalid.method-@@"),
    INVALID_SIGNATURE("40","invalid.signature-@@"),
    INVALID_PARAM("50","invalid.param-@@"),
    INVALID_TIMESTAMP("60","invalid.timestamp-@@"),

    MISSING_APP_KEY("110","missing.appKey-@@"),
    MISSING_METHOD("120","missing.method-@@"),
    MISSING_SIGNATURE("130","missing.signature-@@"),
    MISSING_SESSION("140","missing.session-@@"),
    MISSING_TIMESTAMP("150","missing.timestamp-@@"),
    METHOD_DEPRECATED("160","method.deprecated-@@"),
    UNAUTHORIZED_APP_KEY("170","unauthorized.appKey-@@"),

    ISP_SERVICE_UNAVAILABLE("500","isv.service.unavailable-@@"),
    ISP_SERVICE_TIMEOUT("510","isv.service.timeout-@@"),

    EXCEED_APP_INVOKE_FREQUENCY_LIMITED("200","exceed.app.invoke.frequency.limited-@@"),
    EXCEED_APP_INVOKE_LIMITED("210","exceed.app.invoke.limited-@@"),
    EXCEED_SESSION_INVOKE_LIMITED("220","exceed.session.invoke.limited-@@");


    private String code;

    private String msg;

    MainErrorType(String errorCode,String errorMsg) {
        code=errorCode;
        msg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return msg;
    }
}
