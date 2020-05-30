
package com.srop.event;

import java.util.EventListener;

/**
 * <pre>
 *    监听所有Srop框架的事件
 * </pre>
 *
 * @version 1.0
 */
public interface SropEventListener<E extends SropEvent> extends EventListener {

    /**
     * 响应事件
     *
     * @param ropEvent
     */
    void onRopEvent(E ropEvent);

    /**
     * 执行的顺序号
     *
     * @return
     */
    int getOrder();
}

