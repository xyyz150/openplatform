package com.github.opf.security;

import com.github.opf.OpfContext;
import com.github.opf.annotation.HttpAction;
import com.github.opf.config.SystemParameterNames;
import com.github.opf.request.OpfRequestContext;
import com.github.opf.response.MainErrorType;
import com.github.opf.response.OpfResponse;
import com.github.opf.router.OpfBuilder;
import com.github.opf.session.DefaultSessionManager;
import com.github.opf.session.Session;
import com.github.opf.session.SessionManager;
import com.github.opf.utils.OpfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
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


    public OpfResponse validateSystemParameters(OpfRequestContext opfRequestContext) {
        OpfContext opfContext = opfRequestContext.getOpfContext();
        OpfResponse mainError = null;

        //1.检查appKey
        if (opfRequestContext.getAppKey() == null) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_APP_KEY, opfRequestContext.getMethod(), opfRequestContext.getVersion(), SystemParameterNames.getAppKey() + "不能为空");
        }
        if (!appSecretManager.isValidAppKey(opfRequestContext.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_APP_KEY, opfRequestContext.getMethod(), opfRequestContext.getVersion(), SystemParameterNames.getAppKey() + "不合法");
        }


        //2.检查会话
        mainError = checkSession(opfRequestContext);
        if (mainError != null) {
            return mainError;
        }

        //3.检查method参数
        if (opfRequestContext.getMethod() == null) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_METHOD, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                    SystemParameterNames.getMethod() + "不能为空");
        } else {
            if (!opfContext.isValidMethod(opfRequestContext.getMethod(), opfRequestContext.getVersion())) {
                return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_METHOD, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                        String.format("方法%1$s不合法", opfRequestContext.getMethod()));
            }
        }

        //4.检查签名正确性
        mainError = checkSign(opfRequestContext);
        if (mainError != null) {
            return mainError;
        }

        //6.检查服务方法的版本是否已经过期
        if (opfRequestContext.getServiceMethodHandler().getServiceMethodDefinition().isObsoleted()) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.METHOD_OBSOLETED, opfRequestContext.getMethod(), opfRequestContext.getVersion(),
                    String.format("方法%1$s已过期", opfRequestContext.getMethod()));
        }

        //7.检查请求HTTP方法的匹配性
        mainError = validateHttpAction(opfRequestContext);
        if (mainError != null) {
            return mainError;
        }

        return null;
    }


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


    private OpfResponse checkInvokeTimesLimit(OpfRequestContext opfCtx) {
        if (invokeManager.isAppInvokeFrequencyExceed(opfCtx.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_APP_INVOKE_FREQUENCY_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "应用对服务的访问频率超限");
        } else if (invokeManager.isAppInvokeLimitExceed(opfCtx.getAppKey())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_APP_INVOKE_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "应用的服务访问次数超限");
        } else if (invokeManager.isUserInvokeLimitExceed(opfCtx.getAppKey(), opfCtx.getSession())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_USER_INVOKE_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "用户服务访问次数超限");
        } else if (invokeManager.isSessionInvokeLimitExceed(opfCtx.getAppKey(), opfCtx.getAccessToken())) {
            return OpfBuilder.getOpfResponse(false, MainErrorType.EXCEED_SESSION_INVOKE_LIMITED, opfCtx.getMethod(),
                    opfCtx.getVersion(), "会话的服务访问次数超限");
        } else {
            return null;
        }
    }

    /**
     * 校验是否是合法的HTTP动作
     *
     * @param opfRequestContext
     */
    private OpfResponse validateHttpAction(OpfRequestContext opfRequestContext) {
        OpfResponse mainError = null;
        HttpAction[] httpActions = opfRequestContext.getServiceMethodHandler().getServiceMethodDefinition().getHttpAction();
        if (httpActions.length > 0) {
            boolean isValid = false;
            for (HttpAction httpAction : httpActions) {
                if (httpAction == opfRequestContext.getHttpAction()) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                mainError = OpfBuilder.getOpfResponse(false, MainErrorType.HTTP_ACTION_NOT_ALLOWED, opfRequestContext.getMethod(),
                        opfRequestContext.getVersion(), "httpAction不合法");
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
    private OpfResponse checkSign(OpfRequestContext context) {

        if (!context.getServiceMethodHandler().getServiceMethodDefinition().isIgnoreSign()) {
            if (context.getSign() == null) {
                return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_SIGNATURE, context.getMethod(), context.getVersion(), "sign为空");
            } else {
                //查看密钥是否存在，不存在则说明appKey是非法的
                String signSecret = getAppSecretManager().getSecret(context.getAppKey());
                if (signSecret == null) {
                    return OpfBuilder.getOpfResponse(false, MainErrorType.UNAUTHORIZED_APP_KEY, context.getMethod(),
                            context.getVersion(), String.format("无法获取%1$s对应的密钥", context.getAppKey()));
                }

                String signValue = OpfUtils.sign(context.getAllParams(), context.getBody(), signSecret);
                if (!signValue.equals(context.getSign())) {
                    if (logger.isErrorEnabled()) {
                        logger.error(context.getAppKey() + "的签名不合法，请检查");
                    }
                    return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_SIGNATURE, context.getMethod(),
                            context.getVersion(), String.format("%1$s的签名不合法，请检查", context.getAppKey()));
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
    private OpfResponse checkSession(OpfRequestContext context) {
        //需要进行session检查
        if (context.getServiceMethodHandler() != null &&
                context.getServiceMethodHandler().getServiceMethodDefinition().isNeedInSession()) {
            if (context.getAccessToken() == null) {
                return OpfBuilder.getOpfResponse(false, MainErrorType.MISSING_ACCESS_TOKEN, context.getMethod(), context.getVersion(), "accessToken为空");
            } else {
                Session session = sessionManager.getSession(context.getAccessToken());
                if (session == null) {
                    logger.debug(context.getAccessToken() + "会话不存在，请检查。");
                    return OpfBuilder.getOpfResponse(false, MainErrorType.INVALID_ACCESS_TOKEN, context.getMethod(),
                            context.getVersion(), context.getAccessToken() + "会话不存在，请检查。");
                }
            }
        }
        return null;
    }
}

