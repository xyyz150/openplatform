package com.qimenapi.service;

import com.qimenapi.entity.RequestPostParam;
import com.qimenapi.entity.RequestPostResult;
import com.qimenapi.entity.ServiceMethodInfo;
import com.qimenapi.spring.SpringUtils;
import com.qimenapi.util.HttpHelper;
import com.srop.Constants;
import com.srop.annotation.ServiceMethod;
import com.srop.annotation.ServiceMethodBean;
import com.srop.utils.SropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xyyz150 on 2016/9/13.
 */
@Service
public class ToolService {
    static String API_NAME_LIST_KEY = "API_NAME_LIST_KEY";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CacheManager cacheManager;

    public Cache getCache() {
        return cacheManager.getCache("basedata-cache");
    }

    public void InitApiList() {
        final List<String> apiNameList = new ArrayList<String>();
        try {
            ApplicationContext context = SpringUtils.getApplicationContext();
            String[] beanNames = context.getBeanNamesForType(Object.class);
            for (final String beanName : beanNames) {
                Class<?> handlerType = context.getType(beanName);
                //只对标注 ServiceMethodBean的Bean进行扫描
                if (AnnotationUtils.findAnnotation(handlerType, ServiceMethodBean.class) != null) {
                    ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() {
                                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                                    ReflectionUtils.makeAccessible(method);

                                    ServiceMethod serviceMethod = AnnotationUtils.findAnnotation(method, ServiceMethod.class);
                                    ServiceMethodBean serviceMethodBean = AnnotationUtils.findAnnotation(method.getDeclaringClass(), ServiceMethodBean.class);
                                    //方法注解上的定义
                                    String name = serviceMethod.method();
                                    String version = serviceMethod.version();
                                    if (version == null || version.isEmpty()) {
                                        version = serviceMethodBean.version();
                                    }
                                    String key = name + "(" + version + ")";
                                    apiNameList.add(key);
                                    ServiceMethodInfo info = new ServiceMethodInfo();

                                    info.setVersion(version);
                                    getCache().put(key, info);
                                }
                            },
                            new ReflectionUtils.MethodFilter() {
                                public boolean matches(Method method) {
                                    return !method.isSynthetic() && AnnotationUtils.findAnnotation(method, ServiceMethod.class) != null;
                                }
                            }
                    );
                }
            }
            getCache().put(API_NAME_LIST_KEY, apiNameList);
        } catch (Exception e) {
            logger.error("调试工具初始化异常:{}", e.toString());
        }
    }

    public List<String> getApiList() {
        Cache.ValueWrapper valueWrapper = getCache().get(API_NAME_LIST_KEY);
        if (valueWrapper == null || valueWrapper.get() == null) {
            InitApiList();
            return (List<String>) getCache().get(API_NAME_LIST_KEY).get();
        } else {
            return (List<String>) valueWrapper.get();
        }

    }

    public ServiceMethodInfo getApiInfo(String name) {
        Cache.ValueWrapper valueWrapper = getCache().get(name);
        if (valueWrapper == null) {
            logger.error("缓存失效：" + name);
            ServiceMethodInfo info = new ServiceMethodInfo();
            int start = name.indexOf('(');
            if (start > -1) {
                String version = name.substring(start + 1, name.length() - 1);
                info.setVersion(version);

                getCache().put(name, info);

                return info;
            }
            info.setVersion("1.0");
            return info;
        }
        ServiceMethodInfo info = (ServiceMethodInfo) valueWrapper.get();
        if (info == null) {
            info = new ServiceMethodInfo();
            info.setVersion("1.0");
        }
        return info;
    }

    public RequestPostResult postParam(RequestPostParam postParam) {
        RequestPostResult result = new RequestPostResult();
        try {
            Map<String, String> map = new HashMap<String, String>();
            String method = postParam.getMethod();
            if (method.indexOf("(") > -1) {
                method = method.substring(0, method.indexOf("("));
            }
            map.put("method", method);
            map.put("format", postParam.getFormat());
            map.put("appKey", postParam.getApp_key());
            map.put("v", (postParam.getV()!=null&&postParam.getV().length()>0)?postParam.getV():"2.0");
            map.put("accessToken", postParam.getSessionkey());
            String sign = SropUtils.sign(map, postParam.getBody(), postParam.getApp_serect());
            map.put("sign", sign);
            String url = postParam.getUrl() + HttpHelper.buildQuery(map, Constants.UTF8);
            result.setRequest(url);
            String str = HttpHelper.doPost(url, postParam.getBody());
            result.setResponse(str);
        } catch (Exception e) {
            result.setResponse("程序异常");
            logger.error("测试工具post数据异常：{0}", e.toString());
        }
        return result;
    }
}
