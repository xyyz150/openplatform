
package com.github.opf.marshaller;

import com.alibaba.fastjson.JSON;
import com.github.opf.exception.OpfException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 将响应对象流化成JSON。
 */
public class JsonOpfMessageWriter implements OpfMessageWriter {

    @Override
    public void write(Object object, HttpServletResponse response) {
        try {
            JSON.writeJSONString(response.getOutputStream(), object);
        } catch (IOException e) {
            throw new OpfException(e);
        }
    }
    @Override
    public void write(Object object, OutputStream outputStream) {
        try {
            JSON.writeJSONString(outputStream, object);
        } catch (IOException e) {
            throw new OpfException(e);
        }
    }
}

