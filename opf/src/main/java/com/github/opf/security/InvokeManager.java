package com.github.opf.security;

/**
 * Created by xyyz150
 */
public interface InvokeManager {

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
}
