package com.srop.utils.XmlParser;

import java.util.List;

/**
 * Created by xyyz150 on 2016/1/14.
 */
public interface Reader {
    boolean hasReturnField(Object var1);

    Object getPrimitiveObject(Object var1);

    Object getObject(Object var1, Class<?> var2) throws Exception;

    List<?> getListObjects(Object var1, Object var2, Class<?> var3) throws Exception;
}
