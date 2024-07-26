
package com.github.opf.marshaller;

import com.github.opf.Constants;
import com.github.opf.MessageFormat;
import com.github.opf.exception.OpfException;
import com.github.opf.request.OpfRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 对请求响应的对象转成相应的报文。
 */
public class MessageWriteUtils {

    protected static final Logger logger = LoggerFactory.getLogger(MessageWriteUtils.class);

    private static OpfMessageWriter jsonOpfMarshaller = new JsonOpfMessageWriter();

    private static OpfMessageWriter xmlOpfResponseMarshaller = new XStreamOpfMessageWriter();


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
     * 将{@link OpfRequest}转换为字符串
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
                jsonOpfMarshaller.write(object, bos);
            } else {
                xmlOpfResponseMarshaller.write(object, bos);
            }
            return bos.toString(Constants.UTF8);
        } catch (Throwable e) {
            throw new OpfException(e);
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
    public static void write(MessageFormat format, Object object, OutputStream outputStream) {
        try {
            if (format == MessageFormat.json) {
                jsonOpfMarshaller.write(object, outputStream);
            } else {
                xmlOpfResponseMarshaller.write(object, outputStream);
            }
        } catch (Exception e) {
            throw new OpfException(e);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                logger.error("关闭响应出错", e);
            }
        }
    }

    public static void write(MessageFormat format, Object object, HttpServletResponse response) {
        try {
            if (format == MessageFormat.json) {
                jsonOpfMarshaller.write(object, response);
            } else {
                xmlOpfResponseMarshaller.write(object, response);
            }
        } catch (Exception e) {
            throw new OpfException(e);
        }
    }

}

