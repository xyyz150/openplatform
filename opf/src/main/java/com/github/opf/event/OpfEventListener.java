
package com.github.opf.event;

import java.util.EventListener;

/**
 * 监听所有Opf框架的事件
 * Created by xyyz150
 */
public interface OpfEventListener<E extends OpfEvent> extends EventListener {

    /**
     * 响应事件
     *
     * @param opfEvent
     */
    void onOpfEvent(E opfEvent);

    /**
     * 执行的顺序号
     *
     * @return
     */
    int getOrder();
}

