
package com.srop.event;

import com.srop.SropContext;

/**
 * <pre>
 *   在Srop框架初始化后产生的事件
 * </pre>
 *
 * @version 1.0
 */
public class AfterStartedSropEvent extends SropEvent {

    public AfterStartedSropEvent(Object source, SropContext ropContext) {
        super(source, ropContext);
    }

}

