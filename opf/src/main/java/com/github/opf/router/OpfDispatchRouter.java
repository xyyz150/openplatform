package com.github.opf.router;

import com.github.opf.config.SystemParameterNames;
import com.github.opf.exception.InvalidParameterException;
import com.github.opf.exception.OpfException;
import com.github.opf.handler.ServiceMethodHandler;
import com.github.opf.marshaller.MessageWriteUtils;
import com.github.opf.Constants;
import com.github.opf.MessageFormat;
import com.github.opf.OpfContext;
import com.github.opf.event.*;
import com.github.opf.request.OpfRequest;
import com.github.opf.request.OpfRequestContext;
import com.github.opf.response.MainErrorType;
import com.github.opf.response.OpfResponse;
import com.github.opf.security.DefaultSecurityManager;
import com.github.opf.security.SecurityManager;
import com.github.opf.utils.OpfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 路由分发
 * Created by xyyz150
 */
public class OpfDispatchRouter implements OpfRouter {

    public static final String APPLICATION_XML = "application/xml;charset=utf-8";

    public static final String APPLICATION_JSON = "application/json;charset=utf-8";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    // 上下文
    private OpfContext opfContext;

    //安全管理
    private SecurityManager securityManager;

    //事件管理
    private OpfEventHandler eventHandler;

    private ThreadPoolExecutor threadPoolExecutor;

    private ThreadPoolExecutor eventThreadPoolExecutor;

    //所有服务方法的最大过期时间，单位为毫秒(0或负数代表不限制)
    private int serviceTimeout = -1;
    //事件列表
    private List<OpfEventListener> listenerList = new ArrayList<OpfEventListener>();


    /**
     * 初始化加载
     */
    @Override
    public void init() {
        if (logger.isInfoEnabled()) {
            logger.info("开始启动opf框架...");
        }

        //缺省安全管理对象
        if (this.securityManager == null) {
            this.securityManager = new DefaultSecurityManager();
        }
        //初始化上下文
        this.opfContext = new OpfContext(applicationContext);
        //事件处理程序
        this.eventHandler = OpfBuilder.buildOpfEventHandler(listenerList, this.eventThreadPoolExecutor);

        //框架初始化事件
        fireAfterStartedOpfEvent();

        if (logger.isInfoEnabled()) {
            logger.info("opf框架启动成功！");
        }

    }

    /**
     * 服务方法
     *
     * @param request
     * @param response
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext asyncContext = request.startAsync();
        final HttpServletRequest servletRequest = request;
        final HttpServletResponse servletResponse = response;
        final String method = OpfUtils.urlDecode(request.getParameter(SystemParameterNames.getMethod()));
        final String version = OpfUtils.urlDecode(request.getParameter(SystemParameterNames.getVersion()));

        final int serviceMethodTimeout = getServiceMethodTimeout(method, version);
        final long beginTime = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("调用服务方法：{}#{}", method, version);
        }
        asyncContext.setTimeout(-1);//毫秒
        asyncContext.addListener(new AsyncListener() {

            @Override
            public void onComplete(AsyncEvent event) {
                logger.debug("completed {}#{}", method, version);
            }

            @Override
            public void onError(AsyncEvent event) {
                logger.error("调用服务方法:" + method + "#" + version + "，产生异常", event.getThrowable());
                OpfRequestContext opfRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
                writeErrorResponse(servletResponse, opfRequestContext, MainErrorType.ISP_SERVICE_UNAVAILABLE, "产生异常:" + event.getThrowable().toString());
                fireAfterDoServiceEvent(opfRequestContext);
            }

            @Override
            public void onStartAsync(AsyncEvent event) {
                logger.debug("startAsync {}#{}", method, version);
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                logger.info("调用服务方法:{}#{}，服务调用超时。", method, version);
                OpfRequestContext opfRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
                writeErrorResponse(servletResponse, opfRequestContext, MainErrorType.ISP_SERVICE_TIMEOUT, "服务调用超时");
                fireAfterDoServiceEvent(opfRequestContext);
            }
        });
        Future<?> future = null;
        try {
            future = this.threadPoolExecutor.submit(new ServiceRunnable(servletRequest, servletResponse));
            while (!future.isDone()) {
                if(serviceMethodTimeout>0) {
                    future.get(serviceMethodTimeout, TimeUnit.SECONDS);
                }else {
                    future.get();
                }
                asyncContext.complete();
            }

        } catch (RejectedExecutionException ree) {
            //超过最大的服务平台的最大资源限制，无法提供服务
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:{}#{}，超过最大资源限制，无法提供服务。", method, version);
            }
            OpfRequestContext opfRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            writeErrorResponse(servletResponse, opfRequestContext, MainErrorType.ISP_SERVICE_UNAVAILABLE, "超过最大资源限制，无法提供服务");
            fireAfterDoServiceEvent(opfRequestContext);
        } catch (TimeoutException e) {
            //服务时间超限
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:{}#{}，服务调用超时。", method, version);
            }
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }
            OpfRequestContext opfRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            writeErrorResponse(servletResponse, opfRequestContext, MainErrorType.ISP_SERVICE_UNAVAILABLE, "服务调用超时");
            fireAfterDoServiceEvent(opfRequestContext);
        } catch (Throwable throwable) {
            //产生未知的错误
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "#" + version + "，产生异常", throwable);
            }
            OpfRequestContext opfRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            writeErrorResponse(response, opfRequestContext, MainErrorType.ISP_SERVICE_UNAVAILABLE, "产生异常" + throwable);
            fireAfterDoServiceEvent(opfRequestContext);
        }
    }

    @Override
    public void destroy() {
        fireBeforeCloseOpfEvent();
        threadPoolExecutor.shutdown();
        eventThreadPoolExecutor.shutdown();
    }


    /**
     * 发布opf启动后事件
     */
    private void fireAfterStartedOpfEvent() {
        AfterStartedOpfEvent opfEvent = new AfterStartedOpfEvent(this, this.opfContext);
        this.eventHandler.handlerEvent(opfEvent);
    }

    /**
     * 发布opf启动后事件
     */
    private void fireBeforeCloseOpfEvent() {
        PreCloseOpfEvent opfEvent = new PreCloseOpfEvent(this, this.opfContext);
        this.eventHandler.handlerEvent(opfEvent);
    }

    private void fireAfterDoServiceEvent(OpfRequestContext opfRequestContext) {
        AfterDoServiceEvent opfEvent = new AfterDoServiceEvent(this, opfRequestContext);
        this.eventHandler.handlerEvent(opfEvent);
    }

    private void firePreDoServiceEvent(OpfRequestContext opfRequestContext) {
        PreDoServiceEvent opfEvent = new PreDoServiceEvent(this, opfRequestContext);
        this.eventHandler.handlerEvent(opfEvent);
    }

    private void writeResponse(Object opfResponse, HttpServletResponse httpServletResponse, MessageFormat messageFormat) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("输出响应：" + MessageWriteUtils.getMessage(opfResponse, messageFormat));
            }
            String contentType = APPLICATION_XML;
            if (messageFormat == MessageFormat.json) {
                contentType = APPLICATION_JSON;
            }
            httpServletResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            httpServletResponse.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
            httpServletResponse.setCharacterEncoding(Constants.UTF8);
            httpServletResponse.setContentType(contentType);
            MessageWriteUtils.write(messageFormat, opfResponse, httpServletResponse);
        } catch (Exception e) {
            throw new OpfException(e);
        }
    }

    private void writeErrorResponse(HttpServletResponse httpServletResponse, OpfRequestContext opfRequestContext, String errormsg) {
        OpfResponse opfResponse = OpfBuilder.getOpfResponse(false, MainErrorType.ISP_SERVICE_UNAVAILABLE,
                opfRequestContext.getMethod(), opfRequestContext.getVersion(), errormsg);
        opfRequestContext.setOpfResponse(opfResponse);
        writeResponse(opfResponse, httpServletResponse, opfRequestContext.getFormat());
    }

    private void writeErrorResponse(HttpServletResponse httpServletResponse, OpfRequestContext opfRequestContext, MainErrorType mainErrorType, String errormsg) {
        OpfResponse opfResponse = OpfBuilder.getOpfResponse(false, mainErrorType,
                opfRequestContext.getMethod(), opfRequestContext.getVersion(), errormsg);
        opfRequestContext.setOpfResponse(opfResponse);
        writeResponse(opfResponse, httpServletResponse, opfRequestContext.getFormat());
    }

    private int getServiceMethodTimeout(String method, String version) {
        ServiceMethodHandler serviceMethodHandler = opfContext.getServiceMethodHandler(method, version);
        if (serviceMethodHandler == null) {
            return getServiceTimeout();
        } else {
            int methodTimeout = serviceMethodHandler.getServiceMethodDefinition().getTimeout();
            if (methodTimeout <= 0) {
                return getServiceTimeout();
            } else {
                return methodTimeout;
            }
        }
    }

    public int getServiceTimeout() {
        return serviceTimeout;
    }

    /**
     * 当发生异常时，创建一个请求上下文对象
     *
     * @param request
     * @param beginTime
     * @return
     */
    private OpfRequestContext buildRequestContextWhenException(HttpServletRequest request, long beginTime) {
        OpfRequestContext opfRequestContext = OpfBuilder.buildBySysParams(opfContext, request, null);
        opfRequestContext.setServiceBeginTime(beginTime);
        opfRequestContext.setServiceEndTime(System.currentTimeMillis());
        return opfRequestContext;
    }

    public void invoke(final OpfRequestContext requestContext, final OpfRequest opfRequest) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = requestContext.getServiceMethodHandler().getHandlerMethod().invoke(requestContext.getServiceMethodHandler().getHandler(), opfRequest);
        requestContext.setServiceEndTime(System.currentTimeMillis());
        if (result != null) {
            requestContext.setOpfResponse(result);
        }
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void setServiceTimeout(int serviceTimeout) {
        this.serviceTimeout = serviceTimeout;
    }

    public void addListener(OpfEventListener listener) {
        listenerList.add(listener);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public List<OpfEventListener> getListenerList() {
        return listenerList;
    }

    public void setListenerList(List<OpfEventListener> listenerList) {
        this.listenerList = listenerList;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setEventThreadPoolExecutor(ThreadPoolExecutor eventThreadPoolExecutor) {
        this.eventThreadPoolExecutor = eventThreadPoolExecutor;
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
            OpfRequestContext opfRequestContext = null;
            OpfRequest opfRequest = null;
            try {
                //1用系统级参数构造一个RequestContext实例（第一阶段绑定）
                opfRequestContext = OpfBuilder.buildBySysParams(opfContext,
                        request, response);
                //2验证系统级参数的合法性
                OpfResponse errorResponse = securityManager.validateSystemParameters(opfRequestContext);
                if (errorResponse != null) {
                    //输出响应
                    opfRequestContext.setOpfResponse(errorResponse);
                    writeResponse(errorResponse, response, opfRequestContext.getFormat());
                } else {
                    //3绑定业务数据
                    opfRequest = OpfBuilder.buildOpfRequest(opfRequestContext);
                    //4验证参数非空，长度等
                    errorResponse = OpfBuilder.Validation(opfRequestContext, opfRequest);
                    if (errorResponse != null) {
                        //输出响应
                        opfRequestContext.setOpfResponse(errorResponse);
                        writeResponse(errorResponse, response, opfRequestContext.getFormat());
                    } else {
                        //5进行其它检查业务数据合法性，业务安全等
                        errorResponse = securityManager.validateOther(opfRequestContext);
                        if (errorResponse != null) {
                            //输出响应
                            opfRequestContext.setOpfResponse(errorResponse);
                            writeResponse(errorResponse, response, opfRequestContext.getFormat());
                        } else {
                            firePreDoServiceEvent(opfRequestContext);

                            invoke(opfRequestContext, opfRequest);

                            writeResponse(opfRequestContext.getOpfResponse(), response, opfRequestContext.getFormat());
                        }
                    }
                }
            } catch (InvalidParameterException e) {
                if (opfRequestContext != null) {
                    String message = java.text.MessageFormat.format("service {0}#{1} call error", opfRequestContext.getMethod(), opfRequestContext.getVersion());
                    logger.error(message, e);
                    writeErrorResponse(response, opfRequestContext, MainErrorType.ISP_SERVICE_UNAVAILABLE, "参数异常:" + e);
                } else {
                    throw new OpfException("OpfRequestContext is null.", e);
                }
            } catch (Exception e) {
                if (opfRequestContext != null) {
                    String message = java.text.MessageFormat.format("service {0}#{1} call error", opfRequestContext.getMethod(), opfRequestContext.getVersion());
                    logger.error(message, e);
                    writeErrorResponse(response, opfRequestContext, e.toString());
                } else {
                    throw new OpfException("OpfRequestContext is null.", e);
                }
            } finally {
                if (opfRequestContext != null) {
                    opfRequestContext.setServiceEndTime(System.currentTimeMillis());
                    fireAfterDoServiceEvent(opfRequestContext);
                }
            }
        }

    }
}
