package com.qimenapi.util;

/**
 * Created by xyyz150 on 2016/9/13.
 */
public class ExceptionHelp {
    public static String getExceptionMsg(Exception e) {
        StringBuffer sb = new StringBuffer();
        String s = e.getClass().getName();
        String message = e.getLocalizedMessage();
        sb.append((message != null) ? (s + ": " + message) : s);
        Throwable cause = e.getCause();
        while (cause != null) {
            if (!cause.getClass().getName().equalsIgnoreCase(s)) {
                sb.append("\n原异常:").append(cause.toString());
                StackTraceElement[] stackTraces = cause.getStackTrace();
                if (stackTraces != null && stackTraces.length > 0) {
                    for (StackTraceElement stackTrace : stackTraces) {
                        sb.append("\n").append(stackTrace.toString());
                    }
                }
                cause = cause.getCause();
            } else {
                break;
            }
        }
        return sb.toString();
    }
}
