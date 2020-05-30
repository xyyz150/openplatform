
package com.srop.event;

/**
 * <pre>
 *   注册事件监听器，发布事件
 * </pre>
 */
public interface SropEventHandler {

    /**
     * Add a listener to be notified of all events.
     *
     * @param listener the listener to add
     */
    void addSropListener(SropEventListener listener);

    /**
     * Remove a listener from the notification list.
     *
     * @param listener the listener to remove
     */
    void removesropListener(SropEventListener listener);


    /**
     * Multicast the given application event to appropriate listeners.
     *
     * @param event the event to multicast
     */
    void handlerEvent(SropEvent event);
}

