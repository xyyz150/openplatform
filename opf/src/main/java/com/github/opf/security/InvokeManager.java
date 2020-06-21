package com.github.opf.security;

import com.github.opf.session.Session;

/**
 * Created by xyyz150
 */
public interface InvokeManager {
    /**
     * 计算应用、会话及用户服务调度总数
     *
     * @param appKey
     * @param session
     */
    void calcInvokeTimes(String appKey, Session session);

    /**
     * 用户服务访问次数是否超限
     *
     * @param session
     * @return
     */
    boolean isUserInvokeLimitExceed(String appKey, Session session);

    /**
     * 某个会话的服务访问次数是否超限
     *
     * @param sessionId
     * @return
     */
    boolean isSessionInvokeLimitExceed(String appKey, String sessionId);

    /**
     * 应用的服务访问次数是否超限
     *
     * @param appKey
     * @return
     */
    boolean isAppInvokeLimitExceed(String appKey);

    /**
     * 应用对服务的访问频率是否超限
     *
     * @param appKey
     * @return
     */
    boolean isAppInvokeFrequencyExceed(String appKey);

    /**
     * 计算应用、会话及用户服务调度总数
     *
     * @param appKey
     */
    void countTimes(String appKey);

    /**
     * 用户服务访问超限
     *
     * @return
     */
    boolean invokeLimit(String appKey);
}
