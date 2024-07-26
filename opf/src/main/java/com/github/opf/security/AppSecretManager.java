
package com.github.opf.security;

/**
 * 应用键管理器，可根据appKey获取对应的secret.
 * Created by xyyz150
 */
public interface AppSecretManager {

    /**
     * 获取应用程序的密钥
     *
     * @param appKey
     * @return
     */
    String getSecret(String appKey);

    /**
     * 是否是合法的appKey
     *
     * @param appKey
     * @return
     */
    boolean isValidAppKey(String appKey);
}

