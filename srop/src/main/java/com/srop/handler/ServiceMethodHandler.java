
package com.srop.handler;

import com.srop.request.SropRequest;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <pre>
 *     服务处理器的相关信息
 * </pre>
 *
 * @version 1.0
 */
public class ServiceMethodHandler {

    //处理器对象
    private Object handler;

    //处理器的处理方法
    private Method handlerMethod;

    private ServiceMethodDefinition serviceMethodDefinition;

    //处理方法的请求对象类
    private Class<? extends SropRequest> requestType = SropRequest.class;

    //是否是实现类
    private boolean sropRequestImplType;

    public ServiceMethodHandler() {
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public Class<? extends SropRequest> getRequestType() {
        return requestType;
    }

    public void setRequestType(Class<? extends SropRequest> requestType) {
        this.requestType = requestType;
    }


    public boolean isHandlerMethodWithParameter() {
        return this.requestType != null;
    }

    public ServiceMethodDefinition getServiceMethodDefinition() {
        return serviceMethodDefinition;
    }

    public void setServiceMethodDefinition(ServiceMethodDefinition serviceMethodDefinition) {
        this.serviceMethodDefinition = serviceMethodDefinition;
    }

    public static String methodWithVersion(String methodName, String version) {
        return methodName + "#" + version;
    }

    public boolean isSropRequestImplType() {
        return sropRequestImplType;
    }

    public void setSropRequestImplType(boolean sropRequestImplType) {
        this.sropRequestImplType = sropRequestImplType;
    }

}

