package com.github.opf.security;

/**
 * Created by xyyz150
 */
public class DefaultAppSecretManager implements AppSecretManager {

    @Override
    public String getSecret(String appKey) {
        return null;
    }

    @Override
    public boolean isValidAppKey(String appKey) {
        return true;
    }
}
