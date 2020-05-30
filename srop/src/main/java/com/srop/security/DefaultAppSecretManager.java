package com.srop.security;

/**
 * Created by xyyz150 on 2016/1/6.
 */
public class DefaultAppSecretManager implements AppSecretManager {
    public String getSecret(String appKey) {
        return null;
    }

    public boolean isValidAppKey(String appKey) {
        return false;
    }
}
