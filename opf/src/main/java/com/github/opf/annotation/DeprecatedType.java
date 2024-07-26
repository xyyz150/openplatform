
package com.github.opf.annotation;

/**
 * 服务方法是否已经过期，过期的服务方法不能再访问
 * Created by xyyz150
 */
public enum DeprecatedType {
    YES, NO, DEFAULT;

     public static boolean isDeprecated(DeprecatedType type) {
         if (YES == type ) {
             return true;
         } else {
             return false;
         }
     }
}

