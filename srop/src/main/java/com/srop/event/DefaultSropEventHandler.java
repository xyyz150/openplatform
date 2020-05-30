package com.srop.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;

import java.util.*;
import java.util.concurrent.Executor;

/**
 *默认事件的处理程序
 * Created by xyyz150 on 2016/1/5.
 */
public class DefaultSropEventHandler implements SropEventHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Executor executor;

    private Map<Class<? extends SropEvent>, ListenerRegistry> cacheListenerMap = new HashMap<Class<? extends SropEvent>, ListenerRegistry>();

    private List<SropEventListener> listenerList = new ArrayList<SropEventListener>();

    public DefaultSropEventHandler() {
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    public void addSropListener(SropEventListener listener) {
        listenerList.add(listener);
    }

    public void removesropListener(SropEventListener listener) {
        listenerList.remove(listener);
    }

    public void handlerEvent(final SropEvent event) {
        try {
            for (final SropEventListener listener : getListeners(event)) {
                Executor executor = getExecutor();
                if (executor != null) {
                    executor.execute(new Runnable() {

                        public void run() {
                            listener.onRopEvent(event);
                        }
                    });
                } else {
                    listener.onRopEvent(event);
                }
            }
        } catch (Exception e) {
            logger.error("处理" + event.getClass().getName() + "事件发生异常", e);
        }
    }

    protected boolean checkEventType(Class<? extends SropEvent> eventType, SropEventListener listener) {
        Class typeArg = GenericTypeResolver.resolveTypeArgument(listener.getClass(), SropEventListener.class);
        if (typeArg == null || typeArg.equals(SropEvent.class)) {
            Class targetClass = AopUtils.getTargetClass(listener);
            if (targetClass != listener.getClass()) {
                typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, SropEventListener.class);
            }
        }
        return (typeArg == null || typeArg.isAssignableFrom(eventType));
    }

    protected List<SropEventListener> getListeners(SropEvent event) {
        Class<? extends SropEvent> eventType = event.getClass();
        if (!cacheListenerMap.containsKey(eventType)) {
            ListenerRegistry listenerRegistry = new ListenerRegistry();
            if (listenerList != null && listenerList.size() > 0) {
                for (SropEventListener sropEventListener : listenerList) {
                    if (checkEventType(eventType, sropEventListener)) {
                        listenerRegistry.add(sropEventListener);
                    }
                }
            }
            cacheListenerMap.put(eventType, listenerRegistry);
        }
        return cacheListenerMap.get(eventType);
    }

    private class ListenerRegistry extends LinkedList<SropEventListener> {
    }
}
