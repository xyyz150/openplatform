package com.srop.security;

import com.srop.session.Session;

/**
 * Created by xyyz150 on 2016/1/5.
 */
public class DefaultInvokeManager implements InvokeManager {
    public void caculateInvokeTimes(String appKey, Session session) {

    }

    public boolean isUserInvokeLimitExceed(String appKey, Session session) {
        return false;
    }

    public boolean isSessionInvokeLimitExceed(String appKey, String sessionId) {
        return false;
    }

    public boolean isAppInvokeLimitExceed(String appKey) {
        return false;
    }

    public boolean isAppInvokeFrequencyExceed(String appKey) {
        return false;
    }

    public void countTimes(String appKey) {

    }

    public boolean invokeLimit(String appKey) {
        return false;
    }
}
