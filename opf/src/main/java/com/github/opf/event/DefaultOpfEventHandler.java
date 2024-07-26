package com.github.opf.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * 默认事件的处理程序
 * Created by xyyz150
 */
public class DefaultOpfEventHandler implements OpfEventHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Executor executor;

    private Map<Class<? extends OpfEvent>, ListenerRegistry> cacheListenerMap = new HashMap<Class<? extends OpfEvent>, ListenerRegistry>();

    private List<OpfEventListener> listenerList = new ArrayList<OpfEventListener>();

    public DefaultOpfEventHandler() {
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    public void addOpfListener(OpfEventListener listener) {
        listenerList.add(listener);
    }

    public void removeOpfListener(OpfEventListener listener) {
        listenerList.remove(listener);
    }

    public void handlerEvent(final OpfEvent event) {
        try {
            for (final OpfEventListener listener : getListeners(event)) {
                Executor executor = getExecutor();
                if (executor != null) {
                    executor.execute(()->listener.onOpfEvent(event));
                } else {
                    listener.onOpfEvent(event);
                }
            }
        } catch (Exception e) {
            logger.error("处理" + event.getClass().getName() + "事件发生异常", e);
        }
    }

    protected boolean checkEventType(Class<? extends OpfEvent> eventType, OpfEventListener listener) {
        Class typeArg = GenericTypeResolver.resolveTypeArgument(listener.getClass(), OpfEventListener.class);
        if (typeArg == null || typeArg.equals(OpfEvent.class)) {
            Class targetClass = AopUtils.getTargetClass(listener);
            if (targetClass != listener.getClass()) {
                typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, OpfEventListener.class);
            }
        }
        return (typeArg == null || typeArg.isAssignableFrom(eventType));
    }

    protected List<OpfEventListener> getListeners(OpfEvent event) {
        Class<? extends OpfEvent> eventType = event.getClass();
        if (!cacheListenerMap.containsKey(eventType)) {
            ListenerRegistry listenerRegistry = new ListenerRegistry();
            if (listenerList != null && listenerList.size() > 0) {
                for (OpfEventListener opfEventListener : listenerList) {
                    if (checkEventType(eventType, opfEventListener)) {
                        listenerRegistry.add(opfEventListener);
                    }
                }
            }
            cacheListenerMap.put(eventType, listenerRegistry);
        }
        return cacheListenerMap.get(eventType);
    }

    private class ListenerRegistry extends LinkedList<OpfEventListener> {
    }
}
