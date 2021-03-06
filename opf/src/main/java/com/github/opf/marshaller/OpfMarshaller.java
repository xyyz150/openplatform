
package com.github.opf.marshaller;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * <pre>
 *   负责将请求方法返回的响应对应流化为相应格式的内容。
 * </pre>
 *
 * @version 1.0
 */
public interface OpfMarshaller {
    void marshaller(Object object, HttpServletResponse response);

    void marshaller(Object object, OutputStream outputStream);
}

