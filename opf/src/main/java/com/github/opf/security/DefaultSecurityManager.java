package com.github.opf.security;

import com.github.opf.config.SystemParameterNames;
import com.github.opf.request.OpfRequestContext;
import com.github.opf.response.MainErrorType;
import com.github.opf.response.OpfResponse;
import com.github.opf.router.OpfBuilder;
import com.github.opf.session.DefaultSessionManager;
import com.github.opf.session.Session;
import com.github.opf.session.SessionManager;
import com.github.opf.utils.OpfUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

/**
 *
 */
public class DefaultSecurityManager implements com.github.opf.security.SecurityManager {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected InvokeManager invokeManager;

    protected AppSecretManager appSecretManager;

    protected SessionManager sessionManager;

    public DefaultSecurityManager() {
        invokeManager = new DefaultInvokeManager();
        appSecretManager = new DefaultAppSecretManager();
        sessionManager = new DefaultSessionManager();
    }

    @Override
    public OpfResponse validateSystemParameters(OpfRequestContext opfRequestContext) {
        //1.检查appKey
        if (StringUtils.isEmpty(opfRequestContext.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_APP_KEY, opfRequestContext.getMethod(), opfRequestContext.getVersion(), SystemParameterNames.getAppKey() + "不能为空");
        }
        if (!appSecretManager.isValidAppKey(opfRequestContext.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_APP_KEY, opfRequestContext.getMethod(), opfRequestContext.getVersion(), SystemParameterNames.getAppKey() + "不合法");
        }

        //2.检查method参数
        if (StringUtils.isEmpty(opfRequestContext.getMethod())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_METHOD, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                    SystemParameterNames.getMethod() + "不能为空");
        } else {
            if (opfRequestContext.getServiceMethodHandler() == null) {
                String message = java.text.MessageFormat.format("方法 {0}#{1} 不合法", opfRequestContext.getMethod(), opfRequestContext.getVersion());
                return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_METHOD, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                        message);
            }
        }

        //3.检查服务方法的版本是否已经过期
        if (opfRequestContext.getServiceMethodHandler().getServiceMethodDefinition().isDeprecated()) {
            String message = java.text.MessageFormat.format("方法 {0}#{1} 已过期", opfRequestContext.getMethod(), opfRequestContext.getVersion());
            return OpfBuilder.getOpfResponse(false, MainErrorType.METHOD_DEPRECATED, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                    message);
        }

        //4.检查timestamp是否已经过期
        try {
            if (StringUtils.isEmpty(opfRequestContext.getTimestamp())) {
                return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_TIMESTAMP, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                        SystemParameterNames.getTimestamp() + "不能为空");
            }
            Date timestamp = DateUtils.parseDate(opfRequestContext.getTimestamp(), "yyyy-MM-dd HH:mm:ss");
            if (!OpfUtils.checkTimestamp(timestamp)) {
                String message = java.text.MessageFormat.format("timestamp:{0}签名验证已过期", opfRequestContext.getTimestamp());
                return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_TIMESTAMP, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                        message);
            }
        } catch (ParseException e) {
            String message = java.text.MessageFormat.format("timestamp:{0}格式必须为{1}", opfRequestContext.getTimestamp(), "yyyy-MM-dd HH:mm:ss");
            return OpfBuilder.getOpfResponse(false, MainErrorType.METHOD_DEPRECATED, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                    message);
        }

        //5.检查签名正确性
        OpfResponse errorResponse = checkSign(opfRequestContext);
        if (errorResponse != null) {
            return errorResponse;
        }

        //6.检查会话
        errorResponse = checkSession(opfRequestContext);
        if (errorResponse != null) {
            return errorResponse;
        }

        return null;
    }

    @Override
    public OpfResponse validateOther(OpfRequestContext opfRequestContext) {
        //1.判断应用/会话/用户访问服务的次数或频度是否超限
        OpfResponse mainError = checkInvokeTimesLimit(opfRequestContext);
        if (mainError != null) {
            return mainError;
        }

        return null;
    }

    public InvokeManager getInvokeManager() {
        return invokeManager;
    }

    @Override
    public void setInvokeManager(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public void setAppSecretManager(AppSecretManager appSecretManager) {
        this.appSecretManager = appSecretManager;
    }

    @Override
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private OpfResponse checkInvokeTimesLimit(OpfRequestContext opfCtx) {
        if (invokeManager.isAppInvokeFrequencyExceed(opfCtx.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_APP_INVOKE_FREQUENCY_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "应用对服务的访问频率超限");
        } else if (invokeManager.isAppInvokeLimitExceed(opfCtx.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_APP_INVOKE_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "应用的服务访问次数超限");
        } else if (invokeManager.isSessionInvokeLimitExceed(opfCtx.getAppKey(), opfCtx.getSessionId())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_SESSION_INVOKE_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "会话的服务访问次数超限");
        } else {
            return null;
        }
    }

    public AppSecretManager getAppSecretManager() {
        return appSecretManager;
    }

    /**
     * 检查签名的有效性
     *
     * @param context
     * @return
     */
    private OpfResponse checkSign(OpfRequestContext context) {
        if (!context.getServiceMethodHandler().getServiceMethodDefinition().isIgnoreSign()) {
            if (StringUtils.isEmpty(context.getSign())) {
                return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_SIGNATURE, context.getMethod(), context.getVersion(), "sign为空");
            } else {
                //查看密钥是否存在，不存在则说明appKey是非法的
                String appSecret = getAppSecretManager().getSecret(context.getAppKey());
                if (StringUtils.isEmpty(appSecret)) {
                    String message = java.text.MessageFormat.format("无法获取{0}对应的密钥", context.getAppKey());
                    return OpfBuilder.getOpfResponse(false, MainErrorType.UNAUTHORIZED_APP_KEY, context.getMethod(),
                            context.getVersion(), message);
                }
                try {
                    String signValue = OpfUtils.sign(context.getAllParams(), context.getBody(), appSecret);
                    if (!signValue.equals(context.getSign())) {
                        if (logger.isInfoEnabled()) {
                            logger.info(context.getAppKey() + "的签名不合法，请检查");
                        }
                        String message = java.text.MessageFormat.format("{0}的签名不合法，请检查", context.getAppKey());
                        return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_SIGNATURE, context.getMethod(),
                                context.getVersion(), message);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    String message = java.text.MessageFormat.format("{0}的签名不合法，请检查", context.getAppKey());
                    return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_SIGNATURE, context.getMethod(),
                            context.getVersion(), message);
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(context.getMethod() + "忽略了签名");
            }
            return null;
        }
    }

    /**
     * 是否是合法的会话
     *
     * @param context
     * @return
     */
    private OpfResponse checkSession(OpfRequestContext context) {
        //需要进行session检查
        if (context.getServiceMethodHandler() != null &&
                context.getServiceMethodHandler().getServiceMethodDefinition().isNeedInSession()) {
            if (StringUtils.isEmpty(context.getSessionId())) {
                return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_SESSION, context.getMethod(), context.getVersion(), "session为空");
            } else {
                Session session = sessionManager.getSession(context.getSessionId());
                if (session == null) {
                    logger.info(context.getSessionId() + "会话不存在，请检查。");
                    return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_SESSION, context.getMethod(),
                            context.getVersion(), context.getSessionId() + "会话不存在，请检查。");
                } else {
                    context.setSession(session);
                }
            }
        }
        return null;
    }
}

