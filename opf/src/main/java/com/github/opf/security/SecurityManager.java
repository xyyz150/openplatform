
package com.github.opf.security;

import com.github.opf.request.OpfRequestContext;
import com.github.opf.response.OpfResponse;
import com.github.opf.session.SessionManager;

/**
 * <pre>
 *   负责对请求数据、会话、业务安全、应用密钥安全进行检查并返回相应的错误
 * </pre>
 *
 * @version 1.0
 */
public interface SecurityManager {

    /**
     * 对请求服务的上下文进行检查校验
     *
     * @param opfRequestContext
     * @return
     */
    OpfResponse validateSystemParameters(OpfRequestContext opfRequestContext);

    /**
     * 验证其它的事项：包括业务参数，业务安全性，会话安全等
     *
     * @param opfRequestContext
     * @return
     */
    OpfResponse validateOther(OpfRequestContext opfRequestContext);

    /**
     * 获取应用密钥管理器
     *
     * @return
     */
    void setAppSecretManager(AppSecretManager appSecretManager);

    /**
     * 设置会话管理器，以验证会话的合法性
     *
     * @return
     */
    void setSessionManager(SessionManager sessionManager);


    /**
     * 设置服务调度次数管理器
     */
    void setInvokeManager(InvokeManager invokeManager);

}

