package com.srop.response;

/**
 * Created by xyyz150 on 2016/1/23.
 */
public enum  MainErrorType {
    INVALID_FORMAT("invalid.format-@@"),
    INVALID_APP_KEY("invalid.appkey-@@"),
    INVALID_SESSIONKEY("invalid.sessionkey-@@"),
    INVALID_METHOD("invalid.method-@@"),
    INVALID_SIGNATURE("invalid.signature-@@"),
    INVALID_PARAM("invalid.param-@@"),
    ISP_SERVICE_UNAVAILABLE("isv.service.unavailable-@@"),
    ISP_SERVICE_TIMEOUT("isv.service.timeout-@@"),
    EXCEED_APP_INVOKE_FREQUENCY_LIMITED("exceed.app.invoke.frequency.limited-@@"),
    EXCEED_APP_INVOKE_LIMITED("exceed.app.invoke.limited-@@"),
    EXCEED_SESSION_INVOKE_LIMITED("exceed.session.invoke.limited-@@"),
    EXCEED_USER_INVOKE_LIMITED("exceed.user.invoke.limited-@@"),
    MISSING_APP_KEY("missing.appkey-@@"),
    MISSING_METHOD("missing.method-@@"),
    MISSING_SIGNATURE("missing.signature-@@"),
    MISSING_SESSIONKEY("missing.sessionkey-@@"),
    METHOD_OBSOLETED("method.obsoleted-@@"),
    HTTP_ACTION_NOT_ALLOWED("http.action.not.allowed-@@"),
    UNAUTHORIZED_APP_KEY("unauthorized.appkey-@@");

private String msg;
    private MainErrorType(String errormsg){msg=errormsg;}

    public String getValue(){return msg;}
}
