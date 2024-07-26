package com.github.opf.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by xyyz150
 */
@XStreamAlias("errorResponse")
public class OpfResponse {
    @XStreamAlias("code")
    private String code;

    @XStreamAlias("message")
    private String message;

    @XStreamAlias("errorCode")
    private String errorCode;

    @XStreamAlias("errorMessage")
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
