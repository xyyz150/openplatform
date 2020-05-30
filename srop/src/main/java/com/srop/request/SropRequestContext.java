package com.srop.request;

import com.srop.MessageFormat;
import com.srop.SropContext;
import com.srop.annotation.HttpAction;
import com.srop.handler.ServiceMethodHandler;
import com.srop.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * 接到服务请求后，将产生一个{@link SropRequestContext}上下文对象，它被本次请求直到返回响应的这个线程共享。
 * Created by xyyz150 on 2016/1/5.
 */
public class SropRequestContext {

    public SropRequestContext(SropContext SropContext){
        sropContext=SropContext;
    }

    private SropContext sropContext;

    // http
    private HttpServletRequest httpServletRequest;

    // http
    private HttpServletResponse httpServletResponse;

    private ServiceMethodHandler serviceMethodHandler;

    //请求的方法
    private String method;

    //请求的版本
    private String version;

    //请求的开发者账号
    private String appKey;

    //会话id
    private String sessionId;

    //请求格式xml/json
    private MessageFormat format;

    //签名
    private String sign;

    //时间戳
    private String timestamp;

    private String ip;

    private HttpAction httpAction;

    private String body;
    //请求后的响应
    private Object sropResponse;


    //请求有参数
    private Map<String, String> allParams;

    private long serviceBeginTime = -1;

    private long serviceEndTime = -1;

    //session
    private Session session;



    private String requestId = UUID.randomUUID().toString();

    public HttpAction getHttpAction() {
        return httpAction;
    }

    public void setHttpAction(HttpAction httpAction) {
        this.httpAction = httpAction;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public Object getSropResponse() {
        return sropResponse;
    }

    public void setSropResponse(Object sropResponse) {
        this.sropResponse = sropResponse;
    }

    public ServiceMethodHandler getServiceMethodHandler() {
        return serviceMethodHandler;
    }

    public void setServiceMethodHandler(ServiceMethodHandler serviceMethodHandler) {
        this.serviceMethodHandler = serviceMethodHandler;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageFormat getFormat() {
        return format;
    }

    public void setFormat(MessageFormat format) {
        this.format = format;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String> getAllParams() {
        return allParams;
    }

    public void setAllParams(Map<String, String> allParams) {
        this.allParams = allParams;
    }

    public long getServiceBeginTime() {
        return serviceBeginTime;
    }

    public void setServiceBeginTime(long serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    public long getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(long serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public SropContext getSropContext() {
        return sropContext;
    }

    public void setSropContext(SropContext sropContext) {
        this.sropContext = sropContext;
    }
}
