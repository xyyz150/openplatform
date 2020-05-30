package com.srop.utils.XmlParser;

import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xyyz150 on 2016/1/14.
 */
public class XmlConverter {
    public XmlConverter() {
    }

    public <T> T toResponse(String rsp, Class<T> clazz) throws Exception {
        Element root = XmlUtils.getRootElementFromString(rsp);
        return (T)this.getModelFromXML(root, clazz);
    }

    private <T> T getModelFromXML(final Element element, Class<T> clazz) throws Exception {
        return element == null?null:Converters.convert(clazz, new Reader() {
            public boolean hasReturnField(Object name) {
                Element childE = XmlUtils.getChildElement(element, (String)name);
                return childE != null;
            }

            public Object getPrimitiveObject(Object name) {
                return XmlUtils.getChildElementValue(element, (String)name);
            }

            public Object getObject(Object name, Class<?> type) throws Exception {
                Element childE = XmlUtils.getChildElement(element, (String)name);
                return childE != null?XmlConverter.this.getModelFromXML(childE, type):null;
            }

            public List<?> getListObjects(Object listName, Object itemName, Class<?> subType) throws Exception {
                ArrayList list = null;
                Element listE = XmlUtils.getChildElement(element, (String)listName);
                if(listE != null) {
                    list = new ArrayList();
                    List itemEs = XmlUtils.getChildElements(listE, (String)itemName);
                    Iterator i$ = itemEs.iterator();

                    while(i$.hasNext()) {
                        Element itemE = (Element)i$.next();
                        Object obj = null;
                        String value = XmlUtils.getElementValue(itemE);
                        if(String.class.isAssignableFrom(subType)) {
                            obj = value;
                        } else if(Long.class.isAssignableFrom(subType)) {
                            obj = Long.valueOf(value);
                        } else if(Integer.class.isAssignableFrom(subType)) {
                            obj = Integer.valueOf(value);
                        } else if(Boolean.class.isAssignableFrom(subType)) {
                            obj = Boolean.valueOf(value);
                        } else if(Date.class.isAssignableFrom(subType)) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            try {
                                obj = format.parse(value);
                            } catch (ParseException var13) {
                                throw new Exception(var13);
                            }
                        } else {
                            obj = XmlConverter.this.getModelFromXML(itemE, subType);
                        }

                        if(obj != null) {
                            list.add(obj);
                        }
                    }
                }

                return list;
            }
        });
    }
}
