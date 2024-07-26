
package com.github.opf.event;

/**
 * 注册事件监听器，发布事件
 * Created by xyyz150
 */
public interface OpfEventHandler {

    /**
     * Add a listener to be notified of all events.
     *
     * @param listener the listener to add
     */
    void addOpfListener(OpfEventListener listener);

    /**
     * Remove a listener from the notification list.
     *
     * @param listener the listener to remove
     */
    void removeOpfListener(OpfEventListener listener);


    /**
     * Multicast the given application event to appropriate listeners.
     *
     * @param event the event to multicast
     */
    void handlerEvent(OpfEvent event);
}

