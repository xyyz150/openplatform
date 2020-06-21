
package com.github.opf.config;

import com.github.opf.router.OpfDispatchRouterFactoryBean;
import com.github.opf.session.DefaultSessionManager;
import com.github.opf.security.DefaultAppSecretManager;
import com.github.opf.security.DefaultInvokeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class OpfDispatchBeanDefinitionParser implements BeanDefinitionParser {

    public static final int DEFAULT_CORE_POOL_SIZE = 1;

    public static final int DEFAULT_MAX_POOL_SIZE = 120;

    public static final int DEFAULT_KEEP_ALIVE_SECONDS = 3 * 60;

    public static final int DEFAULT_QUENE_CAPACITY = 10;

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Object source = parserContext.extractSource(element);
        CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
        parserContext.pushContainingComponent(compDefinition);

        //注册ServiceRouter Bean
        RootBeanDefinition serviceRouterDef = new RootBeanDefinition(OpfDispatchRouterFactoryBean.class);
        String serviceRouterName = element.getAttribute("id");
        if (StringUtils.hasText(serviceRouterName)) {
            parserContext.getRegistry().registerBeanDefinition(serviceRouterName, serviceRouterDef);
        } else {
            serviceRouterName = parserContext.getReaderContext().registerWithGeneratedName(serviceRouterDef);
        }
        parserContext.registerComponent(new BeanComponentDefinition(serviceRouterDef, serviceRouterName));

        //会话管理器
        RuntimeBeanReference sessionManager = getSessionManager(element, source, parserContext);
        serviceRouterDef.getPropertyValues().add("sessionManager", sessionManager);

        //密钥管理器
        RuntimeBeanReference appSecretManager = getAppSecretManager(element, source, parserContext);
        serviceRouterDef.getPropertyValues().add("appSecretManager", appSecretManager);

        //服务访问控制器
        RuntimeBeanReference invokeManager = getInvokeManager(element, source, parserContext);
        serviceRouterDef.getPropertyValues().add("invokeManager", invokeManager);

        //设置TaskExecutor
        RuntimeBeanReference taskExecutorDef = getTaskExecutor(element, parserContext, source);
        serviceRouterDef.getPropertyValues().add("threadPoolExecutor", taskExecutorDef);

        //设置服务过期时间
        setServiceTimeout(element, serviceRouterDef);

        parserContext.popAndRegisterContainingComponent();
        return null;
    }


    private RuntimeBeanReference getTaskExecutor(Element element, ParserContext parserContext, Object source) {
        RuntimeBeanReference taskExecutorBeanReference;
        RootBeanDefinition taskExecutorDef = new RootBeanDefinition(org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean.class);
        if (element.hasAttribute("thread-pool-manager")) {
            taskExecutorBeanReference = new RuntimeBeanReference(element.getAttribute("invoke-manager"));
        } else {
            taskExecutorDef.setSource(source);
            taskExecutorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            String taskExecutorName = parserContext.getReaderContext().registerWithGeneratedName(taskExecutorDef);
            taskExecutorDef.getPropertyValues().addPropertyValue("corePoolSize",20);
            taskExecutorDef.getPropertyValues().addPropertyValue("maxPoolSize", 100);
            taskExecutorDef.getPropertyValues().addPropertyValue("keepAliveSeconds",3*60);
            taskExecutorDef.getPropertyValues().addPropertyValue("queueCapacity",1000);
            parserContext.registerComponent(new BeanComponentDefinition(taskExecutorDef, taskExecutorName));
            taskExecutorBeanReference = new RuntimeBeanReference(taskExecutorName);
        }
        return taskExecutorBeanReference;
    }


    private void setServiceTimeout(Element element, RootBeanDefinition serviceRouterDef) {
        String serviceTimeoutSeconds = element.getAttribute("service-timeout-seconds");
        if (StringUtils.hasText(serviceTimeoutSeconds)) {
            serviceRouterDef.getPropertyValues().addPropertyValue("serviceTimeoutSeconds", serviceTimeoutSeconds);
        }
    }

    private RuntimeBeanReference getInvokeManager(Element element, Object source, ParserContext parserContext) {
        RuntimeBeanReference serviceAccessControllerRef;
        if (element.hasAttribute("invoke-manager")) {
            serviceAccessControllerRef = new RuntimeBeanReference(element.getAttribute("invoke-manager"));
        } else {
            RootBeanDefinition serviceAccessControllerDef = new RootBeanDefinition(DefaultInvokeManager.class);
            serviceAccessControllerDef.setSource(source);
            serviceAccessControllerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            String serviceAccessControllerName = parserContext.getReaderContext().registerWithGeneratedName(serviceAccessControllerDef);
            parserContext.registerComponent(new BeanComponentDefinition(serviceAccessControllerDef, serviceAccessControllerName));
            serviceAccessControllerRef = new RuntimeBeanReference(serviceAccessControllerName);
        }
        return serviceAccessControllerRef;
    }

    private RuntimeBeanReference getAppSecretManager(Element element, Object source, ParserContext parserContext) {
        RuntimeBeanReference appSecretManagerRef;
        if (element.hasAttribute("appSecret-manager")) {
            appSecretManagerRef = new RuntimeBeanReference(element.getAttribute("appSecret-manager"));
        } else {
            RootBeanDefinition appSecretManagerDef = new RootBeanDefinition(DefaultAppSecretManager.class);
            appSecretManagerDef.setSource(source);
            appSecretManagerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            String appSecretManagerName = parserContext.getReaderContext().registerWithGeneratedName(appSecretManagerDef);
            parserContext.registerComponent(new BeanComponentDefinition(appSecretManagerDef, appSecretManagerName));
            appSecretManagerRef = new RuntimeBeanReference(appSecretManagerName);
        }
        return appSecretManagerRef;
    }

    private RuntimeBeanReference getSessionManager(Element element, Object source, ParserContext parserContext) {
        RuntimeBeanReference sessionManagerRef;
        if (element.hasAttribute("session-manager")) {
            sessionManagerRef = new RuntimeBeanReference(element.getAttribute("session-manager"));
        } else {
            RootBeanDefinition sessionManagerDef = new RootBeanDefinition(DefaultSessionManager.class);
            sessionManagerDef.setSource(source);
            sessionManagerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            String sessionManagerName = parserContext.getReaderContext().registerWithGeneratedName(sessionManagerDef);
            parserContext.registerComponent(new BeanComponentDefinition(sessionManagerDef, sessionManagerName));
            sessionManagerRef = new RuntimeBeanReference(sessionManagerName);
        }
        return sessionManagerRef;
    }
}

