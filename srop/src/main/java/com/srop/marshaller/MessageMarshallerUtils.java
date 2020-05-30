
package com.srop.marshaller;

import com.srop.MessageFormat;
import com.srop.exception.SropException;
import com.srop.request.SropRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Map;

/**
 * <pre>
 *   对请求响应的对象转成相应的报文。
 * </pre>
 *
 * @version 1.0
 */
public class MessageMarshallerUtils {

    protected static final Logger logger = LoggerFactory.getLogger(MessageMarshallerUtils.class);

    private static final String UTF_8 = "utf-8";

    private static SropMarshaller jacksonJsonSropMarshaller = new JacksonJsonSropMarshaller();

    private static SropMarshaller xmlSropResponseMarshaller = new XStreamSropMarshaller();


    /**
     * 将请求对象转换为String
     *
     * @param request
     * @param format
     * @return
     */
//    public static String getMessage(SropRequest request, MessageFormat format) {
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
//            if (format == MessageFormat.json) {
//                if (request.getSropRequestContext() != null) {
//                    jacksonJsonSropMarshaller.marshaller(request.getSropRequestContext().getAllParams(), bos);
//                } else {
//                    return "";
//                }
//            } else {
//                if (request.getSropRequestContext() != null) {
//                    xmlSropResponseMarshaller.marshaller(request.getSropRequestContext().getAllParams(), bos);
//                } else {
//                    return "";
//                }
//            }
//            return bos.toString(UTF_8);
//        } catch (Exception e) {
//            throw new SropException(e);
//        }
//    }

    /**
     * 将请求对象转换为String
     *
     * @param allParams
     * @return
     */
    public static String asUrlString(Map<String, String> allParams) {
        StringBuilder sb = new StringBuilder(256);
        boolean first = true;
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (!first) {
                sb.append("&");
            }
            first = false;
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }


    /**
     * 将{@link SropRequest}转换为字符串
     *
     * @param object
     * @param format
     * @return
     */
    public static String getMessage(Object object, MessageFormat format) {
        if (object == null) {
            return "NONE MSG";
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        try {
            if (format == MessageFormat.json) {
                jacksonJsonSropMarshaller.marshaller(object, bos);
            } else {
                xmlSropResponseMarshaller.marshaller(object, bos);
            }
            return bos.toString(UTF_8);
        } catch (Throwable e) {
            throw new SropException(e);
        } finally {
            try {
                bos.close();
            } catch (IOException ee) {
                logger.error("消息转换异常", ee);
            }
        }
    }

    /**
     * 输出
     *
     * @param format
     * @param object
     * @param outputStream
     */
    public static void marshaller(MessageFormat format, Object object, OutputStream outputStream) {
        try {
            if (format == MessageFormat.json) {
                jacksonJsonSropMarshaller.marshaller(object, outputStream);
            } else {
                xmlSropResponseMarshaller.marshaller(object, outputStream);
            }
        } catch (Exception e) {
            throw new SropException(e);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                logger.error("关闭响应出错", e);
            }
        }
    }

    public static void marshaller(MessageFormat format, Object object, HttpServletResponse response) {
        try {
            if (format == MessageFormat.json) {
                jacksonJsonSropMarshaller.marshaller(object, response);
            } else {
                xmlSropResponseMarshaller.marshaller(object, response);
            }
        } catch (Exception e) {
            throw new SropException(e);
        }
    }

}

