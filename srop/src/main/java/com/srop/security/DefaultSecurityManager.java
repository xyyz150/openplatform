package com.srop.security;

import com.srop.*;
import com.srop.annotation.HttpAction;
import com.srop.config.SystemParameterNames;
import com.srop.request.SropRequestContext;
import com.srop.response.MainErrorType;
import com.srop.response.SropResponse;
import com.srop.router.SropBuilder;
import com.srop.session.DefaultSessionManager;
import com.srop.session.Session;
import com.srop.session.SessionManager;
import com.srop.utils.SropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 */
public class DefaultSecurityManager implements SecurityManager {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected InvokeManager invokeManager;

    protected AppSecretManager appSecretManager;

    protected SessionManager sessionManager;

    public DefaultSecurityManager() {
        invokeManager = new DefaultInvokeManager();
        appSecretManager = new DefaultAppSecretManager();
        sessionManager = new DefaultSessionManager();
    }


    public SropResponse validateSystemParameters(SropRequestContext context) {
        SropContext ropContext = context.getSropContext();
        SropResponse mainError = null;

        //1.检查appKey
        if (context.getAppKey() == null) {
            return SropBuilder.getSropResponse(false, MainErrorType.MISSING_APP_KEY, context.getMethod(), context.getVersion(), SystemParameterNames.getAppKey() + "不能为空");
        }
        if (!appSecretManager.isValidAppKey(context.getAppKey())) {
            return SropBuilder.getSropResponse(false, MainErrorType.INVALID_APP_KEY, context.getMethod(), context.getVersion(), SystemParameterNames.getAppKey() + "不合法");
        }


        //2.检查会话
        mainError = checkSession(context);
        if (mainError != null) {
            return mainError;
        }

        //3.检查method参数
        if (context.getMethod() == null) {
            return SropBuilder.getSropResponse(false, MainErrorType.MISSING_METHOD, context.getMethod(), context.getVersion(), SystemParameterNames.getMethod() + "不能为空");
        } else {
            if (!ropContext.isValidMethod(context.getMethod(), context.getVersion())) {
                return SropBuilder.getSropResponse(false, MainErrorType.INVALID_METHOD, context.getMethod(), context.getVersion(), String.format("方法%1$s不合法", context.getMethod()));
            }
        }

        //4.检查签名正确性
        mainError = checkSign(context);
        if (mainError != null) {
            return mainError;
        }

        //6.检查服务方法的版本是否已经过期
        if (context.getServiceMethodHandler().getServiceMethodDefinition().isObsoleted()) {
            return SropBuilder.getSropResponse(false, MainErrorType.METHOD_OBSOLETED, context.getMethod(), context.getVersion(), String.format("方法%1$s已过期", context.getMethod()));
        }

        //7.检查请求HTTP方法的匹配性
        mainError = validateHttpAction(context);
        if (mainError != null) {
            return mainError;
        }

        return null;
    }


    public SropResponse validateOther(SropRequestContext rrctx) {

        SropResponse mainError = null;

        //1.判断应用/会话/用户访问服务的次数或频度是否超限
        mainError = checkInvokeTimesLimit(rrctx);
        if (mainError != null) {
            return mainError;
        }

        return null;
    }

    public InvokeManager getInvokeManager() {
        return invokeManager;
    }

    public void setInvokeManager(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setAppSecretManager(AppSecretManager appSecretManager) {
        this.appSecretManager = appSecretManager;
    }


    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    private SropResponse checkInvokeTimesLimit(SropRequestContext rrctx) {
        if (invokeManager.isAppInvokeFrequencyExceed(rrctx.getAppKey())) {
            return SropBuilder.getSropResponse(false, MainErrorType.EXCEED_APP_INVOKE_FREQUENCY_LIMITED, rrctx.getMethod(), rrctx.getVersion(), "应用对服务的访问频率超限");
        } else if (invokeManager.isAppInvokeLimitExceed(rrctx.getAppKey())) {
            return SropBuilder.getSropResponse(false, MainErrorType.EXCEED_APP_INVOKE_LIMITED, rrctx.getMethod(), rrctx.getVersion(), "应用的服务访问次数超限");
        } else if (invokeManager.isUserInvokeLimitExceed(rrctx.getAppKey(), rrctx.getSession())) {
            return SropBuilder.getSropResponse(false, MainErrorType.EXCEED_USER_INVOKE_LIMITED, rrctx.getMethod(), rrctx.getVersion(), "用户服务访问次数超限");
        } else if (invokeManager.isSessionInvokeLimitExceed(rrctx.getAppKey(), rrctx.getSessionId())) {
            return SropBuilder.getSropResponse(false, MainErrorType.EXCEED_SESSION_INVOKE_LIMITED, rrctx.getMethod(), rrctx.getVersion(), "会话的服务访问次数超限");
        } else {
            return null;
        }
    }

    /**
     * 校验是否是合法的HTTP动作
     *
     * @param ropRequestContext
     */
    private SropResponse validateHttpAction(SropRequestContext ropRequestContext) {
        SropResponse mainError = null;
        HttpAction[] httpActions = ropRequestContext.getServiceMethodHandler().getServiceMethodDefinition().getHttpAction();
        if (httpActions.length > 0) {
            boolean isValid = false;
            for (HttpAction httpAction : httpActions) {
                if (httpAction == ropRequestContext.getHttpAction()) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                mainError = SropBuilder.getSropResponse(false, MainErrorType.HTTP_ACTION_NOT_ALLOWED, ropRequestContext.getMethod(), ropRequestContext.getVersion(), "httpaction不合法");
            }
        }
        return mainError;
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
    private SropResponse checkSign(SropRequestContext context) {

        if (!context.getServiceMethodHandler().getServiceMethodDefinition().isIgnoreSign()) {
            if (context.getSign() == null) {
                return SropBuilder.getSropResponse(false, MainErrorType.MISSING_SIGNATURE, context.getMethod(), context.getVersion(), "sign为空");
            } else {
                //查看密钥是否存在，不存在则说明appKey是非法的
                String signSecret = getAppSecretManager().getSecret(context.getAppKey());
                if (signSecret == null) {
                    return SropBuilder.getSropResponse(false, MainErrorType.UNAUTHORIZED_APP_KEY, context.getMethod(), context.getVersion(), String.format("无法获取%1$s对应的密钥", context.getAppKey()));
                }

                String signValue = SropUtils.sign(context.getAllParams(), context.getBody(), signSecret);
                if (!signValue.equals(context.getSign())) {
                    if (logger.isErrorEnabled()) {
                        logger.error(context.getAppKey() + "的签名不合法，请检查");
                    }
                    return SropBuilder.getSropResponse(false, MainErrorType.INVALID_SIGNATURE, context.getMethod(), context.getVersion(), String.format("%1$s的签名不合法，请检查", context.getAppKey()));
                } else {
                    return null;
                }
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(context.getMethod() + "忽略了签名");
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
    private SropResponse checkSession(SropRequestContext context) {
        //需要进行session检查
        if (context.getServiceMethodHandler() != null &&
                context.getServiceMethodHandler().getServiceMethodDefinition().isNeedInSession()) {
            if (context.getSessionId() == null) {
                return SropBuilder.getSropResponse(false, MainErrorType.MISSING_SESSIONKEY, context.getMethod(), context.getVersion(), "sessionkey为空");
            } else {
                Session session = sessionManager.getSession(context.getSessionId());
                if (session == null) {
                    logger.debug(context.getSessionId() + "会话不存在，请检查。");
                    return SropBuilder.getSropResponse(false, MainErrorType.INVALID_SESSIONKEY, context.getMethod(), context.getVersion(), context.getSessionId() + "会话不存在，请检查。");
                }
            }
        }
        return null;
    }

    private boolean isValidSession(SropRequestContext smc) {
        if (sessionManager.getSession(smc.getSessionId()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(smc.getSessionId() + "会话不存在，请检查。");
            }
            return false;
        } else {
            return true;
        }
    }

}

