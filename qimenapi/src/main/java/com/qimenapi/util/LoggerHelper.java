package com.qimenapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xyyz150 on 2016/9/6.
 */
public class LoggerHelper {
    protected static Logger logger = LoggerFactory.getLogger(LoggerHelper.class);

    public static void printStack(Exception e) {
        StringBuffer sb = new StringBuffer();
        sb.append("错误信息：\n").append(e.toString()).append("\n");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append(stackTraceElement.toString()).append("\n");
        }
        logger.error(sb.toString());
    }
}
