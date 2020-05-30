package com.srop.router;

import com.srop.*;
import com.srop.config.SystemParameterNames;
import com.srop.event.*;
import com.srop.exception.InvalidParameteException;
import com.srop.exception.SropException;
import com.srop.handler.ServiceMethodHandler;
import com.srop.marshaller.MessageMarshallerUtils;
import com.srop.request.SropRequest;
import com.srop.request.SropRequestContext;
import com.srop.response.*;
import com.srop.security.*;
import com.srop.security.SecurityManager;
import com.srop.utils.SropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 路由分发
 * Created by xyyz150 on 2016/1/5.
 */
public class SropDispatchRouter implements SropRouter {

    public static final String APPLICATION_XML = "application/xml";

    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";


    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    // 上下文
    private SropContext sropContext;

    //安全管理
    private SecurityManager securityManager;

    //事件管理
    private SropEventHandler eventHandler;

//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private ThreadPoolExecutor threadPoolExecutor;

    //所有服务方法的最大过期时间，单位为秒(0或负数代表不限制)
    private int serviceTimeoutSeconds = Integer.MAX_VALUE;
    //事件列表
    private List<SropEventListener> listenerList = new ArrayList<SropEventListener>();


    /**
     * 初始化加载
     */
    public void init() {
        if (logger.isInfoEnabled()) {
            logger.info("开始启动Srop框架...");
        }


        //缺省安全管理对象
        if (this.securityManager == null) {
            this.securityManager = new DefaultSecurityManager();
        }
        //设置异步执行器
//        if (this.threadPoolTaskExecutor == null) {
//            this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//            this.threadPoolTaskExecutor.setCorePoolSize(1);
//            this.threadPoolTaskExecutor.setMaxPoolSize(Integer.MAX_VALUE);
//            this.threadPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);
//            this.threadPoolTaskExecutor.setKeepAliveSeconds(30000);
//            this.threadPoolTaskExecutor.setThreadNamePrefix("srop");
//        }
//        this.threadPoolTaskExecutor.initialize();

        if (this.threadPoolExecutor == null) {
            this.threadPoolExecutor =
                    new ThreadPoolExecutor(50, Integer.MAX_VALUE, 5 * 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
        }
        //初始化上下文
        this.sropContext = new SropContext(applicationContext);
        //事件处理程序
        this.eventHandler = SropBuilder.buliderSropEventHandler(listenerList, this.threadPoolExecutor);

        //框架初始化事件
        fireAfterStartedRopEvent();

        if (logger.isInfoEnabled()) {
            logger.info("Srop框架启动成功！");
        }

    }

    /**
     * 服务方法
     *
     * @param request
     * @param response
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext asyncContext = request.startAsync();
        final HttpServletRequest servletRequest = request;
        final HttpServletResponse servletResponse = response;
        final String method = SropUtils.urlDecode(request.getParameter(SystemParameterNames.getMethod()));
        final String version = SropUtils.urlDecode(request.getParameter(SystemParameterNames.getVersion()));
        final MessageFormat messageformat = SropUtils.getFormat(servletRequest);

        final int serviceMethodTimeout = getServiceMethodTimeout(method, version);
        final long beginTime = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("调用服务方法：" + method + "(" + version + ")");
        }
        asyncContext.setTimeout(600000L);//毫秒
        asyncContext.addListener(new AsyncListener() {

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                logger.error("completed {}#{}", method, version);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                logger.error("调用服务方法:" + method + "(" + version + ")，产生异常", event.getThrowable());
                SropRequestContext ropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
                SropResponse sropResponse = SropBuilder.getSropResponse(false, MainErrorType.ISP_SERVICE_UNAVAILABLE, ropRequestContext.getMethod(), ropRequestContext.getVersion(), "产生异常" + event.getThrowable().toString());
                writeResponse(sropResponse, servletResponse, messageformat);
                fireAfterDoServiceEvent(ropRequestContext);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                logger.debug("startAsync {}#{}", method, version);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                logger.info("调用服务方法:" + method + "(" + version + ")，服务调用超时。");
                SropRequestContext ropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
                SropResponse sropResponse = SropBuilder.getSropResponse(false, MainErrorType.ISP_SERVICE_TIMEOUT, ropRequestContext.getMethod(), ropRequestContext.getVersion(), "服务调用超时");
                writeResponse(sropResponse, servletResponse, messageformat);
                fireAfterDoServiceEvent(ropRequestContext);
            }
        });
        try {
            Future<?> future = this.threadPoolExecutor.submit(new ServiceRunnable(servletRequest, servletResponse));
            while (!future.isDone()) {
                future.get(serviceMethodTimeout, TimeUnit.SECONDS);
                asyncContext.complete();
            }

        } catch (RejectedExecutionException ree) {//超过最大的服务平台的最大资源限制，无法提供服务
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "(" + version + ")，超过最大资源限制，无法提供服务。");
            }
            SropRequestContext sropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            writeErrorResponse(response, sropRequestContext, "超过最大资源限制，无法提供服务");
            fireAfterDoServiceEvent(sropRequestContext);
        } catch (TimeoutException e) {//服务时间超限
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "(" + version + ")，服务调用超时。");
            }
            SropRequestContext sropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            writeErrorResponse(response, sropRequestContext, "服务调用超时");
            fireAfterDoServiceEvent(sropRequestContext);
        } catch (Throwable throwable) {//产生未知的错误
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "(" + version + ")，产生异常", throwable);
            }
            SropRequestContext sropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            writeErrorResponse(response, sropRequestContext, "产生异常" + throwable.toString());
            fireAfterDoServiceEvent(sropRequestContext);
        }
    }

    public void destroy() {
        fireBeforeCloseRopEvent();
//        threadPoolTaskExecutor.shutdown();
        threadPoolExecutor.shutdown();
    }


    /**
     * 发布Rop启动后事件
     */
    private void fireAfterStartedRopEvent() {
        AfterStartedSropEvent sropEvent = new AfterStartedSropEvent(this, this.sropContext);
        this.eventHandler.handlerEvent(sropEvent);
    }

    /**
     * 发布Rop启动后事件
     */
    private void fireBeforeCloseRopEvent() {
        PreCloseSropEvent sropEvent = new PreCloseSropEvent(this, this.sropContext);
        this.eventHandler.handlerEvent(sropEvent);
    }

    private void fireAfterDoServiceEvent(SropRequestContext ropRequestContext) {
        AfterDoServiceEvent sropEvent = new AfterDoServiceEvent(this, ropRequestContext);
        this.eventHandler.handlerEvent(sropEvent);
    }

    private void firePreDoServiceEvent(SropRequestContext ropRequestContext) {
        PreDoServiceEvent sropEvent = new PreDoServiceEvent(this, ropRequestContext);
        this.eventHandler.handlerEvent(sropEvent);
    }

    private void writeResponse(Object ropResponse, HttpServletResponse httpServletResponse, MessageFormat messageFormat) {
        try {
//            if ((ropResponse instanceof SropResponse) == false) {
//                throw new Exception("返回值不是SropResponse类型");
//            }
            if (logger.isDebugEnabled()) {
                logger.debug("输出响应：" + MessageMarshallerUtils.getMessage(ropResponse, messageFormat));
            }
            String contentType = APPLICATION_XML;
            if (messageFormat == MessageFormat.json) {
                contentType = APPLICATION_JSON;
            }
            httpServletResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            httpServletResponse.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
            httpServletResponse.setCharacterEncoding(Constants.UTF8);
            httpServletResponse.setContentType(contentType);
            MessageMarshallerUtils.marshaller(messageFormat, ropResponse, httpServletResponse);
        } catch (Exception e) {
            throw new SropException(e);
        }
    }

    private void writeErrorResponse(HttpServletResponse httpServletResponse, SropRequestContext sropRequestContext, String errormsg) {
        SropResponse sropResponse = SropBuilder.getSropResponse(false, MainErrorType.ISP_SERVICE_UNAVAILABLE, sropRequestContext.getMethod(), sropRequestContext.getVersion(), errormsg);
        sropRequestContext.setSropResponse(sropResponse);
        writeResponse(sropResponse, httpServletResponse, sropRequestContext.getFormat());
    }

    private int getServiceMethodTimeout(String method, String version) {
        ServiceMethodHandler serviceMethodHandler = sropContext.getServiceMethodHandler(method, version);
        if (serviceMethodHandler == null) {
            return getServiceTimeoutSeconds();
        } else {
            int methodTimeout = serviceMethodHandler.getServiceMethodDefinition().getTimeout();
            if (methodTimeout <= 0) {
                return getServiceTimeoutSeconds();
            } else {
                return methodTimeout;
            }
        }
    }

    public int getServiceTimeoutSeconds() {
        return serviceTimeoutSeconds > 0 ? serviceTimeoutSeconds : Integer.MAX_VALUE;
    }

    /**
     * 当发生异常时，创建一个请求上下文对象
     *
     * @param request
     * @param beginTime
     * @return
     */
    private SropRequestContext buildRequestContextWhenException(HttpServletRequest request, long beginTime) {
        SropRequestContext ropRequestContext = SropBuilder.buildBySysParams(sropContext, request, null);
        ropRequestContext.setServiceBeginTime(beginTime);
        ropRequestContext.setServiceEndTime(System.currentTimeMillis());
        return ropRequestContext;
    }

    /**
     * 线程处理 Runnable
     */
    private class ServiceRunnable implements Runnable {

        private HttpServletRequest request;

        private HttpServletResponse response;

        private ServiceRunnable(HttpServletRequest request,
                                HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

        @Override
        public void run() {
            SropRequestContext sropRequestContext = null;
            SropRequest sropRequest = null;
            try {
                //1用系统级参数构造一个RequestContext实例（第一阶段绑定）
                sropRequestContext = SropBuilder.buildBySysParams(sropContext,
                        request, response);
                //2验证系统级参数的合法性
                SropResponse mainError = securityManager.validateSystemParameters(sropRequestContext);
                if (mainError != null) {
                    //输出响应
                    sropRequestContext.setSropResponse(mainError);
                    writeResponse(mainError, response, sropRequestContext.getFormat());
                } else {
                    //3绑定业务数据
                    sropRequest = SropBuilder.buildSropRequest(sropRequestContext);
                    //4验证参数非空，长度等
                    mainError = SropBuilder.Validation(sropRequest);
                    if (mainError != null) {
                        //输出响应
                        sropRequestContext.setSropResponse(mainError);
                        writeResponse(mainError, response, sropRequestContext.getFormat());
                    } else {
                        //5进行其它检查业务数据合法性，业务安全等
                        mainError = securityManager.validateOther(sropRequestContext);
                        if (mainError != null) {
                            //输出响应
                            sropRequestContext.setSropResponse(mainError);
                            writeResponse(mainError, response, sropRequestContext.getFormat());
                        } else {
                            firePreDoServiceEvent(sropRequestContext);

                            invoke(sropRequestContext, sropRequest);

                            writeResponse(sropRequestContext.getSropResponse(), response, sropRequestContext.getFormat());
                        }
                    }
                }

            } catch (InvalidParameteException e) {
                if (sropRequestContext != null) {
                    String method = sropRequestContext.getMethod();
                    if (logger.isDebugEnabled()) {
                        String message = java.text.MessageFormat.format("service {0} call error", method);
                        logger.debug(message, e);
                    }
                    writeErrorResponse(response, sropRequestContext, "参数异常:" + e.toString());
                } else {
                    throw new SropException("RopRequestContext is null.", e);
                }
            } catch (Exception e) {
                if (sropRequestContext != null) {
                    String method = sropRequestContext.getMethod();
                    if (logger.isDebugEnabled()) {
                        String message = java.text.MessageFormat.format("service {0} call error", method);
                        logger.debug(message, e);
                    }
                    writeErrorResponse(response, sropRequestContext, e.toString());
                } else {
                    throw new SropException("RopRequestContext is null.", e);
                }
            } finally {
                if (sropRequestContext != null) {
                    sropRequestContext.setServiceEndTime(System.currentTimeMillis());
                    fireAfterDoServiceEvent(sropRequestContext);
                }
            }
        }

    }

    public void invoke(final SropRequestContext requestContext, final SropRequest sropRequest) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = requestContext.getServiceMethodHandler().getHandlerMethod().invoke(requestContext.getServiceMethodHandler().getHandler(), sropRequest);
        requestContext.setServiceEndTime(System.currentTimeMillis());
        if (result != null) {
            requestContext.setSropResponse(result);
        }
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void setServiceTimeoutSeconds(int serviceTimeoutSeconds) {
        this.serviceTimeoutSeconds = serviceTimeoutSeconds;
    }

    public void addListen(SropEventListener listener) {
        listenerList.add(listener);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

//    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
//        return threadPoolTaskExecutor;
//    }
//
//    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
//        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
//    }

    public List<SropEventListener> getListenerList() {
        return listenerList;
    }

    public void setListenerList(List<SropEventListener> listenerList) {
        this.listenerList = listenerList;
    }
}
