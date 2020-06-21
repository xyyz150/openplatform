package com.github.opf.response;

/**
 * Created by xyyz150
 */
public enum MainErrorType {
    INVALID_FORMAT("invalid.format-@@"),
    INVALID_APP_KEY("invalid.appKey-@@"),
    INVALID_ACCESS_TOKEN("invalid.accessToken-@@"),
    INVALID_METHOD("invalid.method-@@"),
    INVALID_SIGNATURE("invalid.signature-@@"),
    INVALID_PARAM("invalid.param-@@"),
    ISP_SERVICE_UNAVAILABLE("isv.service.unavailable-@@"),
    ISP_SERVICE_TIMEOUT("isv.service.timeout-@@"),
    EXCEED_APP_INVOKE_FREQUENCY_LIMITED("exceed.app.invoke.frequency.limited-@@"),
    EXCEED_APP_INVOKE_LIMITED("exceed.app.invoke.limited-@@"),
    EXCEED_SESSION_INVOKE_LIMITED("exceed.session.invoke.limited-@@"),
    EXCEED_USER_INVOKE_LIMITED("exceed.user.invoke.limited-@@"),
    MISSING_APP_KEY("missing.appKey-@@"),
    MISSING_METHOD("missing.method-@@"),
    MISSING_SIGNATURE("missing.signature-@@"),
    MISSING_ACCESS_TOKEN("missing.accessToken-@@"),
    METHOD_OBSOLETED("method.obsoleted-@@"),
    HTTP_ACTION_NOT_ALLOWED("http.action.not.allowed-@@"),
    UNAUTHORIZED_APP_KEY("unauthorized.appKey-@@");

    private String msg;

    MainErrorType(String errorMsg) {
        msg = errorMsg;
    }

    public String getValue() {
        return msg;
    }
}
