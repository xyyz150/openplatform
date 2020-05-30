
package com.srop.event;

import com.srop.request.SropRequestContext;

/**
 * <pre>
 *    在执行服务方法之前产生的事件
 * </pre>
 *

 * @version 1.0
 */
public class PreDoServiceEvent extends SropEvent {

    private SropRequestContext ropRequestContext;

    public PreDoServiceEvent(Object source, SropRequestContext ropRequestContext) {
        super(source, ropRequestContext.getSropContext());
        this.ropRequestContext = ropRequestContext;
    }

    public SropRequestContext getRopRequestContext() {
        return ropRequestContext;
    }

    public long getServiceBeginTime() {
        return ropRequestContext.getServiceBeginTime();
    }
}

