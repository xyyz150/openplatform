
package com.srop.marshaller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srop.exception.SropException;
import com.srop.utils.ObjectMapperUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <pre>
 *    将响应对象流化成JSON。 {@link ObjectMapper}是线程安全的。
 * </pre>
 *
 * @version 1.0
 */
public class JacksonJsonSropMarshaller implements SropMarshaller {

    private ObjectMapper objectMapper;

    public void marshaller(Object object, HttpServletResponse response) {
        try {
            if (this.objectMapper == null) {
                this.objectMapper = ObjectMapperUtils.InitObjectMapper();
            }
            JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
            objectMapper.writeValue(jsonGenerator, object);
        } catch (IOException e) {
            throw new SropException(e);
        }
    }

    public void marshaller(Object object, OutputStream outputStream) {
        try {
            if (this.objectMapper == null) {
                this.objectMapper = ObjectMapperUtils.InitObjectMapper();
            }
            JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(outputStream,JsonEncoding.UTF8);
            objectMapper.writeValue(jsonGenerator, object);
        } catch (IOException e) {
            throw new SropException(e);
        }
    }
}

