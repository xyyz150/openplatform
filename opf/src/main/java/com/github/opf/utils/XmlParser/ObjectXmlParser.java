package com.github.opf.utils.XmlParser;

/**
 * Created by xyyz150
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
