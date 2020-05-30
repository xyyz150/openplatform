
package com.srop.router;

import com.srop.config.SropEventListenerHodler;
import com.srop.event.SropEventListener;
import com.srop.security.*;
import com.srop.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.format.support.FormattingConversionService;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class SropDispatchRouterFactoryBean
        implements FactoryBean<SropDispatchRouter>, ApplicationContextAware, InitializingBean, DisposableBean {

    private static final String ALL_FILE_TYPES = "*";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private ThreadPoolExecutor threadPoolExecutor;

    private SessionManager sessionManager;

    private AppSecretManager appSecretManager;

    private InvokeManager invokeManager;

    private boolean signEnable = true;

    private String extErrorBasename;

    private String[] extErrorBasenames;

    private int serviceTimeoutSeconds = -1;

    private FormattingConversionService formattingConversionService;

    private SropDispatchRouter serviceRouter;

    //多值用逗号分隔,默认支持4种格式的文件
    private String uploadFileTypes = ALL_FILE_TYPES;

    //单位为K，默认为10M
    private int uploadFileMaxSize = 10 * 1024;


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public void destroy() throws Exception {
        serviceRouter.destroy();
    }


    public Class<?> getObjectType() {
        return SropDispatchRouter.class;
    }


    public SropDispatchRouter getObject() throws Exception {
        return this.serviceRouter;
    }


    public boolean isSingleton() {
        return true;
    }


    public void afterPropertiesSet() throws Exception {
        //SropDispatchRouter
        serviceRouter = new SropDispatchRouter();

        DefaultSecurityManager securityManager = BeanUtils.instantiate(DefaultSecurityManager.class);

        securityManager.setSessionManager(sessionManager);
        securityManager.setAppSecretManager(appSecretManager);
        securityManager.setInvokeManager(invokeManager);

        serviceRouter.setSecurityManager(securityManager);
        serviceRouter.setServiceTimeoutSeconds(serviceTimeoutSeconds);


        //注册监听器
        ArrayList<SropEventListener> listeners = getListeners();
        if (listeners != null) {
            for (SropEventListener listener : listeners) {
                serviceRouter.addListen(listener);
            }
            if (logger.isInfoEnabled()) {
                logger.info("register total {} listeners", listeners.size());
            }
        }

        //设置Spring上下文信息
        serviceRouter.setApplicationContext(this.applicationContext);

        //启动之
        serviceRouter.init();
    }

    private ArrayList<SropEventListener> getListeners() {
        Map<String, SropEventListenerHodler> listenerMap = this.applicationContext.getBeansOfType(SropEventListenerHodler.class);
        if (listenerMap != null && listenerMap.size() > 0) {
            ArrayList<SropEventListener> ropEventListeners = new ArrayList<SropEventListener>(listenerMap.size());

            //从Spring容器中获取EventListener
            for (SropEventListenerHodler listenerHolder : listenerMap.values()) {
                ropEventListeners.add(listenerHolder.getRopEventListener());
            }
            return ropEventListeners;
        } else {
            return null;
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setServiceTimeoutSeconds(int serviceTimeoutSeconds) {
        this.serviceTimeoutSeconds = serviceTimeoutSeconds;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setAppSecretManager(AppSecretManager appSecretManager) {
        this.appSecretManager = appSecretManager;
    }


    public InvokeManager getInvokeManager() {
        return invokeManager;
    }

    public void setInvokeManager(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
    }
}

