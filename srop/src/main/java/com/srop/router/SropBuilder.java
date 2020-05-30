package com.srop.router;

import com.srop.MessageFormat;
import com.srop.SropContext;
import com.srop.annotation.HttpAction;
import com.srop.config.SystemParameterNames;
import com.srop.event.DefaultSropEventHandler;
import com.srop.event.SropEventHandler;
import com.srop.event.SropEventListener;
import com.srop.exception.InvalidParameteException;
import com.srop.handler.ServiceMethodHandler;
import com.srop.request.SropRequest;
import com.srop.request.SropRequestContext;
import com.srop.response.MainErrorType;
import com.srop.response.SropResponse;
import com.srop.utils.JsonUtils;
import com.srop.utils.SropUtils;
import com.srop.utils.XmlParser.ObjectXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by xyyz150 on 2016/2/17.
 */
public class SropBuilder {
    //通过前端的负载均衡服务器时，请求对象中的IP会变成负载均衡服务器的IP，因此需要特殊处理，下同。
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static Validator validator;

    protected static Logger logger = LoggerFactory.getLogger(SropBuilder.class);

    public static void initValidator() {
        if (validator == null) {
            //spring集成验证，暂不使用，使用原生的
//            LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
//            factoryBean.afterPropertiesSet();
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
    }

    public static SropEventHandler buliderSropEventHandler(List<SropEventListener> eventListeners, ThreadPoolExecutor executor) {
        DefaultSropEventHandler eventHandler = new DefaultSropEventHandler();
        //设置异步执行器
        if (executor != null) {
            eventHandler.setExecutor(executor);
        }

        //添加事件监听器
        if (eventListeners != null && eventListeners.size() > 0) {
            for (SropEventListener sropEventListener : eventListeners) {
                eventHandler.addSropListener(sropEventListener);
            }
        }

        return eventHandler;
    }

    public static SropRequestContext buildBySysParams(SropContext ropContext, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        SropRequestContext requestContext = new SropRequestContext(ropContext);

        //设置请求对象及参数列表
        requestContext.setHttpServletRequest(servletRequest);
        requestContext.setHttpServletResponse(servletResponse);
        requestContext.setServiceBeginTime(System.currentTimeMillis());
        requestContext.setIp(getRemoteAddr(servletRequest));
        //设置服务的系统级参数
        requestContext.setAppKey(SropUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getAppKey())));
        requestContext.setSessionId(SropUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getSessionId())));
        requestContext.setMethod(SropUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getMethod())));
        requestContext.setVersion(SropUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getVersion())));
        requestContext.setFormat(SropUtils.getFormat(servletRequest));
        requestContext.setSign(SropUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getSign())));
        requestContext.setHttpAction(HttpAction.fromValue(servletRequest.getMethod()));
        try {
            requestContext.setBody(SropUtils.getBody(servletRequest));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取body错误：" + e.toString());
        }
        //设置所有参数
        requestContext.setAllParams(getRequestParams(servletRequest));

        //设置服务处理器
        ServiceMethodHandler serviceMethodHandler =
                ropContext.getServiceMethodHandler(requestContext.getMethod(), requestContext.getVersion());
        requestContext.setServiceMethodHandler(serviceMethodHandler);

        return requestContext;
    }

    public static SropRequest buildSropRequest(SropRequestContext sropRequestContext) {
        SropRequest sropRequest = null;
        try {
            if (sropRequestContext.getServiceMethodHandler().isSropRequestImplType()) {
                HttpServletRequest request = sropRequestContext.getHttpServletRequest();
                Class<? extends SropRequest> classType = sropRequestContext.getServiceMethodHandler().getRequestType();
                if (sropRequestContext.getFormat() == MessageFormat.json) {
                    sropRequest = JsonUtils.readObject(sropRequestContext.getBody(), classType);
                } else if (sropRequestContext.getFormat() == MessageFormat.xml) {
                    ObjectXmlParser parser = new ObjectXmlParser(classType);
                    sropRequest = (SropRequest) parser.parse(sropRequestContext.getBody());
//                    sropRequest = (SropRequest) XStreamUtils.getXstream(classType).fromXML(sropRequestContext.getBody());
                }
                sropRequest.setSropRequestContext(sropRequestContext);
            }
        } catch (Exception e) {
            throw new InvalidParameteException(e.getMessage());
        }
        return sropRequest;
    }

    public static SropResponse Validation(SropRequest request) throws Exception {
        initValidator();
        Set<ConstraintViolation<SropRequest>> errorlist = validator.validate(request);
        if (errorlist == null || errorlist.size() == 0) return null;
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation<SropRequest> validate : errorlist) {
            sb.append(validate.getPropertyPath()).append(validate.getMessage()).append(";");
        }
        String str=new String(sb.toString().getBytes(),"gb2312");
        return getSropResponse(false, MainErrorType.INVALID_PARAM, request.getSropRequestContext().getMethod(), request.getSropRequestContext().getVersion(), new String(str.getBytes("utf-8"),"utf-8"));
    }

    private static HashMap<String, String> getRequestParams(HttpServletRequest request) {
        HashMap<String, String> destParamMap = new HashMap<String, String>();
        String queryString = request.getQueryString();
        queryString = SropUtils.urlDecode(queryString);
        String[] queryarray = queryString.split("&");
        for (String query : queryarray) {
            int index = query.indexOf("=");
            if (index < 0) {
                continue;
            }
            String key = query.substring(0, index);
            String value = query.substring(index + 1);
            destParamMap.put(key, value);
        }
        return destParamMap;
    }


    private static String getRemoteAddr(HttpServletRequest request) {
        String remoteIp = request.getHeader(X_REAL_IP); //nginx反向代理
        if (StringUtils.hasText(remoteIp)) {
            return remoteIp;
        } else {
            remoteIp = request.getHeader(X_FORWARDED_FOR);//apache反射代理
            if (StringUtils.hasText(remoteIp)) {
                String[] ips = remoteIp.split(",");
                for (String ip : ips) {
                    if (!"null".equalsIgnoreCase(ip)) {
                        return ip;
                    }
                }
            }
            return request.getRemoteAddr();
        }
    }

    public static SropResponse getSropResponse(boolean isuccess, MainErrorType mainErrorType, String method, String v, String msg) {
        SropResponse response = new SropResponse();
        response.setFlag(isuccess ? "success" : "failure");
        response.setCode(new StringBuffer().append(mainErrorType.getValue()).append(method).append(":").append(v).toString());
        response.setMessage(msg);
        return response;
    }
}
