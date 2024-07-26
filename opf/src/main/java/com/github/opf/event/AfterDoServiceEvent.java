
package com.github.opf.event;

import com.github.opf.request.OpfRequestContext;

/**
 * Created by xyyz150
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

