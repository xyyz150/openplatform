package com.github.opf.router;

import com.github.opf.MessageFormat;
import com.github.opf.OpfContext;
import com.github.opf.annotation.HttpAction;
import com.github.opf.config.SystemParameterNames;
import com.github.opf.event.DefaultOpfEventHandler;
import com.github.opf.event.OpfEventHandler;
import com.github.opf.event.OpfEventListener;
import com.github.opf.exception.InvalidParameterException;
import com.github.opf.handler.ServiceMethodHandler;
import com.github.opf.request.OpfRequest;
import com.github.opf.request.OpfRequestContext;
import com.github.opf.response.MainErrorType;
import com.github.opf.response.OpfResponse;
import com.github.opf.utils.JsonUtils;
import com.github.opf.utils.OpfUtils;
import com.github.opf.utils.XmlParser.ObjectXmlParser;
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
public class OpfBuilder {
    //通过前端的负载均衡服务器时，请求对象中的IP会变成负载均衡服务器的IP，因此需要特殊处理，下同。
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static Validator validator;

    protected static Logger logger = LoggerFactory.getLogger(OpfBuilder.class);

    public static void initValidator() {
        if (validator == null) {
            //spring集成验证，暂不使用，使用原生的
//            LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
//            factoryBean.afterPropertiesSet();
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
    }

    public static OpfEventHandler buildOpfEventHandler(List<OpfEventListener> eventListeners, ThreadPoolExecutor executor) {
        DefaultOpfEventHandler eventHandler = new DefaultOpfEventHandler();
        //设置异步执行器
        if (executor != null) {
            eventHandler.setExecutor(executor);
        }

        //添加事件监听器
        if (eventListeners != null && eventListeners.size() > 0) {
            for (OpfEventListener opfEventListener : eventListeners) {
                eventHandler.addOpfListener(opfEventListener);
            }
        }

        return eventHandler;
    }

    public static OpfRequestContext buildBySysParams(OpfContext opfContext, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        OpfRequestContext requestContext = new OpfRequestContext(opfContext);

        //设置请求对象及参数列表
        requestContext.setHttpServletRequest(servletRequest);
        requestContext.setHttpServletResponse(servletResponse);
        requestContext.setServiceBeginTime(System.currentTimeMillis());
        requestContext.setIp(getRemoteAddress(servletRequest));
        //设置服务的系统级参数
        requestContext.setAppKey(OpfUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getAppKey())));
        requestContext.setAccessToken(OpfUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getAccessToken())));
        requestContext.setMethod(OpfUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getMethod())));
        requestContext.setVersion(OpfUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getVersion())));
        requestContext.setFormat(OpfUtils.getFormat(servletRequest));
        requestContext.setSign(OpfUtils.urlDecode(servletRequest.getParameter(SystemParameterNames.getSign())));
        requestContext.setHttpAction(HttpAction.fromValue(servletRequest.getMethod()));
        try {
            requestContext.setBody(OpfUtils.getBody(servletRequest));
        } catch (Exception e) {
            logger.error("获取body错误：" + e.toString());
        }
        //设置所有参数
        requestContext.setAllParams(getRequestParams(servletRequest));

        //设置服务处理器
        ServiceMethodHandler serviceMethodHandler =
                opfContext.getServiceMethodHandler(requestContext.getMethod(), requestContext.getVersion());
        requestContext.setServiceMethodHandler(serviceMethodHandler);

        return requestContext;
    }

    public static OpfRequest buildOpfRequest(OpfRequestContext opfRequestContext) {
        OpfRequest opfRequest = null;
        try {
            if (opfRequestContext.getServiceMethodHandler().isOpfRequestImplType()) {
                Class<? extends OpfRequest> classType = opfRequestContext.getServiceMethodHandler().getRequestType();
                if (opfRequestContext.getFormat() == MessageFormat.json) {
                    opfRequest = JsonUtils.parse(opfRequestContext.getBody(), classType);
                } else if (opfRequestContext.getFormat() == MessageFormat.xml) {
                    ObjectXmlParser parser = new ObjectXmlParser(classType);
                    opfRequest = (OpfRequest) parser.parse(opfRequestContext.getBody());
                }
                opfRequest.setOpfRequestContext(opfRequestContext);
            }
        } catch (Exception e) {
            throw new InvalidParameterException(e.getMessage());
        }
        return opfRequest;
    }

    public static OpfResponse Validation(OpfRequest request) throws Exception {
        initValidator();
        Set<ConstraintViolation<OpfRequest>> errorList = validator.validate(request);
        if (errorList == null || errorList.size() == 0) return null;
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation<OpfRequest> validate : errorList) {
            sb.append(validate.getPropertyPath()).append(validate.getMessage()).append(";");
        }
        String str=new String(sb.toString().getBytes(),"gb2312");
        return getOpfResponse(false, MainErrorType.INVALID_PARAM, request.getOpfRequestContext().getMethod(),
                request.getOpfRequestContext().getVersion(), new String(str.getBytes("utf-8"),"utf-8"));
    }

    private static HashMap<String, String> getRequestParams(HttpServletRequest request) {
        HashMap<String, String> destParamMap = new HashMap<String, String>();
        String queryString = request.getQueryString();
        queryString = OpfUtils.urlDecode(queryString);
        String[] queryArr = queryString.split("&");
        for (String query : queryArr) {
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


    private static String getRemoteAddress(HttpServletRequest request) {
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

    public static OpfResponse getOpfResponse(boolean success, MainErrorType mainErrorType, String method, String v, String msg) {
        OpfResponse response = new OpfResponse();
        response.setFlag(success ? "success" : "failure");
        response.setCode(new StringBuffer().append(mainErrorType.getValue()).append(method).append(":").append(v).toString());
        response.setMessage(msg);
        return response;
    }
}
