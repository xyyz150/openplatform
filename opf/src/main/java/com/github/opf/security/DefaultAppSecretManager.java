package com.github.opf.security;

/**
 * Created by xyyz150
 */
public class DefaultAppSecretManager implements AppSecretManager {
    public String getSecret(String appKey) {
        return null;
    }

    public boolean isValidAppKey(String appKey) {
        return true;
    }
}
