package com.github.opf.autoConfiguration;

import com.github.opf.OpfServlet;
import com.github.opf.event.OpfEventListener;
import com.github.opf.router.OpfDispatchRouter;
import com.github.opf.security.*;
import com.github.opf.session.DefaultSessionManager;
import com.github.opf.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: xyyz150
 * Date: 2020/6/21 12:44
 * Description:
 */
@Configuration
@EnableConfigurationProperties({OpfTaskExecutorConfiguration.class,OpfEventExecutorConfiguration.class})
public class OpfConfiguration implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    OpfTaskExecutorConfiguration taskExecutorConfiguration;

    @Autowired
    OpfEventExecutorConfiguration eventExecutorConfiguration;

    private ApplicationContext applicationContext;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new OpfServlet(), "/router");
        servlet.setLoadOnStartup(2);
        return servlet;
    }

    @Bean
    @ConditionalOnMissingBean(AppSecretManager.class)
    public DefaultAppSecretManager defaultAppSecretManager() {
        return new DefaultAppSecretManager();
    }

    @Bean
    @ConditionalOnMissingBean(InvokeManager.class)
    public DefaultInvokeManager defaultInvokeManager() {
        return new DefaultInvokeManager();
    }

    @Bean
    @ConditionalOnMissingBean(SessionManager.class)
    public DefaultSessionManager defaultSessionManager() {
        return new DefaultSessionManager();
    }

    @Bean
    public OpfDispatchRouter opfDispatchRouter() {
        System.setProperty("file.encoding","UTF-8");
        OpfDispatchRouter router = new OpfDispatchRouter();

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //会话管理器
        final SessionManager sessionManager = applicationContext.getBean(SessionManager.class);
        securityManager.setSessionManager(sessionManager);
        //服务访问控制器
        final InvokeManager invokeManager = applicationContext.getBean(InvokeManager.class);
        securityManager.setInvokeManager(invokeManager);
        //密钥管理器
        final AppSecretManager appSecretManager = applicationContext.getBean(AppSecretManager.class);
        securityManager.setAppSecretManager(appSecretManager);

        router.setSecurityManager(securityManager);

        //设置TaskExecutor
        int corePoolSize = (taskExecutorConfiguration.getCorePoolSize() != null && taskExecutorConfiguration.getCorePoolSize() > 0) ?
                taskExecutorConfiguration.getCorePoolSize() : 50;
        int maxPoolSize = (taskExecutorConfiguration.getMaxPoolSize() != null && taskExecutorConfiguration.getMaxPoolSize() > 0) ?
                taskExecutorConfiguration.getMaxPoolSize() : 50;
        int keepAliveSeconds = (taskExecutorConfiguration.getKeepAliveSeconds() != null && taskExecutorConfiguration.getKeepAliveSeconds() > 0) ?
                taskExecutorConfiguration.getKeepAliveSeconds() : 5 * 60;
        int queueCapacity = (taskExecutorConfiguration.getQueueCapacity() != null && taskExecutorConfiguration.getQueueCapacity() > 0) ?
                taskExecutorConfiguration.getQueueCapacity() : 10000;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue(queueCapacity));

        router.setThreadPoolExecutor(threadPoolExecutor);

        //设置EventExecutor
        int eventCorePoolSize = (eventExecutorConfiguration.getCorePoolSize() != null && eventExecutorConfiguration.getCorePoolSize() > 0) ?
                eventExecutorConfiguration.getCorePoolSize() : 8;
        int eventMaxPoolSize = (eventExecutorConfiguration.getMaxPoolSize() != null && eventExecutorConfiguration.getMaxPoolSize() > 0) ?
                eventExecutorConfiguration.getMaxPoolSize() : 8;
        int eventKeepAliveSeconds = (eventExecutorConfiguration.getKeepAliveSeconds() != null && eventExecutorConfiguration.getKeepAliveSeconds() > 0) ?
                taskExecutorConfiguration.getKeepAliveSeconds() : 5 * 60;
        int eventQueueCapacity = (eventExecutorConfiguration.getQueueCapacity() != null && eventExecutorConfiguration.getQueueCapacity() > 0) ?
                eventExecutorConfiguration.getQueueCapacity() : 10000;
        ThreadPoolExecutor eventThreadPoolExecutor = new ThreadPoolExecutor(eventCorePoolSize, eventMaxPoolSize, eventKeepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue(eventQueueCapacity));

        router.setEventThreadPoolExecutor(eventThreadPoolExecutor);

        //注册监听器
        ArrayList<OpfEventListener> listeners = getListeners();
        if (listeners != null) {
            for (OpfEventListener listener : listeners) {
                router.addListener(listener);
            }
            if (logger.isInfoEnabled()) {
                logger.info("register total {} listeners", listeners.size());
            }
        }

        router.setApplicationContext(applicationContext);
        //启动
        router.init();

        return router;
    }

    private ArrayList<OpfEventListener> getListeners() {
        final Map<String, OpfEventListener> listenerMap = applicationContext.getBeansOfType(OpfEventListener.class);
        if (listenerMap != null && listenerMap.size() > 0) {
            ArrayList<OpfEventListener> opfEventListeners = new ArrayList<OpfEventListener>(listenerMap.size());
            for (OpfEventListener listener : listenerMap.values()) {
                opfEventListeners.add(listener);
            }
            return opfEventListeners;
        }else {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
