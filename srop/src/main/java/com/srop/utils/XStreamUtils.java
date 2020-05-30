package com.srop.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by xyyz150 on 2016/1/6.
 */
public class XStreamUtils {
    private static Logger logger = LoggerFactory.getLogger(XStreamUtils.class);

    private static Map<Class<?>, XStream> xstreamMap = new WeakHashMap<Class<?>, XStream>();


    private static NameCoder nameCoder = new NameCoder() {
        public String encodeNode(String arg0) {
            return arg0;
        }

        public String encodeAttribute(String arg0) {
            return arg0;
        }

        public String decodeNode(String arg0) {
            return arg0;
        }

        public String decodeAttribute(String arg0) {
            return arg0;
        }
    };

    private static MapperWrapper createSkipOverElementMapperWrapper(Mapper mapper) {
        MapperWrapper resMapper = new MapperWrapper(mapper) {
            @SuppressWarnings("rawtypes")
            @Override
            public Class realClass(String elementName) {
                Class res = null;
                try {
                    res = super.realClass(elementName);
                } catch (CannotResolveClassException e) {
                    logger.warn(String.format("xstream change xml to object. filed %s not exsit. ", elementName));
                }
                return res;
            }
        };
        return resMapper;
    }


    public static XStream getXstream(Class<?> classType) {
        return getXstream(classType, true);
    }


    public static XStream getXstream(Class<?> classType, boolean isSkipOverElement) {
        if (xstreamMap.containsKey(classType)) {
            return xstreamMap.get(classType);
        }
        XStream res = null;
        if (isSkipOverElement) {
            res = new XStream(new Xpp3DomDriver(nameCoder)) {
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return createSkipOverElementMapperWrapper(next);
                }
            };
        } else {
            res = new XStream(new Xpp3DomDriver(nameCoder));
        }
        logger.info(String.format("create xstream by %s , parameter %s", classType.getName(), isSkipOverElement));
        res.autodetectAnnotations(true);
        res.processAnnotations(classType);
        xstreamMap.put(classType, res);
        return res;
    }

    public static String parseObj2Xml(Object obj){
        XStream xStream = getXstream(obj.getClass());
        try{
            String xml = xStream.toXML(obj);
            return xml;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
