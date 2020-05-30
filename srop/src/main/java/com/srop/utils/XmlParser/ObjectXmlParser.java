package com.srop.utils.XmlParser;

/**
 * Created by xyyz150 on 2016/1/14.
 */
public class ObjectXmlParser<T> {
    private Class<T> clazz;

    public ObjectXmlParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws Exception {
        XmlConverter converter = new XmlConverter();
        return converter.toResponse(rsp, this.clazz);
    }

    public Class<T> getResponseClass() {
        return this.clazz;
    }
}
