
package com.github.opf.annotation;

/**
 * 功能说明：是否需求会话检查
 * Created by xyyz150
 */
public enum NeedInSessionType {
    YES, NO, DEFAULT;

    public static boolean isNeedInSession(NeedInSessionType type) {
        if (YES == type || DEFAULT == type) {
            return true;
        } else {
            return false;
        }
    }
}

