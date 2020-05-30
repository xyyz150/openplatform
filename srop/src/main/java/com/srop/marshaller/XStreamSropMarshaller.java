
package com.srop.marshaller;

import com.srop.exception.SropException;
import com.srop.utils.XStreamUtils;
import com.thoughtworks.xstream.XStream;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *    将对象流化成XML，每个类型对应一个{@link JAXBContext}，{@link JAXBContext} 是线程安全的，但是
 * {@link Marshaller}是非线程安全的，因此需要每次创建一个。
 * </pre>
 *
 * @version 1.0
 */
public class XStreamSropMarshaller implements SropMarshaller {

    public void marshaller(Object object, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            XStream xStream = XStreamUtils.getXstream(object.getClass());
            String xml = xStream.toXML(object);
            StringBuffer sb = new StringBuffer();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append(xml);
            out = response.getWriter();
            out.print(sb.toString());

        } catch (Exception e) {
            throw new SropException(e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public void marshaller(Object object, OutputStream outputStream) {
        try {
            XStream xStream = XStreamUtils.getXstream(object.getClass());
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            xStream.toXML(object, writer);
//            xStream.toXML(object, outputStream);
        } catch (Exception e) {
            throw new SropException(e);
        }
    }

}

