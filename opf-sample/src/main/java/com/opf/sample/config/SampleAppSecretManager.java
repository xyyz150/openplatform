package com.opf.sample.config;

import com.github.opf.security.AppSecretManager;
import org.springframework.stereotype.Component;

/**
 * Author: xyyz150
 * Date: 2020/6/21 17:57
 * Description:
 */
@Component
public class SampleAppSecretManager  implements AppSecretManager {
    public String getSecret(String appKey) {
        return "123456";
    }


    public boolean isValidAppKey(String appKey) {
        return true;
    }
}
