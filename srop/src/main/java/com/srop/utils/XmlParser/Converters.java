package com.srop.utils.XmlParser;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xyyz150 on 2016/1/14.
 */
public class Converters {
    public static boolean isCheckJsonType = false;
    private static final Map<String, Field> fieldCache = new ConcurrentHashMap();

    private Converters() {
    }

    public static <T> T convert(Class<T> clazz, Reader reader) throws Exception {
        T rsp = null;

        try {
            rsp = clazz.newInstance();
            BeanInfo e = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = e.getPropertyDescriptors();
            PropertyDescriptor[] arr$ = pds;
            int len$ = pds.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                PropertyDescriptor pd = arr$[i$];
                Method method = pd.getWriteMethod();
                if (method != null) {
                    String itemName = pd.getName();
                    String listItemName = null;
                    if (pd.getName().equals("sropRequestContext")) {
                        continue;
                    }
                    Field field = getField(clazz, pd);

                    XStreamAlias jsonField = field.getAnnotation(XStreamAlias.class);
                    if (jsonField != null) {
                        itemName = jsonField.value();
                    }
                    ListItemAlias jsonlistItemField = field.getAnnotation(ListItemAlias.class);
                    if (jsonlistItemField != null) {
                        listItemName = jsonlistItemField.value();
                    }
                    if (reader.hasReturnField(itemName) || listItemName != null && reader.hasReturnField(listItemName)) {
                        Class typeClass = field.getType();
                        Object obj;
                        if (String.class.isAssignableFrom(typeClass)) {
                            obj = reader.getPrimitiveObject(itemName);
                            if (obj instanceof String) {
                                method.invoke(rsp, new Object[]{obj.toString()});
                            } else {
                                if (isCheckJsonType && obj != null) {
                                    throw new Exception(itemName + " is not a String");
                                }

                                if (obj != null) {
                                    method.invoke(rsp, new Object[]{obj.toString()});
                                } else {
                                    method.invoke(rsp, new Object[]{""});
                                }
                            }
                        } else if (Long.class.isAssignableFrom(typeClass)) {
                            obj = reader.getPrimitiveObject(itemName);
                            if (obj instanceof Long) {
                                method.invoke(rsp, new Object[]{(Long) obj});
                            } else {
                                if (isCheckJsonType && obj != null) {
                                    throw new Exception(itemName + " is not a Number(Long)");
                                }

                                if (StringUtils.isNumeric(obj)) {
                                    method.invoke(rsp, new Object[]{Long.valueOf(obj.toString())});
                                }
                            }
                        } else if (Integer.class.isAssignableFrom(typeClass)) {
                            obj = reader.getPrimitiveObject(itemName);
                            if (obj instanceof Integer) {
                                method.invoke(rsp, new Object[]{(Integer) obj});
                            } else {
                                if (isCheckJsonType && obj != null) {
                                    throw new Exception(itemName + " is not a Number(Integer)");
                                }

                                if (StringUtils.isNumeric(obj)) {
                                    method.invoke(rsp, new Object[]{Integer.valueOf(obj.toString())});
                                }
                            }
                        } else if (Boolean.class.isAssignableFrom(typeClass)) {
                            obj = reader.getPrimitiveObject(itemName);
                            if (obj instanceof Boolean) {
                                method.invoke(rsp, new Object[]{(Boolean) obj});
                            } else {
                                if (isCheckJsonType && obj != null) {
                                    throw new Exception(itemName + " is not a Boolean");
                                }

                                if (obj != null) {
                                    method.invoke(rsp, new Object[]{Boolean.valueOf(obj.toString())});
                                }
                            }
                        } else if (Double.class.isAssignableFrom(typeClass)) {
                            obj = reader.getPrimitiveObject(itemName);
                            if (obj instanceof Double) {
                                method.invoke(rsp, new Object[]{(Double) obj});
                            } else if (isCheckJsonType && obj != null) {
                                throw new Exception(itemName + " is not a Double");
                            }
                        } else if (Number.class.isAssignableFrom(typeClass)) {
                            obj = reader.getPrimitiveObject(itemName);
                            if (obj instanceof Number) {
                                method.invoke(rsp, new Object[]{(Number) obj});
                            } else if (isCheckJsonType && obj != null) {
                                throw new Exception(itemName + " is not a Number");
                            }
                        } else if (Date.class.isAssignableFrom(typeClass)) {
                            SimpleDateFormat var23 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            var23.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                            Object paramType = reader.getPrimitiveObject(itemName);
                            if (paramType instanceof String) {
                                method.invoke(rsp, new Object[]{var23.parse(paramType.toString())});
                            }
                        } else if (List.class.isAssignableFrom(typeClass)) {
                            Type var24 = field.getGenericType();
                            if (var24 instanceof ParameterizedType) {
                                ParameterizedType var22 = (ParameterizedType) var24;
                                Type[] genericTypes = var22.getActualTypeArguments();
                                if (genericTypes != null && genericTypes.length > 0 && genericTypes[0] instanceof Class) {
                                    Class subType = (Class) genericTypes[0];
                                    List listObjs = reader.getListObjects(itemName, listItemName, subType);
                                    if (listObjs != null) {
                                        method.invoke(rsp, new Object[]{listObjs});
                                    }
                                }
                            }
                        } else {
                            obj = reader.getObject(itemName, typeClass);
                            if (obj != null) {
                                method.invoke(rsp, new Object[]{obj});
                            }
                        }
                    }
                }
            }

            return rsp;
        } catch (Exception var21) {
            throw new Exception(var21);
        }
    }

    private static Field getField(Class<?> clazz, PropertyDescriptor pd) throws Exception {
        String key = clazz.getName() + "_" + pd.getName();
        Field field = (Field) fieldCache.get(key);
        if (field == null) {
            try {
                field = clazz.getDeclaredField(pd.getName());
            }catch (NoSuchFieldException ex){
                //兼容首字母大写字段
                field = clazz.getDeclaredField(StringUtils.firstToUp(pd.getName()));
            }
            fieldCache.put(key, field);
        }

        return field;
    }
}
