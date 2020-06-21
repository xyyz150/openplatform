
package com.github.opf.marshaller;

import com.alibaba.fastjson.JSON;
import com.github.opf.exception.OpfException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <pre>
 *    将响应对象流化成JSON。
 * </pre>
 *
 * @version 1.0
 */
public class JsonOpfMarshaller implements OpfMarshaller {

    public void marshaller(Object object, HttpServletResponse response) {
        try {
            JSON.writeJSONString(response.getOutputStream(), object);
        } catch (IOException e) {
            throw new OpfException(e);
        }
    }

    public void marshaller(Object object, OutputStream outputStream) {
        try {
            JSON.writeJSONString(outputStream, object);
        } catch (IOException e) {
            throw new OpfException(e);
        }
    }
}

