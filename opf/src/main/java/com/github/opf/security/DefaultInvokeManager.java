package com.github.opf.security;

import com.github.opf.session.Session;

/**
 * Created by xyyz150
 */
public class DefaultInvokeManager implements InvokeManager {

    @Override
    public boolean isSessionInvokeLimitExceed(String appKey, String sessionId) {
        return false;
    }

    @Override
    public boolean isAppInvokeLimitExceed(String appKey) {
        return false;
    }

    @Override
    public boolean isAppInvokeFrequencyExceed(String appKey) {
        return false;
    }

}
