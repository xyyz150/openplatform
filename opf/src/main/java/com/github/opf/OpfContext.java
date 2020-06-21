package com.github.opf;

import com.github.opf.annotation.*;
import com.github.opf.exception.OpfException;
import com.github.opf.handler.ServiceMethodDefinition;
import com.github.opf.handler.ServiceMethodHandler;
import com.github.opf.request.OpfRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyyz150
 */
public class OpfContext {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, ServiceMethodHandler> serviceHandlerMap = new HashMap<String, ServiceMethodHandler>();

    public OpfContext(final ApplicationContext context) {
        if (logger.isDebugEnabled()) {
            logger.debug("对Spring上下文中的Bean进行扫描，查找opf服务方法: " + context);
        }
        String[] beanNames = context.getBeanNamesForType(Object.class);
        for (final String beanName : beanNames) {
            Class<?> handlerType = context.getType(beanName);
            //只对标注 ServiceMethodBean的Bean进行扫描
            if (AnnotationUtils.findAnnotation(handlerType, ServiceMethodBean.class) != null) {
                ReflectionUtils.doWithMethods(handlerType, method -> {
                            ReflectionUtils.makeAccessible(method);

                            ServiceMethod serviceMethod = AnnotationUtils.findAnnotation(method, ServiceMethod.class);
                            ServiceMethodBean serviceMethodBean = AnnotationUtils.findAnnotation(method.getDeclaringClass(), ServiceMethodBean.class);
                            //方法注解上的定义
                            ServiceMethodDefinition definition = null;
                            if (serviceMethodBean != null) {
                                definition = buildServiceMethodDefinition(serviceMethodBean, serviceMethod);
                            } else {
                                definition = buildServiceMethodDefinition(serviceMethod);
                            }
                            //处理方法的类
                            ServiceMethodHandler serviceMethodHandler = new ServiceMethodHandler();

                            serviceMethodHandler.setServiceMethodDefinition(definition);
                            //1.set handler
                            serviceMethodHandler.setHandler(context.getBean(beanName)); //handler
                            serviceMethodHandler.setHandlerMethod(method); //handler'method


                            if (method.getParameterTypes().length > 1) {//handler method's parameter
                                throw new OpfException(method.getDeclaringClass().getName() + "." + method.getName()
                                        + "的入参只能是" + OpfRequest.class.getName() + "或无入参。");
                            } else if (method.getParameterTypes().length == 1) {
                                Class<?> paramType = method.getParameterTypes()[0];
                                if (!ClassUtils.isAssignable(OpfRequest.class, paramType)) {
                                    throw new OpfException(method.getDeclaringClass().getName() + "." + method.getName()
                                            + "的入参必须是" + OpfRequest.class.getName());
                                }
                                boolean opfRequestImplType = !(paramType.isAssignableFrom(OpfRequest.class) ||
                                        paramType.isAssignableFrom(OpfRequest.class));
                                serviceMethodHandler.setOpfRequestImplType(opfRequestImplType);
                                serviceMethodHandler.setRequestType((Class<? extends OpfRequest>) paramType);
                            } else {
                                logger.info(method.getDeclaringClass().getName() + "." + method.getName() + "无入参");
                            }
                            addServiceMethod(definition.getMethod(), definition.getVersion(), serviceMethodHandler);

                            if (logger.isDebugEnabled()) {
                                logger.debug("注册服务方法：" + method.getDeclaringClass().getCanonicalName() +
                                        "#" + method.getName() + "(..)");
                            }
                        }, method -> !method.isSynthetic() && AnnotationUtils.findAnnotation(method, ServiceMethod.class) != null);

            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("共注册了" + serviceHandlerMap.size() + "个服务方法");
        }
    }

    public void addServiceMethod(String methodName, String version, ServiceMethodHandler serviceMethodHandler) {
        serviceHandlerMap.put(ServiceMethodHandler.methodWithVersion(methodName, version), serviceMethodHandler);
    }

    private ServiceMethodDefinition buildServiceMethodDefinition(ServiceMethod serviceMethod) {
        ServiceMethodDefinition definition = new ServiceMethodDefinition();
        definition.setMethod(serviceMethod.method());
        definition.setMethodTitle(serviceMethod.title());
        definition.setMethodGroup(serviceMethod.group());
        definition.setMethodGroupTitle(serviceMethod.groupTitle());
        definition.setTags(serviceMethod.tags());
        definition.setTimeout(serviceMethod.timeout());
        definition.setVersion(serviceMethod.version());
        definition.setNeedInSession(NeedInSessionType.isNeedInSession(serviceMethod.needInSession()));
        definition.setObsoleted(ObsoletedType.isObsoleted(serviceMethod.obsoleted()));
        definition.setHttpAction(serviceMethod.httpAction());
        return definition;
    }

    private ServiceMethodDefinition buildServiceMethodDefinition(ServiceMethodBean serviceMethodBean, ServiceMethod serviceMethod) {
        ServiceMethodDefinition definition = new ServiceMethodDefinition();
        definition.setMethodGroup(serviceMethodBean.group());
        definition.setMethodGroupTitle(serviceMethodBean.groupTitle());
        definition.setTags(serviceMethodBean.tags());
        definition.setTimeout(serviceMethodBean.timeout());
        definition.setVersion(serviceMethodBean.version());
        definition.setNeedInSession(NeedInSessionType.isNeedInSession(serviceMethodBean.needInSession()));
        definition.setHttpAction(serviceMethodBean.httpAction());
        definition.setObsoleted(ObsoletedType.isObsoleted(serviceMethodBean.obsoleted()));
        definition.setIgnoreSign(IgnoreSignType.isIgnoreSign(serviceMethodBean.ignoreSign()));

        //如果ServiceMethod所提供的值和ServiceMethodGroup不一样，覆盖之
        definition.setMethod(serviceMethod.method());
        definition.setMethodTitle(serviceMethod.title());

        if (!ServiceMethodDefinition.DEFAULT_GROUP.equals(serviceMethod.group())) {
            definition.setMethodGroup(serviceMethod.group());
        }

        if (!ServiceMethodDefinition.DEFAULT_GROUP_TITLE.equals(serviceMethod.groupTitle())) {
            definition.setMethodGroupTitle(serviceMethod.groupTitle());
        }

        if (serviceMethod.tags() != null && serviceMethod.tags().length > 0) {
            definition.setTags(serviceMethod.tags());
        }

        if (serviceMethod.timeout() > 0) {
            definition.setTimeout(serviceMethod.timeout());
        }

        if (StringUtils.hasText(serviceMethod.version())) {
            definition.setVersion(serviceMethod.version());
        }

        if (serviceMethod.needInSession() != NeedInSessionType.DEFAULT) {
            definition.setNeedInSession(NeedInSessionType.isNeedInSession(serviceMethod.needInSession()));
        }

        if (serviceMethod.obsoleted() != ObsoletedType.DEFAULT) {
            definition.setObsoleted(ObsoletedType.isObsoleted(serviceMethod.obsoleted()));
        }

        if (serviceMethod.httpAction().length > 0) {
            definition.setHttpAction(serviceMethod.httpAction());
        }
        if (serviceMethod.ignoreSign() != IgnoreSignType.DEFAULT) {
            definition.setIgnoreSign(IgnoreSignType.isIgnoreSign(serviceMethod.ignoreSign()));
        }
        return definition;
    }

    public ServiceMethodHandler getServiceMethodHandler(String methodName, String version) {
        return serviceHandlerMap.get(ServiceMethodHandler.methodWithVersion(methodName, version));
    }

    public boolean isValidMethod(String methodName, String version) {
        return serviceHandlerMap.containsKey(ServiceMethodHandler.methodWithVersion(methodName, version));
    }
}
