
package com.srop.event;

import com.srop.request.SropRequestContext;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class AfterDoServiceEvent extends SropEvent {

    private SropRequestContext ropRequestContext;

    public AfterDoServiceEvent(Object source, SropRequestContext ropRequestContext) {
        super(source, ropRequestContext.getSropContext());
        this.ropRequestContext = ropRequestContext;
    }

    public long getServiceBeginTime() {
        return ropRequestContext.getServiceBeginTime();
    }

    public long getServiceEndTime() {
        return ropRequestContext.getServiceEndTime();
    }

    public SropRequestContext getRopRequestContext() {
        return ropRequestContext;
    }
}

