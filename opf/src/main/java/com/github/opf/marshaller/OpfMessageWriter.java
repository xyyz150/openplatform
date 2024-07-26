
package com.github.opf.marshaller;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 负责将请求方法返回的响应对应流化为相应格式的内容。
 */
public interface OpfMessageWriter {
    void write(Object object, HttpServletResponse response);

    void write(Object object, OutputStream outputStream);
}

