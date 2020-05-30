package com.srop.annotation;

/**
 * Created by xyyz150 on 2016/1/6.
 */
public enum IgnoreSignType {

    YES, NO, DEFAULT;

    public static boolean isIgnoreSign(IgnoreSignType type) {
        if (NO == type || DEFAULT == type) {
            return false;
        } else {
            return true;
        }
    }
}
