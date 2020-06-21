
package com.github.opf.event;

import com.github.opf.request.OpfRequestContext;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class AfterDoServiceEvent extends OpfEvent {

    private OpfRequestContext opfRequestContext;

    public AfterDoServiceEvent(Object source, OpfRequestContext opfRequestContext) {
        super(source, opfRequestContext.getOpfContext());
        this.opfRequestContext = opfRequestContext;
    }

    public long getServiceBeginTime() {
        return opfRequestContext.getServiceBeginTime();
    }

    public long getServiceEndTime() {
        return opfRequestContext.getServiceEndTime();
    }

    public OpfRequestContext getOpfRequestContext() {
        return opfRequestContext;
    }
}

