package com.qimenapi;


import com.srop.security.AppSecretManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * @version 1.0
 */
public class SampleAppSecretManager implements AppSecretManager {


    public String getSecret(String appKey) {
        return "123456";
    }


    public boolean isValidAppKey(String appKey) {
        return true;
    }
}

