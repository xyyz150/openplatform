package com.github.opf.autoConfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: xyyz150
 * Date: 2020/6/21 13:16
 * Description:
 */
@ConfigurationProperties(prefix = "opf")
public class OpfConfiguration {
    private String appKeyAlias;
    private String accessTokenAlias;
    private String methodAlias;
    private String versionAlias;
    private String formatAlias;
    private String signAlias;
    //单位秒
    private Integer serviceTimeoutSeconds;

    public String getAppKeyAlias() {
        return appKeyAlias;
    }

    public void setAppKeyAlias(String appKeyAlias) {
        this.appKeyAlias = appKeyAlias;
    }

    public String getAccessTokenAlias() {
        return accessTokenAlias;
    }

    public void setAccessTokenAlias(String accessTokenAlias) {
        this.accessTokenAlias = accessTokenAlias;
    }

    public String getMethodAlias() {
        return methodAlias;
    }

    public void setMethodAlias(String methodAlias) {
        this.methodAlias = methodAlias;
    }

    public String getVersionAlias() {
        return versionAlias;
    }

    public void setVersionAlias(String versionAlias) {
        this.versionAlias = versionAlias;
    }

    public String getFormatAlias() {
        return formatAlias;
    }

    public void setFormatAlias(String formatAlias) {
        this.formatAlias = formatAlias;
    }

    public String getSignAlias() {
        return signAlias;
    }

    public void setSignAlias(String signAlias) {
        this.signAlias = signAlias;
    }

    public Integer getServiceTimeoutSeconds() {
        return serviceTimeoutSeconds;
    }

    public void setServiceTimeoutSeconds(Integer serviceTimeoutSeconds) {
        this.serviceTimeoutSeconds = serviceTimeoutSeconds;
    }
}
